package general.chat;

import general.TestChatBotMain;
import general.graph.theory.ParagraphInfo;
import general.graph.theory.WikipediaInfoBoxModel2OldJune14_PERSONAL_CB;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static general.chat.MainGUI.local;
import static general.chat.MainGUI.useNewData;

/**
 * Created by corpi on 2016-09-03.
 */
public class TestChatBot {
    private static Enum SMALL, BIG;
    public static Enum SUM, NO_SUM;
    public static void main(String[] args) throws Exception {
        /*
        File file = new File("D:\\permutations-june-19-2-aug-25\\permutations\\openNLP\\local_docs\\pakalupapito\\local.txts");
        WikipediaInfoBoxModel2OldJune14_PERSONAL_CB.DELIMITER = "<92j8q9g93sajd9f8jqa9pf8j>";
        // user input
        //String question  =  "what is the canny edge detection";

        String contents = new Summerize().sum("");
        ArrayList<String> topics = new ArrayList<>();
        for(String idea : contents.split("[\\.\\?!]+")) {
            String question = idea;
            String answer = TestChatBot.getAnswerBlog(question, false, NO_SUM);

            System.out.println(answer);
            topics.add(answer);
        }
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        for(String sentence : topics)
        {
            System.out.println(sentence+"\n\n");
        }*/
    }
    public ParagraphInfo getAnswerWithGUI(String question, boolean isDeep) throws Exception {
        // hard coded
        /*File file = new File("D:\\permutations-june-19-2-aug-25\\permutations\\openNLP\\local_docs\\bell\\local.txt");

        //File file = new File("D:\\permutations-june-19-2-aug-25\\permutations\\openNLP\\local_docs\\donald_trump_tweets\\local.txt");
        WikipediaInfoBoxModel2OldJune14_PERSONAL_CB.DELIMITER = "<92j8q9g93sajd9f8jqa9pf8j>";//File file = new File("D:\\permutations-june-19-2-aug-25\\permutations\\openNLP\\local_docs\\pakalupapito\\local.txt");
        WikipediaInfoBoxModel2OldJune14_PERSONAL_CB.DELIMITER = "<92j8q9g93sajd9f8jqa9pf8j>";//File file = new File("D:\\permutations-june-19-2-aug-25\\permutations\\openNLP\\local_docs\\pakalupapito\\local.txt");
*/
        //File file = new File("D:\\permutations-june-19-2-aug-25\\permutations\\openNLP\\local_docs\\local.txt");
          // File file = new File("..\\textbooks\\2021\\a.txt");
        File file = new File("..\\textbooks\\test\\a.txt");
        System.setOut(MainGUI.originalStream);
        ArrayList<UrlFileConnector> sentences =  new DMiningGoogleOnlyChunksUnordered().excecute(question,"");
        /*File newFile = new File("..\\textbooks\\test\\info.txt");
        if(!newFile.exists())
            Files.createFile(newFile.toPath());

        System.out.println("size = "+sentences.size());
        writeFile1(sentences, newFile);
        */
         //File file = new File("D:\\permutations-june-19-2-aug-25\\permutations\\openNLP\\local_docs\\env1000\\local.txt");


        //File file = new File("D:\\permutations-june-19-2-aug-25\\permutations\\openNLP\\local_docs\\elepahnts\\local.txt");

        // user inpu

        //System.out.println();
        ArrayList<ParagraphInfo> finalAnswer = getAnswer(new HashSet<>(),"",file,question,"openNLP\\", false||isDeep, NO_SUM,sentences);

        String allCorrectMatches = "";
        for(ParagraphInfo lead : finalAnswer)
        {
            allCorrectMatches += lead.getText().trim();
        }
        ParagraphInfo best = new ParagraphInfo("","","");
        for(ParagraphInfo lead : finalAnswer)
        {
            String quote = lead.getText().trim();
            if(quote.equals(allCorrectMatches))
            {
                best = lead;
            }
        }
        return best;
    }
    public ParagraphInfo matchLength(ArrayList<ParagraphInfo> answers, String question, Enum size, Enum summaryMode)
    {
        String closestLengthMatch = "";
        if(size == BIG) {
            String repeatThis = "hi";
            for(int i =0;i< 1000;i++)
            {
                closestLengthMatch += repeatThis;
            }
        }
        for(ParagraphInfo lead : answers)
        {
            String analyzeLead = lead.getText().trim();

            System.out.println(lead.getInfo());
            if(summaryMode == NO_SUM&&(analyzeLead.contains(question)||question.contains(analyzeLead)))
                continue;

            int currentClosest = Math.abs(closestLengthMatch.length() -question.length());
            int candidate = Math.abs(analyzeLead.length() -question.length());
            if(candidate < currentClosest )
            {
                closestLengthMatch = analyzeLead;
            }
        }
        ParagraphInfo best = new ParagraphInfo("","","");
        for(ParagraphInfo lead : answers)
        {
            String quote = lead.getText().trim();
            if(quote.equals(closestLengthMatch))
            {
                best = lead;
            }
        }
        return best;
    }
    public ParagraphInfo getAnswerBlog(ArrayList<UrlFileConnector> sentences, String originalURL,HashSet<String> usedURLs,String contents, String question, boolean isDeep, Enum isSummary) throws Exception {
        File file = new File("..\\textbooks\\test\\a.txt");
        System.setOut(MainGUI.originalStream);

        /*File newFile = new File("..\\textbooks\\test\\info.txt");
        if(!newFile.exists())
            Files.createFile(newFile.toPath());

        System.out.println("size = "+sentences.size());
        writeFile1(sentences, newFile);
        */
        ArrayList<ParagraphInfo> finalAnswer = getAnswer(usedURLs,contents,file,question,"openNLP\\", false||isDeep, NO_SUM, sentences);

        ParagraphInfo correctMatch = matchLength(finalAnswer, question, SMALL, isSummary);

        if(correctMatch.getText().isEmpty())
            correctMatch = matchLength(finalAnswer, question, BIG,isSummary);
        return correctMatch;
    }
    public ArrayList<ParagraphInfo> getAnswer(HashSet<String> usedURLs,String contents,File file, String question, String openNLPDir, boolean isDeep, Enum isSummary, ArrayList<UrlFileConnector> ufc) throws Exception {
        ArrayList<ParagraphInfo> answer = new ArrayList<>();
        if(file!=null) {
            // Set up environment and DATA
            MainGUI.useNewData = false;
            String name = file.getAbsolutePath().substring(
                    file.getAbsolutePath().lastIndexOf('/') + 1);
            String directory = file.getAbsolutePath().substring(
                    0, file.getAbsolutePath().lastIndexOf('/') + 1);
           // WikipediaInfoBoxModel2OldJune14_PERSONAL_CB.dataDirectoryName = directory;
            local = name;
           // WikipediaInfoBoxModel2OldJune14_PERSONAL_CB.changeStatementsFileName(directory,name,openNLPDir);

            // Give Q and A
            PrintStream dummyStream    = new PrintStream(new OutputStream(){
                public void write(int b) {
                    //NO-OP
                }
            });
            System.setOut(dummyStream);
            //String question  =  new java.util.Scanner(System.in).nextLine();
            answer = new TestChatBotMain().runChatbot(usedURLs, contents, question, isDeep, isSummary,ufc);


                        System.setOut(MainGUI.originalStream);

        }
        return answer;
    }
}
