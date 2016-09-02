package general.graph.theory;

import SentenceGenerator.ComparePhrases;
import SentenceGenerator.GoogleCSAPI;
import SentenceGenerator.testWordnet;
import general.*;
import general.chat.MainGUI;
import general.chat.ProgressBarDemo;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.base.Sys;
import org.apache.lucene.wordnet.SynonymMap;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
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

/**
 * Created by corpi on 2016-04-30.
 */
public class WikipediaInfoBoxModel2OldJune14_PERSONAL2 {
    final public static boolean IS_CLEANING_QUERY = true;
    final public static int KEYWORD_IMPORTANCE_INDEX = 2;
    public static String statementsFileName = "statements2";
    public static String statementsDirectoryName = "openNLP\\";
    public static String dataDirectoryName = "openNLP\\";

    final public static int HOW_MANY_IN_THE_TOP_X = 20;
    //final public static String statementsFileName = "statements_july6.txt";
    //
    public static void changeStatementsFileName(String newName)
    {
        statementsFileName = dataDirectoryName+newName;
    }
    public static boolean statementsFileNameEquals(String other)
    {
        return statementsFileName.equals(dataDirectoryName+other);
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

        //System.exit(-100);
        list.clear();
        if(statementsFileNameEquals(MainGUI.web))
        {
            PrintWriter writer = new PrintWriter(new File(WikipediaInfoBoxModel2OldJune14_PERSONAL.statementsFileName));
            writer.print("");
            writer.close();
        }
        //noun = question + " " + noun;
        //query = question + " " + query;
        //question = question.toLowerCase().replaceAll("i","you");
        /*question = question.toLowerCase().replaceAll("[you\\sare\\s]|^you\\sare\\s]", "i am ");
        question = question.toLowerCase().replaceAll("[i\\sam\\s]|^i\\sam\\s]", "you are ");
        question = question.toLowerCase().replaceAll("[i\\s]|^i\\s]", "you ");

        question = question.toLowerCase().replaceAll("your\\s|^your\\s", "my ");
        question = question.toLowerCase().replaceAll("my\\s|^my\\s", "your ");

        question = question.toLowerCase().replaceAll("me\\s|^me\\s", "you ");
        question = question.toLowerCase().replaceAll("yours\\s|^yours\\s", "mine ");
        question = question.toLowerCase().replaceAll("mine\\s|^mine\\s", "yours ");
        question = question.toLowerCase().replaceAll("you\\s|^you\\s", "me ");
        */
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
// create the pattern joining the keys with '|'
        /*
        regexp = "([you\\sare\\s]|^you\\sare\\s])|" +
                "([i\\sam\\s]|^i\\sam\\s])" +
                "|([i\\s]|^i\\s])" +
                "|(your\\s|^your\\s)" +
                "|(my\\s|^my\\s)" +
                "|(me\\s|^me\\s)" +
                "|(yours\\s|^yours\\s)" +
                "|(mine\\s|^mine\\s)" +
                "|(you\\s|^you\\s)";

      sb = new StringBuffer();
     p = Pattern.compile(regexp);
        m = p.matcher(input);

        while (m.find())
            m.appendReplacement(sb, replacements.get(m.group()));
        m.appendTail(sb);


        //System.out.println(sb.toString());   // lorem repl1 ipsum repl2 dolor repl3 amet

        question = sb.toString();
*/
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

        /*
        org.json.JSONArray keywords = Client1.extractConcept(finalQuery);
        ArrayList<String> words = new ArrayList<>();
        for (int i = 0; i < keywords.length(); i++) {
            final int ii = i;
            es.execute(new Runnable() {
                @Override
                public void run() {
                    String result = finalQuery;
                    String keyword = keywords.getJSONObject(ii).get("concept").toString();
                    System.out.println("WORD BEFORE= " + keyword);

                    for (String usedWord : words)
                        keyword = keyword.replaceAll(usedWord, "");
                    keyword = Client1.gogo(keyword);
                    System.out.println("WORD AFTER= " + keyword);
                    for (String subKeyWord : keyword.split("\\s+"))
                        result = result.replace(subKeyWord, StringUtils.repeat(" " + subKeyWord + " ", WikipediaInfoBoxModel2OldJune14_PERSONAL.KEYWORD_IMPORTANCE_INDEX).trim());
                    words.add(keyword.trim());
                    System.out.println(">>> " + words);
                    list.add(result);
                }
            } );


        }
        es.shutdown();
        boolean finshed = es.awaitTermination(1, TimeUnit.SECONDS);
// all tasks have finished or the time has been reached.
        if (finshed) {

            System.out.println("query>>> " + list);
            //System.exit(0);
            //System.exit(-1);
        }
        */
        try {
            query = list.get(0);
        } catch (Exception k) {
            query = question;
        }
        System.out.println("query>>> " + query);
        //System.exit(-1);
        JFrame frame = new JFrame("ProgressBarDemo");
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
        if(statementsFileNameEquals(MainGUI.web)){
            //Create and set up the window.


            List<String> results = new ArrayList<>(GoogleCSAPI.getLinks((origionalQuestion).replaceAll("\\s+", "+"), false));//.subList(0,3));
            List<String> results2 = new ArrayList<>(GoogleCSAPI.getLinks((origionalQuestion).replaceAll("\\s+", "+"), true));//.subList(0,3));
            results.addAll(results2);
            //results =  results.subList(0,1);

            int index = 0;
            for (String result : results) {
                // ( (ProgressBarDemo)newContentPane).progressBar.setValue((int)(100.0*(((double)index)/results.size())));
                newContentPane.progressBar.setValue((int)(100.0*(((double)index++)/results.size())));
                try {
                    System.out.println(result);
                    Document doc = Jsoup.connect(result).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2").get();
                    Elements paragraphs = doc.select("p");
                    for (Element element : paragraphs) {
                        String biggestText = element.text() +"{1g42fwefx}";//(|mr|ms|mrs)
                        try {
                            Files.write(Paths.get(statementsFileName),
                                    (biggestText).getBytes(), StandardOpenOption.APPEND);
                        } catch (IOException e) {
                            //exception handling left as an exercise for the reader
                        }
                    }
                } catch (Exception l) {
                    l.printStackTrace();
                }
                //break;
            }
        }
        frame.setVisible(false);
        frame.dispose();
        ComparePhrases.keyWords.clear();
        ComparePhrases.keyWordsVerbOrAdjective.clear();
        String[] keyWordsArrayNouns = FindKeyWordsTest.getNouns(question).toLowerCase().split("[^\\w]+");
        String[] keyWordsArrayVerbOrAdjective = FindKeyWordsTest.getVerbOrAdjective(question).toLowerCase().split("[^\\w]+");
        ComparePhrases.keyWords = new HashSet<>(Arrays.asList(keyWordsArrayNouns));
        ComparePhrases.keyWordsVerbOrAdjective = new HashSet<>(Arrays.asList(keyWordsArrayVerbOrAdjective));
        String allText = FileUtils.readFileToString(new File(
                statementsFileName
        ), "utf-8").toLowerCase();
        ComparePhrases.keyWordUniqueness.clear();
        for (String word : ComparePhrases.keyWords) {
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
            }catch (java.lang.ArithmeticException g)
            {
                g.printStackTrace();
            }
        }
        HashSet<String> newKeyWordsNouns = new HashSet<>();
        HashSet<String> newKeyWordsVerbsOrAdjectives = new HashSet<>();
        // NOuns
        HashSet<String> newKeyWordsFullNouns = new HashSet<>();
        HashSet<String> newKeyWordsFullVerbsOrAdjectives = new HashSet<>();
        for (String keyword : ComparePhrases.keyWords)
            newKeyWordsNouns.add(LuceneSnowBallTest.getStem(keyword));
        // adjectives
        for (String keyword : ComparePhrases.keyWordsVerbOrAdjective)
            newKeyWordsVerbsOrAdjectives.add(LuceneSnowBallTest.getStem(keyword));

