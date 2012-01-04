import java.io.BufferedOutputStream;
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
public class QualityCompressor implements Compressor {
    private OutputStream outputStream;
    private ContextDictionary dictionary;

    QualityCompressor(ContextDictionary dictionary) {
        this.dictionary = dictionary;
    }

    public void setOutput(OutputStream output) {
        //To change body of implemented methods use File | Settings | File Templates.
        outputStream = output;
    }

    public void compressNext(ReadData data) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
        outputStream.write(data.getQuality().getBytes());
        outputStream.write(10);

    }




}

