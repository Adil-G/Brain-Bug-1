import SentenceGenerator.Paraphrase1;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;


/**
 * Created by Adil on 2015-06-13.
 */
public class DMiningGoogleOnlyChunks {
    public static int MAX_WEBSITES = 1;
    public static int totalWebsitesVisited = 0;
    public static int totalLinks = 0;
    public static int Max_LINKS = 24;
    public static int NumberOfFacts = 300;//30;
    public static Map<Double, String> mostCommonWords;
    public static List<String> info = new ArrayList<String>();
    public static List<String> nouns;
    public static String prev="";
    public static void main(String[] args) throws Exception {
        // create map of data sorted by comparison factors
        /*
        String input = (new Scanner(System.in)).nextLine();
        List<String> chunk = excecute(input);
        List<String> newChunk = new ArrayList<String>();
        for(int i = 0;i<chunk.size()&&i<5;i++) {
            String text = chunk.get(i);
            String newText = new Paraphrase1().operation0(text);
            System.out.println("FINAL FEWIOSOVNWOEINOEVWINOIEWN ORIGIONAL:\n" + text);
            System.out.println("FINAL FEWIOSOVNWOEINOEVWINOIEWN:\n" + newText);
            newChunk.add(newText);
        }
        for(int j = 0;j<newChunk.size();j++)
        {
            System.out.println("####################################################################################");
            System.out.println(chunk.get(j));
            System.out.println(newChunk.get(j));
        }
        */
    }

    public static List<String> excecute(String input) throws Exception {
        //processPage(input);
        List<GoogleResults2.Result> results=  GSAPI.main(new String[]{input.trim()});
        for(GoogleResults2.Result result :results)
        {
            info.add(result.getUrl());
        }
        //info.clear();
        //info.add(input);
        return searchResult();
    }

    public static List<String> searchResult() throws IOException {
        ArrayList<String> chunks = new ArrayList<String>();
        Map<String, HashSet<String>> firstCluster = new HashMap<String, HashSet<String>>();
        mostCommonWords = new TreeMap<Double, String>(Collections.reverseOrder());
        List<String> list = new ArrayList<String>();
        try {
            String wholeText = "";

            for (String url : info) {
                // Download the HTML and store in a Document

                try {

                    Document doc = Jsoup.connect(url).get();
                    System.out.println("WAITING FOR JSOUP");
                    // Select the <p> Elements from the document
                    Elements paragraphs = doc.select("p");
                    System.out.println("WAITING FOR SELECT(P)");
                    // For each selected <p> element, print out its text

                    for (Element p : paragraphs) {
                        //System.out.println(e.text());
                        if (!p.text().contains(":")&&!p.text().toLowerCase().contains("welcome")&&!p.text().toLowerCase().contains("is a song")) {
                            chunks.add(p.text().replaceAll("\\[[0-9]+\\]",""));
                            wholeText += p.text();
                            System.out.println("Adding more data..."+p.text());
                        }
                    }
                } catch (IOException j) {
                    System.out.println("Failed url connection." + url + " Trying another one.");
                } catch (Exception g) {
                    System.out.println("Failed url connection." + url + " Trying another one.");
                }
            }
            System.out.println(wholeText);
            String[] sentence = wholeText.split("\\.");
            List<String> sentences = chunks;//Arrays.asList(sentence);
            NumberOfFacts = (int) (sentences.size() / 5);
            if (NumberOfFacts < 3) {
                NumberOfFacts = 3;
            }

            System.out.println(sentence.length);
            for (String i : sentences) {
                System.out.println(i);
            }

            Map<String, Double> noRepetition = new HashMap<String, Double>();
            // compare list to itself
            for (int i = 0; i < sentences.size(); i++) {
                double factorSum = 0;
                for (int j = 0; j < sentences.size(); j++) {
                    double currentFactor;
                    // check to see if there is repetition
                    String assumeCombo = sentences.get(i) + sentences.get(j);
                    if (noRepetition.containsKey(assumeCombo)) {

                        // use previous comparison factor
                        currentFactor = noRepetition.get(assumeCombo);
                        // accumulate maching factors for coparison later on
                        factorSum += currentFactor;
                    }
                    // if there is no repetition, register combo in memory and continue as normal.
                    else {
                        // compare the two objects
                        currentFactor = ComparePhrases.compare(sentences.get(i), sentences.get(j));
                        String anticipateCombo = sentences.get(j) + sentences.get(i);
                        noRepetition.put(anticipateCombo, currentFactor);

                        // accumulate maching factors for coparison later on
                        factorSum += currentFactor;
                    }
                }
                // sort sentences by matching factor
                mostCommonWords.put(factorSum, sentences.get(i));
                    /*
                    if(factorSum>bestFactor)
                    {
                        bestSentence = sentences.get(i);
                        bestFactor=factorSum;
                    }*/

            }
            System.out.println("#####################################################################################");

            Map<Double, String> bestResults = new TreeMap<Double, String>(Collections.reverseOrder());
            list = new ArrayList<String>();
            int i = 0;
            boolean stop = true;
            NumberOfFacts = 5000;
            for (Map.Entry entry : mostCommonWords.entrySet()) {
                if (i++ < NumberOfFacts&&stop) {
                    list.add(entry.getValue().toString());
                    //System.out.println("Added: "+entry.getValue().toString());
                    System.out.println(entry);
                    stop = true;
                    //double factor  = ComparePhrases.compare(input, entry.getValue().toString());
                    //bestResults.put(factor,entry.getValue().toString());
                }
            }
            System.out.println("################################################################################");
            System.out.println();
            System.out.println();
        } catch (Exception e) {
            System.out.println("ERROR! DATA EXTRACTION FALIURE! RESTARTING ...");
            searchResult();
        }
        //list = pickNRandom(list,5);
        return list;
    }


}
