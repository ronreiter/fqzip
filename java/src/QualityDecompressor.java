import Huffman.BitInputStream;
import Huffman.CodeTree;
import Huffman.HuffmanDecoder;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ron
 * Date: 12/14/11
 * Time: 1:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class QualityDecompressor implements Decompressor {
    private ContextDictionary dictionary;
    private BitInputStream  bitInputStream;

    QualityDecompressor(ContextDictionary dictionary) {
        this.dictionary = dictionary;
    }

    public void setInput(InputStream input) {
        bitInputStream = new BitInputStream(input);
    }

    public void fillNext(ReadData data) throws IOException {
        if (data.getSequence() == null) {
            return;
        }

        for(int i = 0; i < data.getSequence().length(); i++) {
            // generate the current context
            String context = ContextHasher.hashContext(i, data.getSequence(), data.getQuality());

            // get the relevant Huffman tree (PPM encoding)
            CodeTree tree = dictionary.getHuffmanTree(context);

            // if no context, get the default tree
            if (tree == null) {
                tree = dictionary.getDefaultTree();
            }

            // create a new decoder using the tree
            HuffmanDecoder huffmanDec = new HuffmanDecoder(bitInputStream);
            huffmanDec.codeTree = tree;

            data.appendCharToQuality((char)(huffmanDec.read() + 33));
        }
    }

    public void closeInput() throws IOException {
        bitInputStream.close();
    }
}
