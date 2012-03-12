import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Dan Benjamin
 * Date: 3/12/12
 * Time: 5:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class ThreadPoolManager {

    private int numOfThreads;
    private String runMode;
    private Mode runningMode;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String inputFile;
    private String outputFile;

    public enum Mode {LEARN, COMPRESS, DECOMPRESS}

    public ThreadPoolManager(int numOfThreads, String runMode, String inputFile, String outputFile) throws IOException{

        this.numOfThreads = numOfThreads;
        this.runMode = runMode;
        this.inputFile = inputFile;
        this.outputFile = outputFile;

        if(runMode.equals("learn")) {
            reader = new BufferedReader(new FileReader(inputFile));
            runningMode = Mode.LEARN;
        }
        else if(runMode.equals("compress")) {
            reader = new BufferedReader(new FileReader(inputFile));
            runningMode = Mode.COMPRESS;
        }
        else if(runMode.equals("decompress")) {
            writer = new BufferedWriter(new FileWriter(outputFile));
            runningMode = Mode.DECOMPRESS;
        }
        else
            throw new IllegalArgumentException("Running mode should be either be learn, compress or decompress!");

    }
    
    public void start() throws InterruptedException {
        List<Thread> threadList = new ArrayList<Thread>();
        
        System.err.println("ThreadPoolManager started on " + runMode + " with " + numOfThreads + " threads.");
        for (int i = 0 ; i < numOfThreads; i++) {
            Thread thread = new Thread(new Worker(i, this));
            threadList.add(thread);
            thread.start();
        }

        // wait for threads to complete
        for (int i = 0; i < numOfThreads; i++) {
            threadList.get(i).join();
        }
        System.err.println("ThreadPoolManager complete.");
    }

    public synchronized ReadData getRead() throws IOException {
        ReadData nextRead = new ReadData(reader);

        if (nextRead.getHeader() != null) {
            return nextRead;
        }

        return null;
    }
    
    public synchronized void writeRead(ReadData readData) throws IOException {
        readData.write(writer);
    }
    public Mode getRunningMode() {
        return runningMode;
    }
    public String getInputFileName() {
        return inputFile;
    }
    public String getOutputFileName() {
        return outputFile;
    }
}
