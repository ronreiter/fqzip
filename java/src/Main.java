import java.io.*;
import java.io.File;

public class Main {

    public static ContextDictionary dictionary;
    public static final String TREE_FILENAME = "tree.out";

    public static void main(String[] arguments) throws IOException, InterruptedException {
        int numberOfThreads = 1;
        dictionary = new ContextDictionary();

        if((! arguments[0].equals("compress")) && (! arguments[0].equals("decompress")) && (! arguments[0].equals("learn"))) {
            System.err.println("Usage: <Run Mode: compress or decompress or learn> " +
                    "<input> [output (not necessary in learn mode)] " +
                    "[number of threads (if not given - 1 will be used as default)]");
            return;
        }

        if(arguments.length == 4) {
            numberOfThreads = Integer.parseInt(arguments[3]);
        }
        System.err.println("Thread Pool size configured to: " + numberOfThreads);

        if(arguments[0].equals("learn")) {
            new ThreadPoolManager(1, arguments[0], arguments[1], null).start();
        }
        else {
            System.err.println("Reading dictionary from file...");
            dictionary.readDictionaryFromFile(new FileInputStream(TREE_FILENAME));
            //run manager with the parameters (number of threads and mode)
            System.err.println("Dictionary read complete.");
            new ThreadPoolManager(numberOfThreads, arguments[0], arguments[1], arguments[2]).start();
        }
    }
}
