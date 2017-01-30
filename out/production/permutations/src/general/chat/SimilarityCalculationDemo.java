package general.chat;

/**
 * Created by swaggerton on 11/01/16.
 */

import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.*;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;

import java.util.ArrayList;

public class SimilarityCalculationDemo {

    private static ILexicalDatabase db = new NictWordNet();
    private static RelatednessCalculator[] rcs = {
            new HirstStOnge(db), new LeacockChodorow(db), new Lesk(db),  new WuPalmer(db),
            new Resnik(db), new JiangConrath(db), new Lin(db), new Path(db)
    };

    private static double run( String word1, String word2 ) {
        WS4JConfiguration.getInstance().setMFS(true);
        RelatednessCalculator rc = rcs[1];
        double s = rc.calcRelatednessOfWords(word1, word2);
        System.out.println( rc.getClass().getName()+"\t"+s );
        return s;
    }
    public static void main(String[] args) {

    }
    public String best(String origional, ArrayList<String> syns)
    {
        double best  = 0;
        String closest = origional;
        for (String syn : syns)
        {
            double s = run(origional, syn);
            if(!syn.equals(origional) && s >= best)
            {
                best = s;
                closest = syn;

            }
        }
        return closest;
    }
}
