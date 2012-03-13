import java.io.*;
import java.io.File;

public class Main {

    public static ContextDictionary dictionary;
    public static final String TREE_FILENAME = "tree.out";

    /**
     * Main method will be the primary and only interface into our input program
     * learn mode       - the method will be called as follows: "java FQZIP.jar learn input_file"
     *                    the learn method will create a tree.out file containing all mappings
     *                    method will be run using a single thread!
     *
     * compress mode    - the compress mode will be called: "java FQZIP.jar compress input_file output_file #ofThreads"
     *
     *                    INPUT FILE MUST BE GIVEN AS A RELATIVE PATH OR FULL PATH TO AN EXISTING PATH
     *
     *                    the output file will be a prefix of the generated file. For example (given output: "test"):
     *                      Many files with (with different sequence numbers) will be generated such as:
     *                      test.1.headers, test.1.quality, test.1.qualities, test.2.qualities ......
     *
     *                    Number of threads will indicate the number of threads to be used for the processing
     *                      IF NO NUMBER IS SPECIFIED, A SINGLE THREAD WILL BE USED FOR THE PROCESSING!
     *
     * decompress mode  - the decompress mode will be called: "java fqzip.jar decompress input_file output_file #ofThreads"
     *
     *                    the input file is the prefix of the previously generated files run in compress mode
     *                    therefore if "test" was given as the output_file argument to previous program,
     *                    this mode will search for all files starting with the "test" prefix and decompress
     *                    the groups(files with same sequence number) accordingly
     *
     *                    the output file is the result of the decompression
     *
     *                    Number of threads will indicate the number of threads to be used for the processing
     *                      IF NO NUMBER IS SPECIFIED, A SINGLE THREAD WILL BE USED FOR THE PROCESSING!
     *
     *
     * @param arguments
     * @throws IOException
     * @throws InterruptedException
     */
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
