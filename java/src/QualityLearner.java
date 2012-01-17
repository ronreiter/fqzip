import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: ron
 * Date: 12/14/11
 * Time: 1:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class QualityLearner implements Compressor {
    private ObjectOutputStream outputStream;
    private ContextDictionary dictionary;
    private int reads = 0;

    public void setOutput(OutputStream output) {
        //To change body of implemented methods use File | Settings | File Templates.
        try {
            this.outputStream = new ObjectOutputStream(output);
        } catch (IOException e) {
            System.out.println("Error creating the object output stream.");
        }
    }

    public QualityLearner(ContextDictionary dictionary) {
        this.dictionary = dictionary;
        this.dictionary.startLearning();
    }

    public void compressNext(ReadData data) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
        this.dictionary.learn(data);
        this.reads++;
        if (this.reads % 10000 == 0) {
            System.out.println(Integer.toString(this.reads));

        }
    }
    
    private void save() throws IOException {
        outputStream.writeObject(this.dictionary);

    }

    private void createHuffmanTreeTable() {
        this.dictionary.createHuffmanTreeTable();
    }

    private void createEncodingTable() {
        this.dictionary.createEncodingTable();
    }
    
    public void closeOutput() throws IOException {
        this.createHuffmanTreeTable();
        this.createEncodingTable();
        this.save();
        this.outputStream.close();
    }
}

