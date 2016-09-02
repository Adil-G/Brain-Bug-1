import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by swaggerton on 17/08/15.
 */
public class Test2 {
    public static String gogogo(Set<String> myHashSet)
    {

        String randEl = null;
        int size = myHashSet.size();
        int item = new Random().nextInt(size); // In real life, the Random object should be rather more shared than this
        int i = 0;
        for(String obj : myHashSet)
        {
            if (i == item) {
                randEl = obj;
            }
            i = i + 1;
        }
        return randEl;

    }
    static public void main(String[] argv) throws IOException{

    }
    public static String[] go(Set<String> list) throws IOException {
        String bestString = null;
        String a = null;
        String[] pair = new String[]{a,null};
        if(list.size()>0) {
            a = gogogo(list);
        }
        if(a!=null) {
            pair = new String[]{a, null};
            double highest = 0;
            for (String x : list) {
                double relationship = new ComparePhrases().compare(a, x);
                if (relationship >= highest) {
                    bestString = x;
                    pair = new String[]{a, x};
                    highest = relationship;
                }
            }
        }
        return pair;
    }
}
