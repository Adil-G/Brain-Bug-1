package general.chat;

import general.TestChatBotMain;
import general.graph.theory.WikipediaInfoBoxModel2OldJune14_PERSONAL_CB;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static general.chat.MainGUI.local;

/**
 * Created by corpi on 2016-09-03.
 */
public class TestChatBot {
    public static void main(String[] args) throws Exception {
        // hard coded
        File file = new File("D:\\permutations-june-19-2-aug-25\\permutations\\openNLP\\local_docs\\pakalupapito\\local.txts");
        WikipediaInfoBoxModel2OldJune14_PERSONAL_CB.DELIMITER = "<92j8q9g93sajd9f8jqa9pf8j>";
        // user input
        //String question  =  "what is the canny edge detection";
        String question  =  (new Scanner(System.in)).nextLine();

        System.out.println(getAnswer(file,question,"openNLP\\", false));
    }
    public static String getAnswerWithGUI(String question,boolean isDeep) throws Exception {
        // hard coded
        /*File file = new File("D:\\permutations-june-19-2-aug-25\\permutations\\openNLP\\local_docs\\bell\\local.txt");

        //File file = new File("D:\\permutations-june-19-2-aug-25\\permutations\\openNLP\\local_docs\\donald_trump_tweets\\local.txt");
        WikipediaInfoBoxModel2OldJune14_PERSONAL_CB.DELIMITER = "<92j8q9g93sajd9f8jqa9pf8j>";//File file = new File("D:\\permutations-june-19-2-aug-25\\permutations\\openNLP\\local_docs\\pakalupapito\\local.txt");
        WikipediaInfoBoxModel2OldJune14_PERSONAL_CB.DELIMITER = "<92j8q9g93sajd9f8jqa9pf8j>";//File file = new File("D:\\permutations-june-19-2-aug-25\\permutations\\openNLP\\local_docs\\pakalupapito\\local.txt");
*/
        //File file = new File("D:\\permutations-june-19-2-aug-25\\permutations\\openNLP\\local_docs\\local.txt");
          // File file = new File("..\\textbooks\\2021\\a.txt");
        File file = new File("..\\textfiles_1400\\local.txt");
         //File file = new File("D:\\permutations-june-19-2-aug-25\\permutations\\openNLP\\local_docs\\env1000\\local.txt");


        //File file = new File("D:\\permutations-june-19-2-aug-25\\permutations\\openNLP\\local_docs\\elepahnts\\local.txt");

        // user input

        //System.out.println();
        return getAnswer(file,question,"openNLP\\", true||isDeep);
    }
    public static String getAnswer(File file,String question,String openNLPDir, boolean isDeep) throws Exception {
        String answer = "No Result.";
        if(file!=null) {
            // Set up environment and DATA
            MainGUI.useNewData = false;
            String name = file.getAbsolutePath().substring(
                    file.getAbsolutePath().lastIndexOf('/') + 1);
            String directory = file.getAbsolutePath().substring(
                    0, file.getAbsolutePath().lastIndexOf('/') + 1);
            WikipediaInfoBoxModel2OldJune14_PERSONAL_CB.dataDirectoryName = directory;
            local = name;
            WikipediaInfoBoxModel2OldJune14_PERSONAL_CB.changeStatementsFileName(directory,name,openNLPDir);

            // Give Q and A
            PrintStream dummyStream    = new PrintStream(new OutputStream(){
                public void write(int b) {
                    //NO-OP
                }
            });
            System.setOut(dummyStream);
            //String question  =  new java.util.Scanner(System.in).nextLine();
            answer = TestChatBotMain.runChatbot(question, isDeep);

            System.setOut(MainGUI.originalStream);

        }
        return answer;
    }
}
