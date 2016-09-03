

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
}
