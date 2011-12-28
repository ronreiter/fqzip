import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: barakjacob
 * Date: 12/28/11
 * Time: 10:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContextHasher {
    private byte[] sequence;

    ContextHasher() {
        
    }
    
    String hashContext(int position, byte[] sequenceContext, byte[] qualityContext) {
        return null;
    }

    void resetContext(byte[] sequence) {
        this.sequence = sequence;
    }

    String getNextContext(byte qualityContext) {
        return null;
    }
}
