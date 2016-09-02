import SentenceGenerator.TextToSpeech;
import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by swaggerton on 14/08/15.
 */
public class PoemWriterPlus {
    public static int VERSE_POOL = 50;
    public static List<Parse> nounPhrases = new ArrayList<Parse>();
    public static List<Parse> adjpPhrases = new ArrayList<Parse>();
    public static List<Parse> NP = new ArrayList<Parse>();
    public static List<Parse> areVBP = new ArrayList<Parse>();
    public static List<Parse> end = new ArrayList<Parse>();

    public static List<String> nouns = new ArrayList<String>();
    public static List<String> adj = new ArrayList<String>();
    public static String parsedPhrase = "";
    public static List<String[]> fruits = new ArrayList<String[]>();

    public static void main(String[] args) throws Exception {

        InputStream is = new FileInputStream("openNLP/en-parser-chunking.bin");

        ParserModel model = new ParserModel(is);

        Parser parser = ParserFactory.create(model);
        ops((new Scanner(System.in)).nextLine(),parser);
    }

    public static boolean POSTag(String in) throws IOException {
        String type = "";
        POSModel model = new POSModelLoader()
                .load(new File("openNLP/en-pos-maxent.bin"));
        PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
        POSTaggerME tagger = new POSTaggerME(model);

        String input = "Hi. How are you? This is Mike.";
        input = in;
        ObjectStream<String> lineStream = new PlainTextByLineStream(
                new StringReader(input));

        perfMon.start();
        String line;
        while ((line = lineStream.read()) != null) {

            String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE
                    .tokenize(line);
            String[] tags = tagger.tag(whitespaceTokenizerLine);

            POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
            System.out.println(sample.getTags()[0].toString());
            type = sample.getTags()[0].toString();

            perfMon.incrementCounter();
        }
        perfMon.stopAndPrintFinalResult();
        return (type.equals("NNP")||type.equals("DT"));
    }
    public static boolean POSTag3(String in) throws IOException {
        String type = "";
        POSModel model = new POSModelLoader()
                .load(new File("openNLP/en-pos-maxent.bin"));
        PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
        POSTaggerME tagger = new POSTaggerME(model);

        String input = "Hi. How are you? This is Mike.";
        input = in;
        ObjectStream<String> lineStream = new PlainTextByLineStream(
                new StringReader(input));

        perfMon.start();
        String line;
        while ((line = lineStream.read()) != null) {

            String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE
                    .tokenize(line);
            String[] tags = tagger.tag(whitespaceTokenizerLine);

            POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
            System.out.println(sample.getTags()[0].toString());
            type = sample.getTags()[0].toString();

            perfMon.incrementCounter();
        }
        perfMon.stopAndPrintFinalResult();
        return (type.equals("CC"));
    }
    public static void POSTag2(String input) throws IOException {
        POSModel model = new POSModelLoader()
                .load(new File("openNLP/en-pos-maxent.bin"));
        PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
        POSTaggerME tagger = new POSTaggerME(model);

        //String input = "Hi. How are you? This is Mike.";
        ObjectStream<String> lineStream = new PlainTextByLineStream(
                new StringReader(input));

        perfMon.start();
        String line;
        while ((line = lineStream.read()) != null) {

            String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE
                    .tokenize(line);
            String[] tags = tagger.tag(whitespaceTokenizerLine);

            POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
            System.out.println(sample.toString());

            perfMon.incrementCounter();
        }
        perfMon.stopAndPrintFinalResult();
    }
    public static Parse Parse(String input) throws InvalidFormatException, IOException {
        // http://sourceforge.net/apps/mediawiki/opennlp/index.php?title=Parser#Training_Tool
        InputStream is = new FileInputStream("openNLP/en-parser-chunking.bin");

        ParserModel model = new ParserModel(is);

        Parser parser = ParserFactory.create(model);

        String sentence = "Programcreek is a very huge and useful website.";
        sentence = input;
        Parse topParses[] = ParserTool.parseLine(sentence, parser, 1);

        Parse ps = null;
        String label = "";
        for (Parse p : topParses) {
            p.show();
            ps = p;
            /*
            for(Parse pa :p.getTagNodes())
            {
                ps = pa;
                label  = pa.getType();
                System.out.println(ps+" => "+label);
            }
            */
        }

        is.close();

        return ps;
	/*
	 * (TOP (S (NP (NN Programcreek) ) (VP (VBZ is) (NP (DT a) (ADJP (RB
	 * very) (JJ huge) (CC and) (JJ useful) ) ) ) (. website.) ) )
	 */
    }

