import java.io.*;
import java.io.File;

public class Main {

    public static ContextDictionary dictionary;

    public static void main(String[] arguments) throws IOException {

        if((! arguments[0].equals("compress")) && (! arguments[0].equals("decompress")) && (! arguments[0].equals("learn"))) {
            System.out.println("Usage: <Run Mode: compress or decompress or learn> " +
                    "<input> [output (not necessary in learn mode)] " +
                    "[number of threads (if not given - 1 will be used as default)]");
            return;
        }

        int numberOfThreads = 0;

        if(arguments.length == 4) {

            //will throw an exception if received an invalid number
            numberOfThreads = Integer.parseInt(arguments[3]);

            System.out.println("Thread Pool size configured to: ");
        }

        dictionary = new ContextDictionary();

        Worker worker = numberOfThreads == 0 ? new Worker(arguments[0],1) : new Worker(arguments[0],numberOfThreads);

        File inputFile = new File(arguments[1]);
        
        if(! inputFile.exists()) 
            throw new FileNotFoundException("Input File given does not exist");

        if(arguments[0].equals("learn")) {
            worker.run(arguments[1], null);
        }
        else {
            dictionary.readDictionaryFromFile(new FileInputStream("tree.out"));
            worker.run(arguments[1],arguments[2]);
        }
    }
}