        ComparePhrases.keyWords = newKeyWordsNouns;
        ComparePhrases.keyWordsVerbOrAdjective = newKeyWordsVerbsOrAdjectives;
        ComparePhrases.textSize = allText.split("[^\\w]+").length;
        System.out.println(ComparePhrases.keyWordUniqueness);
        ArrayList<String> wordArray = new ArrayList<>(
                Arrays.asList(allText.split("[^\\w]+"))
        );
        ComparePhrases.mostcommon = ComparePhrases.mostCommonWordsList(wordArray, HOW_MANY_IN_THE_TOP_X);
        ComparePhrases.synMap = testWordnet.getSynonymMap(question);//new HashMap<String,HashSet<String>>();//

        for (String keyword : ComparePhrases.keyWords)
            if(!ComparePhrases.mostcommon.contains(keyword.toLowerCase())) {
                newKeyWordsFullNouns.add(keyword);
                String rootWord = LuceneSnowBallTest.getStem(keyword);
                if(rootWord.length()>2)
                {
                    newKeyWordsFullNouns.add(keyword);
                }
            }
        for (String keyword : ComparePhrases.keyWordsVerbOrAdjective)
            if(!ComparePhrases.mostcommon.contains(keyword.toLowerCase())) {
                newKeyWordsFullNouns.add(keyword);
                String rootWord = LuceneSnowBallTest.getStem(keyword);
                if(rootWord.length()>2)
                {
                    newKeyWordsFullNouns.add(keyword);
                }
            }
        for (String keyword : ComparePhrases.keyWordsVerbOrAdjective)
            if(!ComparePhrases.mostcommon.contains(keyword.toLowerCase())) {
                newKeyWordsFullVerbsOrAdjectives.add(keyword);
                String rootWord = LuceneSnowBallTest.getStem(keyword);
                if(rootWord.length()>2)
                {
                    newKeyWordsFullVerbsOrAdjectives.add(keyword);
                }
            }

