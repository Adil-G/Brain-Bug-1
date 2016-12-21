package general.graph.theory;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by corpi on 2016-09-16.
 */
public class Message {
    public static final String FOUND = "answers";
    public static final String POSSIBLE = "suggestions";
    public static final String IMPOSSIBLE = "no possible answer";
    private String message;
    private ArrayList<ParagraphInfo> answers;
    public Message(String message, ArrayList<ParagraphInfo> answers)
    {
        this.message = message;
        this.answers = answers;
    }
    public String getMessage()
    {
        return this.message;
    }
    public ArrayList<ParagraphInfo> getAnswers()
    {
        return this.answers;
    }

}
