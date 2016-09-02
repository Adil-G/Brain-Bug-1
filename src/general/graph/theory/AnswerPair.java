package general.graph.theory;

/**
 * Created by corpi on 2016-06-19.
 */
public class AnswerPair {
    private String URL;
    private String text;
    public AnswerPair(String URL, String text)
    {
        this.URL = URL;
        this.text = text;
    }
    public String getURL()
    {
        return this.URL;
    }
    public  String getText()
    {
        return  this.text;
    }
}
