import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: ron
 * Date: 12/14/11
 * Time: 1:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class SequenceDecompressor implements Decompressor {
    GZIPInputStream inputStream;

    BufferedReader reader;

    public void setInput(InputStream input) throws IOException {
        inputStream = new GZIPInputStream(input);
    }

    public void fillNext(ReadData data) throws IOException {

        byte[] arr = new byte[100];

        int length = inputStream.read(arr,0,100);

        if(length != -1)
            data.setSequence(new String(arr));
    }

    public void closeInput() throws IOException {
        reader.close();
    }
}
