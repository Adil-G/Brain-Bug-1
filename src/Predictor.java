import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
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
 * Created by swaggerton on 29/08/15.
 */
public class Predictor {
    public static POSTaggerME tagger;
    public static PerformanceMonitor perfMon;
    public static List<String> info  = new ArrayList<String>();
    public static String textX = "robot is a mechanical or virtual artificial agent, usually an electro-mechanical machine that is guided by a";
    public static void main(String[] args) throws Exception{
        POSModel model = new POSModelLoader()
                .load(new File("openNLP/en-pos-maxent.bin"));
        perfMon = new PerformanceMonitor(System.err, "sent");
        tagger = new POSTaggerME(model);
        perfMon.start();
        System.out.print("What is your current thought?: ");
        textX = (new Scanner(System.in)).nextLine();

        String taggedTextX = POSTag(textX);
        String query  = "";
        String[] rawInputs = textX.split("\\s");
        String[] parsedInputs = taggedTextX.split("\\s");
        if(query.length()>5) {
            for (String p : parsedInputs) {
                if (p.contains("_NN")) {
                    Pattern pat = Pattern.compile("(.*?)_[A-Z]+");
                    Matcher mat = pat.matcher(p);
                    if (mat.matches()) {
                        query += mat.group(1).trim() + " + ";
                    }

                }
            }
        }
        query = query.trim();
        List<String> lastFive = new ArrayList<String>(Arrays.asList(rawInputs));
        int LAST_WORDS = 5;
        if(lastFive.size()<LAST_WORDS)
        {
            LAST_WORDS = lastFive.size();
        }
        for(int i = lastFive.size()-LAST_WORDS;i<LAST_WORDS;i++)
        {
            query += " " + lastFive.get(i);
        }
        String[] taggedArray = taggedTextX.split("\\s");
        String tags = "";
        ArrayList<String> x = new ArrayList<String>();
        if(taggedArray.length>1)
        {
            tags += taggedArray[taggedArray.length-2] +" ";
            tags += taggedArray[taggedArray.length-1]+" ";
            x = new ArrayList<String>(excecute(query,tags));
            for(String y:x)
            {
                System.out.println(query + " IIII: "+y);
            }
        }
        /*
        List<String> what = j(x);
        for(String w :what)
        {System.out.println("Adil: "+w);

        }*/

        //System.out.println("final --> "+x.get((new Random()).nextInt(x.size())));
    }
    public static List<String> j(ArrayList<String> sentences) throws IOException {
        Map<Double, String> mostCommonWords;
        mostCommonWords = new TreeMap<Double, String>(Collections.reverseOrder());
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
    public static String POSTag(String input) throws IOException {

        //String input = "Hi. How are you? This is Mike.";
        ObjectStream<String> lineStream = new PlainTextByLineStream(
                new StringReader(input));

            String parsed = "";
        String line;
        while ((line = lineStream.read()) != null) {

            String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE
                    .tokenize(line);
            String[] tags = tagger.tag(whitespaceTokenizerLine);

            POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
            System.out.println(sample.toString());
            parsed = sample.toString();
            perfMon.incrementCounter();
        }
        perfMon.stopAndPrintFinalResult();
        return parsed;
    }
    public static List<String> excecute(String input, String lastTwo) throws Exception {
        //processPage(input);
        List<GoogleResults2.Result> results=  GSAPI.main(new String[]{input.trim()});
        for(GoogleResults2.Result result :results)
        {
            info.add(result.getUrl());
        }
        return searchResult(lastTwo);
    }
    public static List<String> searchResult(String lastTwo) throws IOException {
        String wholeText = "";
        List<String> chunks = new ArrayList<String>();
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
                    if (!p.text().toLowerCase().contains("sign in")&&p.text().length()>0&&!p.text().contains(":")&&!p.text().toLowerCase().contains("welcome")&&!p.text().toLowerCase().contains("is a song")) {
                        //chunks.add(p.text().replaceAll("\\[[0-9]+\\]",""));
                        //wholeText += p.text();

                        String parsed = POSTag(p.text());
                        String[] parsedArray = parsed.split("\\s");


                        String[] one = lastTwo.split("\\s");
                        Pattern x = Pattern.compile("_([A-Z]+)");

                        //HERE * IS GREEDY QUANTIFIER THAT LOOKS FOR ZERO TO MANY COMBINATION THAT
                        //START WITH NUMBER
                        Matcher m1 = x.matcher(lastTwo);
                        List<String> m1Tags = new ArrayList<String>();
                        while (m1.find()) {
                            m1Tags.add(m1.group(1));
                        }
                        System.out.println("iiiiii: " + m1Tags);
                        //m2Tags.retainAll(m1Tags);
                        System.out.println("OOOOOOOOOOOO: " + lastTwo);
                        if ((p.text().toLowerCase().contains(m1Tags.get(m1Tags.size()-1).toLowerCase()))
                                ||(p.text().toLowerCase().contains(m1Tags.get(m1Tags.size()-2).toLowerCase()))){
                            String start = "";
                            String lastInternetWordTag = m1Tags.get(m1Tags.size()-1);
                            // match
                            Pattern pattern = Pattern.compile("(.*?)_" + lastInternetWordTag);
                            Matcher matcher = pattern.matcher(parsed);
                            while(matcher.find()) {
                                start = matcher.group(1).replaceAll("_[A-Z]+","");
                                System.out.println("start: " + start);
                                System.out.println("PPPPPPPPPP: " + p.text().indexOf(start));
                                String continuePhrase = p.text().substring(p.text().indexOf(start), p.text().length());
                                continuePhrase = continuePhrase.replace(start,"");
                                chunks.add(textX + " " + continuePhrase.split("\\.")[0]);
                            }
                        }
                        System.out.println("Adding more data..."+parsedArray[parsedArray.length-1]);
                        // IF REGEX MATCH CONTINUE, AND SPLIT IT THERE TOO
                    }
                }
            } catch (IOException j) {
                System.out.println("Failed url connection." + url + " Trying another one.");
            } catch (Exception g) {
                System.out.println("Failed url connection." + url + " Trying another one.");
            }
        }
        System.out.println(wholeText);
        return chunks;
    }
}
