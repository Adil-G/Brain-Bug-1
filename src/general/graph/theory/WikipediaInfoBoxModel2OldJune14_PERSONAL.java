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

import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

        boolean isUserAskingAQustion = false;
        if (!(!question.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("how ")
                && !question.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("do ")
                && !question.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("does ")
                && !question.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("am ")
                && !question.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("i am")
                && !question.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("are ")
                && !question.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("can ")
                && !question.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("why ")
                && !question.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("who ")
                && !question.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("what ")
                && !question.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("if ")
                && !question.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("where ")
                && !question.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("would ")
                && !question.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("could ")
                && !question.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("will ")
                && !question.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("when ")
                && !question.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("is ")
                && !question.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("was ")
                && !question.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("which ")
                //&&!question.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]","").trim().startsWith("he ")
                //&&!question.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]","").trim().startsWith("she ")
                && !question.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("did ")
                && !question.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("does ")

                && !question.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("how's")
                && !question.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("do's")
                && !question.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("does's")
                && !question.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("can's")
                && !question.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("why's")
                && !question.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("who's")
                && !question.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("what's")
                && !question.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("won't")
                && !question.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("wouldn't")
                && !question.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("couldn't")
                && !question.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("where's")
                && !question.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("when's")
                && !question.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("is's")
                && !question.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("which's")
                && !question.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("he's")
                && !question.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("she's")
        )) {
            isUserAskingAQustion = true;
        }
        PrintStream c = System.out;
        String query = question;//.toLowerCase().replace("known as","");
        ExecutorService es = Executors.newCachedThreadPool();
        final String finalQuery = query;


        try {
            query = list.get(0);
        } catch (Exception k) {
            query = question;
        }
        System.out.println("query>>> " + query);
        //System.exit(-1);
        /*JFrame frame = new JFrame("ProgressBarDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        ProgressBarDemo newContentPane = new ProgressBarDemo();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
        newContentPane.progressBar.setMinimum(0);
        newContentPane.progressBar.setMaximum(100);
        newContentPane.progressBar.setValue(3);
        */
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
        for (File result : listOfFiles) {
            try {
                //newContentPane.progressBar.setValue((int) (100.0 * (((double) index++) / listOfFiles.length)));
                if (result.getName().toLowerCase().endsWith(".txt")
                        && !result.getName().toLowerCase().startsWith(".")
                        && !(Paths.get(statementsFileName).equals(result.toPath()))
                        ) {
                    /*if((newContentPane.progressBar.getValue()-prevValue)>5
                            && newContentPane.progressBar.getValue()>40)
                    {
                        shouldCollect = true;
                        prevValue = newContentPane.progressBar.getValue();
                    }
                    */
                    if(shouldCollect) {
                        double bytes = result.length();
                        double kilobytes = (bytes / 1024);
                        double megabytes = (kilobytes / 1024);
                        double gigabytes = (megabytes / 1024);
                        double terabytes = (gigabytes / 1024);
                        double petabytes = (terabytes / 1024);
                        double exabytes = (petabytes / 1024);
                        double zettabytes = (exabytes / 1024);
                        double yottabytes = (zettabytes / 1024);
                        System.setOut(MainGUI.originalStream);
                        System.out.println("you lost: "+(origionalFreeMemory - Runtime.getRuntime().freeMemory())
                                +", From: "+kilobytes);
                        System.setOut(dummyStream);
                        System.gc();
                        shouldCollect = false;

                    }
                        /*String allText = FileUtils.readFileToString(result, "utf-8").toLowerCase();
                    ComparePhrases.textSize = allText.split("[^\\w]+").length;
                    System.out.println(ComparePhrases.keyWordUniqueness);
                    ArrayList<String> wordArray = new ArrayList<>(
                            Arrays.asList(allText.split("[^\\w]+"))
                    );
                    ComparePhrases.mostcommon = ComparePhrases.mostCommonWordsList(wordArray, HOW_MANY_IN_THE_TOP_X);
                    ComparePhrases.synMap = testWordnet.getSynonymMap(question);//new HashMap<String,HashSet<String>>();//
                    ComparePhrases.synMap.clear();
                    for (String keyword : ComparePhrases.keyWords)
                        if (!ComparePhrases.mostcommon.matches(".*?\\b"+keyWord.toLowerCase()+".*?")) {
                            newKeyWordsFullNouns.add(keyword);
                            String rootWord = LuceneSnowBallTest.getStem(keyword);
                            if (rootWord.length() > 2) {
                                newKeyWordsFullNouns.add(keyword);
                            }
                        }


                    for (String keyword : ComparePhrases.keyWordsVerbOrAdjective)
                        if (!ComparePhrases.mostcommon.matches(".*?\\b"+keyWord.toLowerCase()+".*?")) {
                            newKeyWordsFullVerbsOrAdjectives.add(keyword);
                            String rootWord = LuceneSnowBallTest.getStem(keyword);
                            if (rootWord.length() > 2) {
                                newKeyWordsFullVerbsOrAdjectives.add(keyword);
                            }
                        }
                    */

                    int charsize = 0;
                    newKeyWordsFullNouns = ComparePhrases.keyWords;
                    newKeyWordsFullVerbsOrAdjectives = ComparePhrases.keyWordsVerbOrAdjective;

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
                                //System.exit(99);
                                /*if(y.toLowerCase().contains("kind of service firm whose characteristics have distinctive"
                                        .toLowerCase()))
                                    System.exit(99);
                                    */
                                ParagraphInfo info = new ParagraphInfo(y, publicationName);
                                sentsNouns.add(info);
                                keywordsUsedAsNouns.add(keyWord.toLowerCase());
                                /*System.setOut(MainGUI.originalStream);
                                System.out.println("noun: " + keyWord.toLowerCase());
                                System.setOut(dummyStream);
                                */
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
                                /*if(y.toLowerCase().contains("kind of service firm whose characteristics have distinctive"
                                        .toLowerCase())
                                        && "kind of service firm whose characteristics have distinctive".matches(".*?"+keyWord.toLowerCase()+".*?")
                                        &&y.toLowerCase().matches(".*?"+keyWord.toLowerCase()+".*?"))
                                {
                                    System.setOut(MainGUI.originalStream);
                                    System.out.println("3tag: "+y.toLowerCase());
                                    System.setOut(dummyStream);
                                    System.exit(99);
                                }
                                */
                                System.out.println(y + "->" + keyWord);
                                if (y.toLowerCase().contains(keyWord.toLowerCase())) {

                                    sentsVerbsOrAdjFinal.add(info);
                                    /*System.setOut(MainGUI.originalStream);
                                    System.out.println("verb: " + keyWord.toLowerCase());
                                    System.setOut(dummyStream);
                                    */
                                }
                            }
                        }
                    else
                        for (String y : biggestText.split(DELIMITER)) {
                            int count = 0;
                            for (String keyWord : newKeyWordsFullNouns) {
                                System.out.println(y + "->" + keyWord);
                                if (y.toLowerCase().matches(".*?\\b"+keyWord.toLowerCase()+".*?")) {
                                    count++;
                                }
                            }
                            if (count > 1) {
                                ParagraphInfo info = new ParagraphInfo(y, result.getName()
                                        .substring(0, result.getName().lastIndexOf(".")));
                                sentsVerbsOrAdjFinal.add(info);
                            }
                        }
                    int prevSize = sentsVerbsOrAdjFinal.size();

                    totalFinalfinalSetOfWords.addAll(sentsVerbsOrAdjFinal);
                }
            }catch(Exception exception)
            {
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
        //newContentPane.progressBar.setValue(3);
        int bookNumber = 0;
        ArrayList<TreeMap<Double, HashSet<AnswerPair>>> answerTree = new ArrayList<>();
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
                        for (String sentence : para.split("\\s+")) {
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
                            double comparex = ComparePhrases.compare2(query, sample);
                            compare += comparex;
                            if (comparex > 0)
                            {
                                System.setOut(MainGUI.originalStream);
                                System.out.println("23rt2t3w last res comparex = " + comparex);
                                System.out.println("23rt2t3w last res compare = " + compare);
                                System.out.println("23rt2t3w last res LENGTH = " + sample.split("\\s+").length);
                                System.setOut(dummyStream);
                            }
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


            HashSet<AnswerPair> pairs = match.firstEntry().getValue();
            System.setOut(MainGUI.originalStream);
            System.out.println("frea32g: "+match.firstEntry().getKey());
            System.setOut(dummyStream);
            for (AnswerPair curPair : pairs) {
                speakfull += curPair.getText() + ". ";
                if(curPair.getText().toLowerCase().contains("kind of service firm whose characteristics have distinctive"
                        .toLowerCase())) {
                    System.setOut(MainGUI.originalStream);
                    System.setOut(dummyStream);
                    System.exit(-99);
                }
            }

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
        return WikipediaInfoBoxModel2OldJune14_PERSONAL_CB.chatbot(redoQuestion,redoOriginalQuestion);

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


}

