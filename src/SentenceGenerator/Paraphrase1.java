package SentenceGenerator;

import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import org.apache.lucene.wordnet.SynonymMap;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by swaggerton on 18/08/15.
 */
public class Paraphrase1 {

    public static int MIN_RATE_OF_REWORDING = 8;
    public static SynonymMap map;
    public static void main(String[] args) throws Exception {
        map =new SynonymMap(new FileInputStream("openNLP/wn_s.pl"));
        String fullText = "";
        String input = (new Scanner(System.in)).nextLine();
        String newText = operation1(input);
        //System.out.println(newText);
        List<String> list  = Arrays.asList(newText.split("[\\.]"));
        Collections.shuffle(list);
        for(String sent :list)
        {
            System.out.print(sent+". ");
        }
    }
    public static String POSTag2(String input) throws IOException {
        POSModel model = new POSModelLoader()
                .load(new File("openNLP/en-pos-maxent.bin"));
        PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
        POSTaggerME tagger = new POSTaggerME(model);

        //String input = "Hi. How are you? This is Mike.";
        ObjectStream<String> lineStream = new PlainTextByLineStream(
                new StringReader(input));
        String samp = "";
        perfMon.start();
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
    public static Map<String,ArrayList<String>> POSTag2X(String[] words,Map<String,ArrayList<String>> totalPhrase,
                                  Map<String,String> wordToTag) throws IOException {
        int plagerismStreak = 0;
        POSModel model = new POSModelLoader()
                .load(new File("openNLP/en-pos-maxent.bin"));
        PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
        POSTaggerME tagger = new POSTaggerME(model);
        Map<String,ArrayList<String>> wordMap = new HashMap<String,ArrayList<String>>();
        perfMon.start();
        for(String word:words) {
            word = word.replaceAll("_[A-Z\\$]+","");
            System.out.println("PPPPPPPPPPPPPP: "+word);
            ArrayList<String> suitibleWords = new ArrayList<String>();
            ArrayList<String> stupidWords = new ArrayList<String>();
            if(totalPhrase.get(word)!=null) {
                for (String input : totalPhrase.get(word)) {
                    input = input.replaceAll("_[A-Z\\$]+","");

                    //String input = "Hi. How are you? This is Mike.";
                    stupidWords.add(input);
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

                    // Create a Pattern object
                    Pattern r = Pattern.compile(".*?_([A-Z\\$]+)");

                    // Now create matcher object.
                    Matcher m = r.matcher(samp);
                    if(m.matches()&&!word.equals(input)) {
                        String newTag = m.group(1);
                        System.out.println("YYYYYYYYYYYYYYYYYYY: "+newTag+" : "+wordToTag.get(word));
                        if (newTag.equals(wordToTag.get(word))) {
                            suitibleWords.add(input);
                            plagerismStreak = 0;
                        }
                        else if(plagerismStreak++>MIN_RATE_OF_REWORDING)
                        {
                            int randIndex = (new Random()).nextInt(totalPhrase.get(word).size());
                            String randomSynonym = totalPhrase.get(word).get(randIndex);
                            suitibleWords.add(randomSynonym);
                            plagerismStreak = 0;
                        }
                    }
                }
            }
            // IF THE FILTER REMOVES EVERYTHING, USE BACKUP SYNONYMS
            if(suitibleWords.size()>0)
            {
                //wordMap.put(word, suitibleWords);
                wordMap.put(word, suitibleWords);
            }
            else
            {
                wordMap.put(word, suitibleWords);
            }

        }
        System.out.println("UUUUUUUUUUUUUUUUUUUUUUUU: " + wordMap);
        return wordMap;
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
                    //synonyms = listSynonymsJJ2(x, tag);
                    synonyms = listSynonymsJJ4(x);
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
                    //synonyms = listSynonymsJJ2(x, tag);
                    synonyms = listSynonymsJJ4(x);
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
    public static boolean getRandomBoolean(float probability) {
        return Math.random() < probability;
        //I tried another approaches here, still the same result
    }
    public static ArrayList<String> listSynonymsJJ4(String lastWord) throws IOException {
        ArrayList<String> synonymsList = new ArrayList<String>();

        String[] synonyms = map.getSynonyms(lastWord);
        synonymsList = new ArrayList<String>(Arrays.asList(synonyms));
        System.out.println("DFSIUGHENSVJLNEIONVS last word = <" + lastWord + ">");
        System.out.println("DFSIUGHENSVJLNEIONVS synonym = <"+synonymsList+">");
        if(lastWord.toLowerCase().equals("or"))
        {
            synonymsList = new ArrayList<String>();
            synonymsList.add("or");
        }
        return synonymsList;
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
        newWords = POSTag2X(words, totalPhrase, wordToTag);
        System.out.println("UUUUUUUUUUUUUUUUUUUUUUUUU: "+newWords);
        //newWords = totalPhrase;
        System.out.println("IIIIIIIIIIIIIIIIIIIIIII = "+newWords);
        for (String x : words) {

            //String bestWord = new WiktionaryTestOld().compareDefs(newWords.get(x), x);
            /**/
            String bestWord = null;
            if(newWords.get(x).size()>0)
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
}
