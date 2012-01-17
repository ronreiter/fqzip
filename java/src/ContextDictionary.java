import Huffman.CodeTree;
import Huffman.FrequencyTable;

import java.io.Serializable;
import java.util.ArrayList;
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
public class ContextDictionary implements Serializable {

    private transient Map<String, CodeTree> huffmanTreeTable;
    private transient Map<String, List<List<Integer>>> encodingTable;
    private transient Map<String, ContextStats> statistics;
    
    public void startLearning() {
        statistics = new HashMap<String, ContextStats>();
        huffmanTreeTable = new HashMap<String, CodeTree>();
    }
    
    public void learn(ReadData data) {
        String quality = data.getQuality();
        String sequence = data.getSequence();
        for (int i = 0; i < sequence.length(); i++) {
            String context = ContextHasher.hashContext(i, sequence, quality);
            ContextStats stats;

            // get the stats array for the current context. create it
            // if it doesn't exist.
            if (statistics.containsKey(context)) {
                stats = statistics.get(context);
            } else {
                stats = new ContextStats();
            }
            // add the statistical data.
            stats.addStatistic((char)(quality.charAt(i) - 33));
            statistics.put(context, stats);
        }
    }

    public CodeTree getHuffmanTree (String context) {
        return huffmanTreeTable.get(context);
    }
    
    public List<List<Integer>> getEncodingTable (String context) {
        return encodingTable.get(context);
    }

    public Map<String, CodeTree> getHuffmanTreeTable() {
        return huffmanTreeTable;
    }

    public Map<String, List<List<Integer>>> getEncodingTable () {
        return encodingTable;
    }

    public void setHuffmanTreeTable(Map<String, CodeTree> huffmanTreeTableInput) {
        huffmanTreeTable = huffmanTreeTableInput;
    }

    public void setEncodingTable (Map<String, List<List<Integer>>>  encodingTableInput) {
        encodingTable = encodingTableInput;
    }

    public void createHuffmanTreeTable() {

        for (Map.Entry<String, ContextStats> entry : statistics.entrySet()) {
            huffmanTreeTable.put(entry.getKey(), entry.getValue().buildTree());
        }
    }

    public void createEncodingTable() {
        encodingTable = new HashMap<String, List<List<Integer>>>();

        for (Map.Entry<String, ContextStats> entry : statistics.entrySet()) {
            encodingTable.put(entry.getKey(), entry.getValue().buildEncodingTable());
        }

    }

}