        TreeMap<Double, HashSet<AnswerPair>> match = new TreeMap<>(Collections.reverseOrder());
        int charsize = 0;



        String biggestText = FileUtils.readFileToString(new File(
                statementsFileName
        ), "utf-8").toLowerCase();;//(|mr|ms|mrs)
        biggestText = biggestText.toLowerCase().replaceAll("dr\\.","dr");
        biggestText = biggestText.toLowerCase().replaceAll("mr\\.","mr");
        biggestText = biggestText.toLowerCase().replaceAll("ms\\.","ms");
        biggestText = biggestText.toLowerCase().replaceAll("mrs\\.","mrs");
        //String[] sentences = biggestText.split("[\\.\\?!\\n]+");
        String[] sentences =biggestText.split("\\{1g42fwefx\\}");
        if(!statementsFileNameEquals(MainGUI.web))
            sentences = biggestText.split("[\\.\\?!\\n]+");

        HashSet<String> sentsNouns = new HashSet<>();
        HashSet<String> sentsVerbsOrAdjFinal = new HashSet<>();
        for(String y : sentences)
        {
            if(!TestChatBotMain.answersUsed.contains(y.toLowerCase()))
                for(String keyWord :newKeyWordsFullNouns) {
                    System.out.println(y + "->" + keyWord);
                    if (y.toLowerCase().contains(keyWord.toLowerCase())) {
                        sentsNouns.add(y);

                    }
                }
        }
        System.out.println(sentsNouns.size());
        for(String y : sentsNouns)
        {
            if(!TestChatBotMain.answersUsed.contains(y.toLowerCase()))
                for(String keyWord :newKeyWordsFullVerbsOrAdjectives) {
                    System.out.println(y + "->" + keyWord);
                    if (y.toLowerCase().contains(keyWord.toLowerCase())) {
                        sentsVerbsOrAdjFinal.add(y);

                    }
                }
        }

