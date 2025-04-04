package util;

import geom.Shape3D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.joml.Vector2d;
import org.joml.Vector3d;
import org.joml.Vector3i;

public class FileUtils {

    private static final Logger LOGGER = Logger.getLogger(FileUtils.class.getName());

    public enum Directive {
        FILE("file:", ""),
        RESOURCE("res:", "");

        private final String value;
        private final String expansion;

        Directive(String value, String expansion) {
            this.value = value;
            this.expansion = expansion;
        }

        public static Directive getFromValue(String value) {
            for (Directive d : Directive.values()) {
                if (value.equals(d.value)) {
                    return d;
                }
            }
            return null;
        }

        public String getValue() {
            return value;
        }

        public String expand() {
            if (this == FILE) {
                return System.getProperty("user.dir");
            }
            return expansion;
        }
    }

    public static String simplifyPath(String path) {

        if (path == null) {
            return null;
        }

        boolean containsBadSeparators = path.matches(".*\\\\\\\\.*") // contains escaped backslash
                || path.matches(".*//.*") // contains double slashes
                || path.matches(".*\\\\/"); // contains escaped slash
        boolean containsBackreferences = path.matches(".*\\.\\..*");
        boolean containsDirectives = false;

        for (Directive d : Directive.values()) {
            if (path.contains(d.value)) {
                containsDirectives = true;
                break;
            }
        }

        if (containsBadSeparators) {
            String newPath = path.replaceAll("\\\\", "/")
                                 .replaceAll("//", "/");
            return simplifyPath(newPath);
        }
        else if (containsDirectives) {
            String newPath = path;
            for (Directive d : Directive.values()) {
                newPath = newPath.replaceAll(d.getValue(), d.expand());
            }
            return simplifyPath(newPath);
        }
        else if (containsBackreferences) {
            String[] dirs = path.split("/");
            for (int i = 1; i < dirs.length; i++) {
                if (dirs[i].matches("\\.\\.")) {
                    dirs[i] = null;
                    dirs[i - 1] = null;
                }
            }
            StringBuilder pathBuilder = new StringBuilder();
            for (int i = 0; i < dirs.length; i++) {
                if (dirs[i] == null) {
                    continue;
                }
                pathBuilder.append(dirs[i]);
                if (i < dirs.length - 1) {
                    pathBuilder.append("/");
                }
            }
            return simplifyPath(pathBuilder.toString());
        }

        // else, path is perfect and doesn't need changing
        // return as is
        return path;
    }

    public static void forEach(InputStream in, String separator, Consumer<String> sectionConsumer) {
        forEach(new InputStreamReader(in), separator, sectionConsumer);
    }

    public static void forEach(Reader r, String separator, Consumer<String> sectionConsumer) {
        if (r == null) {
            return;
        }
        Scanner scan = new Scanner(r);
        scan.useDelimiter(separator);

        while (scan.hasNext()) {
            sectionConsumer.accept(scan.next());
        }
    }

    public static InputStream getInputStream(String path) {
        if (path == null) {
            return null;
        }

        /*
         * try to find classpath file first,
         * and if it doesn't exist, then try to find a file on disk.
         * jar resources can mask files since they take priority, but whatever.
         */

        String simplePath = simplifyPath(path);

        InputStream in = FileUtils.class.getResourceAsStream(simplePath);
        if (in == null) {
            try {
                in = new FileInputStream(simplePath);
            }
            catch (FileNotFoundException ignored) {}
        }

        // if the inputstream is STILL null, then that means we didn't find anything.
        if (in == null) {
            LOGGER.log(Level.WARNING, "Couldn't find file '%s'", simplePath);
        }
        return in;
    }

    public static BufferedImage getImage(InputStream in) {
        try {
            if (in == null) {
                throw new IOException();
            }
            return ImageIO.read(in);
        }
        catch (IOException e) {
            BufferedImage ret = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = ret.createGraphics();
            g2.setColor(Color.MAGENTA);
            g2.fillRect(0, 0,
                        ret.getWidth(), ret.getHeight());

            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0,
                        ret.getWidth() / 2, ret.getHeight() / 2);
            g2.fillRect(ret.getWidth() / 2, ret.getHeight() / 2,
                        ret.getWidth() / 2, ret.getHeight() / 2);
            g2.dispose();
            return ret;
        }
    }

    public static Shape3D getShape(InputStream in) {
        if (in == null) {
            return null;
        }

        List<Vector3d> vertices = new ArrayList<>();
        List<Vector2d> uvs = new ArrayList<>();
        List<Vector3d> normals = new ArrayList<>();

        List<Vector3i[]> faces = new ArrayList<>();

        forEach(in, "\n", line -> {
            if (line.startsWith("#")) {
                // comment line, ignore
                return;
            }

            else if (line.startsWith("v ")) {
                String[] args = line.split("\\s+");

                vertices.add(new Vector3d(
                        Double.parseDouble(args[1]),
                        Double.parseDouble(args[2]),
                        Double.parseDouble(args[3])));
            }

            else if (line.startsWith("vt ")) {
                String[] args = line.split("\\s+");

                uvs.add(new Vector2d(
                            Double.parseDouble(args[1]),
                        1 - Double.parseDouble(args[2])));
            }

            else if (line.startsWith("vn ")) {
                String[] args = line.split("\\s+");

                normals.add(new Vector3d(
                        Double.parseDouble(args[1]),
                        Double.parseDouble(args[2]),
                        Double.parseDouble(args[3])).normalize());
            }

            else if (line.startsWith("f ")) {
                String[] args = line.split("\\s+");

                Vector3i[] face = new Vector3i[args.length - 1];

                for (int i = 0; i < face.length; i++) {

                    String[] indices = args[i + 1].split("/");

                    Vector3i vertex = new Vector3i();

                    for (int j = 0; j < indices.length; j++) {
                        String index = indices[j];
                        vertex.setComponent(j, index.isBlank()? Integer.MIN_VALUE : Integer.parseInt(index) - 1);
                    }

                    face[i] = vertex;
                }
                faces.add(face);
            }

        });

        List<Vector3d> finalVertices = new ArrayList<>();
        List<Vector2d> finalUvs = new ArrayList<>();
        List<Vector3d> finalNormals = new ArrayList<>();
        List<Integer> finalIndices = new ArrayList<>();

        int index = 0;

        for (Vector3i[] face : faces) {
            for (int i = 0; i < face.length; i++) {
                Vector3i faceVertex = face[i];
                int positionIndex = faceVertex.x;
                int uvIndex = faceVertex.y;
                int normalIndex = faceVertex.z;

                boolean normalOutOfRange = normalIndex < 0 || normalIndex >= normals.size();
                boolean uvOutOfRange = uvIndex < 0 || uvIndex >= uvs.size();

                Vector3d position = vertices.get(positionIndex);
                Vector2d uv = uvOutOfRange? null : uvs.get(uvIndex);
                Vector3d normal = normalOutOfRange? null : normals.get(normalIndex);

                finalVertices.add(position);
                finalUvs.add(uv == null? new Vector2d() : uv);
                finalNormals.add(normal);
                finalIndices.add(index);
                index++;
            }
            finalIndices.add(Shape3D.INDEX_SEPARATOR);
        }

        LOGGER.log(Level.INFO, "Created mesh with " + faces.size() + " faces");

        return new Shape3D(Color.MAGENTA,
                           finalVertices.toArray(new Vector3d[0]),
                           finalUvs.toArray(new Vector2d[0]),
//                           finalNormals.toArray(new Vector3d[0]),
                           finalIndices.stream().mapToInt(Integer::intValue).toArray());
    }

}
