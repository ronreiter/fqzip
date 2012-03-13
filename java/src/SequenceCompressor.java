import org.apache.tools.bzip2.CBZip2OutputStream;

import java.io.IOException;
import java.io.OutputStream;

public class SequenceCompressor implements Compressor {
    CBZip2OutputStream outputStream;
    
    public void setOutput(OutputStream output) throws IOException {
        outputStream = new CBZip2OutputStream(output);
    }

    public void compressNext(ReadData data) throws IOException {
        byte[] sequenceData = data.getSequence().getBytes();
        outputStream.write(sequenceData);
    }

    public void closeOutput() throws IOException {
        outputStream.close();
    }

}
