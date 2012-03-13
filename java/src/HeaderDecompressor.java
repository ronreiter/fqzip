import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;

/**
 * Created by IntelliJ IDEA. User: ron Date: 12/14/11 Time: 1:15 AM To change
 * this template use File | Settings | File Templates.
 */
public class HeaderDecompressor implements Decompressor {
	DataInputStream input;
	HeaderBlock headerBlock;

	public void setInput(InputStream input) throws IOException {
		this.input = new DataInputStream(new GZIPInputStream(input));
	}

	public void fillNext(ReadData data) throws IOException {
		if (headerBlock == null) {
			headerBlock = new HeaderBlock(input);
		}
		String header = headerBlock.nextHeader();
		if (header != null) {
			data.setHeader(header);
		} else {
			headerBlock = null;
		}
	}

	public void closeInput() throws IOException {
		input.close();
	}

}
