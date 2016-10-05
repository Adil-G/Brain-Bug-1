package general.graph.theory;

import SentenceGenerator.ComparePhrases;
import SentenceGenerator.GoogleCSAPI;
import general.FindKeyWordsTest;
import general.LuceneSnowBallTest;
import general.chat.MainGUI;
import general.chat.ProgressBarDemo;
import general.comparePhrasesold_june7;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.wordnet.SynonymMap;
import sun.applet.Main;

import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static general.FindKeyWordsTest.findName;

/**
 * Created by corpi on 2016-04-30.
 */
public class WikipediaInfoBoxModel2OldJune14_PERSONAL_CB {
    final public static boolean IS_CLEANING_QUERY = true;
    final public static int KEYWORD_IMPORTANCE_INDEX = 2;
    public static String statementsFileName = "statements2";
    public static String statementsDirectoryName = "openNLP\\";
    public static String dataDirectoryName = "openNLP\\";
    public static String mainFileName = "";
    public static String mainFileDir = "";
    //final public static String DELIMITER = "<92j8q9g93sajd9f8jqa9pf8j>";
    public static String DELIMITER = "[\\.\\?!]+";

    final public static int HOW_MANY_IN_THE_TOP_X = 20;
    //final public static String statementsFileName = "statements_july6.txt";
    //
    private static List<String> Parse(String str) {
        List<String> output = new ArrayList<String>();
        Matcher match = Pattern.compile("[0-9]+|[a-z]+|[A-Z]+").matcher(str);
        while (match.find()) {
            output.add(match.group());
        }
        return output;
    }
    public static void changeStatementsFileName(String directory,String filename, String openNLPDir)
    {
        statementsDirectoryName =openNLPDir;
        dataDirectoryName = openNLPDir;
        statementsFileName = directory+filename;
        mainFileDir=  directory;
        mainFileName = filename;

    }
    public static boolean statementsFileNameEquals(String other)
    {
        return statementsFileName.equals(mainFileDir+other);
        //System.out.println(statementsFileName);
        //System.out.println(statementsFileName.substring(statementsFileName.lastIndexOf("openNLP")+1+"openNLP".length()));
        //System.exit(-100);
        /*try {

        }catch (Exception e)
        {
            e.printStackTrace();

            return statementsFileName.substring(statementsFileName.lastIndexOf("openNLP")+1+"openNLP".length()).equals(other);
        }

        if(statementsFileName.indexOf(statementsDirectoryName)<0)
            return false;
        return statementsFileName.substring(statementsFileName.indexOf(statementsDirectoryName)).equals(other);
        */
    }
    public static ArrayList<String> list = new ArrayList<>();
    private static String removeLastChar(String str) {
        return str.substring(0,str.length()-1);
    }
    /**
     * Returns the number of appearances that a string have on another string.
     *
     * @param source    a string to use as source of the match
     * @param sentence  a string that is a substring of source
     * @return the number of occurrences of sentence on source
     */
    public static int numberOfOccurrences(String source, String sentence) {
        int occurrences = 0;

        if (source.contains(sentence)) {
            int withSentenceLength    = source.length();
            int withoutSentenceLength = source.replace(sentence, "").length();
            occurrences = (withSentenceLength - withoutSentenceLength) / sentence.length();
        }

        return occurrences;
    }
    public static general.graph.theory.Message chatbotTypeSept11(String question, String origionalQuestion) throws Exception {
        String newQuestion = new String();
        for(String part :Parse(question))
            newQuestion += part + " ";
        question = newQuestion.trim();
        closestMatchedQueries.clear();
        //System.exit(-100);
        list.clear();
        if (statementsFileNameEquals(MainGUI.web)
                || (statementsFileNameEquals(MainGUI.local
        ) && MainGUI.useNewData == true)) {
            PrintWriter writer = new PrintWriter(new File(WikipediaInfoBoxModel2OldJune14_PERSONAL_CB.statementsFileName));
            writer.print("");
            writer.close();
        }

        Map<String, String> replacements = new HashMap<String, String>() {{
        }};
        String input, regexp;
        StringBuffer sb;
        Matcher m;
        Pattern p;
        replacements = new HashMap<String, String>() {{
            put("are", "i am ");
            put("am", "you are ");
            put("i", "you ");

            put("your", "my ");
            put("my", "your ");

            put("me", "you ");
            put("yours", "mine ");

            put("mine", "yours ");

            put("you", "me ");
            put("yourself", "myself ");
            put("myself", "yourself ");
        }};

        input = question;
        String output = new String();
        for (String word : input.split("[^\\w]+")) {
            boolean matched = false;
            for (String entry : replacements.keySet()) {
                if (!matched)
                    if (word.toLowerCase().equals(entry.toLowerCase())) {
                        word = replacements.get(entry);
                        matched = true;

                    }
            }
            output += word + " ";
        }
        System.out.println("new string = " + output);
        question = output;


        PrintStream c = System.out;
        String query = question;//.toLowerCase().replace("known as","");
       // ExecutorService es = Executors.newCachedThreadPool();
        final String finalQuery = query;


        try {
            query = list.get(0);
        } catch (Exception k) {
            query = question;
        }
        System.out.println("query>>> " + query);

        PrintStream dummyStream = new PrintStream(new OutputStream() {
            public void write(int b) {
                //NO-OP
            }
        });
        ComparePhrases.keyWords.clear();
        ComparePhrases.keyWordsVerbOrAdjective.clear();

        String[] keyWordsArrayNouns = FindKeyWordsTest.getNouns(question).toLowerCase().split("[^\\w]+");
        String[] keyWordsArrayVerbOrAdjective = FindKeyWordsTest.getVerbOrAdjective(question).toLowerCase().split("[^\\w]+");
        ComparePhrases.keyWords = new HashSet<>(Arrays.asList(keyWordsArrayNouns));
        ComparePhrases.keyWordsVerbOrAdjective = new HashSet<>(Arrays.asList(keyWordsArrayVerbOrAdjective));
        //String allText = FileUtils.readFileToString(result, "utf-8").toLowerCase();
        System.setOut(MainGUI.originalStream);
        System.out.println("should be 0: " + (ComparePhrases.keyWords.size() - keyWordsArrayNouns.length
                + ComparePhrases.keyWordsVerbOrAdjective.size() - keyWordsArrayVerbOrAdjective.length));
        System.out.println("real numbers = " + ComparePhrases.keyWords.size() + ", " + ComparePhrases.keyWordsVerbOrAdjective.size());
        System.setOut(dummyStream);
        ComparePhrases.keyWordUniqueness.clear();
        /*for (String word : ComparePhrases.keyWords) {
            try {
                if (ComparePhrases.keyWordUniqueness.containsKey(LuceneSnowBallTest.getStem(word.toLowerCase())))
                    ComparePhrases.keyWordUniqueness.put(LuceneSnowBallTest
                                    .getStem(word.toLowerCase()),
                            ComparePhrases.keyWordUniqueness.get(
                                    LuceneSnowBallTest
                                            .getStem(word.toLowerCase()
                                            )) + numberOfOccurrences(allText, word.toLowerCase()));
                else
                    ComparePhrases.keyWordUniqueness.put(LuceneSnowBallTest.getStem(word.toLowerCase()), numberOfOccurrences(allText, word.toLowerCase()));
            } catch (java.lang.ArithmeticException g) {
                g.printStackTrace();
            }
        }
        */
        HashSet<String> newKeyWordsNouns = new HashSet<>();
        HashSet<String> newKeyWordsVerbsOrAdjectives = new HashSet<>();
        // NOuns
        HashSet<String> newKeyWordsFullNouns = new HashSet<>();
        HashSet<String> newKeyWordsFullVerbsOrAdjectives = new HashSet<>();
        for (String keyword : ComparePhrases.keyWords) {
            // find the root word
            String rootWord = LuceneSnowBallTest.getStem(keyword);
            // Check if root word of key word is an ACTUAL WORD
            if (rootWord.length() > 2)
                // add the root word
                newKeyWordsNouns.add(rootWord);
            else
                // no, add the key word
                newKeyWordsNouns.add(keyword);
        }
        // adjectives
        for (String keyword : ComparePhrases.keyWordsVerbOrAdjective) {
            // find the root word
            String rootWord = LuceneSnowBallTest.getStem(keyword);
            // Check if root word of key word is an ACTUAL WORD
            if (rootWord.length() > 2)
                // add the root word
                newKeyWordsVerbsOrAdjectives.add(rootWord);
            else
                // no, add the key word
                newKeyWordsVerbsOrAdjectives.add(keyword);
        }

        ComparePhrases.keyWords = newKeyWordsNouns;
        ComparePhrases.keyWordsVerbOrAdjective = newKeyWordsVerbsOrAdjectives;
        System.setOut(MainGUI.originalStream);
        System.out.println("Again should be 0: " + (ComparePhrases.keyWords.size() - keyWordsArrayNouns.length
                + ComparePhrases.keyWordsVerbOrAdjective.size() - keyWordsArrayVerbOrAdjective.length));
        System.out.println("Again real numbers = " + ComparePhrases.keyWords.size() + ", " + ComparePhrases.keyWordsVerbOrAdjective.size());
        System.setOut(dummyStream);
        ArrayList<HashSet<ParagraphInfo>> totalListOfTrees = new ArrayList<>();
        HashSet<ParagraphInfo> totalFinalfinalSetOfWords = new HashSet<>();
        TreeMap<Integer, HashSet<ParagraphInfo>> totalFinalfinalSetOfWordsTree = new TreeMap<>(Collections.reverseOrder());
        HashSet<String> match = new HashSet<String>();
        File[] listOfFiles = Paths.get(statementsFileName).getParent().toFile().listFiles();
        int index = 0;

        long origionalFreeMemory = Runtime.getRuntime().freeMemory();
        boolean shouldCollect = true;
        int prevValue = 0;
        newKeyWordsFullNouns = ComparePhrases.keyWords;
        newKeyWordsFullNouns.addAll(ComparePhrases.keyWordsVerbOrAdjective);
        for (File result : listOfFiles) {
            try {
                //newContentPane.progressBar.setValue((int) (100.0 * (((double) index++) / listOfFiles.length)));
                if (result.getName().toLowerCase().endsWith(".txt")
                        && !result.getName().toLowerCase().startsWith(".")
                        && !(Paths.get(statementsFileName).equals(result.toPath()))
                        ) {


                    int charsize = 0;


                    String biggestText = FileUtils.readFileToString(
                            result, "utf-8").toLowerCase();
                    //String[] sentences = biggestText.split("[\\.\\?!\\n]+");


                    HashSet<ParagraphInfo> sentsNouns = new HashSet<>();
                    HashSet<ParagraphInfo> sentsVerbsOrAdjFinal = new HashSet<>();


                    String publicationName = result.getName()
                            .substring(0, result.getName().lastIndexOf("."));

                    for (String y : biggestText.split(DELIMITER)) {
                        /*
                            */
                        HashSet<String> usedKeywords = new HashSet<>();
                        int keyWordCount = 0;
                        for (String keyWord : newKeyWordsFullNouns) {
                            System.out.println(y + "->" + keyWord);


                            if (y.toLowerCase().contains(keyWord.toLowerCase())) {

                                keyWordCount++;
                                usedKeywords.add(keyWord.toLowerCase());
                            }
                        }

                        ParagraphInfo info = new ParagraphInfo(y, publicationName);
                        if (keyWordCount > clamp(newKeyWordsFullNouns.size()-1,0))
                        {
                            System.setOut(MainGUI.originalStream);
                            totalFinalfinalSetOfWords.add(info);
                            System.out.println("298gj2f keyword count "+info.getText());
                            System.out.println("298gj2f size "+newKeyWordsFullNouns.size());
                            System.out.println("298gj2f"+info.getText());
                            System.setOut(dummyStream);
                        }
                        else
                        {
                            if(closestMatchedQueries.size()>0) {
                                if(keyWordCount > closestMatchedQueries.firstKey())
                                {
                                    HashSet<HashSet<String>> currentQueries = new HashSet<>();
                                    currentQueries.add(usedKeywords);
                                    closestMatchedQueries.put(keyWordCount, currentQueries);
                                }
                                else if (keyWordCount == closestMatchedQueries.firstKey()) {
                                    HashSet<HashSet<String>> currentQueries = closestMatchedQueries.get(keyWordCount);
                                    currentQueries.add(usedKeywords);
                                    closestMatchedQueries.put(keyWordCount, currentQueries);
                                }
                            }
                            else
                            {
                                HashSet<HashSet<String>> currentQueries = new HashSet<>();
                                currentQueries.add(usedKeywords);
                                closestMatchedQueries.put(keyWordCount, currentQueries);
                            }
                        }


                        /*if (y.length() < 400)
                            if (totalFinalfinalSetOfWordsTree.containsKey(keyWordCount)) {
                                HashSet<ParagraphInfo> dummy = totalFinalfinalSetOfWordsTree.get(keyWordCount);
                                dummy.add(info);
                                totalFinalfinalSetOfWordsTree.put(keyWordCount, dummy);

                            } else {
                                HashSet<ParagraphInfo> dummy = new HashSet<>();
                                dummy.add(info);
                                totalFinalfinalSetOfWordsTree.put(keyWordCount, dummy);

                            }
                            */

                    }

                    System.out.println(sentsNouns.size());

                    //totalFinalfinalSetOfWordsTree.addAll(sentsNouns);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }

        /*String test= "";
        for(ParagraphInfo para : totalFinalfinalSetOfWords)
         test+= para.getText()+"\n---------------------------------------\n";
        return test;
        */
        boolean isNew = false;
        //newContentPane.progressBar.setMinimum(0);
        //newContentPane.progressBar.setMaximum(100);
        //newContentPane.progressBar.setValue(3);A
        int bookNumber = 0;
        final int NUM_OF_CORES = 8;
        ArrayList<ParagraphInfo> ResultsToArrayList =
                new ArrayList<>(totalFinalfinalSetOfWords);
        int totalNests = ResultsToArrayList.size();
        int step = totalNests / NUM_OF_CORES;
        ArrayList<HashSet<ParagraphInfo>> newstedListInception = new ArrayList<>();
        for(int i =0;i<NUM_OF_CORES;i++)
        {
            newstedListInception.add( new HashSet<>(ResultsToArrayList.subList(step * i, step * (i+1))));
        }

        Thread[] threads = new Thread[NUM_OF_CORES];
        //().start();
        ExecutorService es = Executors.newCachedThreadPool();
        if (true//WikipediaInfoBoxModel2OldJune14_PERSONAL_CB.DELIMITER.equals("<92j8q9g93sajd9f8jqa9pf8j>")
                //&& totalFinalfinalSetOfWordsTree.firstEntry().getValue().size() < 10000
                )
            match.addAll(getchats(totalFinalfinalSetOfWords, query,dummyStream));


        es.shutdown();
        boolean finshed = es.awaitTermination(10, TimeUnit.MINUTES);

        if(finshed) {

        if(match.size()<1)
        {
            HashSet<String> possibleQueries = new HashSet<>();
            if(closestMatchedQueries.size()<1)
            {
                return new Message(Message.IMPOSSIBLE,new ArrayList<String>());
            }
            for(HashSet<String> keywordSeti : closestMatchedQueries.firstEntry().getValue())
            {
                String possibleQuery = new String();
                HashSet<String> missingKeyWords = new HashSet<>();
                for (String keywordFromData : newKeyWordsFullNouns) {
                    boolean contains = false;
                    for (String keyWordFromParsedQuery : keywordSeti) {

                        if (keyWordFromParsedQuery.toLowerCase().equals(keywordFromData.toLowerCase()))
                        {// this always gets excecuted for some reason
                            contains = true;
                        }

                    }
                    if(!contains) {
                        System.setOut(MainGUI.originalStream);
                        System.out.println("4ty34g: "+keywordFromData);
                        missingKeyWords.add(keywordFromData);
                    }
                }
                for(String word: query.split("[^\\w^\\d]+"))
                {
                    boolean isMissingThisWord = false;
                    for (String keyWordFromMissingKeyWords : missingKeyWords) {
                        try {
                            if (LuceneSnowBallTest.getStem(word.toLowerCase()).equals(keyWordFromMissingKeyWords.toLowerCase())) {
                                isMissingThisWord = true;
                            }
                        }
                        catch (Exception e) {
                            if (word.toLowerCase().equals(keyWordFromMissingKeyWords.toLowerCase())) {
                                isMissingThisWord = true;
                            }
                        }
                    }

                    if(isMissingThisWord)
                        possibleQuery+=" -- ";
                    else
                        possibleQuery += " " + word;

                }
                if(FindKeyWordsTest.getNouns(possibleQuery).replaceAll("\\s+","").length()>0
                        ||FindKeyWordsTest.getVerbOrAdjective(possibleQuery).replaceAll("\\s+","").length()>0)
                    possibleQueries.add(possibleQuery);
            }
            return new Message(Message.POSSIBLE,new ArrayList<String>(possibleQueries));
            /*
            String text ="";
            for(ParagraphInfo word : totalFinalfinalSetOfWords)
            {
                text += word.getText() + word.getPub() + "\n\n";
            }
            return text;
            */
        }
            //System.exit(0);
            // results.clear();
            //results.add("https://en.wikipedia.org/wiki/Economics");
            try {
                System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))");
                System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))");
                System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))");
                System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))");
                System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))");
                String speakThis = "";
                index = 0;
                String speakfull = new String();


                /* HashSet<String> pairs = match;
                System.setOut(MainGUI.originalStream);
                System.out.println("frea32g: " + match);
                System.setOut(dummyStream);
                HashSet<String> totalKeyWords = new HashSet<>(ComparePhrases.keyWords);
                totalKeyWords.addAll(new HashSet<String>(ComparePhrases.keyWordsVerbOrAdjective));
                ArrayList<String> sortedPairs = ComparePhrases.rankAnswers(query,pairs,totalKeyWords);
                for (String curPair : sortedPairs) {
                    speakfull += curPair + ". ";

                }
                */
                for (String curPair : match) {
                    speakfull += curPair + ". ";
                    /*if (curPair.toLowerCase().contains("kind of service firm whose characteristics have distinctive"
                            .toLowerCase())) {
                        System.setOut(MainGUI.originalStream);
                        System.setOut(dummyStream);
                        System.exit(-99);
                    }
                    */
                }

                speakfull = speakfull.trim();
                System.out.println("query --> " + query);

                if (match.size() < 1.0)
                    System.out.println("Answer --> " + " I couldn't find anything on that.");
                else
                    System.out.println("Answer --> " + speakfull);
                System.out.println("Overall size --> " + match.size());


                System.out.println(ComparePhrases.synMap);
                if (!statementsFileNameEquals(MainGUI.web) && !statementsFileNameEquals(MainGUI.local)) {
                    speakfull = ((String) match.toArray()
                            [new Random().nextInt(
                            match.size()
                    )]);
                }
                // TestChatBotMain.answersUsed.add(speakfull.toLowerCase());
                if (speakfull.isEmpty())
                    speakfull = "Nothing found";
                if (match.size() < 1.0 && match.size() < 1)
                    return new Message(Message.IMPOSSIBLE,new ArrayList<>());

                else
                    return new Message(Message.FOUND,new ArrayList<>(match));// +origionalQuestion.toLowerCase().matches(".*?how\\s.*?") +speakfull.replaceAll("\\[.*?\\]","").matches(".*?\\d+.*?");


            } catch (Exception l) {

                l.printStackTrace();
            }


            return new Message(Message.IMPOSSIBLE,new ArrayList<>());
            //return WikipediaInfoBoxModel2OldJune14_PERSONAL_CB.chatbot(redoQuestion,redoOriginalQuestion);

        }

        return new Message(Message.IMPOSSIBLE,new ArrayList<>());

    }
    public static TreeMap<Integer, HashSet<HashSet<String>>> closestMatchedQueries = new TreeMap<>(Collections.reverseOrder());
    public static HashSet<String> getchats(HashSet<ParagraphInfo> newstedList, String query, PrintStream dummyStream) throws IOException {
        HashSet<String> container = new HashSet<>();
        TreeMap<Double, HashSet<AnswerPair>> match = new TreeMap<>(Collections.reverseOrder());
        for (ParagraphInfo bigtextInfo : newstedList) {
            String bigtext = bigtextInfo.getText();
            container.add(bigtext + " - ( " + bigtextInfo.getPub() + " )" + "\n\n");

        }
        return container;
    }


    //in what decade was the first modern crossword puzzle published and oreo cookies are introduced
    public static String getNouns(String raw) throws IOException {
        ArrayList<String> rawParsed = FindKeyWordsTest.POSTag2(raw);
        ArrayList<String> noun = new ArrayList<>();
        ArrayList<String> adjective = new ArrayList<>();
        ArrayList<String> nnp = new ArrayList<>();
        ArrayList<String> properNoun = new ArrayList<>();
        ArrayList<String> adverb = new ArrayList<>();
        ArrayList<String> verb = new ArrayList<>();
        for(String word : rawParsed)
        {
            try {
                String tag = word.substring(word.indexOf('_')).toLowerCase();
                String partOfSpeech = word.substring(0, word.indexOf('_'));
                if (tag.contains("n") && !tag.contains("p") && !tag.contains("i")) {
                    // this is a regular noun
                    noun.add(partOfSpeech);
                    System.out.println("regular noun = " + partOfSpeech);
                } else if (tag.contains("n") && tag.contains("p")) {
                    // this is a proper noun WITH THE NAME ID e.g. Japan
                    nnp.add(partOfSpeech);
                    System.out.println("proper noun = " + partOfSpeech);
                } else if (!tag.contains("n") && tag.contains("prp")) {
                    // this is a proper noun WITHOUT ID e.g. I, She, He
                    properNoun.add(partOfSpeech);
                    System.out.println("proper noun = " + partOfSpeech);
                } else if (tag.contains("rb")) {
                    // This is an ADVERB
                    adverb.add(partOfSpeech);
                    System.out.println("adverb = " + partOfSpeech);
                } else if (tag.contains("jj")) {
                    // This is an Adjective
                    adjective.add(partOfSpeech);
                    System.out.println("adjective = " + partOfSpeech);
                } else if (tag.contains("vb")) {
                    // This is an VERB
                    verb.add(partOfSpeech);
                    System.out.println("verb = " + partOfSpeech);
                }
            }catch (Exception l)
            {
                l.printStackTrace();
            }
        }
        String result =  "";
        for(String x : noun)
            result += x + " ";
        result.trim();
        return result + "\nError!";
    }
    public static float clamp(int val, int min) {
        return Math.max(min, val);
    }
}

