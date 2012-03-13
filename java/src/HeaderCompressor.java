import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.BitSet;
import java.util.HashMap;
import java.util.zip.GZIPOutputStream;

/**
 * Created by IntelliJ IDEA. User: ron Date: 12/14/11 Time: 1:15 AM To change
 * this template use File | Settings | File Templates.
 */
public class HeaderCompressor implements Compressor {

    GZIPOutputStream outputStream;

    public void setOutput(OutputStream output) throws IOException {
        outputStream = new GZIPOutputStream(output);
    }

    public void compressNext(ReadData data) throws IOException {
        outputStream.write(data.getHeader().getBytes());
    }

    public void closeOutput() throws IOException {
        outputStream.close();
    }

    /*
	public static void debug() {

	}

	private static final int SUPERBLOCK_SIZE = 512;
	private DataOutputStream output;
	private int readRecordsInBlock = 0;
	private HeaderBlock headerBlock;

	@Override
	public void setOutput(OutputStream output) {
		this.output = new DataOutputStream(output);
	}

	@Override
	public void compressNext(ReadData data) throws IOException {

		String header = data.getHeader();

		if (readRecordsInBlock == 0) {
			headerBlock = new HeaderBlock(header);
			readRecordsInBlock++;
		} else {
			headerBlock.add(header);
			readRecordsInBlock++;
			if (readRecordsInBlock == SUPERBLOCK_SIZE - 1) {
				headerBlock.serialize(output);
				readRecordsInBlock = 0;
			}
		}
	}

	@Override
	public void closeOutput() throws IOException {
		if (headerBlock != null) {
			headerBlock.serialize(output);
		}
		output.close();
	}*/
}
