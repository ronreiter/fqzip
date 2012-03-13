import org.apache.tools.bzip2.CBZip2InputStream;

import java.io.*;

public class HeaderDecompressor implements Decompressor {
	DataInputStream input;
	HeaderBlock headerBlock;

	public void setInput(InputStream input) throws IOException {
		this.input = new DataInputStream(new CBZip2InputStream(input));
	}

	public void fillNext(ReadData data) throws IOException {
		if (headerBlock == null) {
			headerBlock = new HeaderBlock(input);
		}

		String header = headerBlock.nextHeader();

        if (header == null) {
            headerBlock = new HeaderBlock(input);
            header = headerBlock.nextHeader();
        }

        data.setHeader(header);

	}

	public void closeInput() throws IOException {
		input.close();
	}

}
