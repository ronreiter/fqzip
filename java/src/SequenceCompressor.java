import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: ron
 * Date: 12/14/11
 * Time: 1:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class SequenceCompressor implements Compressor {
    GZIPOutputStream outputStream;
    
    public void setOutput(OutputStream output) throws IOException {
        outputStream = new GZIPOutputStream(output);
    }

    public void compressNext(ReadData data) throws IOException {
        outputStream.write(data.getSequence().getBytes());
    }

    public void closeOutput() throws IOException {
        outputStream.close();
    }

}
