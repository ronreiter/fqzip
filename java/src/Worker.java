import java.io.*;

public class Worker implements Runnable {

    private ThreadPoolManager manager;
    private ThreadPoolManager.Mode runningMode;
    private int sequenceNumber;
    private String outputFile;
    private String inputFile;
    
    public Worker(int sequenceNumber, ThreadPoolManager manager) {
        this.manager = manager;
        this.sequenceNumber = sequenceNumber;
        this.runningMode = manager.getRunningMode();
        this.outputFile = manager.getOutputFileName();
        this.inputFile = manager.getInputFileName();
    }

    @Override
    public void run() {

        System.err.println("Worker #" + sequenceNumber + " started.");

        try {
            switch (runningMode) {
                case LEARN:
                    learn();
                    break;

                case COMPRESS:
                    compress();
                    break;

                case DECOMPRESS:
                    decompress();
                    break;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

        System.err.println("Worker #" + sequenceNumber + " complete.");

    }

    private void learn() throws IOException {
        QualityLearner qualityLearner = new QualityLearner(Main.dictionary);

        qualityLearner.setOutput(new FileOutputStream(Main.TREE_FILENAME));
        ReadData read;

        while ((read = manager.getRead()) != null) {
            // get a new read from the input
            qualityLearner.compressNext(read);
        }

        qualityLearner.closeOutput();
    }

    private void compress() throws IOException {
        ReadData read;

        Compressor headerCompressor = new HeaderCompressor();
        Compressor sequenceCompressor = new SequenceCompressor();
        Compressor qualityCompressor = new QualityCompressor(Main.dictionary);

        headerCompressor.setOutput(new FileOutputStream(outputFile + "." + sequenceNumber  + ".headers" ));
        sequenceCompressor.setOutput(new FileOutputStream(outputFile + "." + sequenceNumber  + ".sequence" ));
        qualityCompressor.setOutput(new FileOutputStream(outputFile + "." + sequenceNumber  + ".quality"));

        while ((read = manager.getRead()) != null) {
            
            // get a new read from the input
            headerCompressor.compressNext(read);
            sequenceCompressor.compressNext(read);
            qualityCompressor.compressNext(read);
        }

        headerCompressor.closeOutput();
        sequenceCompressor.closeOutput();
        qualityCompressor.closeOutput();
    }
    
    private void decompress() throws IOException {
        Decompressor headerDecompressor = new HeaderDecompressor();
        Decompressor sequenceDecompressor = new SequenceDecompressor();
        Decompressor qualityDecompressor = new QualityDecompressor(Main.dictionary);

        headerDecompressor.setInput(new FileInputStream(inputFile + "." + sequenceNumber + ".headers"));
        sequenceDecompressor.setInput(new FileInputStream(inputFile + "." + sequenceNumber + ".sequence"));
        qualityDecompressor.setInput(new FileInputStream(inputFile + "." + sequenceNumber + ".quality"));

        while (true) {
            ReadData nextRead = new ReadData();

            headerDecompressor.fillNext(nextRead);
            sequenceDecompressor.fillNext(nextRead);
            qualityDecompressor.fillNext(nextRead);
            
            if (nextRead.getHeader() == null) {
                break;
            }

            manager.writeRead(nextRead);            
        }

    }
}
