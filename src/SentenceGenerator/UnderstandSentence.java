package SentenceGenerator;

import java.util.List;
import java.util.Map;

/**
 * Created by adil on 14/07/15.
 */
public class UnderstandSentence {
    Map<Integer,String[]> mSentenceOrder;
    List<String> mRelevantPOS;
    public UnderstandSentence(Map<Integer,String[]> sentenceOrder,List<String> relevantPOS)
    {
        mSentenceOrder = sentenceOrder;
        mRelevantPOS = relevantPOS;
    }
    public Map<Integer,String[]> getSentenceOrder()
    {
        return mSentenceOrder;
    }
    public List<String>  getRelevantPOS()
    {
        return mRelevantPOS;
    }
}
