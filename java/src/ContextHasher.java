import com.sun.xml.internal.ws.util.StringUtils;

import java.util.ArrayList;
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
        String context = "" + (position / POSITION_DIVISION) + ":";
    
        for (int i = position - SEQUENCE_BACKWARDS; i < Math.min(position + SEQUENCE_FORWARD, 100); i++) {
            context += sequenceContext.charAt(i);
        }
        context += ":";



        List<Integer> qualities = new ArrayList<Integer> ();
        for (int i = Math.max((position - QUALITY_BACKWARDS), 0); i < position + QUALITY_BACKWARDS; i++) {

            qualities.add((((int)qualityContext.charAt(i) - 33) / QUALITY_DIVISION));
        }
        context += ":" + Utils.join(qualities,",");

        System.out.println(context);
         return context;


            
    }
    
    public static void main (String[] args) {
        new ContextHasher().hashContext(10, "AGTAACGAGAGATCTACGACTGCATCACTACGACTACGACTCGACTACTG", "$%%^^$GSDSDSD$SA$SAS$^S$SA#A$SAS^ASA$S^A");
    }


}

