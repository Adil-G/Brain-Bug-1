package general.graph.theory;

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
}
