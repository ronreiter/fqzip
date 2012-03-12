import Huffman.BitInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: ron
 * Date: 12/14/11
 * Time: 1:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class QualityDecompressor implements Decompressor {
    private ContextDictionary dictionary;
    private BitInputStream bitInputStream;

    public void setInput(InputStream input) {
        bitInputStream = new BitInputStream(input);
    }

    public String getNext() {
        String context = null;
        dictionary.getHuffmanTree(context);
        return null;
    }

    public void closeInput() throws IOException {
        bitInputStream.close();
    }
}
