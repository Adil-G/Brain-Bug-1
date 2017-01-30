package SentenceGenerator;

import general.LuceneSnowBallTest;
import general.chat.MainGUI;
import general.graph.theory.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Adil on 2015-06-04.
 */
public class ComparePhrases {
    public static double compare(String phrase0,String phrase1) throws IOException {
        double outcome;//Authority control GND:
        boolean atleastOneAlphaA = phrase0.matches(".*[a-zA-Z]+.*") && !phrase0.toLowerCase().contains("authority control gnd")&&!phrase0.toLowerCase().contains("privacy policy")
                && !phrase0.toLowerCase().contains("sign up");
        boolean atleastOneAlphaB = phrase1.matches(".*[a-zA-Z]+.*") &&!phrase1.toLowerCase().contains("authority control gnd:")&& !phrase1.toLowerCase().contains("privacy policy")
                && !phrase1.toLowerCase().contains("sign up");
        boolean goodLength = Math.abs(phrase1.length() - phrase0.length()) < 50;
        if(atleastOneAlphaA&&atleastOneAlphaB && goodLength)
        {
            //System.out.println("still processing ...");
            String[] arrayA = phrase0.toLowerCase().split("[^\\w]+");
            String[] arrayB = phrase1.toLowerCase().split("[^\\w]+");
            Set<String> setA = new HashSet<String>(Arrays.asList(arrayA));
            Set<String> setB = new HashSet<String>(Arrays.asList(arrayB));
            setA.retainAll(setB);
            outcome = setA.size();
            int max = Math.max(arrayA.length, arrayB.length);
            int min = Math.min(arrayA.length, arrayB.length);
            Pattern pat = Pattern.compile(".*?(\\d+,[\\d,\\s]+).*?");
            Matcher match = pat.matcher(phrase1);
            if(match.matches())
                outcome+=(match.group(1).length());
            //outcome *= (double)min/(double)max;
            //int CLAMP = phrase0.length();
            //outcome = CLAMP*(Math.round(outcome/CLAMP));
        }
        else
        {
            outcome=0;
        }

        return outcome;
    }
    /*for (int i = 0; i < arrayA.length; i++) {
                        Vertex va = new Vertex(arrayA[i]);

                        for (int j = 0; j < arrayA.length; j++) {


                            Vertex vb = new Vertex(arrayA[j]);
                            graph0.addVertex(vb, false);
                            va.addNeighbor(new Edge(va,vb));
                        }
                        graph0.addVertex(va, false);
                    }
                    */

    public  HashSet<String> keyWords = new HashSet<>();
    public  HashSet<String> keyWordsVerbOrAdjective = new HashSet<>();
    public  HashMap<String,Integer> keyWordUniqueness = new HashMap<>();
    public  int textSize = 0;
    public  boolean hasAUniqueKeyWord = false;
    public  ArrayList<String> mostcommon = new ArrayList<>();
    public  HashMap<String, HashSet<String>> synMap = new HashMap();
    public  static final int EDGE_LENGTH =100 ;
    public static final int CREATION_EDGE_LENGTH =100 ;
    public  final HashSet<String> globalUsedWords = new HashSet<>();


    public static ArrayList<ParagraphInfo> rankAnswers(String query, ArrayList<ParagraphInfo> unsortedParagraphs, HashSet<KeyWordPattern> keyWords)
    {
        TreeMap<Double, HashSet<ParagraphInfo>> sorted = new TreeMap<>();

        for(ParagraphInfo paragraph : unsortedParagraphs)
        {
            double score = Math.abs(computeParagraph(paragraph.getInfo(),keyWords) - computeParagraph(query,keyWords));
            if(sorted.containsKey(score))
            {
                HashSet<ParagraphInfo> set = sorted.get(score);
                set.add(paragraph);
                sorted.put(score,set);
            }
            else
            {
                HashSet<ParagraphInfo> set = new HashSet<>();
                set.add(paragraph);
                sorted.put(score,set);
            }
        }
        ArrayList<ParagraphInfo> sortedList = new ArrayList<>();
        for(Map.Entry<Double,HashSet<ParagraphInfo>> entry : sorted.entrySet())
        {
            for(ParagraphInfo para : entry.getValue())
                sortedList.add(para);
        }
        return  sortedList;
    }
    public static double computeParagraph(String paragraph, HashSet<KeyWordPattern> keyWords)
    {
        HashSet<Integer> inteciesOfKeyWords = new HashSet<>();
        for(KeyWordPattern words : keyWords)
        {
            for(String word : words.getKeyWords()) {
                int index = paragraph.toLowerCase().indexOf(word.toLowerCase());
                inteciesOfKeyWords.add(index);
            }
        }
        int total = 0;
        for(int index : inteciesOfKeyWords)
        {
            total += index;
        }
        double average = (double) total / (double) inteciesOfKeyWords.size();

        double distanceFromAverage = 0.0;
        for(int index : inteciesOfKeyWords)
        {
            distanceFromAverage += Math.abs(index - average);
        }

        return distanceFromAverage;
    }

}
