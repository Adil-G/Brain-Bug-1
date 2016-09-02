

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
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
import opennlp.tools.util.Span;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by swaggerton on 13/08/15.
 */
public class TestOpenNLP {
    public static int VERSE_POOL = 50;
    public static List<Parse> nounPhrases= new ArrayList<Parse>();
    public static List<Parse> adjpPhrases= new ArrayList<Parse>();
    public static List<String> nouns = new ArrayList<String>();
    public static List<String> adj = new ArrayList<String>();
    public static String parsedPhrase = "";
    public static List<String[]> fruits =  new ArrayList<String[]>();
    public static List<String> allPhrases = new ArrayList<String>();
    public static void main(String[] args) throws Exception{

        InputStream is = new FileInputStream("openNLP/en-parser-chunking.bin");

        ParserModel model = new ParserModel(is);

        Parser parser = ParserFactory.create(model);

        System.out.print("What do you want me to learn about?");
        String learn = (new Scanner(System.in)).nextLine();
        ops3(learn,parser,false);

        //String response = "I suppose that " + ops3(learn);
        //System.out.println("@ ~"+response);
        /*
        String learn  =args[0];
        DMiningGoogleOnlyChunks dmg = new DMiningGoogleOnlyChunks();
        List<String> paragraphs = dmg.excecute(learn);
        int i = 0;
        for(String paragraph:paragraphs)
        {
            if(i++<10) {
                //ops(paragraph);
            }
            ops2(paragraph);
        }*/
/*
        int idx = new Random().nextInt(fruits.size());
        String[] group = (fruits.get(idx));
        String phrase  = "why is the "+group[0] + " " + group[1];
        System.out.println("final--> "+phrase);
       */

    }
    public static int syllable(String text)
    {
        Pattern p = Pattern.compile("[aeiouy]+?\\w*?[^e]");
        String[] result = p.split(text);
        return result.length;
    }
    private static String pickNRandom(String url) throws IOException {
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
    public static List<String> pickNRandom (List < String > lst,int n){
        List<String> copy = new LinkedList<String>(lst);
        Collections.shuffle(copy);
        return copy.subList(0, n);
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
    public static void POSTag(String input) throws IOException {
        POSModel model = new POSModelLoader()
                .load(new File("en-pos-maxent.bin"));
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
    public static void gogo(String paragraphOfChunks) throws IOException {
        String phrase = paragraphOfChunks;
        List<String> allPhrasesx = new ArrayList<String>();
        allPhrasesx.add(phrase);
        List<String> usedPhrases5 = new ArrayList<String>();
        for (String x : allPhrasesx) {
            System.out.print("\n");
            ;
            int count = syllable(x);//&&!POSTag(x)
            if (count > 15 && count < 40 && !usedPhrases5.contains(x)) {
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
        System.out.println("MMM: " + poems);
        for (Set<String> poemX : poems) {
            int n = 0;
            for (String poemY : poemX) {
                if (n++ < 3 || true) {
                    System.out.println("nl: " + poemY);
                }
            }
        }
    }
    public static List<String> ops3(String input, Parser parserTemp,boolean isPersonal) throws Exception {

        System.out.print("What do you want me to learn about?");
        //String learn = (new Scanner(System.in)).nextLine();
        String learn  =input;
        String response = "I don't know";
        DMiningGoogleOnlyChunks dmg = new DMiningGoogleOnlyChunks();
        List<String> paragraphs = dmg.excecute(learn);
        int i = 0;
        allPhrases.clear();
        List<String> newPhrases = new ArrayList<String>();
        for(String paragraph:paragraphs)
        {
            // EACH PARAGRAPH HERE IS A CHUNK7
            // NEXT LINE SPLITS CHUNKS INTO MORE CHUNKS
            response = ops(paragraph,parserTemp);
            newPhrases.add(response);
            //gogo(response);
            if(i<10) {

                i++;
            }

        }
        if(isPersonal) {
            allPhrases = bestPhrase(newPhrases);
        }else
        {
            allPhrases = bestPhrase(allPhrases);
        }

        return allPhrases;
    }
    public static String ops4(String input) throws Exception {
        System.out.print("What do you want me to learn about?");
        //String learn = (new Scanner(System.in)).nextLine();
        String learn  =input;
        String response = "I don't know";
        DMiningGoogleOnlyChunks dmg = new DMiningGoogleOnlyChunks();
        List<String> paragraphs = dmg.excecute(learn);
        int i = 0;
        for(String paragraph:paragraphs)
        {
            if(i<10) {
                response = ops2(paragraph);
                i++;
            }

        }
        return response;
    }
    public static String ops2(String input) throws IOException {
        parsedPhrase = "";
        nouns.clear();
        adj.clear();
        chunk(input);
        //System.out.println(parsedPhrase);
        // FIRST INDEX OF ARRAY IS NNP, SECCOND IS (JJ|NN)
        List<String[]> k = new ArrayList<String[]>();
        // Create a Pattern object
        Pattern r = Pattern.compile(".*?([A-Za-z0-9]+)[_]([A-Z]+).*?");

        // Now create matcher object.
        Matcher m = r.matcher(parsedPhrase);

        while (m.find()) {
            if (m.group(2).equals("NNP")) {
                nouns.add(m.group(1));
            } else if (m.group(2).equals("JJ")) {
                if(!m.group(1).toLowerCase().equals("same")) {
                    adj.add(m.group(1));
                }
            }
            if (nouns.size() > 0 && adj.size() > 0) {
                k.add(new String[]{nouns.get(nouns.size() - 1), adj.get(adj.size() - 1)});
                parsedPhrase = parsedPhrase.replaceFirst(nouns.get(nouns.size() - 1), "").replaceFirst(adj.get(adj.size() - 1), "");
                nouns.clear();
                adj.clear();
            }
        }

        for (String[] group : k) {
            System.out.println(group[0]+" are " + group[1]);
        }
        fruits.addAll(k);
        if (fruits.size() > 0) {
            List<String> l  = new ArrayList<String>();
            for(String[] x :fruits)
            {
                if(x[0].length()>3) {
                    //l.add(x[0].toLowerCase().trim());
                }
                if(x[1].length()>3&&!x[1].toLowerCase().trim().equals("other")
                        &&!x[1].toLowerCase().trim().equals("s")) {
                    l.add((x[0] + " are " + x[1]).toLowerCase().trim());
                }
            }
            String phrase = l.get((new Random()).nextInt(l.size()));//mostCommon(l);
            /*
            int idx = new Random().nextInt(fruits.size());
            String[] group = (fruits.get(idx));
            String phrase = "";
            if(!group[0].toLowerCase().endsWith("s"))
            {
                phrase= group[0] + " is " + group[1];
            }
            else
            {
                phrase= group[0] + " are " + group[1];
            }
            */
            return phrase;
        } else
        {
            return "I don't know";
        }
    }
    public static <T> T mostCommon(List<T> list) {
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
    public static List<String> bestPhrase(List<String> sentences) throws IOException {
        Map<Double, String> mostCommonWords = new TreeMap<Double, String>(Collections.reverseOrder());
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
        List<String>list = new ArrayList<String>();
        int i = 0;
        boolean stop = true;
        int NumberOfFacts = 5;
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
        return list;
    }
    public static String ops(String par,Parser parserTemp) throws IOException {

        nounPhrases.clear();
        String paragraph = "Ubisoft is a very huge and useful website.";
        //paragraph = (new Scanner(System.in)).nextLine();
        paragraph = par;
        String finalSay = "";
        String[] sentences;// = SentenceDetect(paragraph);
        sentences = paragraph.split("[\\.\\?!]+");
        boolean stop = false;
        for(String input: sentences) {
            //System.out.println("\n$$$ "+input);
            String[] all = input.split("[,\\.!\\?]");
            allPhrases.addAll(Arrays.asList(all));
            //allPhrases.add(input);

            if(!stop) {
                stop = true;
                getNounPhrases(Parse(input,parserTemp), "NP", "VP");
                int max = Math.min(nounPhrases.size(), adjpPhrases.size());
                if (max > 0) {
                    max = 1;
                    for (int i = 0; i < max; i++) {
                        String np = nounPhrases.get(i).toString();
                        String adjp = adjpPhrases.get(i).toString();
                        String china = null;//ops2(np);
                        if (true || np.toLowerCase().contains("china")) {
                            if (china == null) {
                                //System.out.println("hello there");
                                /*
                                NP.clear();
                                getNounPhrases(Parse(adjp));
                                if(NP.size()>0)
                                {
                                    adjp = NP.get(0).getText().toString();
                                }
                                */
                                System.out.println("\n@ " + adjp);//.split(",")[0]
                                finalSay = np+" "+adjp;
                            } else {
                                System.out.println("\n@ " + china + "--> " + adjp);
                                finalSay = np+" "+adjp;
                            }
                        }
                    }
                }
                nounPhrases.clear();
                adjpPhrases.clear();
            }

        }
        return finalSay;
    }
    public static List<Parse> NP= new ArrayList<Parse>();
    public static void getNounPhrases(Parse p) {
        if (p.getType().equals("NP")) {
            //nounPhrases.add(p);
            NP.add(p);
            //System.out.println("SFLSKEJF");
        }
        for (Parse child : p.getChildren()) {
            getNounPhrases(child);
        }
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
    public static Parse Parse(String input,Parser parserTemp) throws InvalidFormatException, IOException {
        // http://sourceforge.net/apps/mediawiki/opennlp/index.php?title=Parser#Training_Tool

        String sentence = "Programcreek is a very huge and useful website.";
        sentence = input;
        Parse[] topParses;
        try {
            topParses = ParserTool.parseLine(sentence, parserTemp, 1);
        }catch (StringIndexOutOfBoundsException e)
        {
            topParses = new Parse[0];
            /*
            InputStream is = new FileInputStream("openNLP/en-parser-chunking.bin");

            ParserModel model = new ParserModel(is);

            Parser parser = ParserFactory.create(model);
            return Parse(input,parser);
            */
        }

        Parse ps = null;
        String label="";
        for (Parse p : topParses) {
            System.out.print("\n");
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

        //is.close();

        return ps;
	/*
	 * (TOP (S (NP (NN Programcreek) ) (VP (VBZ is) (NP (DT a) (ADJP (RB
	 * very) (JJ huge) (CC and) (JJ useful) ) ) ) (. website.) ) )
	 */
    }
    public static void getNounPhrases(Parse p,String labelA,String labelB) {
        if(p!=null) {
            if (p.getType().equals("NP")) {
                nounPhrases.add(p);
            }
            if (p.getType().equals(labelB) || p.getType().equals("VP")) {
                adjpPhrases.add(p);
            }
            for (Parse child : p.getChildren()) {
                getNounPhrases(child, labelA, labelB);
            }
        }
    }
    public static void getNounPhrases2(Parse p,String labelA) {
        if (p.getType().equals(labelA)) {
            nounPhrases.add(p);
        }
        if (p.getType().equals(labelA)) {
            adjpPhrases.add(p);
        }
        for (Parse child : p.getChildren()) {
            getNounPhrases2(child,labelA);
        }
    }
    public static void chunk(String inp) throws IOException {
        POSModel model = new POSModelLoader()
                .load(new File("openNLP/en-pos-maxent.bin"));
        PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
        POSTaggerME tagger = new POSTaggerME(model);

        String input = "He was visiting the United States when Adolf Hitler came to power in 1933 and, being Jewish, did not go back to Germany, where he had been a professor at the Berlin Academy of Sciences.";
        input = inp;
        ObjectStream<String> lineStream = new PlainTextByLineStream(
                new StringReader(input));

        perfMon.start();
        String line;
        String whitespaceTokenizerLine[] = null;

        String[] tags = null;
        while ((line = lineStream.read()) != null) {
            whitespaceTokenizerLine = WhitespaceTokenizer.INSTANCE
                    .tokenize(line);
            tags = tagger.tag(whitespaceTokenizerLine);

            POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
            //System.out.println(sample.toString().replaceAll("[,\\.]", ""));
            parsedPhrase =sample.toString().replaceAll("[,\\.]", "");
            perfMon.incrementCounter();
        }
        perfMon.stopAndPrintFinalResult();

        // chunker
        /*
        InputStream is = new FileInputStream("openNLP/en-chunker.bin");
        ChunkerModel cModel = new ChunkerModel(is);

        ChunkerME chunkerME = new ChunkerME(cModel);
        String result[] = chunkerME.chunk(whitespaceTokenizerLine, tags);

        for (String s : result) {
            //System.out.println(s);
        }

        Span[] span = chunkerME.chunkAsSpans(whitespaceTokenizerLine, tags);
        for (Span s : span)
            if(s.getType().equals("ADVP")) {
                String[] words = input.split("\\s");
                String[] grp = Arrays.copyOfRange(words, s.getStart()-1, s.getEnd()+1);
                for(String word:grp)
                {
                    //System.out.print(word+" ");
                }
                //System.out.print("\n");
                //System.out.println(s);
            }
            */
    }
}
