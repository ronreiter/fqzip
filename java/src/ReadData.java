import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: ron
 * Date: 12/14/11
 * Time: 12:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReadData {
    private String header;
    private String sequence;
    private String quality;

    ReadData(BufferedReader reader) throws IOException {
        header = reader.readLine();
        sequence = reader.readLine();
        reader.readLine();
        quality = reader.readLine();
    }
    public String getHeader() {
        return header;
    }
    public String getSequence() {
        return sequence;
    }
    public String getQuality() {
        return quality;
    }
}
