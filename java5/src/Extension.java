import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Scanner;
import java.util.function.Consumer;

public class Extension {

    public static void main(String[] args) {

        FileInputStream in;
        try {
            in = new FileInputStream("java5/words_alpha.txt");
        }
        catch (FileNotFoundException e) {
            System.out.println("Could not find file");
            return;
        }

        forEach(in, "\n", line -> {

            int aCount = 0;

            for (int i = 0; i < line.length(); i++) {
                if (line.toLowerCase().charAt(i) == 'a') {
                    aCount++;
                }
            }

            if (aCount == 5) {
                System.out.println(line);
            }
        });
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

}
