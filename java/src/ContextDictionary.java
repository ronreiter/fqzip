import Huffman.CodeTree;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContextDictionary implements Serializable {

    private  Map<String, CodeTree> huffmanTreeTable;
    private  Map<String, List<List<Integer>>> encodingTable;
    private  Map<String, ContextStats> statistics;
    
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
    
    public void readDictionaryFromFile(InputStream inputFile) throws IOException {

        // Read object using ObjectInputStream
        ObjectInputStream obj_in =
                new ObjectInputStream (inputFile);

        try {
            Main.dictionary = (ContextDictionary) obj_in.readObject();
        }
        catch(Exception e) {
            throw new IllegalArgumentException("Input Context Dictionary is invalid");
        }
    }

    public void writeDictionaryToFile(OutputStream outputFile) throws IOException {
        ObjectOutputStream outputStream;
        try {
            outputStream = new ObjectOutputStream(outputFile);
            outputStream.writeObject(this);
        } catch (IOException e) {
            System.out.println("Error creating the object output stream.");
            System.out.println(e);
        }
    }
}