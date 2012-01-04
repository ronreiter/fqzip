import Huffman.CodeTree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: barakjacob
 * Date: 12/28/11
 * Time: 9:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContextDictionary {

    private static HashMap<String, CodeTree> huffmanTreeTable;
    private static HashMap<String, List<List<Integer>>> encodingTable;
    private static HashMap<String, ContextStats> statistics;
    
    public static void startLearning() {
        statistics = new HashMap<String, ContextStats>();
        huffmanTreeTable = new HashMap<String, CodeTree>();
    }
    
    public static void learn(ReadData data) {
        ContextHasher hasher = new ContextHasher();
        
        String quality = data.getQuality();
        String sequence = data.getSequence();
        for (int i = 0; i < sequence.length(); i++) {
            String context = hasher.hashContext(i, sequence, quality);
            ContextStats stats;

            // get the stats array for the current context. create it
            // if it doesn't exist.
            if (statistics.containsKey(context)) {
                stats = new ContextStats();
            } else {
                stats = statistics.get(context);
            }
            // add the statistical data.
            stats.addStatistic(quality.charAt(i - 33));
            statistics.put(context, stats);
        }
    }

    public static CodeTree getHuffmanTree (String context) {
        return huffmanTreeTable.get(context);
    }
    
    public static List<List<Integer>> getEncodingTable (String context) {
        return encodingTable.get(context);
    }

    public static HashMap<String, CodeTree> getHuffmanTreeTable() {
        return huffmanTreeTable;
    }

    public static HashMap<String, List<List<Integer>>> getEncodingTable () {
        return encodingTable;
    }

    public static void setHuffmanTreeTable(HashMap<String, CodeTree> huffmanTreeTableInput) {
        huffmanTreeTable = huffmanTreeTableInput;
    }

    public static void setEncodingTable (HashMap<String, List<List<Integer>>>  encodingTableInput) {
        encodingTable = encodingTableInput;
    }

    public static void createHuffmanTreeTable() {
        for (Map.Entry<String, ContextStats> entry : statistics.entrySet()) {
            entry.getKey();
            entry.getValue();
        }
    }

    public static void createEncodingTable() {
    }
}
