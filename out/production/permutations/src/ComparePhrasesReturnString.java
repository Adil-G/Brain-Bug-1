import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Adil on 2015-06-04.
 */
public class ComparePhrasesReturnString {
    public static String compare(String phrase0,String phrase1) throws IOException {
        String outcome=null;
        boolean atleastOneAlphaA = phrase0.matches(".*[a-zA-Z]+.*") && !phrase0.toLowerCase().contains("privacy policy")
        && !phrase0.toLowerCase().contains("sign up");
        boolean atleastOneAlphaB = phrase1.matches(".*[a-zA-Z]+.*") && !phrase1.toLowerCase().contains("privacy policy")
                && !phrase1.toLowerCase().contains("sign up");
        if(atleastOneAlphaA&&atleastOneAlphaB)
        {
            System.out.println("still processing ...");
            //FlagConfig flagConfig = FlagConfig.getFlagConfig(
            //        new String[] {"-elementalmethod", "orthographic", "-vectortype", "complex", "-seedlength", "100", phrase0.toLowerCase(), phrase1.toLowerCase()});
            //System.out.println("-"+phrase0);
            //System.out.println("-"+phrase1);
            //outcome = CompareTerms.runCompareTerms(flagConfig);
            //Compare c = new Compare(phrase0,phrase1);
            //outcome =c.getResult();
            String[] arrayA = phrase0.split(" ");
            String[] arrayB = phrase1.split(" ");
            Set<String> setA = new HashSet<String>(Arrays.asList(arrayA));
            Set<String> setB = new HashSet<String>(Arrays.asList(arrayB));
            setA.retainAll(setB);
            // - Math.abs(arrayA.length-arrayB.length);
            for(String x:setA)
            {
                System.out.println("INTESECTSSLAKFIOJOW: "+x);
                outcome = x;
            }
            //System.out.println(outcome);
        }

        return outcome;
    }
}
