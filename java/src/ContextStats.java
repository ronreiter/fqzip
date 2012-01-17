import Huffman.CodeTree;
import Huffman.FrequencyTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ron
 * Date: 1/4/12
 * Time: 7:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContextStats {
    private int[] qualities;
    private int total;
    private CodeTree tree;
    private final char MAX_QUALITY_VALUE = 100;

    ContextStats() {
        this.qualities = new int[MAX_QUALITY_VALUE];

        // set all qualities to 1 because we can't use 0 as a statistic.
        // the huffman tree just won't build right.
        for (int i = 0; i < MAX_QUALITY_VALUE; i++) {
            this.qualities[i] = 1;
        }
        this.total = MAX_QUALITY_VALUE;

    }
    
    public void addStatistic(char index) {
        this.qualities[index]++;
        this.total++;
    }
    
    public int getTotal() {
        return total;
    }

    /**
     * using the statistical data, return a Huffman tree
     * which is used for the decompression process
     * @return CodeTree the encoding tree
     */
    public CodeTree buildTree() {
        FrequencyTable frequencyTable = new FrequencyTable(this.qualities);
        this.tree = frequencyTable.buildCodeTree();
        return this.tree;
    }

    /**
     * Builds an encoding table for the current context
     * (uses the Huffman tree created from the statistical data)
     * @return List<List<Integer>> an array of bit strings. Each
     * bit string refers to a specific quality score to encode.
     */
    public List<List<Integer>> buildEncodingTable() {
        List<List<Integer>> encodingTable = new ArrayList<List<Integer>>(MAX_QUALITY_VALUE);
        if (this.tree == null) {
            buildTree();
        }
        for (int i = 0; i < MAX_QUALITY_VALUE; i++) {
            encodingTable.add(this.tree.getCode(i));
        }
        return encodingTable;
    }
}
