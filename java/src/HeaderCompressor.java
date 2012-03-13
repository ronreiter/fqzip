import org.apache.tools.bzip2.CBZip2OutputStream;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.BitSet;
import java.util.HashMap;
import java.util.zip.GZIPOutputStream;

/**
 * Created by IntelliJ IDEA. User: ron Date: 12/14/11 Time: 1:15 AM To change
 * this template use File | Settings | File Templates.
 */
public class HeaderCompressor implements Compressor {

    public static void debug() {

    }

    private DataOutputStream output;
    private int readRecordsInBlock = 0;
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

    /*GZIPOutputStream outputStream;

    public void setOutput(OutputStream output) throws IOException {
        outputStream = new GZIPOutputStream(output);
    }

    public void compressNext(ReadData data) throws IOException {
        outputStream.write(data.getHeader().getBytes());
    }

    public void closeOutput() throws IOException {
        outputStream.close();
    }/*
}