        //String line = biggestText;
        //String pat = "[\\.\\?!\\n]+(";
        //for(String x :newKeyWordsFull)
        //        pat += "[^\\.^\\?^!^\\n]+"+x.toLowerCase() + "\\w+\\s+[^\\.^\\?^!^\\n]+|";
        /*for(Map.Entry<String,HashSet<String>> entry : ComparePhrases.synMap.entrySet()) {
            pat += "[^\\.^\\?^!^\\n]+"+entry.getKey() + "\\w+\\s+[^\\.^\\?^!^\\n]+|";

            for (String val : entry.getValue())
            {
                pat += "[^\\.^\\?^!^\\n]+"+val + "\\w+\\s+[^\\.^\\?^!^\\n]+|";
            }

        }

        pat = removeLastChar(pat);
        pat += ")[\\.\\?!\\n]+";
        p = Pattern.compile(pat);
        m = p.matcher(line);
        */
/*
        while (m.find()) {
            if(origionalQuestion.toLowerCase().matches(".*?how\\s.*?"))
                if(m.group(1).replaceAll("\\[.*?\\]","").matches(".*?\\d+.*?"))
                    sents.add(m.group(1));
            else
                sents.add(m.group(1));

        }
        */
        int i = 0;
        System.out.println(sentsVerbsOrAdjFinal.size()+ "old = "+biggestText.split("[\\.\\?!\\n]+").length);
        for (String bigtext : sentsVerbsOrAdjFinal) {
            for (String para : bigtext.split("[\\.\\?!\\n]+")) {
                String result = Integer.toString(i);
                String p12 = para.replaceAll("\\[.*?\\]", "");
                try {
                    p12 = p12.substring(p12.indexOf(':'));
                } catch (Exception k) {//do nothing
                }
                System.out.println("2464gfds: " + p12 + " from " + para);
                if (!isUserAskingAQustion) {
                    if (!TestChatBotMain.answersUsed.contains(para.toLowerCase()))
                        if (!(!p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("how ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("do ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("does ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("can ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("why ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("who ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("what ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("if ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("will ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("would ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("could ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("where ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("would ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("could ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("where ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("when ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("is ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("was ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("which ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("he ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("she ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("did ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("does ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("am ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("are ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("am ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("i am")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("are ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("how's")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("do's")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("does's")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("can's")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("why's")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("who's")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("what's")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("won't")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("wouldn't")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("couldn't")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("where's")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("when's")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("is's")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("which's")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("he's")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("she's")
                                && !p12.contains(">>")
                                && !TestChatBotMain.answersUsed.contains(para.toLowerCase()
                        ))) {
                            double compare = ComparePhrases.compare2(query, para);
                            charsize += para.length();
                            if (match.containsKey(compare)) {
                                HashSet<AnswerPair> dummy = match.get(compare);
                                if (para.toLowerCase().matches(".*?\\s(it|she|he|they|their|there|him|her|" +
                                        "his|they're|we|we're|that|this|thus)['\\s\\.\\?!].*?"))//thus?
                                    dummy.add(new AnswerPair(result, para));
                                else
                                    dummy.add(new AnswerPair(result, para));
                                match.put(compare, dummy);
                            } else {
                                HashSet<AnswerPair> dummy = new HashSet<>();
                                if (para.toLowerCase().matches(".*?\\s(it|she|he|they|their|there|him|her|" +
                                        "his|they're|we|we're)['\\s\\.\\?!].*?"))
                                    dummy.add(new AnswerPair(result, para));
                                else
                                    dummy.add(new AnswerPair(result, para));
                                match.put(compare, dummy);
                            }
                        }
                } else if (!TestChatBotMain.answersUsed.contains(para.toLowerCase())) {
                    if ((!p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("how ")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("do ")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("am ")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("i am")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("are ")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("does ")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("can ")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("why ")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("who ")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("what ")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("if ")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("will ")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("would ")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("could ")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("where ")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("where ")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("when ")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("is ")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("was ")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("which ")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("he ")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("she ")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("did ")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("does ")

                            && !p12.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("how's")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("do's")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("does's")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("can's")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("why's")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("who's")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("what's")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("won't")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("wouldn't")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").trim().startsWith("couldn't")

                            && !p12.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("where's")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("when's")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("is's")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("which's")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("he's")
                            && !p12.toLowerCase().replaceAll("[^\\w^\\s^']+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("she's")
                            && !p12.contains(">>")
                            && !TestChatBotMain.answersUsed.contains(para.toLowerCase())
                    )) {
                        double compare = ComparePhrases.compare2(query, para);
                        charsize += para.length();
                        if (match.containsKey(compare)) {
                            HashSet<AnswerPair> dummy = match.get(compare);
                            if (para.toLowerCase().matches(".*?\\s(it|she|he|they|their|there|him|her|" +
                                    "his|they're|we|we're|that|this|thus)['\\s\\.\\?!].*?"))//thus?
                                dummy.add(new AnswerPair(result, para));
                            else
                                dummy.add(new AnswerPair(result, para));
                            match.put(compare, dummy);
                        } else {
                            HashSet<AnswerPair> dummy = new HashSet<>();
                            if (para.toLowerCase().matches(".*?\\s(it|she|he|they|their|there|him|her|" +
                                    "his|they're|we|we're)['\\s\\.\\?!].*?"))
                                dummy.add(new AnswerPair(result, para));
                            else
                                dummy.add(new AnswerPair(result, para));
                            match.put(compare, dummy);
                        }
                    }
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
            int index = 0;
            String speakfull = new String();

            for(Map.Entry<Double,HashSet<AnswerPair>> answeri : match.entrySet()) {
                if(index++<5) {
                    HashSet<AnswerPair> pairs = answeri.getValue();

                    for (AnswerPair curPair : pairs) {
                        speakfull += curPair.getText() + ". ";
                    }
                }
            }
            speakfull = speakfull.trim();
            System.out.println("query --> "+query);
            System.out.println(ComparePhrases.mostcommon);
            if(match.firstEntry().getKey()<1.0)
                System.out.println("Answer --> "+" I couldn't find anything on that.");
            else
                System.out.println("Answer --> "+speakfull);
            System.out.println("Overall score --> "+match.firstEntry().getKey());



            System.out.println(ComparePhrases.synMap);
            if(!statementsFileNameEquals(MainGUI.web))
            {
                speakfull = ((AnswerPair)match.firstEntry().getValue().toArray()
                        [new Random().nextInt(
                        match.firstEntry().getValue().size()
                )]).getText();
            }
            TestChatBotMain.answersUsed.add(speakfull.toLowerCase());
            if(match.firstEntry().getKey()<1.0)
                return " I couldn't find anything on that.";
            else
                return speakfull;// +origionalQuestion.toLowerCase().matches(".*?how\\s.*?") +speakfull.replaceAll("\\[.*?\\]","").matches(".*?\\d+.*?");


        }
        catch (Exception l) {
            /*
            System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))");
            System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))");
            System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))");
            System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))");
            System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))");

            String speakfull = ((AnswerPair)match.firstEntry().getValue().toArray()[(new Random()).nextInt(match.firstEntry().getValue().size())])
                    .getText();
            System.out.println("Answer --> "+speakfull);
            */

        }
        return "Either you are not giving me a lot to go on, or I don't have any info on this. Perhaps you can provide me with more detail?";
    }
    public static ArrayList<String> listSynonymsJJ2(String lastWord) throws IOException {
        ArrayList<String> rjhymes = new ArrayList<String>();
        System.out.println("... Getting source");
        //String url = "http://www.thesaurus.com/browse/"+ lastWord+"?s=t";
        String url = "http://www.wordhippo.com/what-is/another-word-for/" + lastWord + ".html";
        //String url = "http://www.getanotherwordfor.com/"+lastWord;
        String html = "";
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
            html = doc.html();
        } catch (HttpStatusException e) {
            System.out.println("No internet");
        }
        //System.out.println(html);
        if (html.equals("") || html.contains("No words found.")) {
            System.out.println("REWORDING FAILED.");
            return new ArrayList<String>();//listSynonymsBACKUP(lastWord);
        } else {
            System.out.println("... Got source succeeded");
            if (!html.equals("") && !html.contains("no thesaurus results")) {
                System.out.println("GOt the HTML");
                String pattern = "";
                for (String type : new String[]{"v", "j", "r"}){
                    if (type.toLowerCase().startsWith("v")) {
                        pattern = "<div class=\"wordtype\">Verb</div>(.*?)";
                    } else if (type.toLowerCase().startsWith("j")) {
                        pattern = "<div class=\"wordtype\">Adjective</div>(.*?)";
                    } else if (type.toLowerCase().startsWith("r")) {
                        pattern = "<div class=\"wordtype\">Adverb</div>(.*?)";
                    }
                    Pattern p = Pattern.compile(pattern);
                    Matcher m = p.matcher(html);
                    while (m.find()) {
                        if (m.groupCount() > 0) {
                            html = m.group(1);
                        }
                    }
                    /////////////////////////////////////////////////////////////
                    pattern = "/what-is/another-word-for/.*?\">([a-z\\s]+)</a>";
                    p = Pattern.compile(pattern);
                    m = p.matcher(html);
                    final int WORD_BANK_SIZE = 4;
                    int h = 0;
                    while (h++ < WORD_BANK_SIZE && m.find()) {
                        String rhyme1 = m.group(1);
                        System.out.println("Synonym = " + rhyme1);
                        if (!rhyme1.equals(lastWord)) {
                            rjhymes.add(rhyme1);
                        }
                    }
                    //TreeMap<Integer,String> set = new TreeMap<Integer,String>(Collections.reverseOrder());
                    if (rjhymes.size() > 1) {
                        int maxSize = 4;
                        if (rjhymes.size() < maxSize) {
                            maxSize = rjhymes.size();
                        }

                        //rjhymes = pickNRandom(rjhymes, maxSize - 1);
                    } else {
                        //System.out.println(usedPhrases7.get(i));
                        //System.out.println(usedPhrases5.get(i + 1));
                    }
                }
            }
        }

        System.out.println("DFSIUGHENSVJLNEIONVS last word = <" + lastWord + ">");
        System.out.println("DFSIUGHENSVJLNEIONVS synonym = <"+rjhymes+">");
        return rjhymes;
    }
    public static String getBestFromCollection(HashSet<String> list, String search,final String TYPE) throws IOException {
        double awsome = 0;
        //String best = getRandomFromCollection(list);
        String best = "NOTHING FOUND";
        for(String x : list)
        {
            double current = comparePhrasesold_june7.compare2Type(search,x,false,TYPE);
            if(current>awsome)
            {
                best = x;
                awsome = current;
            }
        }
        return best;
    }
    public static String getRandomFromCollection(Collection list)
    {
        Random generator = new Random(list.size());
        int number = generator.nextInt();
        if(number >=list.size())
            number = list.size()-1;
        else if(number<0)
            number =0;
        return list.toArray()[0].toString();
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
    public static HashSet<String> getDataWHO_WHERE(String RAW, String NAME, String nouns
                                              /*
                                              String adjectives,
                                              String nnps, String properNouns,
                                              String adverbs, String verbs
                                              */
    ) throws IOException {
        PrintStream c = System.out;
        boolean big = false;
        String noun = (NAME + " " + RAW).replaceAll("\\s+","+");
        String query = nouns;
        // ADD SYNONYMS TOO
        String[] words = query.toLowerCase().split("[^\\w]+");
        SynonymMap map = new SynonymMap(new FileInputStream(WikipediaInfoBoxModel2OldJune14_PERSONAL.statementsDirectoryName+"wn_s.pl"));
        for (int i = 0; i < words.length; i++) {
            String[] synonyms = map.getSynonyms(words[i]);
            for(String x : synonyms)
            {
                query += " " + x;
            }
        }
        query = getNouns(query);
        System.out.println("ALL QUERIES USED: " + query.replaceAll("\\s",", "));
        ArrayList<String> results = GoogleCSAPI.getLinks(noun,true);
        TreeMap<Double, HashSet<String>> match = new TreeMap<>(Collections.reverseOrder());
        for (String result : results) {
            try {
                //if(!result.getUrl().toLowerCase().contains("wikipedia"))
                //    continue;
                Connection.Response res = Jsoup.connect(result)
                        .execute();

                String html = res.body();

                Document doc = Jsoup.parseBodyFragment(html);
                Element body = doc.body();
                Elements tables = body.getElementsByTag("table");// hasClass("infobox bordered");
                //System.out.println(tables.get(0));
                String text = "";

                for (int k = 0; k < tables.size(); k++)
                    for (int i = 0; i < tables.get(k).children().size(); i++) {
                        ;
                        c.println(">>> " + tables.get(k).children().get(i).text());
                        for (int j = 0; j < tables.get(k).children().get(i).children().size(); j++) {
                            if (true||!(RAW.toLowerCase().contains("where")||RAW.toLowerCase().contains("who") || RAW.toLowerCase().contains("name"))) {
                                String line = tables.get(k).children().get(i).children().get(j).text();
                                line = line.replaceAll("\\([sS]\\)","");
                                //c.println("-->" + line);
                                text += "\n" + line + "\n";//
                                String parent = tables.get(k).children().get(i).text();
                                String superParent = tables.get(k).text();
                                //double comparisonFactor = ComparePhrases.compare2(query, line, big);
                                double comparisonFactor = comparePhrasesold_june7.compare2SHORT(query, line, big);
                                if (match.containsKey(comparisonFactor)) {
                                    HashSet<String> addON = new HashSet<String>(match.get(comparisonFactor));
                                    //addON.add(parent);
                                    addON.add(line);
                                    match.put(comparisonFactor, addON);
                                } else {
                                    HashSet<String> newLine = new HashSet<>();
                                    newLine.add(line);
                                    match.put(comparisonFactor, newLine);
                                }

                            } else {
                                String line = tables.get(k).children().get(i).children().get(j).text();

                                double comparisonFactor = comparePhrasesold_june7.compare(query, line);
                                c.println("cc::"+comparisonFactor+"->"+line);
                                if (match.containsKey(comparisonFactor)) {
                                    HashSet<String> addON = new HashSet<String>(match.get(comparisonFactor));
                                    //addON.add(parent);
                                    addON.add(line);
                                    match.put(comparisonFactor, addON);
                                } else {
                                    HashSet<String> newLine = new HashSet<>();
                                    newLine.add(line);
                                    match.put(comparisonFactor, newLine);
                                }
                            }
                        }
                        //if (match.size() > 0 && match.firstEntry().getKey() != 0)
                        //    System.out.println(match.firstEntry().getKey() + "RESULT:::" + match.firstEntry().getValue());
                    }
            }catch (Exception j) {

            }
        }
        HashSet<String> first = new HashSet<>(match.firstEntry().getValue());
        TreeMap<Double, HashSet<String>> second = new TreeMap<>(Collections.reverseOrder());

        for(String line : first)
        {

            if (line.length()<40&&FindKeyWordsTest.findName(line).size() > 0) {
                // THIS IS VALID
                System.out.println("FOUND SOEMTHING! >> "+ line);
                double comparisonFactor = comparePhrasesold_june7.compare(query, line);
                if (second.containsKey(comparisonFactor)) {
                    HashSet<String> addON = new HashSet<String>(second.get(comparisonFactor));
                    //addON.add(parent);
                    addON.add(line);
                    second.put(comparisonFactor, addON);
                } else {
                    HashSet<String> newLine = new HashSet<>();
                    newLine.add(line);
                    second.put(comparisonFactor, newLine);
                }
            }
        }
        try {
            first = new HashSet<>(second.firstEntry().getValue());
            System.out.println("seccon-->" + first);
            return first;
        }catch (NullPointerException k) {
            System.out.println("YOUMESSEDUP");
            if ((RAW.toLowerCase().contains("place")||RAW.toLowerCase().contains("where")||RAW.toLowerCase().contains("who") || RAW.toLowerCase().contains("name"))) {

                for (String result : results) {
                    try {
                        String x = result.substring(result.lastIndexOf('/')).replaceAll("_", " ");
                        if (x.split("\\s").length == 2 && FindKeyWordsTest.findName(x).size() > 0)
                            return new HashSet<String>(FindKeyWordsTest.findName(x));
                    } catch (Exception o) {
                    }
                }
            }
            else
            {
            }
            //first.add("I'm not sure... is the answer ");
            //
            return new HashSet<String>();
        }


    }
    public static HashSet<String> getDataWHAT_WHEN_NUMBER(String RAW, String NAME, String nouns
                                              /*
                                              String adjectives,
                                              String nnps, String properNouns,
                                              String adverbs, String verbs
                                              */
    ) throws IOException {
        PrintStream c = System.out;
        boolean big = RAW.toLowerCase().contains("population");
        String noun = (NAME + " " + RAW).replaceAll("\\s+","+");
        //noun = NAME.replaceAll("\\s+","+");
        String query = nouns;
        // ADD SYNONYMS TOO
        String[] words = query.toLowerCase().split("[^\\w]+");
        SynonymMap map = new SynonymMap(new FileInputStream(WikipediaInfoBoxModel2OldJune14_PERSONAL.statementsDirectoryName+"wn_s.pl"));
        for (int i = 0; i < words.length; i++) {
            String[] synonyms = map.getSynonyms(words[i]);
            for(String x : synonyms)
            {
                query += " " + x;
            }
        }
        query = getNouns(query);
        System.out.println("ALL QUERIES USED: " + query.replaceAll("\\s",", "));
        ArrayList<String> results = GoogleCSAPI.getLinks(noun,true);
        TreeMap<Double, HashSet<String>> match = new TreeMap<>(Collections.reverseOrder());
        for (String result : results) {
            try {
                //if(!result.getUrl().toLowerCase().contains("wikipedia"))
                //    continue;
                Connection.Response res = Jsoup.connect(result)
                        .execute();

                String html = res.body();

                Document doc = Jsoup.parseBodyFragment(html);
                Element body = doc.body();
                Elements tables = body.getElementsByTag("table");// hasClass("infobox bordered");
                //System.out.println(tables.get(0));
                String text = "";

                for (int k = 0; k < tables.size(); k++) {
                    c.println("~~~~"+tables.get(k).text());
                    for (int i = 0; i < tables.get(k).children().size(); i++) {
                        ;
                        c.println(">>> " + tables.get(k).children().get(i).text());
                        for (int j = 0; j < tables.get(k).children().get(i).children().size(); j++) {
                            if (true || !(RAW.toLowerCase().contains("where") || RAW.toLowerCase().contains("who") || RAW.toLowerCase().contains("name"))) {
                                String line = tables.get(k).children().get(i).children().get(j).text();

                                line = line.replaceAll("\\([sS]\\)", "");
                                //c.println("-->" + line);
                                text += "\n" + line + "\n";//
                                String parent = tables.get(k).children().get(i).text();
                                String superParent = tables.get(k).text();
                                //double comparisonFactor = ComparePhrases.compare2(query, line, big);
                                double comparisonFactor = comparePhrasesold_june7.compare2SHORT(query, line, big);
                                if (match.containsKey(comparisonFactor)) {
                                    HashSet<String> addON = new HashSet<String>(match.get(comparisonFactor));
                                    //addON.add(parent);
                                    addON.add(line);
                                    match.put(comparisonFactor, addON);
                                } else {
                                    HashSet<String> newLine = new HashSet<>();
                                    newLine.add(line);
                                    match.put(comparisonFactor, newLine);
                                }

                            } else {
                            }
                        }
                        //if (match.size() > 0 && match.firstEntry().getKey() != 0)
                        //    System.out.println(match.firstEntry().getKey() + "RESULT:::" + match.firstEntry().getValue());
                    }
                }
            }catch (HttpStatusException j) {
                System.out.println("EEROEOREWORPWERWERWOEKOEWR");
                j.printStackTrace();
            }
        }
        HashSet<String> first = new HashSet<>(match.firstEntry().getValue());
        try{
            HashSet<String> second = new HashSet<>();
            Pattern pat = Pattern.compile(".*?\\d+.*?");
            for(String sec : first) {
                Matcher matcher = pat.matcher(sec);
                if(matcher.matches())
                    second.add(sec);
            }
            return  second;
        }catch (Exception l)
        {
            l.printStackTrace();
            return  first;
        }

        //return match.firstEntry().getValue();


    }
    public static HashSet<String> getDataWHAT_WHEN_NAME(String RAW, String NAME, String nouns
                                              /*
                                              String adjectives,
                                              String nnps, String properNouns,
                                              String adverbs, String verbs
                                              */
    ) throws IOException {
        PrintStream c = System.out;
        boolean big = RAW.toLowerCase().contains("population");
        String noun = (NAME + " " + RAW).replaceAll("\\s+","+");
        //noun = NAME.replaceAll("\\s+","+");
        String query = nouns;
        // ADD SYNONYMS TOO
        String[] words = query.toLowerCase().split("[^\\w]+");
        SynonymMap map = new SynonymMap(new FileInputStream(WikipediaInfoBoxModel2OldJune14_PERSONAL.statementsDirectoryName+"wn_s.pl"));
        for (int i = 0; i < words.length; i++) {
            String[] synonyms = map.getSynonyms(words[i]);
            for(String x : synonyms)
            {
                query += " " + x;
            }
        }
        query = getNouns(query);
        System.out.println("ALL QUERIES USED: " + query.replaceAll("\\s",", "));
        ArrayList<String> results = GoogleCSAPI.getLinks(noun,true);
        TreeMap<Double, HashSet<String>> match = new TreeMap<>(Collections.reverseOrder());
        for (String result : results) {
            try {
                //if(!result.getUrl().toLowerCase().contains("wikipedia"))
                //    continue;
                Connection.Response res = Jsoup.connect(result)
                        .execute();

                String html = res.body();

                Document doc = Jsoup.parseBodyFragment(html);
                Element body = doc.body();
                Elements tables = body.getElementsByTag("table");// hasClass("infobox bordered");
                //System.out.println(tables.get(0));
                String text = "";

                for (int k = 0; k < tables.size(); k++) {
                    c.println("~~~~"+tables.get(k).text());
                    for (int i = 0; i < tables.get(k).children().size(); i++) {
                        ;
                        c.println(">>> " + tables.get(k).children().get(i).text());
                        String pLine = tables.get(k).children().get(i).text();
                        double comparisonFactor = comparePhrasesold_june7.compare2SHORT(query, pLine, big);
                        //double comparisonFactor = ComparePhrases.compare2SHORT(RAW.toLowerCase(), pLine, big);
                        pLine = pLine.replaceAll("\\([sS]\\)", "");
                        //c.println("-->" + line);
                        text += "\n" + pLine + "\n";//
                        String parent = tables.get(k).children().get(i).text();
                        String superParent = tables.get(k).text();
                        //double comparisonFactor = ComparePhrases.compare2(query, line, big);

                        if (match.containsKey(comparisonFactor)) {
                            HashSet<String> addON = new HashSet<String>(match.get(comparisonFactor));
                            //addON.add(parent);
                            addON.add(pLine);
                            match.put(comparisonFactor, addON);
                        } else {
                            HashSet<String> newLine = new HashSet<>();
                            newLine.add(pLine);
                            match.put(comparisonFactor, newLine);
                        }
                        for (int j = 0; j < tables.get(k).children().get(i).children().size(); j++) {
                            if (true || !(RAW.toLowerCase().contains("where") || RAW.toLowerCase().contains("who") || RAW.toLowerCase().contains("name"))) {
                                String line = tables.get(k).children().get(i).children().get(j).text();
                                comparisonFactor = comparePhrasesold_june7.compare2SHORT(query, line, big);
                                //comparisonFactor = ComparePhrases.compare2SHORT(RAW.toLowerCase(), line, big);
                                line = line.replaceAll("\\([sS]\\)", "");
                                //c.println("-->" + line);
                                text += "\n" + line + "\n";//
                                parent = tables.get(k).children().get(i).text();
                                superParent = tables.get(k).text();
                                //double comparisonFactor = ComparePhrases.compare2(query, line, big);

                                if (match.containsKey(comparisonFactor)) {
                                    HashSet<String> addON = new HashSet<String>(match.get(comparisonFactor));
                                    //addON.add(parent);
                                    addON.add(line);
                                    match.put(comparisonFactor, addON);
                                } else {
                                    HashSet<String> newLine = new HashSet<>();
                                    newLine.add(line);
                                    match.put(comparisonFactor, newLine);
                                }

                            } else {
                            }
                        }
                        //if (match.size() > 0 && match.firstEntry().getKey() != 0)
                        //    System.out.println(match.firstEntry().getKey() + "RESULT:::" + match.firstEntry().getValue());
                    }
                }
            }catch (HttpStatusException j) {
                System.out.println("EEROEOREWORPWERWERWOEKOEWR");
                j.printStackTrace();
            }
        }
        return match.firstEntry().getValue();


    }
}


