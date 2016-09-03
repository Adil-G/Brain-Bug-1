package general.chat;

import general.TestChatBotMain;
import general.graph.theory.WikipediaInfoBoxModel2OldJune14_PERSONAL;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;

import static general.chat.MainGUI.local;

/**
 * Created by corpi on 2016-09-03.
 */
public class TestChatBot {
    public static void main(String[] args) throws Exception {
        // hard coded
        File file = new File("D:\\permutations-june-19-2-aug-25\\permutations\\openNLP\\local_docs\\localData.txt");
        // user input
        String question  =  new java.util.Scanner(System.in).nextLine();

        System.out.println(getAnswer(file,question));
    }
    private static String getAnswer(File file,String question) throws Exception {
        String answer = "No Result.";
        if(file!=null) {
            // Set up environment and DATA
            MainGUI.useNewData = false;
            String name = file.getAbsolutePath().substring(
                    file.getAbsolutePath().lastIndexOf('/') + 1);
            String directory = file.getAbsolutePath().substring(
                    0, file.getAbsolutePath().lastIndexOf('/') + 1);
            WikipediaInfoBoxModel2OldJune14_PERSONAL.dataDirectoryName = directory;
            local = name;
            WikipediaInfoBoxModel2OldJune14_PERSONAL.changeStatementsFileName(local);

            // Give Q and A
            PrintStream dummyStream    = new PrintStream(new OutputStream(){
                public void write(int b) {
                    //NO-OP
                }
            });
            System.setOut(dummyStream);
            //String question  =  new java.util.Scanner(System.in).nextLine();
            answer = TestChatBotMain.runChatbot(question);

            System.setOut(MainGUI.originalStream);

        }
        return answer;
    }
}
