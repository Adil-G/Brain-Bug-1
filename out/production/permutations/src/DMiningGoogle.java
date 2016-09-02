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
public class DMiningGoogle {
    public static int MAX_WEBSITES = 1;
    public static int totalWebsitesVisited = 0;
    public static int totalLinks = 0;
    public static int Max_LINKS = 24;
    public static int NumberOfFacts = 1;//30;
    public static Map<Double, String> mostCommonWords;
    public static List<String> info = new ArrayList<String>();
    public static List<String> nouns;
    public static String prev="";
    public static void main(String[] args) throws Exception {
        // create map of data sorted by comparison factors
        while(true) {
            mostCommonWords = new TreeMap<Double, String>(Collections.reverseOrder());
            String input = (new Scanner(System.in)).nextLine();
            System.out.print("Memory for next answer? (0/1)");
            int gogo = (new Scanner(System.in)).nextInt();
            List<String> ls = new ArrayList<String>();

            if(gogo==0||false) {
                ls = excecute(input.toLowerCase());
                System.out.println("PREV="+input.toLowerCase());
                prev = "";

            }
            else
            {
                ls = excecute(input.toLowerCase() + prev);
                System.out.println("PREV="+prev);
                prev = "";
            }
            System.out.println("Answer: " + ls.get(0));//StanfordParser.findNoun2().get(0));
            textToSpeech(ls.get(0),20);
            String allNouns   ="";
            List<String> n = StanfordParser.findNoun2(input.toLowerCase());
            for(String noun :n)
            {
                allNouns += noun+" ";
            }
            prev = allNouns;

        }

    }

    public static List<String> excecute(String input) throws Exception {
        //processPage(input);
        List<GoogleResults2.Result> results=  GSAPI.main(new String[]{input.trim()});
        for(GoogleResults2.Result result :results)
        {
            info.add(result.getUrl());
        }
        return searchResult();
    }

    public static void textToSpeech(String bigText, int maxWords) {
        bigText = bigText.replaceAll("\\W", " ");
        String[] bigWords = bigText.split(" ");
        List<String[]> sayThis = splitArray(bigWords, maxWords);
        for (String[] list : sayThis) {
            String whatToSay = "";
            for (String word : list) {
                whatToSay += word + " ";
            }
            whatToSay = whatToSay.trim().replaceAll("'s", "s").replaceAll(" s ", "s ").replaceAll("\\[.*?\\]", "");
        }
    }

    public static <T> List<T[]> splitArray(String[] items, int maxSubArraySize) {
        List<T[]> result = new ArrayList<T[]>();
        if (items == null || items.length == 0) {
            return result;
        }

        int from = 0;
        int to = 0;
        int slicedItems = 0;
        while (slicedItems < items.length) {
            to = from + Math.min(maxSubArraySize, items.length - to);
            T[] slice = Arrays.copyOfRange((T[]) items, from, to);
            result.add(slice);
            slicedItems += slice.length;
            from = to;
        }
        return result;
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
            for(String send:sentence)
            {
                new Trial01().main(new String[]{send});
            }
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
                for (int j = i + 1; j < sentences.size(); j++) {
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
            for (Map.Entry entry : mostCommonWords.entrySet()) {
                if (i++ < NumberOfFacts&&stop) {
                    list.add(entry.getValue().toString());
                    //System.out.println("Added: "+entry.getValue().toString());
                    System.out.println(entry);
                    stop = false;
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

        if(list.size()>0) {
            list = pickNRandom(list, 1);
        }
        else
        {
            list.add("Sorry, I don't know.");
        }
        return list;
    }


    public static List<String> pickNRandom(List<String> lst, int n) {
        List<String> copy = new LinkedList<String>(lst);
        Collections.shuffle(copy);
        return copy.subList(0, n);
    }
    public static <T> T mostCommon(ArrayList<T> list) {
        Map<T, Integer> map = new HashMap<>();

        for (T t : list) {
            Integer val = map.get(t);
            map.put(t, val == null ? 1 : val + 1);
        }

        Map.Entry<T, Integer> max = null;

        for (Map.Entry<T, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue())
                max = e;
        }

        return max.getKey();
    }


}
