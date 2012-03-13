import org.apache.tools.bzip2.CBZip2InputStream;

import java.io.*;

public class SequenceDecompressor implements Decompressor {
    CBZip2InputStream inputStream;

    BufferedReader reader;

    public void setInput(InputStream input) throws IOException {
        inputStream = new CBZip2InputStream(input);
    }

    public void fillNext(ReadData data) throws IOException {

        byte[] arr = new byte[100];

        int length = inputStream.read(arr,0,100);

        data.setSequence(length == 0 ? null : new String(arr));
    }

    public void closeInput() throws IOException {
        inputStream.close();
    }
}
