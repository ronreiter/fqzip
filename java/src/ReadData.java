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
    private StringBuilder header;
    private StringBuilder sequence;
    private StringBuilder quality;

    ReadData(BufferedReader reader) throws IOException {
        StringBuilder header = new  StringBuilder(reader.readLine());
        StringBuilder sequence = new StringBuilder (reader.readLine());
        reader.readLine();
        StringBuilder quality =  new StringBuilder (reader.readLine());
    }
    public String getHeader() {
        return header.toString();
    }
    public String getSequence() {
        return sequence.toString();
    }
    public String getQuality() {
        return quality.toString();
    }
    public void appendCharToHeader(char character) {
      header.append(character);
    }
    public void appendCharToSequencechar (char character){
        sequence.append(character);
    }
    public void appendCharToQuality(char character) {
        quality.append(character);
    }
}
