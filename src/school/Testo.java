package school;

/**
 * Created by swaggerton on 06/11/15.
 */
public class Testo {
    public static void main(String args[])
    {
        System.out.println(pairStar("xxyy"));
    }
    public static String pairStar(String str) {

// GET STRING TO MODIFY
        // GET BOUNDERIES OF STRING INDEX
        int limit = str.length() - 1;
//  GET INDEX OF LAST COMPUTATION
        int last = str.lastIndexOf(".");
        if(last < 0)
        {
            last = 0;
        }//[)
        // GET INDEX OF  NEXT CHARACTER
        int next = last + 1;
        // BASE CASE:
        // hello.  // last = 5, length = 6
        if(last == limit)
        {
            return str.replaceAll("\\.", "");
        }
        else if(last == 0)
        {
            str = str.substring(0, last + 1) + "." + str.substring(last + 1, str.length());
            System.out.println(last + "##########" + str);
            return pairStar(str);
        }
        else if(str.charAt(next) == str.charAt(last - 1))
        {
            str = str.substring(0, last -1 + 2) + "*." + str.substring(last -1 + 2, str.length());
            return pairStar(str);
        }
        else
        {
            str = str.substring(0, last + 2) + "." + str.substring(last + 2, str.length());
            System.out.println(last + "##########" + str);
            return pairStar(str);
        }

    }
}
