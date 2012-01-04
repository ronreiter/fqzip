import com.sun.xml.internal.ws.util.StringUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: barakjacob
 * Date: 12/28/11
 * Time: 10:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContextHasher {
    private final int SEQUENCE_FORWARD = 3;
    private final int SEQUENCE_BACKWARDS = 3;
    private final int QUALITY_BACKWARDS = 3;
    private final int POSITION_DIVISION = 1;
    private final int QUALITY_DIVISION = 1;


    ContextHasher() {

    }

    String hashContext(int position, String sequenceContext, String qualityContext) {
      String context = "" + (position / POSITION_DIVISION);

      for (int i = position - SEQUENCE_BACKWARDS; i < position + SEQUENCE_FORWARD; i++ ) {

          if (i > 0 && i < 99 ) {
              context += sequenceContext.charAt(i);
          }
      }


      
              
    }


}
