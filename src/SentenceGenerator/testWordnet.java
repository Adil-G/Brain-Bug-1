package SentenceGenerator;

import general.LuceneSnowBallTest;
import general.graph.theory.WikipediaInfoBoxModel2OldJune14_PERSONAL_CB;
import org.apache.lucene.wordnet.SynonymMap;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by swaggerton on 30/08/15.
 */
public class testWordnet {
    public static SynonymMap map;
    public static void main(String[] args) throws Exception {
        String[] words = new String[] { "high"};
        SynonymMap map = new SynonymMap(new FileInputStream(new WikipediaInfoBoxModel2OldJune14_PERSONAL_CB().statementsDirectoryName
                +"wn_s.pl"));

        for (int i = 0; i < words.length; i++) {
            String[] synonyms = map.getSynonyms(words[i]);
            System.out.println(words[i] + ":" + java.util.Arrays.asList(synonyms).toString());
        }

    }
    public static void synonymsInit() throws IOException {
        map = new SynonymMap(new FileInputStream(new WikipediaInfoBoxModel2OldJune14_PERSONAL_CB().statementsDirectoryName
                +"wn_s.pl"));

    }
    public static HashSet<String> getSynonyms(String rootWord) throws IOException {
        if(map==null)
            synonymsInit();
        String[] syns = map.getSynonyms(rootWord);
        String[] rootSyns = new String[syns.length];
        for(int i=0;i<syns.length;i++)
            rootSyns[i] = LuceneSnowBallTest.getStem(syns[i]);
        return new HashSet<>(Arrays.asList(rootSyns));
        //return new HashSet<>(Arrays.asList(new String[]{rootWord}));
    }
    public static HashMap<String, HashSet<String>> getSynonymMap(String query) throws IOException {
        query = query.toLowerCase();
        HashMap<String,HashSet<String>> synMap = new HashMap<>();

        for(String word : query.split("[^\\w]+"))
            synMap.put(LuceneSnowBallTest.getStem(word),getSynonyms(word));
        return synMap;
    }
}
