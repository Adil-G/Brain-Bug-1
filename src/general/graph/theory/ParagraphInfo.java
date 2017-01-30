package general.graph.theory;

/**
 * Created by corpi on 2016-08-21.
 */
public class ParagraphInfo {
    private String text;
    private String publication;
    private String pageNumber;
    public ParagraphInfo(String text,String publication, String pageNumber)
    {
        this.text = text;
        this.publication = publication;
        this.pageNumber=  pageNumber;
    }
    public String getText()
    {
        return this.text;
    }
    public String getPub()
    {
        return this.publication.replaceAll("asds2f9fs_","");
        //return " ( " + this.publication + " )\n\n";
    }
    public String getPageNumber()
    {
        return this.pageNumber;
    }
    public String getInfo()
    {
        return "\n"+this.getText()
               // + "(" + this.getPub() +","+this.getPageNumber()+")";
                + "(" + this.getPub() + ")";
    }
    public String getBlog()
    {
        if(this.getPub().toLowerCase().contains("wikipedia"))
            return "\n"+this.getText();
        else
            return "\n"+this.getText()
                    // + "(" + this.getPub() +","+this.getPageNumber()+")";
                    + " (" + "<a href=\""+this.getPub()+"\">"+this.getPub()+"</a>" + ")";
    }
    public String toString()
    {
        return  this.getInfo();
    }
}
