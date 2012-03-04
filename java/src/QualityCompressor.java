import Huffman.BitOutputStream;

import java.io.BufferedOutputStream;
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
public class QualityCompressor implements Compressor {
    private ContextDictionary dictionary;
    private BitOutputStream bitOutputStream;

    /**
     * 
     * @param dictionary
     */
    QualityCompressor(ContextDictionary dictionary) {
        this.dictionary = dictionary;
    }

    public void setOutput(OutputStream output) {
        //To change body of implemented methods use File | Settings | File Templates.
        bitOutputStream = new BitOutputStream(output);
        
    }

    public void closeOutput() throws IOException {
        bitOutputStream.close();
    }

    public void compressNext(ReadData data) throws IOException {
        for(int i = 0; i < data.getQuality().length(); i++) {
            String context = ContextHasher.hashContext(i, data.getSequence(), data.getQuality());
            List<List<Integer>> encodingTable = dictionary.getEncodingTable(context);
            List<Integer> encodeBits = encodingTable.get(data.getQuality().charAt(i) - 33);

            for(int bit : encodeBits) {
                bitOutputStream.write(bit);
            }
        }
    }





}

