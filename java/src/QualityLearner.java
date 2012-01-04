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
    public void setOutput(OutputStream output) {
        //To change body of implemented methods use File | Settings | File Templates.
        outputStream = output;
    }

    public QualityLearner() {
        ContextDictionary.startLearning();
    }

    public void compressNext(ReadData data) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
        ContextDictionary.learn(data);
        outputStream.write(data.getQuality().getBytes());
        outputStream.write(10);

    }
    
    public void save(String decodingTreesFile, String encodingTablesFile) throws FileNotFoundException, IOException {
        ObjectOutputStream decodingTreesFileStream = new ObjectOutputStream(new FileOutputStream(decodingTreesFile));
        ObjectOutputStream encodingTablesFileStream = new ObjectOutputStream(new FileOutputStream(encodingTablesFile));

        decodingTreesFileStream.writeObject(ContextDictionary.getHuffmanTreeTable());
        encodingTablesFileStream.writeObject(ContextDictionary.getEncodingTable());

    }


    public void createHuffmanTreeTable() {
        ContextDictionary.createHuffmanTreeTable();
    }

    public void createEncodingTable() {
        ContextDictionary.createEncodingTable();
    }
}

