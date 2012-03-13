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

    public void fillNext(ReadData data) {
        
        for(int i = 0; i < data.getSequence().length(); i++) {
            String context = ContextHasher.hashContext(i, data.getSequence(), data.getQuality());
            CodeTree tree =   dictionary.getHuffmanTree(context);
            
            HuffmanDecoder huffmanDec = new HuffmanDecoder(bitInputStream);
            huffmanDec.codeTree = tree;

            if(huffmanDec.codeTree == null) {
                System.out.print("hi");
            }

            try {
            data.appendCharToQuality((char)(huffmanDec.read() + 33));
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }

        }
    }

    public void closeInput() throws IOException {
        bitInputStream.close();
    }
}
