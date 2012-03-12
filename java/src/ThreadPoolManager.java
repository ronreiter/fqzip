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

    public enum Mode {LEARN, COMPRESS, DECOMPRESS}

    public ThreadPoolManager(int numOfThreads, String runMode, String inputFile) throws IOException{
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
            throw new IllegalArgumentException("Running Mode should be either LEARN/COMPRESS or DECOMPRESS");

        FileReader input = new FileReader(inputFile);
        reader =  new BufferedReader(input);
    }
    
    public void Run() {

        for (int i = 0 ; i < numberofthreads; i++) {

            Thread x = new Thread(new Worker(runningMode,i, this));
        }
    }

    public synchronized ReadData getRead() throws IOException {
        return new ReadData(reader);
    }
}
