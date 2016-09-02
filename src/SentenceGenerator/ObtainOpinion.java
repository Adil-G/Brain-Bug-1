package SentenceGenerator;

import general.FindKeyWordsTest;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by corpi on 2016-04-23.
 */
public class ObtainOpinion {
    static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
    public static String bodyText = "";
    public static String urlText = "";
    static ArrayList<String> info  = new ArrayList<String>();
    public static void excecute(String input) throws Exception {
        //processPage(input);
        ArrayList<String> results=  GoogleCSAPI.getLinks(input.trim().replaceAll("\\s","+"),true);
        for(String result :results)
        {
            info.add(result);
        }
        searchResult();
    }
    public static void searchResult()
    {
        ArrayList<String> chunks = new ArrayList<String>();
        Map<String, HashSet<String>> firstCluster = new HashMap<String, HashSet<String>>();
        List<String> list = new ArrayList<String>();
        try {

            for (String url : info) {
                // Download the HTML and store in a Document


                try {

                    Document doc = Jsoup.connect(url).get();
                    System.out.println("WAITING FOR JSOUP");
                    // Select the <p> Elements from the document
                    Elements paragraphs = doc.select("p");
                    System.out.println("WAITING FOR SELECT(P)");
                    // For each selected <p> element, print out its text
                    int MAX = 30;
                    int count = 0;
                    for (Element p : paragraphs) {
                        //System.out.println(e.text());
                        if (count++<MAX&&!p.text().contains(":") && !p.text().toLowerCase().contains("welcome") && !p.text().toLowerCase().contains("is a song")) {
                            bodyText += p.text();
                        }
                    }
                    url = url.substring(url.lastIndexOf('/')).replaceAll("_-"," ");
                    String s = url;
                    String[] words = s.split("[^\\w]+");
                    for (int i = 0; i < words.length; i++) {
                        // You may want to check for a non-word character before blindly
                        // performing a replacement
                        // It may also be necessary to adjust the character class
                        //words[i] = words[i].replaceAll("[^\\w]", "");
                        urlText += words[i] + " ";
                    }
                } catch (IOException j) {
                    System.out.println("Failed url connection." + url + " Trying another one.");
                    j.printStackTrace();
                } catch (Exception g) {
                    System.out.println("Failed url connection." + url + " Trying another one.");
                    g.printStackTrace();
                }
            }
        }catch (Exception l){}
    }
    public static String mostCommon(ArrayList<String> s1, String textBody)
    {
        Map<String,Integer> words_count = new HashMap<String,Integer>();

//read your line (you will have to determine if this line should be split or is equations
//also just noticed that the trailing '!' would need to be removed

        String[] words = textBody.toLowerCase().split("[^\\w]+");
        for(int i=0;i<words.length;i++)
        {
            String s = words[i];
            if(words_count.keySet().contains(s))
            {
                Integer count = words_count.get(s) + 1;
                words_count.put(s, count);
            }
            else
                words_count.put(s, 1);

        }
        String best = "";
        int bestc = 0;
        for(String x : s1)
        try {
            x = x.toLowerCase();
            if (words_count.get(x) >bestc)
            {
                best = x;
                bestc = words_count.get(x);
            }
        }catch (Exception e){}
        return best;
    }
    final static boolean POSITIVE = true;
    final static boolean NEGATIVE = false;
    public static String processnegativeOpinion(HashSet<String> input) throws Exception {
        //String text = readFile("C:\\Users\\corpi\\Documents\\robot\\permutations-december-25\\permutations\\src\\SentenceGenerator\\Input.txt",
        //      StandardCharsets.UTF_8);
        ArrayList<String> BodyWords = new ArrayList<String>();
        BodyWords = new ArrayList<>(input);

        String best = poissonOpinion(BodyWords, 1.0, ObtainOpinion.NEGATIVE);
        System.out.println("final result = " +best + "\nconnotation = " + SentiWordNetDemoCode.connotationGeneral(best));
        return best;
    }
    public static String processPositiveOpinion(HashSet<String> input) throws Exception {
        //String text = readFile("C:\\Users\\corpi\\Documents\\robot\\permutations-december-25\\permutations\\src\\SentenceGenerator\\Input.txt",
        //      StandardCharsets.UTF_8);
        ArrayList<String> BodyWords = new ArrayList<String>();
        BodyWords = new ArrayList<>(input);

        String best = poissonOpinion(BodyWords, 1.0, ObtainOpinion.POSITIVE);
        System.out.println("final result = " +best + "\nconnotation = " + SentiWordNetDemoCode.connotationGeneral(best));
        return best;
    }
    //What was the name of the first electronic general-purpose computer
    public static String getPopularOpinions(String input) throws Exception {
        //String text = readFile("C:\\Users\\corpi\\Documents\\robot\\permutations-december-25\\permutations\\src\\SentenceGenerator\\Input.txt",
        //      StandardCharsets.UTF_8);
        bodyText = "";
        excecute(input);
        ArrayList<String> BodyWords = new ArrayList<String>();
        BodyWords = new ArrayList<>(Arrays.asList(bodyText.split("[\\.\\?!]+")));

        String best = poissonOpinion(BodyWords, 1.0, ObtainOpinion.POSITIVE);
        System.out.println("final result = " +best + "\nconnotation = " + SentiWordNetDemoCode.connotationGeneral(best));
        return best;
    }
    public static String poissonOpinion(ArrayList<String> input,double alignment, boolean opinion) throws IOException {
        String bestMatch = "";
        double bestAnalysis;

        if(opinion == ObtainOpinion.POSITIVE)
            bestAnalysis = -10000;
        else
            bestAnalysis = 10000;
        input = SentiWordNetDemoCode.bestPhrases(input, 10);
        for(String sentence : input)
        {
            double anylysis = SentiWordNetDemoCode.connotationGeneral(sentence);
            System.out.println("connotation = " + anylysis);
            if(opinion == ObtainOpinion.POSITIVE)
                if(anylysis>=bestAnalysis)
                {
                    System.out.println("3241234134P: " + anylysis);
                    bestMatch = sentence;
                    bestAnalysis = anylysis;
                }//why should the sale of violent videogames to minors be banned
                else
                {
                    System.out.println("3241234134N: " + anylysis);
                }
            else
                if(anylysis<=bestAnalysis)
                {
                    bestMatch = sentence;
                    bestAnalysis = anylysis;
                }
        }
        return bestMatch;
    }
    public static void main(String[] args) throws Exception {
        /*
        urlText = "";
        bodyText = "";
        //String text = readFile("C:\\Users\\corpi\\Documents\\robot\\permutations-december-25\\permutations\\src\\SentenceGenerator\\Input.txt",
        //      StandardCharsets.UTF_8);
        String input =null;
        if(args.length<1)
        input =(new Scanner(System.in)).nextLine();
        else
        input = args[0];
        excecute(input);
        List<CardKeyword> keywordsList = KeywordsExtractor.getKeywordsList(urlText);
        ArrayList<String> URLWords = new ArrayList<String>();
        int MAX = 5;
        int count = 0;
        for(CardKeyword k : keywordsList)
        {
            for (String word : k.getTerms()) {

                System.out.println(word);
                if(!input.toLowerCase().contains(word.toLowerCase())
                        && FindKeyWordsTest.getPropernoun(WordUtils.capitalize(word)).length()>0
                        )
                    URLWords.add(word.toLowerCase());
            }
        }
        System.out.println("final result = " + mostCommon(URLWords,urlText));
        */

        System.out.println("connotation = " + SentiWordNetDemoCode.connotation((new Scanner(System.in)).nextLine()));

    }
    public static double connotationOnInternet(String input) throws Exception {
        urlText = "";
        bodyText = "";

        excecute(input);

        return SentiWordNetDemoCode.connotation(bodyText);
    }
    public static String backupLearn(String input) throws Exception {
        excecute(input);
        List<CardKeyword> keywordsList = KeywordsExtractor.getKeywordsList(bodyText);
        ArrayList<String> bodyWords = new ArrayList<String>();
        int MAX = 5;
        int count = 0;
        for(CardKeyword k : keywordsList)
        {
            if(count++ < MAX) {
                for (String word : k.getTerms()) {
                    System.out.println(word);
                    if(!input.toLowerCase().contains(word.toLowerCase()))
                        bodyWords.add(word.toLowerCase());
                }
            }
        }
        keywordsList = KeywordsExtractor.getKeywordsList(urlText);
        ArrayList<String> URLWords = new ArrayList<String>();
        MAX = 5;
        count = 0;
        for(CardKeyword k : keywordsList)
        {
            for (String word : k.getTerms()) {
                System.out.println(word);
                if(!input.toLowerCase().contains(word.toLowerCase()))
                    URLWords.add(word.toLowerCase());
            }
        }
        HashSet<String> setA = new HashSet<>(bodyWords);
        HashSet<String> setB = new HashSet<>(URLWords);
        setA.retainAll(setB);
        System.out.println(bodyText +"\n"+urlText);
        System.out.println("final result = " + setA);
        double con = SentiWordNetDemoCode.connotation(bodyText);
        System.out.println("connotation = " + con);
        return setA.toString() + "\nWith connotation " + con;
    }
}
