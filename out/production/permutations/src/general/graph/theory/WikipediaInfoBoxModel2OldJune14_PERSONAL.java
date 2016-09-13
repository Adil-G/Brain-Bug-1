package general.graph.theory;

import SentenceGenerator.ComparePhrases;
import SentenceGenerator.GoogleCSAPI;
import general.FindKeyWordsTest;
import general.LuceneSnowBallTest;
import general.chat.MainGUI;
import general.chat.ProgressBarDemo;
import general.comparePhrasesold_june7;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.wordnet.SynonymMap;
import org.omg.CORBA.AnySeqHelper;

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
import java.util.concurrent.atomic.DoubleAccumulator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by corpi on 2016-04-30.
 */
public class WikipediaInfoBoxModel2OldJune14_PERSONAL {
    final public static boolean IS_CLEANING_QUERY = true;
    final public static int KEYWORD_IMPORTANCE_INDEX = 2;
    public static String statementsFileName = "statements2";
    public static String statementsDirectoryName = "openNLP\\";
    public static String dataDirectoryName = "openNLP\\";
    public static String mainFileName = "";
    public static String mainFileDir = "";
    public static String DELIMITER = "[\\.\\?!]+";
    final public static int HOW_MANY_IN_THE_TOP_X = 20;
    //final public static String statementsFileName = "statements_july6.txt";
    //
    public static void changeStatementsFileName(String directory,String filename, String openNLPDir)
    {
        statementsDirectoryName =openNLPDir;
        dataDirectoryName = openNLPDir;
        statementsFileName = directory+filename;
        mainFileDir=  directory;
        mainFileName = filename;

        WikipediaInfoBoxModel2OldJune14_PERSONAL_CB.statementsDirectoryName =openNLPDir;
        WikipediaInfoBoxModel2OldJune14_PERSONAL_CB.dataDirectoryName = openNLPDir;
        WikipediaInfoBoxModel2OldJune14_PERSONAL_CB.statementsFileName = directory+filename;
        WikipediaInfoBoxModel2OldJune14_PERSONAL_CB.mainFileDir=  directory;
        WikipediaInfoBoxModel2OldJune14_PERSONAL_CB.mainFileName = filename;

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
    public static String chatbot(String question, String origionalQuestion) throws Exception {
        if(false)//if(!WikipediaInfoBoxModel2OldJune14_PERSONAL_CB.DELIMITER.equals("<92j8q9g93sajd9f8jqa9pf8j>"))
        {
            return "Either you are not giving me a lot to go on, or I don't have any info on this. Perhaps you can provide me with more detail?\n\n"
                    + WikipediaInfoBoxModel2OldJune14_PERSONAL_CB.chatbotTypeSept11(question,origionalQuestion);
        }
        String redoQuestion = question;
        String redoOriginalQuestion = origionalQuestion;
        //System.exit(-100);
        list.clear();
        if (statementsFileNameEquals(MainGUI.web)
                || (statementsFileNameEquals(MainGUI.local
        ) && MainGUI.useNewData == true)) {
            PrintWriter writer = new PrintWriter(new File(WikipediaInfoBoxModel2OldJune14_PERSONAL.statementsFileName));
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
        System.setOut(MainGUI.originalStream);
        String[] keyWordsArrayNouns = FindKeyWordsTest.getNouns(question).toLowerCase().split("[^\\w]+");
        String[] keyWordsArrayVerbOrAdjective = FindKeyWordsTest.getVerbOrAdjective(question).toLowerCase().split("[^\\w]+");
        System.setOut(dummyStream);
        ComparePhrases.keyWords = new HashSet<>(Arrays.asList(keyWordsArrayNouns));
        ComparePhrases.keyWordsVerbOrAdjective = new HashSet<>(Arrays.asList(keyWordsArrayVerbOrAdjective));
        //String allText = FileUtils.readFileToString(result, "utf-8").toLowerCase();
        System.setOut(MainGUI.originalStream);
        System.out.println("should be 0: " +(ComparePhrases.keyWords.size() - keyWordsArrayNouns.length
        +ComparePhrases.keyWordsVerbOrAdjective.size() - keyWordsArrayVerbOrAdjective.length));
        System.out.println("real numbers = "+ComparePhrases.keyWords.size()+", "+ComparePhrases.keyWordsVerbOrAdjective.size());
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
        for (String keyword : ComparePhrases.keyWords)
        {
            // find the root word
            String rootWord = LuceneSnowBallTest.getStem(keyword);
            // Check if root word of key word is an ACTUAL WORD
            if (rootWord.length()>2)
                // add the root word
                newKeyWordsNouns.add(rootWord);
            else
                // no, add the key word
                newKeyWordsNouns.add(keyword);
        }
        // adjectives
        for (String keyword : ComparePhrases.keyWordsVerbOrAdjective)
        {
            // find the root word
            String rootWord = LuceneSnowBallTest.getStem(keyword);
            // Check if root word of key word is an ACTUAL WORD
            if (rootWord.length()>2)
                // add the root word
                newKeyWordsVerbsOrAdjectives.add(rootWord);
            else
                // no, add the key word
                newKeyWordsVerbsOrAdjectives.add(keyword);
        }

        ComparePhrases.keyWords = newKeyWordsNouns;
        ComparePhrases.keyWordsVerbOrAdjective = newKeyWordsVerbsOrAdjectives;
        System.setOut(MainGUI.originalStream);
        System.out.println("Again should be 0: " +(ComparePhrases.keyWords.size() - keyWordsArrayNouns.length
                +ComparePhrases.keyWordsVerbOrAdjective.size() - keyWordsArrayVerbOrAdjective.length));
        System.out.println("Again real numbers = "+ComparePhrases.keyWords.size()+", "+ComparePhrases.keyWordsVerbOrAdjective.size());
        System.setOut(dummyStream);
        ArrayList<HashSet<ParagraphInfo>> totalListOfTrees = new ArrayList<>();
        HashSet<ParagraphInfo> totalFinalfinalSetOfWords = new HashSet<>();
        TreeMap<Double, HashSet<AnswerPair>> match = new TreeMap<>(Collections.reverseOrder());
        File[] listOfFiles = Paths.get(statementsFileName).getParent().toFile().listFiles();
        int index = 0;

        long origionalFreeMemory = Runtime.getRuntime().freeMemory();
        boolean shouldCollect  =true;
        int prevValue = 0;
        final int NUM_OF_CORES2 = 8;
        ArrayList<File> ResultsToArrayList2 =
                new ArrayList<>(Arrays.asList(listOfFiles));
        int totalNests2 = ResultsToArrayList2.size();
        int step2 = totalNests2 / NUM_OF_CORES2;
        ArrayList<HashSet<File>> newstedListInception2 = new ArrayList<>();
        for(int i =0;i<NUM_OF_CORES2;i++)
        {
            newstedListInception2.add( new HashSet<>(ResultsToArrayList2.subList(step2 * i, step2 * (i+1))));
        }
        ExecutorService es2 = Executors.newCachedThreadPool();
        Thread[] threads2 = new Thread[NUM_OF_CORES2];


        for (int i =0;i<NUM_OF_CORES2;i++) {
            HashSet<File> newstedList  = newstedListInception2.get(i);
            String finalQuery1 = query;


            threads2[i] = new Thread() {
                public void run() {
                    totalFinalfinalSetOfWords.addAll(asfd(newstedList,dummyStream));
                }
            };
            es2.execute(threads2[i]);
        }
        es2.shutdown();
        boolean finshed2 = es2.awaitTermination(10, TimeUnit.MINUTES);

        if(finshed2) {


        /*String test= "";
        for(ParagraphInfo para : totalFinalfinalSetOfWords)
         test+= para.getText()+"\n---------------------------------------\n";
        return test;
        */
            boolean isNew = false;
            //newContentPane.progressBar.setMinimum(0);
            //newContentPane.progressBar.setMaximum(100);
            //newContentPane.progressBar.setValue(3);
            int bookNumber = 0;
            final int NUM_OF_CORES = 8;
            ArrayList<ParagraphInfo> ResultsToArrayList =
                    new ArrayList<>(totalFinalfinalSetOfWords);
            int totalNests = ResultsToArrayList.size();
            int step = totalNests / NUM_OF_CORES;
            ArrayList<HashSet<ParagraphInfo>> newstedListInception = new ArrayList<>();
            for (int i = 0; i < NUM_OF_CORES; i++) {
                newstedListInception.add(new HashSet<>(ResultsToArrayList.subList(step * i, step * (i + 1))));
            }

            Thread[] threads = new Thread[NUM_OF_CORES];
            //().start();
            ExecutorService es = Executors.newCachedThreadPool();
            for (int i = 0; i < NUM_OF_CORES; i++) {
                HashSet<ParagraphInfo> newstedList = newstedListInception.get(i);
                String finalQuery1 = query;


                threads[i] = new Thread() {
                    public void run() {
                        try {
                            match.putAll(getChat(newstedList, finalQuery1, dummyStream));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                es.execute(threads[i]);
            }
            es.shutdown();
            boolean finshed = es.awaitTermination(10, TimeUnit.MINUTES);


            //System.exit(0);
            // results.clear();
            //results.add("https://en.wikipedia.org/wiki/Economics");
            if(finshed)
            try {
                System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))");
                System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))");
                System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))");
                System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))");
                System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))");
                String speakThis = "";
                index = 0;
                String speakfull = new String();


                HashSet<AnswerPair> pairs = new HashSet<>();
                int count = 0;
                for(Map.Entry<Double,HashSet<AnswerPair>> ans : match.entrySet())
                    if(ans.getKey() > 0.1)
                        for(AnswerPair text : ans.getValue())
                            speakfull += "Answer ("+ans.getKey()+"): "+text.getText() + "\n\n";
                /*System.setOut(MainGUI.originalStream);
                System.out.println("frea32g: " + match.firstEntry().getKey());
                System.setOut(dummyStream);
                for (AnswerPair curPair : pairs) {
                    speakfull += curPair.getText() + ". ";
                    if (curPair.getText().toLowerCase().contains("kind of service firm whose characteristics have distinctive"
                            .toLowerCase())) {
                        System.setOut(MainGUI.originalStream);
                        System.setOut(dummyStream);
                        System.exit(-99);
                    }
                }*/

                speakfull = speakfull.trim();
                System.out.println("query --> " + query);

                if (match.firstEntry().getKey() < 1.0)
                    System.out.println("Answer --> " + " I couldn't find anything on that.");
                else
                    System.out.println("Answer --> " + speakfull);
                System.out.println("Overall score --> " + match.firstEntry().getKey());


                System.out.println(ComparePhrases.synMap);
                if (!statementsFileNameEquals(MainGUI.web) && !statementsFileNameEquals(MainGUI.local)) {
                    speakfull = ((AnswerPair) match.firstEntry().getValue().toArray()
                            [new Random().nextInt(
                            match.firstEntry().getValue().size()
                    )]).getText();
                }
                // TestChatBotMain.answersUsed.add(speakfull.toLowerCase());
                if (speakfull.isEmpty())
                    speakfull = "Nothing found";
                if (match.firstEntry().getKey() < 1.0 && match.firstEntry().getValue().size() < 1)
                    return " I couldn't find anything on that.";

                else
                    return speakfull;// +origionalQuestion.toLowerCase().matches(".*?how\\s.*?") +speakfull.replaceAll("\\[.*?\\]","").matches(".*?\\d+.*?");


            } catch (Exception l) {


            }



            //return "Either you are not giving me a lot to go on, or I don't have any info on this. Perhaps you can provide me with more detail?";
            return "Either you are not giving me a lot to go on, or I don't have any info on this. Perhaps you can provide me with more detail?\n\n"
                    + WikipediaInfoBoxModel2OldJune14_PERSONAL_CB.chatbotTypeSept11(redoQuestion, redoOriginalQuestion);
        }
        return "wrong";
    }

    public static TreeMap<Double, HashSet<AnswerPair>> getChat(HashSet<ParagraphInfo> totalFinalfinalSetOfWords,String query, PrintStream dummyStream) throws IOException {
        TreeMap<Double, HashSet<AnswerPair>> match = new TreeMap<>(Collections.reverseOrder());

        for (ParagraphInfo bigtextInfo : totalFinalfinalSetOfWords) {
            String bigtext = bigtextInfo.getText();
            //newContentPane.progressBar.setValue((int) (100.0 * (((double) bookNumber++) / (double) totalFinalfinalSetOfWords.size())));
            for (String paraBig : bigtext.split(DELIMITER))
                for (String para : paraBig.split("[\\.\\?!]+")) {
                    String resultMeaningless = Integer.toString(1);


                    double compare = 0;
                    ArrayList<String> total = new ArrayList<>();
                    int targetWordCount = 10;
                    int currentWordCount = 0;
                    String targetString = new String();
                    for (String sentence : para.split("[\\s\\n]+")) {
                        targetString += sentence + " ";
                        if (currentWordCount++ > targetWordCount || sentence.contains(".")
                                || sentence.contains("?")
                                || sentence.contains("!")) {
                            total.add(targetString);
                            targetString = new String();
                            currentWordCount = 0;
                        }
                    }
                    for (String sample : total) {
                        System.setOut(MainGUI.originalStream);
                        System.out.println("23rt2t3w last res comparex = " + sample.length());
                        System.out.println("23rt2t3w last res compare = " + compare);
                        System.out.println("23rt2t3w last res LENGTH = " + totalFinalfinalSetOfWords.size());
                        System.setOut(dummyStream);
                        double comparex = ComparePhrases.compare2(query, sample);
                        compare += comparex;

                    }


                    if (match.containsKey(compare)) {
                        HashSet<AnswerPair> dummy = match.get(compare);
                        dummy.add(new AnswerPair(resultMeaningless, paraBig + " - ( " + bigtextInfo.getPub() + " )" + "\n\n"));
                        match.put(compare, dummy);

                    } else {
                        HashSet<AnswerPair> dummy = new HashSet<>();
                        dummy.add(new AnswerPair(resultMeaningless, paraBig + " - ( " + bigtextInfo.getPub() + " )" + "\n\n"));
                        match.put(compare, dummy);

                    }


                }

        }
        return match;
    }
    public static String start(String input,String origionalQuestion, boolean isQuestioning) throws Exception {

        return chatbot(input,origionalQuestion);
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
            {}
        }
        String result =  "";
        for(String x : noun)
            result += x + " ";
        result.trim();
        return result;
    }

    public static HashSet<ParagraphInfo> asfd( HashSet<File> listOfFiles, PrintStream dummyStream)
    {
        HashSet<ParagraphInfo> totalFinalfinalSetOfWords =  new HashSet<>();
        for (File result : listOfFiles) {
            try {
                //newContentPane.progressBar.setValue((int) (100.0 * (((double) index++) / listOfFiles.length)));
                if (result.getName().toLowerCase().endsWith(".txt")
                        && !result.getName().toLowerCase().startsWith(".")
                        && !(Paths.get(statementsFileName).equals(result.toPath()))
                        ) {


                    int charsize = 0;
                    HashSet<String> newKeyWordsFullNouns = ComparePhrases.keyWords;
                    HashSet<String> newKeyWordsFullVerbsOrAdjectives = ComparePhrases.keyWordsVerbOrAdjective;

                    String biggestText=FileUtils.readFileToString(
                            result, "utf-8").toLowerCase();
                    ;//(|mr|ms|mrs)
                    biggestText = biggestText.toString().toLowerCase().replaceAll("dr\\.", "dr");
                    biggestText = biggestText.toString().toLowerCase().replaceAll("mr\\.", "mr");
                    biggestText = biggestText.toString().toLowerCase().replaceAll("ms\\.", "ms");
                    biggestText = biggestText.toString().toLowerCase().replaceAll("mrs\\.", "mrs");
                    //String[] sentences = biggestText.split("[\\.\\?!\\n]+");


                    HashSet<ParagraphInfo> sentsNouns = new HashSet<>();
                    HashSet<ParagraphInfo> sentsVerbsOrAdjFinal = new HashSet<>();
                    HashSet<String> keywordsUsedAsNouns = new HashSet<>();


                    String publicationName = result.getName()
                            .substring(0, result.getName().lastIndexOf("."));

                    for (String y : biggestText.split(DELIMITER)) {
                        /*
                            */

                        for (String keyWord : newKeyWordsFullNouns) {
                            System.out.println(y + "->" + keyWord);


                            if (y.toLowerCase().contains(keyWord.toLowerCase())) {

                                ParagraphInfo info = new ParagraphInfo(y, publicationName);
                                sentsNouns.add(info);
                                keywordsUsedAsNouns.add(keyWord.toLowerCase());

                            }
                        }
                    }

                    System.out.println(sentsNouns.size());
                    newKeyWordsFullVerbsOrAdjectives.remove("");
                    System.setOut(MainGUI.originalStream);
                    System.out.println("2tag: "+newKeyWordsFullVerbsOrAdjectives);
                    System.setOut(dummyStream);
                    if (newKeyWordsFullVerbsOrAdjectives.size() > 0)
                        for (ParagraphInfo info : sentsNouns) {//what length should my line be
                            String y = info.getText();

                            for (String keyWord : newKeyWordsFullVerbsOrAdjectives) {

                                System.out.println(y + "->" + keyWord);
                                if (y.toLowerCase().contains(keyWord.toLowerCase())) {

                                    sentsVerbsOrAdjFinal.add(info);

                                }
                            }
                        }
                    totalFinalfinalSetOfWords.addAll(sentsVerbsOrAdjFinal);
                }
            }catch(Exception exception)
            {
                exception.printStackTrace();
            }

        }
        return totalFinalfinalSetOfWords;
    }
}

