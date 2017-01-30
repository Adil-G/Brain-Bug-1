package general.chat;

import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.wordnet.SynonymMap;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by swaggerton on 18/08/15.
 */
public class Paraphrase1 {

    public static int MIN_RATE_OF_REWORDING = 8;
    public static SynonymMap map;
    public static HashMap<String, Integer> nlpToWordNet =
    new HashMap<>();
    public static void main(String[] args) throws Exception {
        //nlpToWordNet.put("NN", )
        //morphy_tag = {'NN':wordnet.NOUN,'JJ':wordnet.ADJ,'VB':wordnet.VERB,'RB':wordnet.ADV}[penn_tag[:2]]
        map =new SynonymMap(new FileInputStream("openNLP/wn_s.pl"));
        String fullOutput = "";
        String fullText = "";
        String inputO = "The 106-page framework covers everything from advertising and branding -- effectively banned, similar to tobacco -- to penalties for illicit production and trafficking, all legislated under a proposed new Cannabis Control Act.\n" +
                "And it says the current medical marijuana regime should remain in place, at least until the legal recreational market dynamics play out.\n" +
                "The overall direction of the task force was never in doubt as it toured legalized U.S. states Colorado and Washington.\n" +
                "Its chair, former federal Liberal justice minister Anne McLellan, said the nine members wouldn't have taken the job if they didn't believe in ending marijuana prohibition.\n" +
                "\"As a matter of public policy,\" said McLellan, \"legalization now is the time to move away from a system that for decades has been based on prohibition of cannabis, into a regulated legal market. I think we're all aware of the challenges and societal problems that the existing system has created.\"";//(new Scanner(System.in)).nextLine();
        inputO = "Pretty much known as the TMZ of tech after they paid for a “lost” next-gen Apple iPhone.";
        for(String input : inputO.replaceAll("\\n","<adsabas>").split("[\\.]")) {
            System.out.println("sadfadsf");
            String newText = operation1(input, new String());
            //System.out.println(newText);
            List<String> list = Arrays.asList(newText.split("[\\.]"));

            //Collections.shuffle(list);

            for (String sent : list) {
                String rearanged = new String();
                String[] tolkens = sent.split(",");
                int numberOfRearangements = tolkens.length / 2;
                if (numberOfRearangements > 0) {

                }
                boolean hasLeftOver = !(tolkens.length % 2 == 0);
                for (int i = 0; i < numberOfRearangements; i++) {
                    String s1 = tolkens[i + 1];
                    String[] and1 = s1.split("and");

                    if (and1.length > 1) {
                        String and1c = new String();
                        and1c = and1[1] + and1[0];
                        s1 = and1c;
                    }
                    String s2 = tolkens[i];
                    String[] and2 = s1.split("and");

                    if (and2.length > 1) {
                        String and2c = new String();
                        and2c = and2[1] + and2[0];
                        s1 = and2c;
                    }
                    rearanged += s1 + " {} " + s2 + " {} ";
                }
                if (hasLeftOver) {
                    rearanged = tolkens[tolkens.length - 1] + " {} " + rearanged;
                }

                //System.out.print(sent+". ");
                System.out.print(rearanged + ". ");
                String[] parcels = rearanged.split(" \\{\\} ");
                int targetUniqueness = 1;
                int uniquenessCount = parcels.length;
                for(String part :parcels)
                {
                    if(!inputO.toLowerCase().contains(part.toLowerCase()))
                        uniquenessCount--;
                }
                if(uniquenessCount <= targetUniqueness)
                    fullOutput += sent + ". ";
                else
                    fullOutput += rearanged + ". ";
            }
        }
        System.out.println("####################################################");
        System.out.println(fullOutput.replaceAll("--_: ","").replaceAll("\\{\\} ","").replaceAll("\\. \\.",".").replaceAll("\\s+"," ").replaceAll("<adsabas>","\n"));
    }
    public static String paraphrase(String inputO )throws IOException {
        map =new SynonymMap(new FileInputStream("openNLP/wn_s.pl"));
        String fullOutput = "";
        for(String input : inputO.replaceAll("\\n","<adsabas>").split("[\\.]")) {
            System.out.println("sadfadsf");
            String newText = operation1(input, new String());
            //System.out.println(newText);
            List<String> list = Arrays.asList(newText.split("[\\.]"));

            //Collections.shuffle(list);

            for (String sent : list) {
                String rearanged = new String();
                String[] tolkens = sent.split(",");
                int numberOfRearangements = tolkens.length / 2;
                if (numberOfRearangements > 0) {

                }
                boolean hasLeftOver = !(tolkens.length % 2 == 0);
                for (int i = 0; i < numberOfRearangements; i++) {
                    String s1 = tolkens[i + 1];
                    String[] and1 = s1.split("and");

                    if (and1.length > 1) {
                        String and1c = new String();
                        and1c = and1[1] + and1[0];
                        s1 = and1c;
                    }
                    String s2 = tolkens[i];
                    String[] and2 = s1.split("and");

                    if (and2.length > 1) {
                        String and2c = new String();
                        and2c = and2[1] + and2[0];
                        s1 = and2c;
                    }
                    rearanged += s1 + " {} " + s2 + " {} ";
                }
                if (hasLeftOver) {
                    rearanged = tolkens[tolkens.length - 1] + " {} " + rearanged;
                }

                //System.out.print(sent+". ");
                System.out.print(rearanged + ". ");
                String[] parcels = rearanged.split(" \\{\\} ");
                int targetUniqueness = 1;
                int uniquenessCount = parcels.length;
                for(String part :parcels)
                {
                    if(!inputO.toLowerCase().contains(part.toLowerCase()))
                        uniquenessCount--;
                }
                if(uniquenessCount <= targetUniqueness)
                    fullOutput += sent + ". ";
                else
                    fullOutput += rearanged + ". ";
            }
        }
        System.out.println("####################################################");
        fullOutput = fullOutput.replaceAll("--_: ","").replaceAll("\\{\\} ","").replaceAll("\\. \\.",".").replaceAll("\\s+"," ").replaceAll("<adsabas>","\n");
        System.out.println(fullOutput);
        return fullOutput;
    }
    public static void init() throws IOException {
        map =new SynonymMap(new FileInputStream("openNLP/wn_s.pl"));

    }
    public static String rephraseString(String input, String textDataBase) throws IOException {
        if(map!=null) {
            //nlpToWordNet.put("NN", )
            //morphy_tag = {'NN':wordnet.NOUN,'JJ':wordnet.ADJ,'VB':wordnet.VERB,'RB':wordnet.ADV}[penn_tag[:2]]
            String fullText = "";
            String newText = operation1(input, textDataBase);
            //newText = input;
            //System.out.println(newText);
            List<String> list = Arrays.asList(newText.split("[\\.]"));
            //Collections.shuffle(list);
            StringBuilder output = new StringBuilder();
            for(String sent :list)
            {
                String rearanged = new String();
                String[] tolkens = sent.split(",");
                int numberOfRearangements = tolkens.length/2;
                boolean hasLeftOver = !(tolkens.length % 2 == 0);
                for(int i = 0; i < numberOfRearangements; i++)
                {
                    String s1 = tolkens[i + 1];
                    String[] and1 = s1.split("and");

                    if(and1.length > 1)
                    {
                        String and1c = new String();
                        and1c = and1[1] + and1[0];
                        s1 = and1c;
                    }
                    String s2 = tolkens[i];
                    String[] and2 = s1.split("and");

                    if(and2.length > 1)
                    {
                        String and2c = new String();
                        and2c = and2[1] + and2[0];
                        s1 = and2c;
                    }
                    rearanged += s1 + " {} " + s2 + " {} ";
                }
                if(hasLeftOver)
                {
                    rearanged += tolkens[tolkens.length - 1];
                }
                //System.out.print(sent+". ");
                //System.out.print(rearanged + ". ");
                output.append(rearanged + ". ");
            }
            return output.toString();
        }
        return "Paraphraser has to be initialized.";
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
                        else if(false && plagerismStreak++>MIN_RATE_OF_REWORDING)
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
    public static String operation1(String text, String textDataBase) throws IOException {
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
               // int frequency = StringUtils.countMatches(textDataBase.toLowerCase(),
                        //x.replaceAll("_[A-Z\\$]+","").toLowerCase());;
                if(!tag.contains("DT")&&!tag.contains("IN")&&(x.length() >2) && (chance < 0.5)) {
                   // System.out.println("324234 frequency of " + x.replaceAll("_[A-Z\\$]+", "").toLowerCase() + "- " + frequency);
                    // If word is a noun and occurs more than n times in the raw data
                    // then ignore it (make synonym list empty)
                    float maxFreq = 5 * ((float) textDataBase.length() / 2669.0f);//
                    boolean endsWithS = x.toLowerCase().endsWith("s");
                    ArrayList<String> synonyms = null;
                    //synonyms = listSynonymsJJ2(x, tag);
                    //synonyms.addAll(listSynonymsJJ4(x));
                    synonyms = listSynonymsJJ4(x);
                    if (synonyms.size() == 0) {
                        //synonyms = listSynonymsJJ2(x, tag);
                    }
                    x = x.replaceAll("_[A-Z\\$]+", "");
                    ArrayList<String> synonymsTemp = new ArrayList<String>();
                    for (String y : synonyms) {
                        y = y.replaceAll("_[A-Z\\$]+", "");
                        synonymsTemp.add(y);
                    }
                    synonyms = synonymsTemp;
                    totalPhrase.put(x, new ArrayList<>(synonyms));
                    System.out.println("SFEWGWSGWRPOX: all passed for " + x + " = " + synonyms);
                }
            }
        }
        String newText=filterWithTag2(words, totalPhrase, wordToTag);
        System.out.println("KKKKKKKKKKKKKKKKKKK: " + totalPhrase);
        return newText;
    }
    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }
    public static boolean getRandomBoolean(float probability) {
        return Math.random() < probability;
        //I tried another approaches here, still the same result
    }
    public static ArrayList<String> listSynonymsJJ4(String lastWord) throws IOException {
        ArrayList<String> synonymsList = new ArrayList<String>();

        String[] synonyms = map.getSynonyms(lastWord);
        synonymsList = new ArrayList<String>(Arrays.asList(synonyms));
        /*
        HashSet<String> synonyms = Thesaurus.getSynonyms(lastWord, true);//map.getSynonyms();
        synonymsList = new ArrayList<String>(synonyms);
         */
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
        newWords = totalPhrase;
        System.out.println("UUUUUUUUUUUUUUUUUUUUUUUUU: "+newWords);
        //newWords = totalPhrase;
        System.out.println("IIIIIIIIIIIIIIIIIIIIIII = "+newWords);
        for (String x : words) {

            //String bestWord = new WiktionaryTestOld().compareDefs(newWords.get(x), x);
            //String bestWord =

            String bestWord = null;
            if(newWords.get(x)!=null&&newWords.get(x).size()>1)
            {
                // bestWord= newWords.get(x).get((new Random()).nextInt(newWords.get(x).size()));

                //bestWord= newWords.get(x).get(0);
                //bestWord = newWords.get(x).get(new Random().nextInt(newWords.get(x).size()));
                bestWord= new SimilarityCalculationDemo().best(x, newWords.get(x));
            }

            System.out.println("OOOOOOOOOOOOOO: bestword for " + x + " = "+bestWord);
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
