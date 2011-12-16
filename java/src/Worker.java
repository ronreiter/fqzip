import java.io.*;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: ron
 * Date: 12/14/11
 * Time: 12:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class Worker {
    public static void run(String inputFile, String outputFile) throws FileNotFoundException, IOException {
        FileReader input = new FileReader(inputFile);
        BufferedReader bufferedInput = new BufferedReader(input);
        
        Vector<Compressor> compressors = new Vector<Compressor>();
        Compressor headerCompressor = new HeaderCompressor();
        Compressor sequenceCompressor = new SequenceCompressor();
        Compressor qualityCompressor = new QualityCompressor();

        headerCompressor.setOutput(new FileOutputStream("headers.out"));
        sequenceCompressor.setOutput(new FileOutputStream("sequences.out"));
        qualityCompressor.setOutput(new FileOutputStream("qualities.out"));

        while (bufferedInput.ready()) {
            // get a new read from the input
            ReadData read = new ReadData(bufferedInput);
            headerCompressor.compressNext(read);
            sequenceCompressor.compressNext(read);
            qualityCompressor.compressNext(read);

        }
    }

}