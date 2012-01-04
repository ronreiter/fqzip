import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: ron
 * Date: 12/14/11
 * Time: 1:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class QualityLearner implements Compressor {
    private OutputStream outputStream;
    private ContextDictionary dictionary;
    public void setOutput(OutputStream output) {
        //To change body of implemented methods use File | Settings | File Templates.
        outputStream = output;
        dictionary = new ContextDictionary();
    }

    public void compressNext(ReadData data) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
        dictionary.learn(data);
        outputStream.write(data.getQuality().getBytes());
        outputStream.write(10);

    }




}

