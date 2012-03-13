import org.apache.tools.bzip2.CBZip2InputStream;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;

/**
 * Created by IntelliJ IDEA. User: ron Date: 12/14/11 Time: 1:15 AM To change
 * this template use File | Settings | File Templates.
 */
public class HeaderDecompressor implements Decompressor {

    /*
    GZIPInputStream inputStream;

    BufferedReader reader;

    public void setInput(InputStream input) throws IOException {
        inputStream = new GZIPInputStream(input);
    }

    public void fillNext(ReadData data) throws IOException {

        byte[] arr = new byte[100];

        //int length = inputStream.read(arr,0,100);
        int length = inputStream.read(arr);
        
        if(length != -1)
            data.setHeader(new String(arr));
    }

    public void closeInput() throws IOException {
        reader.close();
    }*/

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
