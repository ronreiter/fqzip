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
    private static final int SEQUENCE_FORWARD = 1;
    private static final int SEQUENCE_BACKWARDS = 1;
    private static final int QUALITY_BACKWARDS = 1;
    private static final int POSITION_DIVISION = 20;
    private static final int QUALITY_DIVISION = 10;
    private static final int SEQUENCE_SIZE = 100;

    ContextHasher() {

    }
    /**
     *
     * @param position
     * @param sequenceContext
     * @param qualityContext
     * @return
     */
     public static String hashContext(int position, String sequenceContext, String qualityContext) {
        String context = "" + (position / POSITION_DIVISION) + ":";
    
        for (int i = Math.max(position - SEQUENCE_BACKWARDS, 0);
             i <= Math.min(position + SEQUENCE_FORWARD, SEQUENCE_SIZE - 1); i++) {
            context += sequenceContext.charAt(i);
        }

        List<Integer> qualities = new ArrayList<Integer> ();
        for (int i = Math.max((position - QUALITY_BACKWARDS), 0); i < position; i++) {

            qualities.add((((int)qualityContext.charAt(i) - 33) / QUALITY_DIVISION));
        }
        context += ":" + Utils.join(qualities,",");

         return context;
    }
}

