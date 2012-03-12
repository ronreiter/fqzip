import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: ron
 * Date: 12/14/11
 * Time: 12:50 AM
 * To change this template use File | Settings | File Templates.
 */
public interface Decompressor {
    void setInput(InputStream input) throws IOException;
    void fillNext(ReadData data) throws IOException;
    void closeInput() throws IOException;
}
