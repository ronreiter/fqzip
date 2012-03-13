import org.apache.tools.bzip2.CBZip2OutputStream;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by IntelliJ IDEA. User: ron Date: 12/14/11 Time: 1:15 AM To change
 * this template use File | Settings | File Templates.
 */
public class HeaderCompressor implements Compressor {

	public static void debug() {

	}

	private static final int SUPERBLOCK_SIZE = 512;
	private DataOutputStream output;
	private int readRecordsInBlock = 0;
	private HeaderBlock headerBlock = null;

	@Override
	public void setOutput(OutputStream output) throws IOException {
		this.output = new DataOutputStream(new CBZip2OutputStream(output));
	}

	@Override
	public void compressNext(ReadData data) throws IOException {

		String header = data.getHeader();

		if (headerBlock == null) {
			headerBlock = new HeaderBlock(header);
		}

        readRecordsInBlock++;
        headerBlock.add(header);

        if (readRecordsInBlock == SUPERBLOCK_SIZE - 1) {
            headerBlock.serialize(output);
            readRecordsInBlock = 0;
            headerBlock = null;
        }

	}

	@Override
	public void closeOutput() throws IOException {
		if (headerBlock != null) {
			headerBlock.serialize(output);
		}
		output.close();
	}
}
