import SentenceGenerator.*;
import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Adil on 2015-06-13.
 */
public class DMiningGoogleOnlyChunksAlternating {
    public static int MAX_WEBSITES = 1;
    public static int totalWebsitesVisited = 0;
    public static int totalLinks = 0;
    public static int Max_LINKS = 24;
    public static int NumberOfFacts = 300;//30;
    public static Map<Double, String> mostCommonWords;
    public static Set<String> info = new HashSet<String>();
    public static List<String> nouns;
    public static String prev="";
    public static POSModel model;
    public static PerformanceMonitor perfMon;
    public static POSTaggerME tagger;
    public static int MAX_SEARCHES = 100;
    public static void initialize()
    {
        System.out.println("WFIWOFWOEFIJ");
        POSModel model = new POSModelLoader()
                .load(new File("openNLP/en-pos-maxent.bin"));
        perfMon = new PerformanceMonitor(System.err, "sent");
        tagger = new POSTaggerME(model);

    }
    public static String POSTag2(String input) throws IOException {
        if (!perfMon.isStarted())
        {
            perfMon.start();
        }
        //String input = "Hi. How are you? This is Mike.";
        ObjectStream<String> lineStream = new PlainTextByLineStream(
                new StringReader(input));
        String samp = "";

        String line;
        while ((line = lineStream.read()) != null) {

            String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE
                    .tokenize(line);
            String[] tags = tagger.tag(whitespaceTokenizerLine);

            POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
            System.out.println(sample.toString());
            samp = sample.toString();

            perfMon.incrementCounter();
        }
        perfMon.stopAndPrintFinalResult();
        return samp;
    }
    public static String operation1(String text) throws IOException {
        /*String[] words = text.split("\\s+");

        for (int i = 0; i < words.length; i++) {
            // You may want to check for a non-word character before blindly
            // performing a replacement
            // It may also be necessary to adjust the character class
            words[i] = words[i].replaceAll("[^\\w]", "");
        }
        */
        String parsed = POSTag2(text);

        String[] words = parsed.split("\\s");
        Map<String,ArrayList<String>> totalPhrase = new HashMap<String,ArrayList<String>>();
        Map<String,String> wordToTag = new HashMap<String,String>();
        for (String x : words) {
            x = x.replaceAll("\\[[0-9]+\\]", "");

            float chance = (new Random()).nextFloat();
            boolean doIt = chance <= 1.0f;
            boolean doIt90 = chance <= 0.0f;
            // Now create matcher object.
            Pattern rs = Pattern.compile("[A-Z]+.*?");
            Matcher ms = rs.matcher(x);

            if (!ms.matches()) {
                System.out.println("DSFKLJWOIFWEOSNDLGEW");
                String tag = "";
                // Create a Pattern object
                Pattern r = Pattern.compile(".*?_([A-Z\\$]+)");

                // Now create matcher object.
                Matcher m = r.matcher(x);
                if (m.matches()) {
                    tag = m.group(1);
                }

                x = x.replaceAll("_[A-Z\\$]+", "");
                String end = " ";
                if (x.endsWith(".")) {
                    end = ". ";
                }

                x = x.replaceAll("[,\\.]+", "");
                x = x.replaceAll("_[A-Z\\$]+","");
                wordToTag.put(x,tag);
               /* if (doIt && (tag.equals("JJ") || tag.equals("JJ") || tag.equals("JJ") || tag.equals("JJ") || tag.equals("JJR") || ((true) && (tag.equals("VBN")
                        || tag.equals("JJ") || tag.equals("JJR") || tag.equals("VBG") || tag.equals("WDT")
                        || tag.equals("VBD")||tag.equals("JJ")||tag.equals("TO")||tag.equals("JJ")
                        ||tag.equals("VBD")||tag.equals("VB")||tag.equals("JJ")||tag.equals("DT")||tag.equals("VBZ")||tag.equals("JJR")||tag.equals("VBG")||tag.equals("WDT")
                        ||tag.equals("VBD")||tag.equals("RB")||tag.equals("CD")||tag.equals("MD")||tag.equals("TO")||tag.equals("JJ")
                        ||tag.equals("VBD")||tag.equals("VBD")||tag.equals("VBD"))))) {//||tag.equals("VB")||tag.equals("TO")||tag.equals("VB")
                    //||tag.equals("VB")||tag.equals("DT")||tag.equals("RB")
                /* full:
                tag.equals("JJ")||tag.equals("JJ")||tag.equals("CC")||tag.equals("JJ")||tag.equals("VBP")||tag.equals("JJR")||tag.equals("VBN")
                    ||tag.equals("JJ")||tag.equals("DT")||tag.equals("VBZ")||tag.equals("JJR")||tag.equals("VBG")||tag.equals("WDT")
                    ||tag.equals("VBD")||tag.equals("VB")||tag.equals("CD")||tag.equals("MD")||tag.equals("TO")||tag.equals("VB")
                    ||tag.equals("VB")||tag.equals("VB")||tag.equals("VB"))
                 */
                if(!x.toLowerCase().equals("are")&&!x.toLowerCase().equals("be")&&!x.toLowerCase().equals("can")&&!x.toLowerCase().equals("or")&&!tag.contains("NN")&&!tag.contains("IN")&&!tag.contains("DT")&&!tag.contains("IN")&&!tag.contains("IN")&&!tag.contains("IN")){
                    //!x.toLowerCase().equals("a")&&!tag.contains("NN")&&!tag.contains("NN")&&!tag.contains("NN")){
                    //if(!tag.contains("NN")&&((false)||(!x.toLowerCase().equals("are")&&!tag.contains("DT")&&!tag.contains("IN")))){

                    boolean endsWithS = x.toLowerCase().endsWith("s");
                    ArrayList<String> synonyms = null;
                    synonyms = listSynonymsJJ2(x, tag);
                    //synonyms = listSynonymsJJ4(x);
                    if(synonyms.size()==0)
                    {
                        //synonyms = listSynonymsJJ2(x, tag);
                    }
                    x = x.replaceAll("_[A-Z\\$]+","");
                    ArrayList<String> synonymsTemp = new ArrayList<String>();
                    for(String y:synonyms)
                    {
                        y = y.replaceAll("_[A-Z\\$]+","");
                        synonymsTemp.add(y);
                    }
                    synonyms = synonymsTemp;
                    totalPhrase.put(x, synonyms);
                    System.out.println("SFEWGWSGWRPOX: all passed for " + x + " = " + synonyms);
                }
                else if(getRandomBoolean(-1f)&&!tag.contains("DT")&&!tag.contains("IN"))
                {
                    boolean endsWithS = x.toLowerCase().endsWith("s");
                    ArrayList<String> synonyms = null;
                    synonyms = listSynonymsJJ2(x, tag);
                    //synonyms = listSynonymsJJ4(x);
                    if(synonyms.size()==0)
                    {
                        //synonyms = listSynonymsJJ2(x, tag);
                    }
                    x = x.replaceAll("_[A-Z\\$]+","");
                    ArrayList<String> synonymsTemp = new ArrayList<String>();
                    for(String y:synonyms)
                    {
                        y = y.replaceAll("_[A-Z\\$]+","");
                        synonymsTemp.add(y);
                    }
                    synonyms = synonymsTemp;
                    totalPhrase.put(x, synonyms);
                    System.out.println("SFEWGWSGWRPOX: all passed for " + x + " = " + synonyms);
                }
            }
        }
        String newText=filterWithTag2(words, totalPhrase, wordToTag);
        System.out.println("KKKKKKKKKKKKKKKKKKK: " + totalPhrase);
        return newText;
    }
    public static String filterWithTag2(String[] words,Map<String,ArrayList<String>> totalPhrase,
                                        Map<String,String> wordToTag) throws IOException {
        List<String> filteredList = new ArrayList<String>();
        String newText="";
        String[] wordsX = new String[words.length];
        for(int i = 0;i<words.length;i++)
        {
            wordsX[i] = words[i].replaceAll("_[A-Z\\$]+","");
        }
        words= wordsX;
        Map<String,ArrayList<String>> newWords = null;
        //newWords = POSTag2X(words, totalPhrase, wordToTag);
        newWords =totalPhrase;
        System.out.println("UUUUUUUUUUUUUUUUUUUUUUUUU: "+newWords);
        System.out.println("IIIIIIIIIIIIIIIIIIIIIII = "+newWords);
        for (String x : words) {

            //String bestWord = new WiktionaryTestOld().compareDefs(newWords.get(x), x);
            /**/
            String bestWord = null;
            System.out.println("WOI: "+x+" : "+newWords.size());
            if(newWords.containsKey(x)&&newWords.get(x).size()>0)
            {
                bestWord= newWords.get(x).get((new Random()).nextInt(newWords.get(x).size()));
            }

            System.out.println("OOOOOOOOOOOOOO: bestword = "+bestWord);
            if(bestWord!=null)
            {
                newText += bestWord + " ";
            }
            else
            {
                newText += x + " ";
            }
        }
        return newText;
    }
    public static boolean getRandomBoolean(float probability) {
        return Math.random() < probability;
        //I tried another approaches here, still the same result
    }
    public static ArrayList<String> listSynonymsJJ2(String lastWord,String type) throws IOException {
        ArrayList<String> rjhymes = new ArrayList<String>();
        System.out.println("... Getting source");
        //String url = "http://www.thesaurus.com/browse/"+ lastWord+"?s=t";
        String url = "http://www.wordhippo.com/what-is/another-word-for/"+lastWord+".html";
        //String url = "http://www.getanotherwordfor.com/"+lastWord;
        String html = "";
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
            html = doc.html();
        }catch(HttpStatusException e)
        {
            System.out.println("No internet");
        }
        //System.out.println(html);
        if(html.equals("")||html.contains("No words found."))
        {
            System.out.println("REWORDING FAILED.");
            return new ArrayList<String>();//listSynonymsBACKUP(lastWord);
        }
        else {
            System.out.println("... Got source succeeded");
            if (!html.equals("") && !html.contains("no thesaurus results")) {
                System.out.println("GOt the HTML");
                String pattern ="";
                if(type.toLowerCase().startsWith("v")) {
                    pattern = "<div class=\"wordtype\">Verb</div>(.*?)";
                }
                else if(type.toLowerCase().startsWith("j"))
                {
                    pattern = "<div class=\"wordtype\">Adjective</div>(.*?)";
                }
                else if(type.toLowerCase().startsWith("r"))
                {
                    pattern = "<div class=\"wordtype\">Adverb</div>(.*?)";
                }
                Pattern p = Pattern.compile(pattern);
                Matcher m = p.matcher(html);
                while(m.find())
                {
                    if(m.groupCount()>0) {
                        html = m.group(1);
                    }
                }
                /////////////////////////////////////////////////////////////
                pattern = "/what-is/another-word-for/.*?\">([a-z\\s]+)</a>";
                p = Pattern.compile(pattern);
                m = p.matcher(html);
                final int WORD_BANK_SIZE = 4;
                int h = 0;
                while (h++<WORD_BANK_SIZE&&m.find()) {
                    String rhyme1 = m.group(1);
                    System.out.println("Synonym = " + rhyme1);
                    if(!rhyme1.equals(lastWord)) {
                        rjhymes.add(rhyme1);
                    }
                }
                //TreeMap<Integer,String> set = new TreeMap<Integer,String>(Collections.reverseOrder());
                if (rjhymes.size() > 1) {
                    int maxSize = 4;
                    if (rjhymes.size() < maxSize) {
                        maxSize = rjhymes.size();
                    }

                    //rjhymes = pickNRandom(rjhymes, maxSize - 1);
                } else {
                    //System.out.println(usedPhrases7.get(i));
                    //System.out.println(usedPhrases5.get(i + 1));
                }
            }
        }
        System.out.println("DFSIUGHENSVJLNEIONVS last word = <" + lastWord + ">");
        System.out.println("DFSIUGHENSVJLNEIONVS synonym = <"+rjhymes+">");
        System.out.println("DFSIUGHENSVJLNEIONVS type = <" + type + ">");
        return rjhymes;
    }

    public static void main(String[] args) throws Exception {

        // create map of data sorted by comparison factors
        String input = (new Scanner(System.in)).nextLine();
        List<String> chunk = excecute(input);
        for(String p:chunk)
        {
            System.out.println(p+"\n");
        }
    }

    public static List<String> excecute(String input) throws Exception {

        List<GoogleResults2.Result> results = GSAPI.main(new String[]{input.trim()});
        for (GoogleResults2.Result result : results) {
            info.add(result.getUrl());
        }
        //info.clear();
        //info.add(input);
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
                        if (p.text().length()>150&&!p.text().contains(":")&&!p.text().toLowerCase().contains("welcome")&&!p.text().toLowerCase().contains("is a song")) {
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
                        currentFactor = SentenceGenerator.ComparePhrases.compare(sentences.get(i), sentences.get(j));
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
