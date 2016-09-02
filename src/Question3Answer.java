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
public class Question3Answer {
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
        while (true) {
            ops((new Scanner(System.in)).nextLine());
        }
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
        return (type.equals("NNP"));
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

    public static void ops(String par) throws Exception {

        InputStream is = new FileInputStream("openNLP/en-parser-chunking.bin");

        ParserModel model = new ParserModel(is);

        Parser parser = ParserFactory.create(model);
        //String response = "I suppose that " + new TestOpenNLP().ops3(par);
        List<String> allPhrases = new TestOpenNLP().ops3(par,parser,false);
        List<String> usedPhrases5 = new ArrayList<String>();
        List<String> usedPhrases7 = new ArrayList<String>();
        for (String x : allPhrases) {
            System.out.print("\n");
            ;
            int count = syllable(x);//&&!POSTag(x)
            if (count == 15 && !usedPhrases5.contains(x)) {
                System.out.println(x);
                usedPhrases5.add(x);
            }
            if (count == 16 && !usedPhrases7.contains(x)) {
                System.out.println(x);
                usedPhrases7.add(x);
            }
            if (count == 15 && !usedPhrases5.contains(x)) {
                System.out.println(x);
                usedPhrases5.add(x);
            }
        }
        System.out.println("################################################################");
        int max = Math.min((int) (usedPhrases5.size() / 2.0), usedPhrases7.size());

        for (int i = 0; i < max; i++) {
            String first = "";
            String second ="";
            String third = "";
            System.out.print("\n");
            System.out.println(usedPhrases5.get(i));
            first = usedPhrases5.get(i);
            String lastWord = usedPhrases5.get(i).split("\\s")[usedPhrases5.get(i).split("\\s").length - 1];
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
                //System.out.println("Rhyme = "+rhyme1);
                rjhymes.add(rhyme1);
            }
            //TreeMap<Integer,String> set = new TreeMap<Integer,String>(Collections.reverseOrder());
            if (rjhymes.size() > 1) {
                int maxSize = 5;
                if(rjhymes.size()<maxSize) {
                    maxSize = rjhymes.size();
                }

                rjhymes = pickNRandom(rjhymes, maxSize-2);
                // SECCOND SENTNECE
                //set.clear();
                lastWord = usedPhrases7.get(i).split("\\s")[usedPhrases7.get(i).split("\\s").length - 1];
                String perfectWord = "FAILED";
                if(rjhymes.size()<1)
                {
                    // set.put(0,"FAILED");
                }
                perfectWord = new WiktionaryTest().compareDefs(rjhymes,lastWord);
                rjhymes.remove(perfectWord);
                System.out.println(usedPhrases7.get(i).replaceAll(" [^ ]+$", "") + " " + perfectWord);
                second = usedPhrases7.get(i).replaceAll(" [^ ]+$", "") + " " + perfectWord;
                // THIRD SENTNECE
                //set.clear();
                lastWord = usedPhrases5.get(i+1).split("\\s")[usedPhrases5.get(i+1).split("\\s").length - 1];
                if(rjhymes.size()<1)
                {
                    // set.put(0,"FAILED");
                }
                perfectWord = new WiktionaryTest().compareDefs(rjhymes,lastWord);

                rjhymes.remove(perfectWord);
                System.out.println(usedPhrases5.get(i + 1).replaceAll(" [^ ]+$", "") + " " + perfectWord);
                third = usedPhrases5.get(i + 1).replaceAll(" [^ ]+$", "") + " " + perfectWord;
                System.out.println("\n\n"+first+"\n"+second+"\n"+third);
            } else {
                //System.out.println(usedPhrases7.get(i));
                //System.out.println(usedPhrases5.get(i + 1));
            }
        }



    }
    public static List<String> pickNRandom (List < String > lst,int n){
        List<String> copy = new LinkedList<String>(lst);
        Collections.shuffle(copy);
        return copy.subList(0, n);
    }
}