    public static String[] SentenceDetect(String input) throws InvalidFormatException,
            IOException {
        String paragraph = "Hi. How are you? This is Mike.";
        paragraph = input;
        // always start with a model, a model is learned from training data
        InputStream is = new FileInputStream("openNLP/en-sent.bin");
        SentenceModel model = new SentenceModel(is);
        SentenceDetectorME sdetector = new SentenceDetectorME(model);

        String sentences[] = sdetector.sentDetect(paragraph);
        is.close();
        return sentences;
    }
public static List<String> j(ArrayList<String> sentences) throws IOException {
    Map<Double, String> mostCommonWords;
    mostCommonWords = new TreeMap<Double, String>(Collections.reverseOrder());
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
    int i  = 0;
    List<Double> indecies   = new ArrayList<Double>();
    List<String> list = new ArrayList<String>();
    for (Map.Entry entry : mostCommonWords.entrySet()) {
        if (i++ < 3) {
            list.add(entry.getValue().toString());
            indecies.add((Double) entry.getKey());
            //System.out.println("Added: "+entry.getValue().toString());
            System.out.println(entry);
            //double factor  = ComparePhrases.compare(input, entry.getValue().toString());
            //bestResults.put(factor,entry.getValue().toString());
        }
    }
    if(indecies.size()>0&&indecies.get(0)>0)
    {
        return list;
    }
    else
    {
        return null;
    }
}
    public static void getNounPhrases(Parse p, String labelA, String labelB) {
        if (p.getType().equals("NP")) {
            nounPhrases.add(p);
            NP.add(p);
            //System.out.println("SFLSKEJF");
        }
        if (p.getType().equals(labelB) || p.getType().equals("VP") || p.getType().equals("PP")) {
            adjpPhrases.add(p);
        }
        if (p.getType().equals("NNS")) {
            areVBP.add(p);
        }
        if (p.getType().equals(".")) {
            end.add(p);
        }

        for (Parse child : p.getChildren()) {
            getNounPhrases(child, labelA, labelB);
        }
    }

    public static boolean getRandomBoolean() {
        return Math.random() < 0.5;
        //I tried another approaches here, still the same result
    }

    public static int syllable(String text) {
        Pattern p = Pattern.compile("[aeiouy]+?\\w*?[^e]");
        String[] result = p.split(text);
        return result.length;
    }

    private static String getUrlSource(String url) throws IOException {
        URL yahoo = new URL(url);
        URLConnection yc = yahoo.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                yc.getInputStream(), "UTF-8"));
        String inputLine;
        StringBuilder a = new StringBuilder();
        while ((inputLine = in.readLine()) != null)
            a.append(inputLine);
        in.close();

        return a.toString();
    }
