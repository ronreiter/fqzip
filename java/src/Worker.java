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

        // a lazy exception handling. should be refactored someday.
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

        // we have a special compressor class called a learner to learn and create the huffman tree.
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

        // let's create 3 compressors we can use for the compression
        Compressor headerCompressor = new HeaderCompressor();
        Compressor sequenceCompressor = new SequenceCompressor();
        Compressor qualityCompressor = new QualityCompressor(Main.dictionary);

        headerCompressor.setOutput(new FileOutputStream(outputFile + "." + sequenceNumber  + ".headers"));
        sequenceCompressor.setOutput(new FileOutputStream(outputFile + "." + sequenceNumber  + ".sequence" ));
        qualityCompressor.setOutput(new FileOutputStream(outputFile + "." + sequenceNumber  + ".quality"));

        // as long as the manager gives us a read, we work
        while ((read = manager.getRead()) != null) {
            
            // after compressing all reads, the compressed output
            headerCompressor.compressNext(read);
            sequenceCompressor.compressNext(read);
            qualityCompressor.compressNext(read);
        }

        // when we finish writing the output, close the output files
        headerCompressor.closeOutput();
        sequenceCompressor.closeOutput();
        qualityCompressor.closeOutput();

    }
    
    private void decompress() throws IOException {
        // open the decompressors using the input files
        Decompressor headerDecompressor = new HeaderDecompressor();
        Decompressor sequenceDecompressor = new SequenceDecompressor();
        Decompressor qualityDecompressor = new QualityDecompressor(Main.dictionary);

        FileInputStream headerInput = new FileInputStream(inputFile + "." + sequenceNumber + ".headers");
        FileInputStream sequenceInput = new FileInputStream(inputFile + "." + sequenceNumber + ".sequence");
        FileInputStream qualityInput = new FileInputStream(inputFile + "." + sequenceNumber + ".quality");

        headerDecompressor.setInput(headerInput);
        sequenceDecompressor.setInput(sequenceInput);
        qualityDecompressor.setInput(qualityInput);

        while (true) {
            // create a single read block and fill it
            ReadData nextRead = new ReadData();

            // first we fill the headers
            headerDecompressor.fillNext(nextRead);

            if (nextRead.getHeader() == null) {
                break;
            }

            // if we've read a header, we have it in the read data. so we assume
            // we have a valid read to read.

            // we must fill in the sequences before the qualities so we have the context in the qualities!
            sequenceDecompressor.fillNext(nextRead);
            qualityDecompressor.fillNext(nextRead);

            // finally, write the read to the manager.
            // this technique will write the output in a random (but safe) order. we don't really
            // care about the order so this is OK. we might want to refactor and use a thread pool executor someday.
            manager.writeRead(nextRead);


        }

        // close the inputs and the output file.
        manager.closeWriter();

        headerDecompressor.closeInput();
        sequenceDecompressor.closeInput();
        qualityDecompressor.closeInput();
    }
}
