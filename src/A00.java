import SentenceGenerator.GoogleCSAPI;
import com.gtranslate.Audio;
import com.gtranslate.Language;
import javazoom.jl.decoder.JavaLayerException;
import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.ml.AbstractEventModelSequenceTrainer;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by swaggerton on 17/09/15.
 */
public class A00 {
    public static POSModel model;
    public static PerformanceMonitor perfMon;
    public static POSTaggerME tagger;
    public static int MAX_SEARCHES = 100;
    public static ArrayList<String> info = new ArrayList<String>();
    public static Parser parser;
    public static void go(String query) throws IOException {
        initialize();
        getAnyPhraseSS(Parse(query,parser),"VP");
        getAnyPhraseSS(Parse(query,parser),"NP");
        System.out.println("subject: "+phrases.get("VP"));
        System.out.println("object: "+phrases.get("NP"));
        System.out.println(phrases);
    }
    public static Parse Parse(String input,Parser parserTemp) throws InvalidFormatException, IOException {
        // http://sourceforge.net/apps/mediawiki/opennlp/index.php?title=Parser#Training_Tool
        System.out.println("GOGOOG");
        String sentence = "Programcreek is a very huge and useful website.";
        sentence = input;
        Parse[] topParses;
        /*try {

            topParses = ParserTool.parseLine(sentence, parserTemp, 1);
        }catch (StringIndexOutOfBoundsException e)
        {
            topParses = new Parse[0];

            InputStream is = new FileInputStream("openNLP/en-parser-chunking.bin");

            ParserModel model = new ParserModel(is);

            Parser parser = ParserFactory.create(model);
            return Parse(input,parser);

        }
        */
        topParses = ParserTool.parseLine(sentence, parserTemp, 1);
        Parse ps = null;
        String label="";
        System.out.println(topParses.length);
        for (Parse p : topParses) {
            System.out.print("asdfadsfasdf\n");
            p.show();
            ps = p;
            //getAnyPhraseSS(p,"PP");
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
    public static void initialize() throws IOException {
        System.out.println("WFIWOFWOEFIJ");
        POSModel model = new POSModelLoader()
                .load(new File("openNLP/en-pos-maxent.bin"));
        perfMon = new PerformanceMonitor(System.err, "sent");
        tagger = new POSTaggerME(model);
        InputStream is = new FileInputStream("openNLP/en-parser-chunking.bin");

        ParserModel parserModel1 = new ParserModel(is);

        parser = ParserFactory.create(parserModel1);
        adjpPhrases.clear();
        nounPhrases.clear();
        perfMon.start();
        System.out.println("DONE INIT");

    }
    public static TreeMap<String, ArrayList<HashSet<String>>> excecute(String input) throws Exception {

        ArrayList<String> results = GoogleCSAPI.getLinks(input.replaceAll("\\s","+"),true);
        for (String result : results) {
            if(true||!result.toString().toLowerCase().contains("wikipedia.org")) {
                info.add(result);
            }
        }
        //info.clear();
        //info.add(input);
        return searchResult();
    }

    public static void textToSpeech(String bigText, int maxWords) throws IOException, JavaLayerException {
        bigText = bigText.replaceAll("\\W", " ");
        String[] bigWords = bigText.split(" ");
        List<String[]> sayThis = splitArray(bigWords, maxWords);
        for (String[] list : sayThis) {
            String whatToSay = "";
            for (String word : list) {
                whatToSay += word + " ";
            }
            whatToSay = whatToSay.trim().replaceAll("'s", "s").replaceAll(" s ", "s ").replaceAll("\\[.*?\\]", "");
            Audio audio = Audio.getInstance();
            InputStream sound  = audio.getAudio(whatToSay, Language.ENGLISH);
            audio.play(sound);
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
    static TreeMap<String, ArrayList<HashSet<String>>>  mostCommonWords = new TreeMap<String, ArrayList<HashSet<String>>>(Collections.reverseOrder());
    public static TreeMap<String, ArrayList<HashSet<String>>> searchResult() throws IOException {
        mostCommonWords.clear();
        int NumberOfFacts = 5000;
        ArrayList<String> chunks = new ArrayList<String>();
        Map<String, HashSet<String>> firstCluster = new HashMap<String, HashSet<String>>();

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
                        if (!p.text().contains("?")&&p.text().length()>150&&!p.text().contains(":")&&!p.text().toLowerCase().contains("welcome")&&!p.text().toLowerCase().contains("is a song")) {

                            adjpPhrases.clear();
                            nounPhrases.clear();
                            nounPhrases2 = POSTag2(p.text().replace("\\[\\d+\\]",""));
                            HashSet<String> x = new HashSet<String>(nounPhrases2);
                            ArrayList<HashSet<String>> all = new ArrayList<HashSet<String>>();
                            all.add(x);
                            mostCommonWords.put(p.text().replace("\\[\\d+\\]", ""), all);
                            System.out.println("F$)(FREOJG: nouns: " + nounPhrases2);
                        }
                    }
                } catch (IOException j) {
                    System.out.println("Failed url connection." + url + " Trying another one.");
                } catch (Exception g) {
                    System.out.println("Failed url connection." + url + " Trying another one.");
                }
            }
        }catch (Exception e) {
            System.out.println("ERROR! DATA EXTRACTION FALIURE! RESTARTING ...");
            searchResult();
        }
        return mostCommonWords;
    }
    public static String getParsedNoun(String[] parsedString, int index)
    {
        while(index>=0&&parsedString[index].contains("_NN"))
        {

            //System.out.println("ppppp?:"+index+" :: "+parsedString[index]);
            index --;
        }
        index ++;
        String nounB = "";
        while(index<parsedString.length&&parsedString[index].contains("_NN"))
        {
            nounB += parsedString[index] + " ";
            index ++;
        }
        System.out.println(" "+nounB);
        return nounB;
    }
    public static Map<String,String> fullPhrase =  new HashMap<String,String>();
    public static HashSet<String> POSTag2(String input) throws IOException {
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
            //System.out.println(sample.toString());
            samp = sample.toString();

            perfMon.incrementCounter();
        }
        perfMon.stopAndPrintFinalResult();

