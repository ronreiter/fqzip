import java.io.*;

public class QualityLearner implements Compressor {

    private ObjectOutputStream outputStream;
    private ContextDictionary dictionary;
    private int reads = 0;

    public QualityLearner(ContextDictionary dictionary) {
        this.dictionary = dictionary;
        this.dictionary.startLearning();
    }

    public void compressNext(ReadData data) throws IOException {
        this.dictionary.learn(data);
        this.reads++;
    }

    public void setOutput(OutputStream output) {
        try {
            this.outputStream = new ObjectOutputStream(output);
        } catch (IOException e) {
            System.out.println("Error creating the object output stream.");
        }
    }

    private void createHuffmanTreeTable() {
        this.dictionary.createHuffmanTreeTable();
    }

    private void createEncodingTable() {
        this.dictionary.createEncodingTable();
    }
    
    public void closeOutput() throws IOException {

        createHuffmanTreeTable();
        createEncodingTable();

        save();

        this.outputStream.close();
    }

    private void save() throws IOException {
        dictionary.writeDictionaryToFile(new FileOutputStream("tree.out"));
    }
}

