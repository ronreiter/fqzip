import com.sun.xml.internal.bind.v2.model.impl.RuntimeModelBuilder;

import java.io.*;
import java.util.Vector;

import ThreadPoolManager.Mode;

public class Worker implements Runnable {

    private ThreadPoolManager manager;
    private ThreadPoolManager.Mode runningMode;
    private int sequenceNumber;
    private QualityLearner qualityLearner;
    private String outputFile;

    public Worker(Mode runMode, int sequenceNumber, ThreadPoolManager manager, String outputFile) {
        this.manager = manager;
        this.sequenceNumber = sequenceNumber;
        this.runningMode = runMode;

        qualityLearner = new QualityLearner(Main.dictionary);
    }

    @Override
    public void run() {
        try {
            switch (runningMode) {
                case LEARN:
                    learn(qualityLearner, bufferedInput);
                    break;

                case COMPRESS:
                    compress();
                    break;

                case DECOMPRESS:
                    break;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
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

    private void compress() throws IOException {
        Compressor headerCompressor = new HeaderCompressor();
        Compressor sequenceCompressor = new SequenceCompressor();
        Compressor qualityCompressor = new QualityCompressor(Main.dictionary);

        headerCompressor.setOutput(new FileOutputStream(this.outputFile + "." + this.sequenceNumber  + ".headers" ));
        sequenceCompressor.setOutput(new FileOutputStream(this.outputFile + "." + this.sequenceNumber  + ".sequence" ));
        qualityCompressor.setOutput(new FileOutputStream(this.outputFile + "." + this.sequenceNumber  + ".quality"));

        while (bufferedInput.ready()) {
            // get a new read from the input
            ReadData read = new ReadData(bufferedInput);
            headerCompressor.compressNext(read);
            sequenceCompressor.compressNext(read);
            qualityCompressor.compressNext(read);
        }

        qualityCompressor.closeOutput();
    }
}
