import sun.rmi.runtime.NewThreadAction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Dan Benjamin
 * Date: 3/12/12
 * Time: 5:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class ThreadPoolManager {
    
    private int numberofthreads;
    private Mode runningMode;
    private BufferedReader reader;
    private String outputFile;

    public enum Mode {LEARN, COMPRESS, DECOMPRESS}

    public ThreadPoolManager(int numOfThreads, String runMode, String inputFile, String outputFile) throws IOException{
        this.numberofthreads = numOfThreads;

        if(runMode.equals("learn")) {
            runningMode = Mode.LEARN;
        }
        else if(runMode.equals("compress")) {
            runningMode = Mode.COMPRESS;
        }
        else if(runMode.equals("decompress")) {
            runningMode = Mode.DECOMPRESS;
        }
        else
            throw new IllegalArgumentException("Running mode should be either be learn, compress or decompress!");

        reader = new BufferedReader(new FileReader(inputFile));
    }
    
    public void start() {

        for (int i = 0 ; i < numberofthreads; i++) {
            Thread x = new Thread(new Worker(i, this));
        }
    }

    public synchronized ReadData getRead() throws IOException {
        return new ReadData(reader);
    }
    
    public synchronized Mode getRunningMode() {
        return runningMode;
    }
    
    public synchronized String getOutputFileName() {
        return outputFile;
    }
}
