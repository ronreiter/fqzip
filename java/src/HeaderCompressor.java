import org.apache.tools.bzip2.CBZip2OutputStream;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class HeaderCompressor implements Compressor {

    private DataOutputStream output;
    private HeaderBlock headerBlock = null;

    @Override
    public void setOutput(OutputStream output) throws IOException {
        this.output = new DataOutputStream(new CBZip2OutputStream(output));
    }

    @Override
    public void compressNext(ReadData data) throws IOException {

        String header = data.getHeader();

        if (headerBlock == null) {
            headerBlock = new HeaderBlock(header);
        }

        // try adding the header to the current block.
        // this might fail either because the superblock is full,
        // or if there is a mismatch.
        if (!headerBlock.add(header)) {
            headerBlock.serialize(output);
            headerBlock = new HeaderBlock(header);
        }
    }

    @Override
    public void closeOutput() throws IOException {
        if (headerBlock != null) {
            headerBlock.serialize(output);
        }
        output.close();
    }
}
