package general;

import general.chat.MainGUI;
import general.graph.theory.WikipediaInfoBoxModel2OldJune14_PERSONAL;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by corpi on 2016-05-07.
 */
public class TestChatBotMain {
    public static ArrayList<String> list = new ArrayList<>();
    public static ArrayList<String> answersUsed =  new ArrayList<>();
    public static void main(String[] args) throws Exception {

        while(true) {
            runChatbot((new Scanner(System.in)).nextLine());
        }

    }
    public static String runChatbot(String userInput) throws Exception {
        // Empty chatbot log file
        list.clear();
        // String query = (new Scanner(System.in)).nextLine();
        String query = userInput;



        String originalInput = query;
        query = query.replaceAll("\\([\\d\\w\\s\\-_]+$", "")
                .replaceAll("\\(.*\\)", "")

                .toLowerCase().replaceAll("[^\\w^\\d^\\s]+", "").trim();//.toLowerCase().replace("known as","");

        System.out.println(query);
        //System.exit(0);
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
                        result = result.replace(subKeyWord, StringUtils.repeat(" " + subKeyWord + " ", WikipediaInfoBoxModel2.KEYWORD_IMPORTANCE_INDEX).trim());
                    words.add(keyword.trim());
                    System.out.println(">>> " + words);
                    list.add(result);
                }
            } );


        }
        String phrase1 = originalInput;
        es.shutdown();
        boolean finshed = es.awaitTermination(1, TimeUnit.MINUTES);
        */
        if (true) {

            boolean isUserAskingAQustion = false;
            if (!(!originalInput.toLowerCase().replaceAll("[^\\w^\\s]+", "").trim().startsWith("how ")
                    && !originalInput.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("do ")
                    && !originalInput.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("does ")
                    && !originalInput.toLowerCase().replaceAll("[^\\w^\\s]+", "").trim().startsWith("can ")
                    && !originalInput.toLowerCase().replaceAll("[^\\w^\\s]+", "").trim().startsWith("why ")
                    && !originalInput.toLowerCase().replaceAll("[^\\w^\\s]+", "").trim().startsWith("who ")
                    && !originalInput.toLowerCase().replaceAll("[^\\w^\\s]+", "").trim().startsWith("what ")
                    && !originalInput.toLowerCase().replaceAll("[^\\w^\\s]+", "").trim().startsWith("where ")
                    && !originalInput.toLowerCase().replaceAll("[^\\w^\\s]+", "").trim().startsWith("when ")
                    && !originalInput.toLowerCase().replaceAll("[^\\w^\\s]+", "").trim().startsWith("is ")
                    && !originalInput.toLowerCase().replaceAll("[^\\w^\\s]+", "").trim().startsWith("which ")
                    //&&!originalInput.toLowerCase().replaceAll("[^\\w^\\s]+","").trim().startsWith("he ")
                    //&&!originalInput.toLowerCase().replaceAll("[^\\w^\\s]+","").trim().startsWith("she ")
                    && !originalInput.toLowerCase().replaceAll("[^\\w^\\s]+", "").trim().startsWith("did ")
                    && !originalInput.toLowerCase().replaceAll("[^\\w^\\s]+", "").trim().startsWith("does ")

                    && !originalInput.toLowerCase().replaceAll("[^\\w^\\s]+", "").trim().startsWith("how's")
                    && !originalInput.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("do's")
                    && !originalInput.toLowerCase().replaceAll("[^\\w^\\s]+", "").replaceAll("\\[.*?\\]", "").replaceAll("\\[.*?\\]", "").trim().startsWith("does's")
                    && !originalInput.toLowerCase().replaceAll("[^\\w^\\s]+", "").trim().startsWith("can's")
                    && !originalInput.toLowerCase().replaceAll("[^\\w^\\s]+", "").trim().startsWith("why's")
                    && !originalInput.toLowerCase().replaceAll("[^\\w^\\s]+", "").trim().startsWith("who's")
                    && !originalInput.toLowerCase().replaceAll("[^\\w^\\s]+", "").trim().startsWith("what's")
                    && !originalInput.toLowerCase().replaceAll("[^\\w^\\s]+", "").trim().startsWith("where's")
                    && !originalInput.toLowerCase().replaceAll("[^\\w^\\s]+", "").trim().startsWith("when's")
                    && !originalInput.toLowerCase().replaceAll("[^\\w^\\s]+", "").trim().startsWith("is's")
                    && !originalInput.toLowerCase().replaceAll("[^\\w^\\s]+", "").trim().startsWith("which's")
                    && !originalInput.toLowerCase().replaceAll("[^\\w^\\s]+", "").trim().startsWith("he's")
                    && !originalInput.toLowerCase().replaceAll("[^\\w^\\s]+", "").trim().startsWith("she's")
            )) {
                isUserAskingAQustion = true;
            }
            Map<String, String> replacements = new HashMap<String, String>() {{
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

            String input = originalInput;
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
            if (!WikipediaInfoBoxModel2OldJune14_PERSONAL.statementsFileNameEquals(MainGUI.web)
                    &&!WikipediaInfoBoxModel2OldJune14_PERSONAL.statementsFileNameEquals(MainGUI.local))
                if (isUserAskingAQustion) {
                    writeFile1(output);
                    //writeFile1(originalInput);
                } else
                    writeFile1(output);

            System.out.println("faf3ggh: " + query);
            return WikipediaInfoBoxModel2OldJune14_PERSONAL.start(query, originalInput, false);
            //return query;
        }
        return "HPE Haven Timeout";
    }
    public static void writeFile1(String input) throws IOException {
        try {
            Files.write(Paths.get(WikipediaInfoBoxModel2OldJune14_PERSONAL.statementsFileName),
                    (input+"\n").getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }
}
