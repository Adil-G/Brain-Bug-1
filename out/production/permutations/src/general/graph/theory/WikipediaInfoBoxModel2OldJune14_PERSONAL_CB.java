package general.graph.theory;

import SentenceGenerator.ComparePhrases;
import SentenceGenerator.GoogleCSAPI;
import general.FindKeyWordsTest;
import general.LuceneSnowBallTest;
import general.chat.MainGUI;
import general.chat.ProgressBarDemo;
import general.chat.UrlFileConnector;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.wordnet.SynonymMap;
import sun.applet.Main;

import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * Created by corpi on 2016-04-30.
 */
public class WikipediaInfoBoxModel2OldJune14_PERSONAL_CB {
    final public  boolean IS_CLEANING_QUERY = true;
    final public  int KEYWORD_IMPORTANCE_INDEX = 2;
    public  String statementsFileName = "statements2";
    public  String statementsDirectoryName = "openNLP\\";
    public  String dataDirectoryName = "openNLP\\";
    public  String mainFileName = "";
    public  String mainFileDir = "";
    public  PrintStream dummyStream = MainGUI.originalStream;
    //final public static String DELIMITER = "<92j8q9g93sajd9f8jqa9pf8j>";
    public  String DELIMITER = "[\\.\\?!]+";

    final public static int HOW_MANY_IN_THE_TOP_X = 20;
    //final public static String statementsFileName = "statements_july6.txt";
    //
    private List<String> Parse(String str) {
        List<String> output = new ArrayList<String>();
        Matcher match = Pattern.compile("[0-9]+|[a-z]+|[A-Z]+").matcher(str);
        while (match.find()) {
            output.add(match.group());
        }
        return output;
    }
    public  void changeStatementsFileName(String directory,String filename, String openNLPDir)
    {
        statementsDirectoryName =openNLPDir;
        dataDirectoryName = openNLPDir;
        statementsFileName = directory+filename;
        mainFileDir=  directory;
        mainFileName = filename;

    }
    public  boolean statementsFileNameEquals(String other)
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
    public  ArrayList<String> list = new ArrayList<>();
    public general.graph.theory.Message chatbotTypeSept12(HashSet<String> usedURLs,String contents, String question, String origionalQuestion, boolean isDeep, ArrayList<UrlFileConnector> ufc) throws Exception {

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
            PrintWriter writer = new PrintWriter(new File(statementsFileName));
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


        dummyStream = MainGUI.originalStream;
        comparePhrases.keyWords.clear();
        comparePhrases.keyWordsVerbOrAdjective.clear();

        String[] keyWordsArrayNouns = new FindKeyWordsTest().getNouns(question).toLowerCase().split("[^\\w]+");
        String[] keyWordsArrayVerbOrAdjective = new FindKeyWordsTest().getVerbOrAdjective(question).toLowerCase().split("[^\\w]+");
        comparePhrases.keyWords = new HashSet<>(Arrays.asList(keyWordsArrayNouns));
        comparePhrases.keyWordsVerbOrAdjective = new HashSet<>(Arrays.asList(keyWordsArrayVerbOrAdjective));
        //String allText = FileUtils.readFileToString(result, "utf-8").toLowerCase();
        comparePhrases.keyWordUniqueness.clear();
        /*for (String word : comparePhrases.keyWords) {
            try {
                if (comparePhrases.keyWordUniqueness.containsKey(LuceneSnowBallTest.getStem(word.toLowerCase())))
                    comparePhrases.keyWordUniqueness.put(LuceneSnowBallTest
                                    .getStem(word.toLowerCase()),
                            comparePhrases.keyWordUniqueness.get(
                                    LuceneSnowBallTest
                                            .getStem(word.toLowerCase()
                                            )) + numberOfOccurrences(allText, word.toLowerCase()));
                else
                    comparePhrases.keyWordUniqueness.put(LuceneSnowBallTest.getStem(word.toLowerCase()), numberOfOccurrences(allText, word.toLowerCase()));
            } catch (java.lang.ArithmeticException g) {
                g.printStackTrace();
            }
        }
        */
        HashSet<KeyWordPattern> newKeyWordsNouns = new HashSet<KeyWordPattern>();
        HashSet<KeyWordPattern> newKeyWordsVerbsOrAdjectives = new HashSet<KeyWordPattern>();
        // NOuns
        HashSet<KeyWordPattern> newKeyWordsFullNouns = new HashSet<KeyWordPattern>();
        HashSet<KeyWordPattern> newKeyWordsFullVerbsOrAdjectives = new HashSet<KeyWordPattern>();
        for (String keyword : comparePhrases.keyWords) {
            // find the root word
            String rootWord = LuceneSnowBallTest.getStem(keyword);
            // Check if root word of key word is an ACTUAL WORD
            if (rootWord.length() > 2 && !(rootWord.isEmpty())&&!(keyword.isEmpty())) {//
                // add the root word
                newKeyWordsNouns.add(new KeyWordPattern(new String[]{rootWord,keyword}));
            }
            else if (!(keyword.isEmpty())){//
                // no, add the key word
                newKeyWordsNouns.add(new KeyWordPattern(new String[]{keyword,keyword}));
            }
        }
        // adjectives
        for (String keyword : comparePhrases.keyWordsVerbOrAdjective) {
            // find the root word
            String rootWord = LuceneSnowBallTest.getStem(keyword);
            // Check if root word of key word is an ACTUAL WORD
            if (rootWord.length() > 2&& !(rootWord.isEmpty())&&!(keyword.isEmpty())) {//
                // add the root word
                newKeyWordsVerbsOrAdjectives.add(new KeyWordPattern(new String[]{rootWord,keyword}));
            }
            else if (!(keyword.isEmpty())){//
                // no, add the key word
                newKeyWordsVerbsOrAdjectives.add(new KeyWordPattern(new String[]{keyword,keyword}));
            }
        }

        ArrayList<HashSet<ParagraphInfo>> totalListOfTrees = new ArrayList<>();
        TreeMap<Integer, HashSet<ParagraphInfo>> totalFinalfinalSetOfWordsTree = new TreeMap<>(Collections.reverseOrder());
        ArrayList<ParagraphInfo> match = new ArrayList<ParagraphInfo>();
//        File[] listOfFiles = Paths.get(statementsFileName).getParent().toFile().listFiles();
        int index = 0;

        long origionalFreeMemory = Runtime.getRuntime().freeMemory();
        boolean shouldCollect = true;
        int prevValue = 0;
        newKeyWordsFullNouns = newKeyWordsNouns;
        newKeyWordsFullNouns.addAll(newKeyWordsVerbsOrAdjectives);
        HashMap<KeyWordPattern, HashSet<KeyWordPattern>> keyword2SynonymMap = new general.FindKeyWordsTest().getSynonyms(newKeyWordsFullNouns,isDeep);




        /*int bookNumber = 0;

        ArrayList<ParagraphInfo> ResultsToArrayList =
                new ArrayList<>(totalFinalfinalSetOfWords);
        int totalNests = ResultsToArrayList.size();
        int step = totalNests / NUM_OF_CORES;
        ArrayList<HashSet<ParagraphInfo>> newstedListInception = new ArrayList<>();
        for(int i =0;i<NUM_OF_CORES;i++)
        {
            newstedListInception.add( new HashSet<>(ResultsToArrayList.subList(step * i, step * (i+1))));
        }*/
        int NUM_OF_CORES = 16;
        ArrayList<UrlFileConnector> chunks = ufc;
        NUM_OF_CORES = chunks.size();
        //File[] someFile = chunks.get(0);
        Thread[] threads = new Thread[NUM_OF_CORES];
        Scan scan = new Scan();
        HashSet<KeyWordPattern> finalNewKeyWordsFullNouns = newKeyWordsFullNouns;
        for (int i = 0; i < NUM_OF_CORES; i++) {
            int finalI = i;
            threads[i] = new Thread() {
                public void run() {
                    // do stuff
                    UrlFileConnector[] someFiles = new UrlFileConnector[]{chunks.get(finalI)};
                    scan.scanit(closestMatchedQueries,usedURLs,contents, totalFinalfinalSetOfWordsTree
                            ,someFiles, keyword2SynonymMap
                            , finalNewKeyWordsFullNouns);
                }
            };
        }
        System.setOut(MainGUI.originalStream);

        ExecutorService service = Executors.newCachedThreadPool();

        for (int i = 0; i < NUM_OF_CORES; i++) {
            service.execute(threads[i]);
        }

        // wait for termination
        service.shutdown();
        boolean finshed = service.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

        System.out.println("330981fj09s");
        //System.exit(100);

        //es.shutdown();
        // = es.awaitTermination(Integer.MAX_VALUE, TimeUnit.MINUTES);





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
            /*
            HashSet<String> possibleQueries = new HashSet<>();
            if(closestMatchedQueries.size()<1)
            {
                return new Message(Message.IMPOSSIBLE,new ArrayList<String>());
            }
            for(Map.Entry<Integer,HashSet<HashSet<String>>> matchedQueriesClosest: closestMatchedQueries.entrySet())
            for(HashSet<String> keywordSeti : matchedQueriesClosest.getValue())
            {
                String possibleQuery = new String();
                HashSet<String> missingKeyWords = new HashSet<>();
                for (KeyWordPattern keywordFromData : newKeyWordsFullNouns) {
                    boolean contains = false;
                    for (String keyWordFromParsedQuery : keywordSeti) {
                        // does the word from the question match with
                        // the keyword used in the program
                        // get the keyword, not the root word
                        if (keyWordFromParsedQuery.toLowerCase().equals(keywordFromData.getKeyWords()[1].toLowerCase()))
                        {
                            contains = true;
                        }

                    }
                    if(!contains) {
                        System.setOut(MainGUI.originalStream);
                        System.out.println("4ty34g: "+keywordFromData);
                        missingKeyWords.add(keywordFromData.getKeyWords()[1]);
                    }
                }
                int numberOfMissedWords = 0;
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

                    if(isMissingThisWord) {
                        possibleQuery += " -- ";
                        numberOfMissedWords++;
                    }
                    else
                        possibleQuery += " " + word;

                }
                if((FindKeyWordsTest.getNouns(possibleQuery).replaceAll("\\s+","").length()>0
                        ||FindKeyWordsTest.getVerbOrAdjective(possibleQuery).replaceAll("\\s+","").length()>0)
                        && numberOfMissedWords >0)
                    possibleQueries.add(possibleQuery);
            }
            */
                ArrayList<ParagraphInfo> answers = new ArrayList<>();
                int kwCount = 0;
                TreeMap<Integer, HashSet<HashSet<String>>> closestMatchedQueriesCopy = new TreeMap<>(closestMatchedQueries);
                int keyCount = 0;
                for(Map.Entry<Integer, HashSet<HashSet<String>>> map :closestMatchedQueriesCopy.entrySet())
                {

                    if(keyCount++<2)
                        for(HashSet<String> kwElement :map.getValue()) {
                            String message = "";
                            for (String kw : kwElement) {
                                message += " + " + kw;
                            }
                            ++kwCount;
                            if(!message.isEmpty()) {
                                /*Message answerX = WikipediaInfoBoxModel2OldJune14_PERSONAL_CB.chatbotTypeSept11(message, message, isDeep, ufc);
                                for (ParagraphInfo ans : answerX.getAnswers())
                                    answers.add(ans);*/
                            }
                            //finalsAns += "\n\nSuggestion #" + kwCount + ".\n" + message;
                        }

                }
                //answers.add(new ParagraphInfo(finalsAns,"suggestion","1"));
                return new Message(Message.FOUND,answers);

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
    private ComparePhrases comparePhrases = new ComparePhrases();
    public general.graph.theory.Message chatbotTypeSept11(HashSet<String> usedURLs,String contents, String question, String origionalQuestion, boolean isDeep, ArrayList<UrlFileConnector> ufc) throws Exception {
        System.setOut(MainGUI.originalStream);
        System.out.println("f3094wfja09wj30w3fj0wjga0");
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
            PrintWriter writer = new PrintWriter(new File(this.statementsFileName));
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


        dummyStream = MainGUI.originalStream;
        comparePhrases.keyWords.clear();
        comparePhrases.keyWordsVerbOrAdjective.clear();

        String[] keyWordsArrayNouns = new FindKeyWordsTest().getNouns(question).toLowerCase().split("[^\\w]+");
        String[] keyWordsArrayVerbOrAdjective = new FindKeyWordsTest().getVerbOrAdjective(question).toLowerCase().split("[^\\w]+");
        comparePhrases.keyWords = new HashSet<>(Arrays.asList(keyWordsArrayNouns));

        comparePhrases.keyWordsVerbOrAdjective = new HashSet<>(Arrays.asList(keyWordsArrayVerbOrAdjective));
        //String allText = FileUtils.readFileToString(result, "utf-8").toLowerCase();
        comparePhrases.keyWordUniqueness.clear();
        /*for (String word : comparePhrases.keyWords) {
            try {
                if (comparePhrases.keyWordUniqueness.containsKey(LuceneSnowBallTest.getStem(word.toLowerCase())))
                    comparePhrases.keyWordUniqueness.put(LuceneSnowBallTest
                                    .getStem(word.toLowerCase()),
                            comparePhrases.keyWordUniqueness.get(
                                    LuceneSnowBallTest
                                            .getStem(word.toLowerCase()
                                            )) + numberOfOccurrences(allText, word.toLowerCase()));
                else
                    comparePhrases.keyWordUniqueness.put(LuceneSnowBallTest.getStem(word.toLowerCase()), numberOfOccurrences(allText, word.toLowerCase()));
            } catch (java.lang.ArithmeticException g) {
                g.printStackTrace();
            }
        }
        */
        HashSet<KeyWordPattern> newKeyWordsNouns = new HashSet<KeyWordPattern>();
        HashSet<KeyWordPattern> newKeyWordsVerbsOrAdjectives = new HashSet<KeyWordPattern>();
        // NOuns
        HashSet<KeyWordPattern> newKeyWordsFullNouns = new HashSet<KeyWordPattern>();
        HashSet<KeyWordPattern> newKeyWordsFullVerbsOrAdjectives = new HashSet<KeyWordPattern>();
        for (String keyword : comparePhrases.keyWords) {
            // find the root word
            String rootWord = LuceneSnowBallTest.getStem(keyword);
            // Check if root word of key word is an ACTUAL WORD
            if (rootWord.length() > 2 && !(rootWord.isEmpty())&&!(keyword.isEmpty())) {//
                // add the root word
                newKeyWordsNouns.add(new KeyWordPattern(new String[]{rootWord,keyword}));
            }
            else if (!(keyword.isEmpty())){//
                // no, add the key word
                newKeyWordsNouns.add(new KeyWordPattern(new String[]{keyword,keyword}));
            }
        }
        // adjectives
        for (String keyword : comparePhrases.keyWordsVerbOrAdjective) {
            // find the root word
            String rootWord = LuceneSnowBallTest.getStem(keyword);
            // Check if root word of key word is an ACTUAL WORD
            if (rootWord.length() > 2&& !(rootWord.isEmpty())&&!(keyword.isEmpty())) {//
                // add the root word
                newKeyWordsVerbsOrAdjectives.add(new KeyWordPattern(new String[]{rootWord,keyword}));
            }
            else if (!(keyword.isEmpty())){//
                // no, add the key word
                newKeyWordsVerbsOrAdjectives.add(new KeyWordPattern(new String[]{keyword,keyword}));
            }
        }

        ArrayList<HashSet<ParagraphInfo>> totalListOfTrees = new ArrayList<>();
        TreeMap<Integer, HashSet<ParagraphInfo>> totalFinalfinalSetOfWordsTree = new TreeMap<>(Collections.reverseOrder());
        ArrayList<ParagraphInfo> match = new ArrayList<ParagraphInfo>();
//        File[] listOfFiles = Paths.get(statementsFileName).getParent().toFile().listFiles();
        int index = 0;

        long origionalFreeMemory = Runtime.getRuntime().freeMemory();
        boolean shouldCollect = true;
        int prevValue = 0;
        newKeyWordsFullNouns = newKeyWordsNouns;
        newKeyWordsFullNouns.addAll(newKeyWordsVerbsOrAdjectives);
        HashMap<KeyWordPattern, HashSet<KeyWordPattern>> keyword2SynonymMap = new general.FindKeyWordsTest().getSynonyms(newKeyWordsFullNouns,isDeep);




        /*int bookNumber = 0;

        ArrayList<ParagraphInfo> ResultsToArrayList =
                new ArrayList<>(totalFinalfinalSetOfWords);
        int totalNests = ResultsToArrayList.size();
        int step = totalNests / NUM_OF_CORES;
        ArrayList<HashSet<ParagraphInfo>> newstedListInception = new ArrayList<>();
        for(int i =0;i<NUM_OF_CORES;i++)
        {
            newstedListInception.add( new HashSet<>(ResultsToArrayList.subList(step * i, step * (i+1))));
        }*/
        int NUM_OF_CORES = 16;
        ArrayList<UrlFileConnector> chunks = ufc;
        NUM_OF_CORES = chunks.size();
        //File[] someFile = chunks.get(0);
        Thread[] threads = new Thread[NUM_OF_CORES];
        Scan scan = new Scan();
        System.out.println("f309ja0fajeg0aw9j0gjw039g:"+keyword2SynonymMap);
        HashSet<KeyWordPattern> finalNewKeyWordsFullNouns = newKeyWordsFullNouns;
        for (int i = 0; i < NUM_OF_CORES; i++) {
            int finalI = i;
            threads[i] = new Thread() {
                public void run() {
                    // do stuff
                    UrlFileConnector[] someFiles = new UrlFileConnector[]{chunks.get(finalI)};
                    scan.scanit( closestMatchedQueries, usedURLs,contents,totalFinalfinalSetOfWordsTree
                            ,someFiles, keyword2SynonymMap
                            , finalNewKeyWordsFullNouns);
                }
            };
        }
        System.setOut(MainGUI.originalStream);

        ExecutorService service = Executors.newCachedThreadPool();

        for (int i = 0; i < NUM_OF_CORES; i++) {
            service.execute(threads[i]);
        }

        // wait for termination
        service.shutdown();
        boolean finshed = service.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

        System.out.println("330981fj09s");
        //System.exit(100);

        //es.shutdown();
        // = es.awaitTermination(Integer.MAX_VALUE, TimeUnit.MINUTES);





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
            /*
            HashSet<String> possibleQueries = new HashSet<>();
            if(closestMatchedQueries.size()<1)
            {
                return new Message(Message.IMPOSSIBLE,new ArrayList<String>());
            }
            for(Map.Entry<Integer,HashSet<HashSet<String>>> matchedQueriesClosest: closestMatchedQueries.entrySet())
            for(HashSet<String> keywordSeti : matchedQueriesClosest.getValue())
            {
                String possibleQuery = new String();
                HashSet<String> missingKeyWords = new HashSet<>();
                for (KeyWordPattern keywordFromData : newKeyWordsFullNouns) {
                    boolean contains = false;
                    for (String keyWordFromParsedQuery : keywordSeti) {
                        // does the word from the question match with
                        // the keyword used in the program
                        // get the keyword, not the root word
                        if (keyWordFromParsedQuery.toLowerCase().equals(keywordFromData.getKeyWords()[1].toLowerCase()))
                        {
                            contains = true;
                        }

                    }
                    if(!contains) {
                        System.setOut(MainGUI.originalStream);
                        System.out.println("4ty34g: "+keywordFromData);
                        missingKeyWords.add(keywordFromData.getKeyWords()[1]);
                    }
                }
                int numberOfMissedWords = 0;
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

                    if(isMissingThisWord) {
                        possibleQuery += " -- ";
                        numberOfMissedWords++;
                    }
                    else
                        possibleQuery += " " + word;

                }
                if((FindKeyWordsTest.getNouns(possibleQuery).replaceAll("\\s+","").length()>0
                        ||FindKeyWordsTest.getVerbOrAdjective(possibleQuery).replaceAll("\\s+","").length()>0)
                        && numberOfMissedWords >0)
                    possibleQueries.add(possibleQuery);
            }
            */
            ArrayList<ParagraphInfo> answers = new ArrayList<>();
            int kwCount = 0;
            TreeMap<Integer, HashSet<HashSet<String>>> closestMatchedQueriesCopy = new TreeMap<>(closestMatchedQueries);
            int keyCount = 0;
            for(Map.Entry<Integer, HashSet<HashSet<String>>> map :closestMatchedQueriesCopy.entrySet())
            {

                if(keyCount++<2)
                for(HashSet<String> kwElement :map.getValue()) {
                    String message = "";
                    for (String kw : kwElement) {
                        message += " + " + kw;
                    }
                    ++kwCount;
                    if(!message.isEmpty()) {
                        Message answerX = this.chatbotTypeSept12(usedURLs,contents,message, message, isDeep, ufc);
                        for (ParagraphInfo ans : answerX.getAnswers())
                            answers.add(ans);
                    }
                    //finalsAns += "\n\nSuggestion #" + kwCount + ".\n" + message;
                }

            }
            //answers.add(new ParagraphInfo(finalsAns,"suggestion","1"));
            return new Message(Message.FOUND,answers);

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
    private  TreeMap<Integer, HashSet<HashSet<String>>> closestMatchedQueries = new TreeMap<>(Collections.reverseOrder());
    public  ArrayList<ParagraphInfo> getchats(TreeMap<Integer, HashSet<ParagraphInfo>> treeOfAnswers, String query, PrintStream dummyStream
    ,HashSet<KeyWordPattern> newKeyWordsFullNouns) throws IOException {
        ArrayList<ParagraphInfo> container = new ArrayList<>();
        int paragraphCount = 0;
        TreeMap<Double, HashSet<AnswerPair>> match = new TreeMap<>(Collections.reverseOrder());
        for ( int score : treeOfAnswers.keySet()) {
            ArrayList<ParagraphInfo> miniContainer = new ArrayList<>();
            for(ParagraphInfo paragraph : treeOfAnswers.get(score)) {
                if(paragraphCount++ < 500)
                    miniContainer.add(paragraph);
            }
            container.addAll(comparePhrases.rankAnswers(query,miniContainer,newKeyWordsFullNouns));

        }
        return container;
    }


    //in what decade was the first modern crossword puzzle published and oreo cookies are introduced

    public  float clamp(int val, int min) {
        return Math.max(min, val);
    }
}

