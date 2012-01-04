import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: ron
 * Date: 12/14/11
 * Time: 12:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    public static boolean learnMode = true;
    public static String decodingTreesFile = "decoding_trees.data";
    public static String encodingTableFile = "encoding_table.data";
    public static ContextDictionary dictionary;

    public static void main(String[] arguments) throws FileNotFoundException, IOException {
        dictionary = new ContextDictionary();
        Worker worker = new Worker();
        worker.run(arguments[0], arguments[1]);
    }
}
