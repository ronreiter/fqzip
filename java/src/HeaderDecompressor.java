import java.io.*;
import java.util.zip.ZipInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: ron
 * Date: 12/14/11
 * Time: 1:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class HeaderDecompressor implements Decompressor {
    BufferedReader reader;

    public void setInput(InputStream input) {
        reader = new BufferedReader(new InputStreamReader(new ZipInputStream(input)));
    }

    public void fillNext(ReadData data) throws IOException {

    }

    public void closeInput() throws IOException {
        reader.close();
    }

}
