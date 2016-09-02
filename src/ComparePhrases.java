import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Adil on 2015-06-04.
 */
public class ComparePhrases {
    public static double compare(String phrase0,String phrase1) throws IOException {
        double outcome;
        boolean atleastOneAlphaA = phrase0.matches(".*[a-zA-Z]+.*") && !phrase0.toLowerCase().contains("privacy policy")
        && !phrase0.toLowerCase().contains("sign up");
        boolean atleastOneAlphaB = phrase1.matches(".*[a-zA-Z]+.*") && !phrase1.toLowerCase().contains("privacy policy")
                && !phrase1.toLowerCase().contains("sign up");
        if(atleastOneAlphaA&&atleastOneAlphaB)
        {
            //System.out.println("still processing ...");
            String[] arrayA = phrase0.split(" ");
            String[] arrayB = phrase1.split(" ");
            Set<String> setA = new HashSet<String>(Arrays.asList(arrayA));
            Set<String> setB = new HashSet<String>(Arrays.asList(arrayB));
            setA.retainAll(setB);
            outcome = setA.size();// - Math.abs(arrayA.length-arrayB.length);
            //System.out.println(outcome);
        }
        else
        {
            outcome=0;
        }

        return outcome;
    }
}
