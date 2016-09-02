package SentenceGenerator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by corpi on 2016-04-23.
 */
public class ConversationBot_april23_2016 {
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
                String s = url;
                String[] words = s.split("[^\\w]+");
                for (int i = 0; i < words.length; i++) {
                    // You may want to check for a non-word character before blindly
                    // performing a replacement
                    // It may also be necessary to adjust the character class
                    //words[i] = words[i].replaceAll("[^\\w]", "");
                    urlText += words[i] + " ";
                }

                try {

                    Document doc = Jsoup.connect(url).get();
                    System.out.println("WAITING FOR JSOUP");
                    // Select the <p> Elements from the document
                    Elements paragraphs = doc.select("p");
                    System.out.println("WAITING FOR SELECT(P)");
                    // For each selected <p> element, print out its text

                    for (Element p : paragraphs) {
                        //System.out.println(e.text());
                        if (!p.text().contains(":") && !p.text().toLowerCase().contains("welcome") && !p.text().toLowerCase().contains("is a song")) {
                            bodyText += p.text();
                        }
                    }
                } catch (IOException j) {
                    System.out.println("Failed url connection." + url + " Trying another one.");
                } catch (Exception g) {
                    System.out.println("Failed url connection." + url + " Trying another one.");
                }
            }
        }catch (Exception l){}
    }
    public static void main(String[] args) throws Exception {

        //String text = readFile("C:\\Users\\corpi\\Documents\\robot\\permutations-december-25\\permutations\\src\\SentenceGenerator\\Input.txt",
          //      StandardCharsets.UTF_8);
        String input =(new Scanner(System.in)).nextLine();
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
        System.out.println("connotation = " + SentiWordNetDemoCode.connotation(bodyText));

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
