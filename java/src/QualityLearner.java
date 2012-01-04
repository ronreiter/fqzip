import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: ron
 * Date: 12/14/11
 * Time: 1:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class QualityLearner implements Compressor {
    private OutputStream outputStream;
    private ContextDictionary dictionary;

    public void setOutput(OutputStream output) {
        //To change body of implemented methods use File | Settings | File Templates.
        outputStream = output;
    }

    public QualityLearner(ContextDictionary dictionary) {
        this.dictionary = dictionary;
        this.dictionary.startLearning();
    }

    public void compressNext(ReadData data) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
        this.dictionary.learn(data);
        outputStream.write(data.getQuality().getBytes());
        outputStream.write(10);

    }
    
    public void save(String decodingTreesFile, String encodingTablesFile) throws FileNotFoundException, IOException {
        ObjectOutputStream decodingTreesFileStream = new ObjectOutputStream(new FileOutputStream(decodingTreesFile));
        ObjectOutputStream encodingTablesFileStream = new ObjectOutputStream(new FileOutputStream(encodingTablesFile));

        decodingTreesFileStream.writeObject(this.dictionary.getHuffmanTreeTable());
        encodingTablesFileStream.writeObject(this.dictionary.getEncodingTable());

    }

    public void createHuffmanTreeTable() {
        this.dictionary.createHuffmanTreeTable();
    }

    public void createEncodingTable() {
        this.dictionary.createEncodingTable();
    }
}

