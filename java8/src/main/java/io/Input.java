package io;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import logging.Level;
import logging.Logger;
import org.joml.Vector2d;

public class Input implements KeyListener,
                              MouseListener, MouseMotionListener, MouseWheelListener {

    // this probably shouldn't be a singleton but i could genuinely care less

    private Input() {}

    private static Input instance;

    private static int currentTick = 0;
    private static Vector2d screen = new Vector2d(1, 1);
    private static Vector2d cursorPos = new Vector2d();

    private static final List<Bind> binds = new ArrayList<>();

    public static class Bind {
        public String action;
        public String key;
        public int lastPressed = -2;
        public int lastReleased = -1;
        public Bind(String action, String key) {
            this.action = action;
            this.key = key;
        }
    }

    public static Input get() {
        if (instance == null) {
            instance = new Input();
        }
        return instance;
    }

    public static void addBind(String action, String key) {
        if (key == null || action == null) {
            return;
        }
        synchronized (binds) {
            // overwrite existing binds with same key
            binds.removeIf(bind -> bind.action.equals(action));
            binds.add(new Bind(key, action));
        }
    }

    public static boolean isActionPressed(String action) {
        Bind b = getBindByAction(action);
        if (b == null) return false;
        return b.lastReleased < b.lastPressed;
    }

    public static boolean isActionJustPressed(String action) {
        Bind b = getBindByAction(action);
        if (b == null) return false;
        return b.lastPressed == currentTick;
    }

    public static boolean isActionJustReleased(String action) {
        Bind b = getBindByAction(action);
        if (b == null) return false;
        return b.lastReleased == currentTick;
    }

    private static Bind getBindByAction(String action) {
        synchronized (binds) {
            for (Bind bind : binds) {
                if (bind.action.equals(action)) {
                    return bind;
                }
            }
        }
        return null;
    }

    private static Bind[] getBindsByKey(String key) {
        List<Bind> b = new ArrayList<>();

        synchronized (binds) {
            for (Bind bind : binds) {
                if (bind.key.equals(key)) {
                    b.add(bind);
                }
            }
        }

        return b.toArray(new Bind[0]);
    }

    public static String getKey(int code) {

        for (Field f : KeyEvent.class.getFields()) {

            // only check static fields
            if (!Modifier.isStatic(f.getModifiers())) {
                continue;
            }

            String name = f.getName();
            int value;
            try {
                value = f.getInt(null);
            }
            catch (IllegalAccessException e) {
                // fail silently
                continue;
            }

            if (value == code) {
                return name.replaceAll("VK_", "");
            }

        }

        Logger.log(Level.INFO, "No such key %d", code);

        return null;
    }

    public static String getMouseButton(int code) {
        for (Field f : MouseEvent.class.getFields()) {

            // only check static fields
            if (!Modifier.isStatic(f.getModifiers())) {
                continue;
            }

            String name = f.getName();
            int value;
            try {
                value = f.getInt(null);
            }
            catch (IllegalAccessException e) {
                // fail silently
                continue;
            }

            if (value == code) {
                return name;
            }

        }

        Logger.log(Level.WARN, "No such key %d", code);

        return null;
    }

    public static Vector2d getCursorPos() {
        double ratio = screen.x / screen.y;

        return new Vector2d(cursorPos).mul(1, -1 / ratio).div(screen).sub(0.5, -0.5 /ratio);
    }

    public void setScreenSize(Vector2d dimensions) {
        screen.set(dimensions);
    }

    public void update() {
        currentTick++;
    }


    private void wheelEvent(MouseWheelEvent e) {

        Bind[] b;

        int delta = e.getWheelRotation();

        if (delta == 0) {
            return;
        }

        if (delta < 0) {
            b = getBindsByKey("mwheelup");
        }
        else {
            b = getBindsByKey("mwheeldown");
        }

        for (Bind bind : b) {
            if (bind == null) {
                continue;
            }
            bind.lastPressed = currentTick;
            bind.lastReleased = currentTick + 1;
        }
    }

    private void cursorEvent(MouseEvent e) {
        cursorPos.x = e.getX();
        cursorPos.y = e.getY();
    }

    private void buttonEvent(MouseEvent e, boolean pressed) {
        Bind[] bs = getBindsByKey(getMouseButton(e.getButton()));

        for (Bind b : bs) {
            if (pressed) {
                b.lastPressed = currentTick;
            }
            else {
                b.lastReleased = currentTick;
            }
        }
    }

    private void keyEvent(KeyEvent e, boolean pressed) {
        Bind[] bs = getBindsByKey(getKey(e.getKeyCode()));

        for (Bind b : bs) {
            if (pressed) {
                b.lastPressed = currentTick;
            }
            else {
                b.lastReleased = currentTick;
            }
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    @Override public void keyPressed(KeyEvent e) {keyEvent(e, true);}
    @Override public void keyReleased(KeyEvent e) {keyEvent(e, false);}
    @Override public void mousePressed(MouseEvent e) {buttonEvent(e, true);}
    @Override public void mouseReleased(MouseEvent e) {buttonEvent(e, false);}
    @Override public void mouseDragged(MouseEvent e) {cursorEvent(e);}
    @Override public void mouseMoved(MouseEvent e) {cursorEvent(e);}
    @Override public void mouseWheelMoved(MouseWheelEvent e) {wheelEvent(e);}
}
