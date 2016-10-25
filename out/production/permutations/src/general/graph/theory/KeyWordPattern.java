package general.graph.theory;

import general.chat.MainGUI;

import javax.swing.event.MouseInputAdapter;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Created by corpi on 2016-10-20.
 */
public class KeyWordPattern {
    private String[] keyWords;
    private Pattern pattern1,pattern2,pattern3;
    public KeyWordPattern(String[] keyWords)
    {
        this.keyWords = keyWords;
        this.pattern1 = Pattern.compile("\\b"+keyWords[0]);
        this.pattern2 = Pattern.compile("\\b"+keyWords[1]);
        this.pattern3 = Pattern.compile("\\b"+keyWords[1]+"\\b");
    }
    public String[] getKeyWords()
    {
        return this.keyWords;
    }
    public Pattern getPattern1()
    {
        return this.pattern1;
    }
    public Pattern getPattern2()
    {
        return this.pattern2;
    }
    public Pattern getPattern3()
    {
        return this.pattern3;
    }
    @Override
    public String toString()
    {
        String stringPrint = "";

        stringPrint += " | "+ Arrays.toString(this.getKeyWords());

        return stringPrint;
    }
    @Override
    public int hashCode() {
        return this.getKeyWords()[1].hashCode();
    }
    @Override
    public boolean equals(Object obj) {
        if (this.getClass() == obj.getClass()){


            KeyWordPattern other =(KeyWordPattern)obj;
            if(this.keyWords[1].equals(((KeyWordPattern) obj).getKeyWords()[1])){
                //System.setOut(MainGUI.originalStream);
                //System.out.println("989u8r true");
                return true;
            }
        }
        //System.setOut(MainGUI.originalStream);
        //System.out.println("989u8r false");
        return false;
    }
    /*
    @Override
    public int compareTo(Object anotherKeyWordPattern) {
        if (!(anotherKeyWordPattern instanceof KeyWordPattern))
            throw new ClassCastException("A KeyWordPattern object expected.");
        return this.keyWords[1].compareTo(((KeyWordPattern) anotherKeyWordPattern).getKeyWords()[1]);
    }
    */
}
