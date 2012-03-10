import java.io.*;
import java.util.zip.ZipInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: ron
 * Date: 12/14/11
 * Time: 1:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class SequenceDecompressor implements Decompressor {
    BufferedReader reader;

    public void setInput(InputStream input) {
        reader = new BufferedReader(new InputStreamReader(new ZipInputStream(input)));
    }

    public String getNext() throws IOException {
        return reader.readLine();
    }

    public void closeInput() throws IOException {
        reader.close();
    }

}
