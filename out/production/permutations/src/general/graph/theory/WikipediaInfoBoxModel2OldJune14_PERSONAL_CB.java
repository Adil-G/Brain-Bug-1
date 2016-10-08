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
        HashSet<String[]> newKeyWordsNouns = new HashSet<>();
        HashSet<String[]> newKeyWordsVerbsOrAdjectives = new HashSet<>();
        // NOuns
        HashSet<String[]> newKeyWordsFullNouns = new HashSet<>();
        HashSet<String[]> newKeyWordsFullVerbsOrAdjectives = new HashSet<>();
        for (String keyword : ComparePhrases.keyWords) {
            // find the root word
            String rootWord = LuceneSnowBallTest.getStem(keyword);
            // Check if root word of key word is an ACTUAL WORD
            if (rootWord.length() > 2 && !(rootWord.isEmpty())&&!(keyword.isEmpty())) {//
                // add the root word
                newKeyWordsNouns.add(new String[]{rootWord,keyword});
            }
            else if (!(keyword.isEmpty())){//
                // no, add the key word
                newKeyWordsNouns.add(new String[]{keyword,keyword});
            }
        }
        // adjectives
        for (String keyword : ComparePhrases.keyWordsVerbOrAdjective) {
            // find the root word
            String rootWord = LuceneSnowBallTest.getStem(keyword);
            // Check if root word of key word is an ACTUAL WORD
            if (rootWord.length() > 2&& !(rootWord.isEmpty())&&!(keyword.isEmpty())) {//
                // add the root word
                newKeyWordsVerbsOrAdjectives.add(new String[]{rootWord,keyword});
            }
            else if (!(keyword.isEmpty())){//
                // no, add the key word
                newKeyWordsVerbsOrAdjectives.add(new String[]{keyword,keyword});
            }
        }

        ArrayList<HashSet<ParagraphInfo>> totalListOfTrees = new ArrayList<>();
        TreeMap<Integer, HashSet<ParagraphInfo>> totalFinalfinalSetOfWordsTree = new TreeMap<>(Collections.reverseOrder());
        ArrayList<String> match = new ArrayList<String>();
        File[] listOfFiles = Paths.get(statementsFileName).getParent().toFile().listFiles();
        int index = 0;

        long origionalFreeMemory = Runtime.getRuntime().freeMemory();
        boolean shouldCollect = true;
        int prevValue = 0;
        newKeyWordsFullNouns = newKeyWordsNouns;
        newKeyWordsFullNouns.addAll(newKeyWordsVerbsOrAdjectives);
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
                    int parNum = 0;
                    for (String y : biggestText.split(DELIMITER)) {
                        /*
                            */
                        parNum++;
                        HashSet<String> usedKeywords = new HashSet<>();
                        int keyWordCount = 0;
                        int exactKeyWordCount = 0;
                        System.setOut(MainGUI.originalStream);

                        for (String[] keyWord : newKeyWordsFullNouns) {
                            //System.out.println(y + "->" + keyWord);

                            /*if(keyWord[0].isEmpty() || keyWord[1].isEmpty())
                                continue;
                                */
                            //keyWordCount++;
                            // Ask this:
                            //what is the meaning of life
                            /*String stringToSearch = y.toLowerCase();

                            Pattern p0 = Pattern.compile(".*?\\b"+keyWord[0].toLowerCase()+".*?");   // the pattern to search for
                            Matcher m0 = p0.matcher(stringToSearch);

                            Pattern p1 = Pattern.compile(".*?\\b"+keyWord[1].toLowerCase()+".*?");   // the pattern to search for
                            Matcher m1 = p1.matcher(stringToSearch);
                            */
                            // now try to find at least one match


                            if (//m0.find()||m1.find()
                                    y.toLowerCase().contains(keyWord[0].toLowerCase())
                                    ||
                                            y.toLowerCase().contains(keyWord[1].toLowerCase())
                                    )
                            {

                                keyWordCount++;
                                usedKeywords.add(keyWord[1]);
                                //System.out.println(parNum+": ");
                                /*for(String word : usedKeywords)
                                    System.out.println(word+", "+keyWordCount);
                                    */

                            }
                            if(y.toLowerCase().matches(".*?\\b"+keyWord[1].toLowerCase()+".*?"))
                            {
                                exactKeyWordCount += 1;
                                System.out.println("2f0a9kg0a9k: "+ keyWord[1].toLowerCase());
                            }
                            if(y.toLowerCase().matches(".*?\\b"+keyWord[1].toLowerCase()+"\\b.*?"))
                            {
                                exactKeyWordCount += 2;
                            }
                        }

                        //keyWordCount = 0;
                        System.setOut(dummyStream);
                        ParagraphInfo info = new ParagraphInfo(y, publicationName);
                        //newKeyWordsFullNouns.size()

                        if (usedKeywords.size() > clamp(newKeyWordsFullNouns.size()-1,0))
                        {
                            keyWordCount = keyWordCount + exactKeyWordCount;
                            System.setOut(MainGUI.originalStream);
                            if (totalFinalfinalSetOfWordsTree.containsKey(keyWordCount)) {
                                HashSet<ParagraphInfo> dummy = totalFinalfinalSetOfWordsTree.get(keyWordCount);
                                dummy.add(info);
                                totalFinalfinalSetOfWordsTree.put(keyWordCount, dummy);

                            } else {
                                HashSet<ParagraphInfo> dummy = new HashSet<>();
                                dummy.add(info);
                                totalFinalfinalSetOfWordsTree.put(keyWordCount, dummy);

                            }
                            //totalFinalfinalSetOfWords.add(info);
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
        /*
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
        */
        //ExecutorService es = Executors.newCachedThreadPool();



        //System.exit(100);

        match = getchats(totalFinalfinalSetOfWordsTree, query,dummyStream, newKeyWordsFullNouns);
        System.setOut(MainGUI.originalStream);

        //es.shutdown();
        //boolean finshed = es.awaitTermination(10, TimeUnit.MINUTES);

        if(true) {
            if (match.size() >=1) {
                System.out.println("4313gqa: "+totalFinalfinalSetOfWordsTree.size());

                System.out.println("4313gqa: "+match.size());
                return new Message(Message.FOUND, match);
            }
        else if(match.size()<1)
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
                for (String[] keywordFromData : newKeyWordsFullNouns) {
                    boolean contains = false;
                    for (String keyWordFromParsedQuery : keywordSeti) {
                        // does the word from the question match with
                        // the keyword used in the program
                        // get the keyword, not the root word
                        if (keyWordFromParsedQuery.toLowerCase().equals(keywordFromData[1].toLowerCase()))
                        {
                            contains = true;
                        }

                    }
                    if(!contains) {
                        System.setOut(MainGUI.originalStream);
                        System.out.println("4ty34g: "+keywordFromData);
                        missingKeyWords.add(keywordFromData[1]);
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
    public static ArrayList<String> getchats(TreeMap<Integer, HashSet<ParagraphInfo>> treeOfAnswers, String query, PrintStream dummyStream
    ,HashSet<String[]> newKeyWordsFullNouns) throws IOException {
        ArrayList<String> container = new ArrayList<>();
        int paragraphCount = 0;
        TreeMap<Double, HashSet<AnswerPair>> match = new TreeMap<>(Collections.reverseOrder());
        for ( int score : treeOfAnswers.keySet()) {
            ArrayList<String> miniContainer = new ArrayList<>();
            for(ParagraphInfo paragraph : treeOfAnswers.get(score)) {
                if(paragraphCount++ < 500)
                    miniContainer.add(paragraph.getText() + " - ( " + paragraph.getPub() + " )" + "\n\n");
            }
            container.addAll(ComparePhrases.rankAnswers(query,miniContainer,newKeyWordsFullNouns));

        }
        return container;
    }


    //in what decade was the first modern crossword puzzle published and oreo cookies are introduced

    public static float clamp(int val, int min) {
        return Math.max(min, val);
    }
}

