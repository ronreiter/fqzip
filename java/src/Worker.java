import java.io.*;
import java.util.Vector;

public class Worker {

    private enum Mode {LEARN, COMPRESS, DECOMPRESS}
    Mode runningMode;
    private int numberOfThreads;

    public Worker(String mode) {
        if(mode.equals("learn")) {
            runningMode = Mode.LEARN;
        }
        else if(mode.equals("compress")) {
            runningMode = Mode.COMPRESS;
        }
        else if(mode.equals("decompress")) {
            runningMode = Mode.DECOMPRESS;
        }
        else
            throw new IllegalArgumentException("Running Mode should be either LEARN/COMPRESS or DECOMPRESS");
    }
    
    public Worker(String mode, int numberOfThreads) {
        this(mode);
        this.numberOfThreads = numberOfThreads;
    }

    public void run(String inputFile, String outputFile) throws FileNotFoundException, IOException {
        FileReader input = new FileReader(inputFile);
        BufferedReader bufferedInput = new BufferedReader(input);

        Vector<Compressor> compressors = new Vector<Compressor>();
        QualityLearner qualityLearner = new QualityLearner(Main.dictionary);

        switch (runningMode) {
            case LEARN:
                learn(qualityLearner, bufferedInput);
                break;

        case COMPRESS:
            compress(bufferedInput);
        break;

        case DECOMPRESS:
            break;
    }
}

    private void learn(QualityLearner qualityLearner, BufferedReader bufferedInput) throws IOException {

        qualityLearner.setOutput(new FileOutputStream("tree.out"));

        while (bufferedInput.ready()) {
            // get a new read from the input
            ReadData read = new ReadData(bufferedInput);
            qualityLearner.compressNext(read);
        }

        qualityLearner.closeOutput();
    }

    private void compress(BufferedReader bufferedInput) throws IOException {
        Compressor headerCompressor = new HeaderCompressor();
        Compressor sequenceCompressor = new SequenceCompressor();
        Compressor qualityCompressor = new QualityCompressor(Main.dictionary);

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

        qualityCompressor.closeOutput();
    }



    private void runInThreadPool(Mode runningMode) {



    }
}