        String[] x = samp.split("\\s");
        HashSet<String> m = new HashSet<String>();
        for(int i=0;i<x.length;i++)
        {
            String nounA = x[i];
            if(nounA.contains("NN"))
            {
                String nounB = getParsedNoun(x,i);
                nounA = nounA.replaceAll("_[A-Z]+", "");
                nounB =  nounB.replaceAll("_[A-Z]+","");
                m.add(nounA);

                // CREATE ALTERNATIVE, LONGER NOUN TO CLASSIFY PARAGRAPHS
                if(!fullPhrase.containsKey(nounA))
                {
                    // TOTALLY NEW OCCURSANCE
                    fullPhrase.put(nounA,nounB);
                }
                else
                {
                    int currentLength = fullPhrase.get(nounA).split("\\s").length;
                    int newLength = nounB.split("\\s").length;
                    if(currentLength<newLength)
                    {
                        // REPLACE CURRENT NOUN CLASSIFICATION WITH NEW, LONGER ONE
                        fullPhrase.put(nounA,nounB);
                    }
                    else
                    {
                        // KEEP ORIGIONAL
                    }
                }
            }
        }

        return m;
    }
    public static int numberOfOccurrences(String source, String sentence) {
        int occurrences = 0;

        if (source.contains(sentence)) {
            int withSentenceLength    = source.length();
            int withoutSentenceLength = source.replace(sentence, "").length();
            occurrences = (withSentenceLength - withoutSentenceLength) / sentence.length();
        }

        return occurrences;
    }

    static public void main(String[] argv) throws Exception {
        initialize();
        HashMap<String,HashSet<String>> categories = new HashMap<String,HashSet<String>>();
        String input = (new Scanner(System.in)).nextLine();
        //DMiningGoogleOnlyChunksAlternating x = new DMiningGoogleOnlyChunksAlternating();
        TreeMap<String, ArrayList<HashSet<String>>>  information = new TreeMap<String, ArrayList<HashSet<String>>>(Collections.reverseOrder());

        information.putAll(A00.excecute(input));

        System.out.println("#################################################################################################");
        int u = 0;
        for(String n :information.keySet())
        {
            if(u++<1) {
                String keyi = n;
                ArrayList<HashSet<String>> valuei = information.get(n);
                HashSet<String> nounsI = new HashSet<String>(valuei.get(0));
                // LOOP THROUGH EACH CATEGORY AND COMPARE ITS NOUNS TO THE FIRST CATEGORY, SECCOND CATEGORY, AND SO ON
                for (String m : information.keySet()) {
                    // IF THE TWO CATEGORIES IN QUESTION ARE NOT THE SAME, CONTINUE
                    if (!m.equals(n)) {
                        String keyj = n;
                        // GET THE SET OF NOUNS FOR THE PARAGRAPH IN QUESTION
                        ArrayList<HashSet<String>> valuej = information.get(m);
                        HashSet<String> nounsJ = new HashSet<String>(valuej.get(0));
                        nounsJ.retainAll(nounsI);
                        HashSet<String> paragraphPair = new HashSet<String>();
                        // ADD PARARGRAPHS TO A SET TO BE ABLE TO ADD IT TO THE CATEGORY
                        paragraphPair.add(m);
                        paragraphPair.add(n);
                        for (String cat : nounsJ) {
                            cat = fullPhrase.get(cat);
                            System.out.println("NounsFWIOFWOFSINOG: " + cat);
                            if (!categories.containsKey(cat)) {
                                categories.put(cat, paragraphPair);
                            } else {
                                categories.get(cat).addAll(paragraphPair);
                            }
                        }


                    }
                }}
        }
                // CONCATENATE CATEGORIAL PARAGRAPHS IN A BANK WHILE REMOVING THOSE PARAGRAPHS FROM THE BANK
                // USE A REVERSE - MOST RELEVENT FILTERING ALGORITHM

                //categories = cascadeTopics(categories);
                System.out.println(categories);
                // PRINT OUT PARAGRAPHS IN ORDER OF CATEGORIES
                int i = 0;
        String allText = "";
        allText += "\n\n\n";
        ArrayList<String> nouns = new ArrayList<String>(POSTag2(input));
        String noun = input;
        if(nouns.size()>0)
        {
            noun = fullPhrase.get(anyItem(nouns));
        }
        allText += "When it comes to " + noun + ", we come across examples of ";
        ArrayList<String> h = new ArrayList<String>(categories.keySet());
        for(int k= 0;k<h.size();k++)
        {
            String topic = h.get(k);
            if(k==(h.size()-1))
            {
                allText += "and ofcoarse, " + topic +".";
            }
            else
            {
                allText += topic +", ";
            }

        }
        allText += " In this piece, we will investigate how all of this relates to " + input+".";
        // Test thesis: "king duncan's contribution to the play"
        // Test thesis: "Characterize the relationship between Macbeth and Lady Macbeth. If the main theme of Macbeth is ambition, whose ambition is the driving force of the play—Macbeth’s, Lady Macbeth’s, or both?"
                for (String cat : categories.keySet()) {
                    if ((i++ < 1 || true) && cat != null) {
                        allText += "\n\n\n";
                        System.out.println("##################         "+ i+" : " + cat + "            #######################");
                        // FIND EXAMPLE IN THE CATEGORY (QOATES ARE BETTER THAN DATES)
                        ArrayList<String> paras = new ArrayList<>(categories.get(cat));
                        ArrayList<String> examples = new ArrayList<String>();
                        boolean foundOne = false;
                        for (int j = 0; !foundOne && j < paras.size(); j++) {

                            String p = paras.get(j);
                            Pattern pat = Pattern.compile("\\d\\d\\d\\d");
                            Matcher mat = pat.matcher(p);
                            //p.contains("\"")||
                            /*
                            if(p.contains("\""))
                            {
                                examples.add(p);
                                foundOne = true;
                            }
                            else*/ if (mat.matches()) {
                                examples.add(p);
                            }
                        }
                        String bestExample = null;
                        if (examples.size() > 0) {
                            bestExample = anyItem(examples);
                        } else {
                            bestExample = anyItem(paras);
                        }
                        paras.remove(bestExample);
                        // MAKE TOPIC SENTENCE
                        String full = "";
                        for (String paragraph : paras) {
                            full += paragraph;
                        }
                        // String topicSentence = bestPhrase(new ArrayList<String>(Arrays.asList(full.split("[\\.\\?!]+"))));
                        // LIST OF SENTENCES
                        ArrayList<String> s = new ArrayList<String>(Arrays.asList(full.split("[\\.\\?!]+")));
                        String bestSentence = new String();
                        for (String x : s) {
                            if (x.contains(cat)) {
                                bestSentence = x;
                            }
                        }
                        if(bestSentence.length()<6)
                        {
                            for (String x : s) {
                                for(String subCategory:cat.split("\\s")) {
                                    if (x.contains(subCategory)) {
                                        bestSentence = x;
                                    }
                                }
                            }
                        }
                        if(bestSentence.length()<6)
                        {
                            bestSentence = anyItem(s);
                        }
                        //String topicSentence = anyItem(s);
                        String topicSentence = bestSentence;

                        //topicSentence = stringRefinedTopicSentence(topicSentence);
                        allText += "Furthermore, if we look into " + input +" in relation to " + cat
                                +", we can also say ";
                        System.out.println("--> " + topicSentence + ". To elaborate, ");
                        allText += topicSentence + ". To elaborate, "+"\n";
                        for (String paragraph : paras) {
                            System.out.println("- " + paragraph);
                            allText += " " + paragraph+"\n";
                        }
                        System.out.println("For example, " + bestExample);
                        allText += "For example, " + bestExample+"\n";
                        // Get thesis
                        String thesis = bestPhrase(new ArrayList<String>(Arrays.asList(full.split("[\\.\\?!]+"))));
                        thesis = stringRefinedTopicSentence(thesis);
                        //System.out.println("::>> " + ppPhrasesForTopicSentence);
                        String refinedInput = stringRefinedTopicSentence(input);
                        String[] responses = new String[]{"Ultimately", "This is why", "It is for this reason"
                                , "This is why I believe"};
                        ;
                        ArrayList<String> responseList = new ArrayList<String>(Arrays.asList(responses));
                        String response = anyItem(responseList);
                        //System.out.println("\n"+response+", " + refinedInput + " because of" + thesis);
                        System.out.println("\n" + response + ", " + refinedInput + " because " + thesis);
                        allText += "\n" + response + ", " + refinedInput + " because " + thesis+"\n";
                    }
                }
                System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                System.out.println(allText);
        textToSpeech(allText,20);

    }
    public static HashMap<String,HashSet<String>> cascadeTopics(HashMap<String,HashSet<String>> categories) throws IOException {
        ArrayList<String> keys = new ArrayList<String>(categories.keySet());
        ArrayList<HashSet<String>> values = new ArrayList<HashSet<String>>();
        for(String key:keys)
        {
            values.add(categories.get(key));
        }
        HashSet<String> allPossibleParagraphs = new HashSet<String>();
        for(HashSet<String> catSet:values)
        {
            // GET CORRESPONDING VALUES FROM ORIGIONAL MAP
            for(String paragraph :catSet)
            {
                allPossibleParagraphs.add(paragraph);
            }
        }
        HashMap<String,ArrayList<String>> relationTree = new HashMap<String,ArrayList<String>>();
        // FORMAT IS LIKE THIS: map = {paragraph1 => [key1, key2, ... ,keyn]}
        for(String paragraph:allPossibleParagraphs)
        {
            for(String key:keys)
            {
                if(categories.get(key).contains(paragraph))
                {
                    // THIS CATEGORY HAS IT, ADD IT TO THE MAP
                    if(!relationTree.containsKey(paragraph))
                    {
                        // This paragraph is new
                        relationTree.put(paragraph,new ArrayList<String>(Arrays.asList(new String[]{key})));
                    }
                    else
                    {
                        // This paragraph is old
                        relationTree.get(paragraph).add(key);
                    }
                }
                else
                {
                    // THIS CATEGORY DOESN'T HAVE PARAGRAPH. DO NOTHING WITH IT
                }
            }
        }
        HashMap<String,HashSet<String>> finalCategoriesMap = new HashMap<String,HashSet<String>>();
        // Format like this: map = {category1 => [paragraph1, paragraph2, ... ,paragraphn]}

        // A MAP CALLED RELPATIONTREE IS A MAP THAT GIVES ALL THE USES OF A
        // PARTICULAR PARAGRAPH
        for(String key : relationTree.keySet())
        {
            String currentParagraph = key;
            ArrayList<String> listOfCategories = relationTree.get(key);
            ArrayList<String> listOfCategoriesFiltered = new ArrayList<String>();
            for(String category : listOfCategories)
            {
                if(finalCategoriesMap.containsKey(category))
                {
                    if(finalCategoriesMap.get(category).size()>3)
                    {
                        // DO NOTHING
                    }
                    else
                    {
                        // THIS IS A SATISFIABLE CATEGORY TO ADD TO
                        listOfCategoriesFiltered.add(category);
                    }
                }
                else
                {
                    // THIS IS A SATISFIABLE CATEGORY TO ADD TO
                    listOfCategoriesFiltered.add(category);
                }
            }
            String suitableCategory =null;
            if(listOfCategoriesFiltered.size()>0)
            {
                // IF THERE ARE ANY SELDOM USED CATEGORIES OUT THERE
                suitableCategory= anyItem(listOfCategoriesFiltered);
            }
            else
            {
                // IF ALL THE CATEGORIES ARE ABUSED
               suitableCategory = anyItem(listOfCategories);
            }
            if(!finalCategoriesMap.containsKey(suitableCategory))
            {
                // This category is new
                finalCategoriesMap.put(suitableCategory,new HashSet<String>(Arrays.asList(new String[]{currentParagraph})));
            }
            else
            {
                // This category is old
                finalCategoriesMap.get(suitableCategory).add(currentParagraph);
            }
        }

        return finalCategoriesMap;
    }

    public static HashMap<String,HashSet<String>> leastRelevent(HashMap<String,HashSet<String>> categories) throws IOException {
        ArrayList<String> keys = new ArrayList<String>(categories.keySet());
        ArrayList<HashSet<String>> values = new ArrayList<HashSet<String>>();
        HashMap<String,HashSet<String>> finalCategoriesMap = new HashMap<String,HashSet<String>>();
        for(String key:keys)
        {
            values.add(categories.get(key));
        }
        ArrayList<HashSet<String>> newCategories = worstPhrase(values);

        for(HashSet<String> catSet:newCategories)
        {
            // GET CORRESPONDING VALUES FROM ORIGIONAL MAP
            int index = values.indexOf(catSet);
            String key = keys.get(index);
            finalCategoriesMap.put(key,catSet);
        }
        return finalCategoriesMap;
    }
    public static String anyItem(ArrayList<String> catalogue)
    {
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(catalogue.size());
        System.out.println("Managers choice this week" + catalogue.get(index) + "our recommendation to you");
        return catalogue.get(index);
    }
    public static String stringRefinedTopicSentenceSS(String topicSentence) throws IOException {
        ppPhrasesForTopicSentence.clear();
        getNounPhrasesSS(Parse(topicSentence, parser));
        if(parser==null) {
           // System.out.println("PARSER IS NULL!!!!");
        }
        // GET PP PHARSE FROM TOPIC SENTENCE

        String pp = ppPhrasesForTopicSentence.firstEntry().getValue();

        //System.out.println("LOOL: "+ppPhrasesForTopicSentence);
        // GET INDEX OF PP PHRASE
        int indexPP = topicSentence.indexOf(pp);
        int INDEX0 = 0;
        int INDEX_FIRST = indexPP;
        int INDEX_LAST = indexPP + pp.length();
        int INDEX_END = topicSentence.length();
        String topicSentenceFiltered = topicSentence.substring(INDEX0, INDEX_FIRST) + topicSentence.substring(INDEX_LAST, INDEX_END);
        fullPhrase.clear();
        // GET ALL NOUNS FROM TOPIC SENTENCE (IN UN-PARSED FROMAT)
        HashSet<String> nouns = POSTag2(topicSentenceFiltered);
        // ORDER NOUS FROM CLOSSEST TO PP (ordered map from smallest to highest)7

        TreeMap<Integer, String> closeness = new TreeMap<Integer, String>();
        for (String i : nouns) {
            closeness.put(Math.abs(INDEX_FIRST - topicSentenceFiltered.indexOf(i)), i);
        }
        // PUT CLOSEST NOUN WITH PP PHRASE
        if (closeness.size() > 0) {
            String newTopicSentence = fullPhrase.get(closeness.firstEntry().getValue()) + " " + pp;
            return newTopicSentence;
        } else {
            return new String();
        }
        /*
        try {

        }catch (NullPointerException e)
        {
            return "<< No sentence gernated >>";
        }
        catch (StringIndexOutOfBoundsException j)
        {//ARSER IS NULL!!!
            return "<< No sentence gernated >>";
        }*/

    }
    public static String stringRefinedTopicSentence(String topicSentence) throws IOException {
        ppPhrasesForTopicSentence.clear();
        Parse play = Parse(topicSentence, parser);
        TreeMap<Integer,String> ppPhrasesAll = getNounPhrases(play, new TreeMap<Integer,String>());
try {
    // GET PP PHARSE FROM TOPIC SENTENCE
    String pp = topicSentence;
    if (ppPhrasesAll.size() > 0) {
        pp = ppPhrasesAll.firstEntry().getValue();

        //System.out.println("LOOL: "+ppPhrasesForTopicSentence);
        // GET INDEX OF PP PHRASE
        int indexPP = topicSentence.indexOf(pp);
        int INDEX0 = 0;
        int INDEX_FIRST = indexPP;
        int INDEX_LAST = indexPP + pp.length();
        int INDEX_END = topicSentence.length();
        String topicSentenceFiltered = topicSentence.substring(INDEX0, INDEX_FIRST) + topicSentence.substring(INDEX_LAST, INDEX_END);
        fullPhrase.clear();
        // GET ALL NOUNS FROM TOPIC SENTENCE (IN UN-PARSED FROMAT)
        HashSet<String> nouns = POSTag2(topicSentenceFiltered);
        // ORDER NOUS FROM CLOSSEST TO PP (ordered map from smallest to highest)7

        TreeMap<Integer, String> closeness = new TreeMap<Integer, String>();
        for (String i : nouns) {
            closeness.put(Math.abs(INDEX_FIRST - topicSentenceFiltered.indexOf(i)), i);
        }
        // PUT CLOSEST NOUN WITH PP PHRASE
        if (closeness.size() > 0) {
            String newTopicSentence = "The " + fullPhrase.get(closeness.firstEntry().getValue()) + " " + pp;
            return newTopicSentence;
        } else {
            return topicSentence;
        }
        /*
        try {

        }catch (NullPointerException e)
        {
            return "<< No sentence gernated >>";
        }
        catch (StringIndexOutOfBoundsException j)
        {//ARSER IS NULL!!!
            return "<< No sentence gernated >>";
        }*/
    } else {
        //System.out.println("ooahhh");
        return topicSentence;
    }
}
catch (StringIndexOutOfBoundsException e)
{
    return topicSentence;
}

    }

    public static TreeMap<Integer,String> ppPhrasesForTopicSentence= new TreeMap<Integer,String>(Collections.reverseOrder());
    public static ArrayList<Parse> nounPhrases= new ArrayList<Parse>();
    public static HashSet<String> nounPhrases2= new HashSet<String>();
    public static ArrayList<Parse> adjpPhrases= new ArrayList<Parse>();
    public static TreeMap<Integer,String> getNounPhrases(Parse p,TreeMap<Integer,String> addPP) {
        if(p!=null) {
            if (p.getType().equals("NP")) {
                nounPhrases.add(p);
            }
            if (p.getType().equals("VP")||p.getType().equals("PP")) {
               // System.out.println("--> "+p.toString());
                ppPhrasesForTopicSentence.put(p.toString().length(), p.toString());
                addPP.put(p.toString().length(), p.toString());
                //System.out.println(">>:>:> " + p.toString());
            }
            for (Parse child : p.getChildren()) {
                getNounPhrases(child,addPP);
            }
        }
        //System.out.println("EFIOWJFOWEIJDSF: "+addPP);
        return addPP;
    }
    public static void getNounPhrasesSS(Parse p) {
        if(p!=null) {
            if (p.getType().equals("NP")) {
                nounPhrases.add(p);
            }
            if (p.getType().equals("S")) {
                // System.out.println("--> "+p.toString());
                ppPhrasesForTopicSentence.put(p.toString().length(), p.toString());
            }
            for (Parse child : p.getChildren()) {
                getNounPhrases(child, new TreeMap<Integer,String>());
            }
        }
    }
    static HashMap<String,Parse> phrases = new HashMap<String,Parse>();
    public static void getAnyPhraseSS(Parse p, final String TAG) {
        if(p!=null) {

            if (p.getType().equals(TAG)) {
                if(phrases.containsKey(TAG)) {
                    if (phrases.get(TAG).getText().length() < p.getText().length()) {
                        phrases.put(TAG, p);
                    }

                }
                else{
                    phrases.put(TAG, p);
                }

            }
            for (Parse child : p.getChildren()) {
                getAnyPhraseSS(child, TAG);

            }
        }
    }
    public static String bestPhrase(ArrayList<String> sentences) throws IOException {
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
        ///System.out.println("#####################################################################################");

        Map<Double, String> bestResults = new TreeMap<Double, String>(Collections.reverseOrder());
        List<String>list = new ArrayList<String>();
        int i = 0;
        boolean stop = true;
        int NumberOfFacts = 5;
        for (Map.Entry entry : mostCommonWords.entrySet()) {
            if (i++ < NumberOfFacts&&stop) {
                list.add(entry.getValue().toString());
                //System.out.println("Added: "+entry.getValue().toString());
                //System.out.println(entry);
                stop = true;
                //double factor  = ComparePhrases.compare(input, entry.getValue().toString());
                //bestResults.put(factor,entry.getValue().toString());
            }
        }
        return list.get(0);
    }
    public static ArrayList<HashSet<String>> worstPhrase(ArrayList<HashSet<String>> sentences) throws IOException {
        TreeMap<Double, HashSet<String>> mostCommonWords = new TreeMap<Double, HashSet<String>>();
        HashSet<Double> previousFactorSums = new HashSet<Double>();
        // compare list to itself
        for (int i = 0; i < sentences.size(); i++)
        {
            double factorSum = 0;
            for (int j = 0; j < sentences.size(); j++)
            {
                double currentFactor;
                // check to see if there is repetition
                // compare the two objects
                HashSet<String> dummySeti = new HashSet<String>(sentences.get(i));
                HashSet<String> dummySetj = new HashSet<String>(sentences.get(j));
                dummySeti.retainAll(dummySetj);
                currentFactor = dummySeti.size();

                // accumulate maching factors for coparison later on
                factorSum += currentFactor;
            }

            // sort sentences by matching factor
            factorSum = factorSumIncrement(factorSum,previousFactorSums);
            if(previousFactorSums.contains(factorSum))
            {
                System.out.println("Error!!!!!! Overloaded FactorSum!");
                System.exit(0);
            }
            mostCommonWords.put(factorSum, sentences.get(i));
                    /*
                    if(factorSum>bestFactor)
                    {
                        bestSentence = sentences.get(i);
                        bestFactor=factorSum;
                    }*/
            // REGISTER FACTOR SUM SO IT DOES NOT OVERIDE FUTURE SETS OF INFORMATION
            previousFactorSums.add(factorSum);
        }
        ///System.out.println("#####################################################################################");
        ArrayList<HashSet<String>> list = new ArrayList<HashSet<String>>();
        int i = 0;
        for(HashSet<String> h :mostCommonWords.values())
        {
            if(i++<30)
            {
                list.add(h);
            }
        }

        return list;
    }
    public static double factorSumIncrement(double factorSum,HashSet<Double> previousFactorSums)
    {
        if(previousFactorSums.contains(factorSum))
        {
            factorSum += 0.1;
            return factorSumIncrement(factorSum,previousFactorSums);
        }
        else
        {
            return factorSum;
        }
    }
}
