package general.graph.theory;

/**
 * Created by corpi on 2016-08-21.
 */
public class ParagraphInfo {
    private String text;
    private String publication;
    public ParagraphInfo(String text,String publication)
    {
        this.text = text;
        this.publication = publication;
    }
    public String getText()
    {
        return this.text;
    }
    public String getPub()
    {
        return new String();
        //return " ( " + this.publication + " )\n\n";
    }
}
