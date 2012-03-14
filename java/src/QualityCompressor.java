import Huffman.BitOutputStream;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

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
        // we use the sequence length just to get it to conform with the decompression.
        for(int i = 0; i < data.getSequence().length(); i++) {
            // generate the current context string
            String context = ContextHasher.hashContext(i, data.getSequence(), data.getQuality());

            // get the relevant encoding table
            List<List<Integer>> encodingTable = dictionary.getEncodingTable(context);

            // if there's no context, use a default encoding table (generated from the default tree)
            if (encodingTable == null) {
                encodingTable = dictionary.getDefaultEncodingTable();
            }

            // write out the bits
            List<Integer> encodeBits = encodingTable.get(data.getQuality().charAt(i) - 33);

            for(int bit : encodeBits) {
                bitOutputStream.write(bit);
            }
        }
    }
}