public static List<String> listRymes(String lastWord) throws IOException {
    String url = "http://words.bighugelabs.com/" + lastWord;
    String html = getUrlSource(url);
    //System.out.println(html);

    Pattern p = Pattern.compile(".*?(rhymes with</h3>.*?.*?$).*?");
    Matcher m = p.matcher(html);
    if(m.matches()){
        html = m.group(1);
        //System.out.println("adsgasgoijaov"+html);
    }
    p = Pattern.compile(">([a-z]+)</a>");
    m = p.matcher(html);
    List<String> rjhymes = new ArrayList<String>();
    while (m.find()) {
        String rhyme1 = m.group(1);
        System.out.println("Rhyme = "+rhyme1);
        rjhymes.add(rhyme1);
    }
    //TreeMap<Integer,String> set = new TreeMap<Integer,String>(Collections.reverseOrder());
    if (rjhymes.size() > 1) {
        int maxSize = 10000;
        if(rjhymes.size()<maxSize) {
            maxSize = rjhymes.size();
        }

        rjhymes = pickNRandom(rjhymes, maxSize-1);
    } else {
        //System.out.println(usedPhrases7.get(i));
        //System.out.println(usedPhrases5.get(i + 1));
    }
    return rjhymes;
}
    public static void ops(String par, Parser parserTemp) throws Exception {
        //String response = "I suppose that " + new TestOpenNLP().ops3(par);
        List<String> allPhrases = new TestOpenNLP().ops3(par,parserTemp,false);
        List<String> usedPhrases5 = new ArrayList<String>();
        for (String x : allPhrases) {
            System.out.print("\n");
            ;
            int count = syllable(x);//&&!POSTag(x)
            if (count > 2 && !usedPhrases5.contains(x)) {
                System.out.println(x);
                usedPhrases5.add(x);
            }
        }
        System.out.println("################################################################");
        // LOOP THROUGH A LIST OF VERSES IN THE POEM

        Set<TreeSet<String>> poems = new HashSet<TreeSet<String>>();
        List<String> usedPhrases5Temp = new ArrayList<String>(usedPhrases5);
        int g = 0;
        List<String> cutoffRepeat = new ArrayList<String>();

        for (String x : usedPhrases5Temp) {
            TreeSet<String> poem = new TreeSet<String>();
            if (g++ < VERSE_POOL) {
                int rep = 0;
                String firstLine = x;
                poem.add(firstLine);
                // GET THE LAST WORD OF THE FIRST VERSE
                String keyWord = firstLine.split("\\s")[firstLine.split("\\s").length - 1];
                // LIST THE RHYMING WORDS OF THAT LAST WORD
                List<String> rhymingWords = listRymes(keyWord);
                List<String> temp = new ArrayList<String>();
                int l = 0;
                double percent = (double) rhymingWords.size() / 5.0;
                for (String p : rhymingWords) {
                    if (l++ < percent)
                        temp.add(p);
                }
                String rhymingWordSentence = "";
                System.out.println("INTESECTSSLAKFIOJOW2: " + keyWord);
                for (String n : rhymingWords) {
                    System.out.println("INTESECTSSLAKFIOJOW2: " + n);
                    rhymingWordSentence += n + " ";
                }
                rhymingWordSentence = rhymingWordSentence.trim();
                for (int k = 0; k < usedPhrases5.size() && rep < 5; k++) {
                    // SEE IF ANY FOLLOWING VERSES CONTAIN A WORD IN SAID LIST
                    String n = usedPhrases5.get(k);
                    String cutOff = new ComparePhrasesReturnString().compare(rhymingWordSentence, n);
                    if (cutOff != null) {
                        System.out.println("KDSKJNFIEUWSDF: " + keyWord);
                        System.out.println("KDSKJNFIEUWSDF: " + cutOff);
                    }
                    // CUT OFF THESE VERSES AT SAID WORD
                    String newVerse = null;
                    if (cutOff != null && n.contains(cutOff)) {
                        newVerse = n.substring(0, n.indexOf(cutOff)) + " " + cutOff;
                    }
                    // ADD THAT VERSE TO THE POEM
                    if (newVerse != null && syllable(newVerse) > 7 && !cutoffRepeat.contains(cutOff)) {
                        poem.add(newVerse);
                        cutoffRepeat.add(cutOff);
                        rep++;
                    }
                }
            }
            if (poem.size() > 1) {
                System.out.println("TOWHOMEITMAYCONCERNDSFFEWIO poem=" + poem);
                poems.add(poem);
            }
            for (String verse : poem) {
                System.out.println(verse);
            }
        }
        List<String> s = new ArrayList<String>();
        System.out.println("MMM: " + poems);
        String[] pair = null;
        for (Set<String> poemX : poems) {
            int n = 0;
            List<String> l = j(new ArrayList<String>(poemX));
            if (l != null) {
                for (String poemY : l) {

                    if (n++ < 3 || true) {
                        // IF THE FIRST LETTER SHOULD BE REMOVED
                        if(POSTag3(poemY)) {
                        poemY = poemY.split(" ", 2)[1];
                        }
                        s.add(poemY);
                        //System.out.println("nl: " + poemY);
                    }
                }
            }
        }
        for (String p : s)
        {
            System.out.println(p);
        }
        for(String p:s)
        {
            TextToSpeech.textToSpeech(p, 20);
        }
    }


    public static List<String> pickNRandom (List < String > lst,int n){
        List<String> copy = new LinkedList<String>(lst);
        Collections.shuffle(copy);
        return copy.subList(0, n);
    }
}
