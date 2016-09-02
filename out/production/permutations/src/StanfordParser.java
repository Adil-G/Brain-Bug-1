/*import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;*/

import SentenceGenerator.UnderstandSentence;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.LabeledWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

//import sun.security.pkcs11.wrapper.Functions;

/**
 * Created by Adil on 2015-06-05.
 */
public class StanfordParser {

    public static ArrayList<ArrayList<String>> findNoun3_DMiningSearch(String input)
    {
        //get an object of list<Tree> type from input
        //String[] sent = input.split("^([A-Za-z])");
        String[] sent = input.split("\\s+");
        for (int i = 0; i < sent.length; i++) {
            // You may want to check for a non-word character before blindly
            // performing a replacement
            // It may also be necessary to adjust the character class
            sent[i] = sent[i].replaceAll("[^\\w]", "");
            //System.out.println("Sentene: "+sent[i].toString());
        }

        List<HasWord> words = new ArrayList<HasWord>();
        for(String i :sent)
        {
            //System.out.println(i);
            if(!(i.contains(".")||i.contains("?")||i.contains(",")))
            {
                HasWord word = new Word();
                word.setWord(i);
                words.add(word);
            }
        }
        //System.out.println(words);
        LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
        lp.setOptionFlags(new String[]{"-maxLength", "80", "-retainTmpSubcategories"});
        Tree parse = (Tree) lp.apply(words);
        // Create multidimentional array of keywords in text with catagorization in parts of speech.
        ArrayList<ArrayList<String>> keyWords = new ArrayList<ArrayList<String>>();
        ArrayList<String> h = new ArrayList<String>();
        //System.out.println("this is the ticket: "+getNounPhrases(parse,"JJ").toString());
        //fill in keyWords list
        keyWords.add(h);
        keyWords.add(h);
        keyWords.add(h);
        keyWords.add(h);
        h.addAll(getNounPhrases(parse,"JJ"));
        h.addAll(getNounPhrases(parse,"JJR"));
        h.addAll(getNounPhrases(parse,"JJS"));
        keyWords.get(0).addAll(h);
        h.clear();
        h.addAll(getNounPhrases(parse,"VB"));
        h.addAll(getNounPhrases(parse,"RBR"));
        h.addAll(getNounPhrases(parse,"RBS"));
        h.addAll(getNounPhrases(parse,"VBD"));
        h.addAll(getNounPhrases(parse,"VBG"));
        h.addAll(getNounPhrases(parse,"VBN"));
        h.addAll(getNounPhrases(parse,"VBP"));
        keyWords.get(1).addAll(h);
        h.clear();
        h.addAll(getNounPhrases(parse,"NN"));
        h.addAll(getNounPhrases(parse,"NNS"));
        h.addAll(getNounPhrases(parse,"NNP"));
        h.addAll(getNounPhrases(parse,"NNPS"));
        keyWords.get(2).addAll(h);
        h.clear();

        return keyWords;
    }
    public static UnderstandSentence findNoun3(String input)
    {
        Map<Integer,String[]> sentenceOrder = new TreeMap<Integer,String[]>();
        List<String> relevantPOS = new ArrayList<String>();
        //get an object of list<Tree> type from input
        //String[] sent = input.split("^([A-Za-z])");
        String[] sent = input.split("\\s+");
        for (int i = 0; i < sent.length; i++) {
            // You may want to check for a non-word character before blindly
            // performing a replacement
            // It may also be necessary to adjust the character class
            sent[i] = sent[i].replaceAll("[^\\w]", "");
            //System.out.println("Sentene: "+sent[i].toString());
        }
        List<String> dataBasket = Arrays.asList(sent);
        List<HasWord> words = new ArrayList<HasWord>();
        for(String i :sent)
        {
            //System.out.println(i);
            if(!(i.contains(".")||i.contains("?")||i.contains(",")))
            {
                HasWord word = new Word();
                word.setWord(i);
                words.add(word);
            }
        }
        //System.out.println(words);
        LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
        lp.setOptionFlags(new String[]{"-maxLength", "80", "-retainTmpSubcategories"});
        Tree parse = (Tree) lp.apply(words);

        String[] typesOfParts = new String[]{
                "CC","CD","DT","EX","FW","IN","JJ","JJR","JJS","LS","MD","NN","NNP","NNPS","NNS","PDT",
                "POS","PRP","RB","RBR","RBS","RP","SYM","TO","UH","VB","VBD","VBG","VBN","VBP",
                "VBZ","WDT","WP","WRB"
        };//"PRP$","WP$"
        for(String part:typesOfParts) {
            List<String> h = new ArrayList<String>();
            h.addAll(getNounPhrases(parse, part));
            for (String word : h) {
                if (dataBasket.contains(word)) {
                    sentenceOrder.put(dataBasket.indexOf(word), new String[]{part,word});
                }
                relevantPOS.add(part);
            }
        }
        // SENTENCE_ORDER VARIABLE IS THE ONLY VARIABLE THAT IS IMPROTANT HERE
        return new UnderstandSentence(sentenceOrder, relevantPOS);
    }
    public static List<String> findNoun2(String input)
    {

        //get an object of list<Tree> type from input
        //String[] sent = input.split("^([A-Za-z])");
        String[] sent = input.split("\\s+");
        for (int i = 0; i < sent.length; i++) {
            // You may want to check for a non-word character before blindly
            // performing a replacement
            // It may also be necessary to adjust the character class
            sent[i] = sent[i].replaceAll("[^\\w]", "");
            //System.out.println("Sentene: "+sent[i].toString());
        }

        List<HasWord> words = new ArrayList<HasWord>();
        for(String i :sent)
        {
            //System.out.println(i);
            if(!(i.contains(".")||i.contains("?")||i.contains(",")))
            {
                HasWord word = new Word();
                word.setWord(i);
                words.add(word);
            }
        }
        //System.out.println(words);
        LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
        lp.setOptionFlags(new String[]{"-maxLength", "80", "-retainTmpSubcategories"});
        Tree parse = (Tree) lp.apply(words);

        List<String> h = new ArrayList<String>();

        h = getNounPhrases(parse,"NN");
        //h.addAll(getNounPhrases(parse,"JJ"));
        //h.addAll(getNounPhrases(parse,"JJR"));
        //h.addAll(getNounPhrases(parse,"JJS"));
        h.addAll(getNounPhrases(parse,"VB"));
        //h.addAll(getNounPhrases(parse,"RBR"));
        //h.addAll(getNounPhrases(parse,"RBS"));
        h.addAll(getNounPhrases(parse,"VBD"));
        h.addAll(getNounPhrases(parse,"VBG"));
        h.addAll(getNounPhrases(parse,"VBN"));
        h.addAll(getNounPhrases(parse,"VBP"));
        h.addAll(getNounPhrases(parse,"NNS"));
        h.addAll(getNounPhrases(parse,"NNP"));
        h.addAll(getNounPhrases(parse,"NNPS"));
       // h.addAll(getNounPhrases(parse,"S"));

        //NNS,NNP,NNPS
        //System.out.println(h.toString());*/
        return h;
    }

    private static List<String> getNounPhrases(Tree parse, String type) {
        List<String> result = new ArrayList<>();
        TregexPattern pattern = TregexPattern.compile(type);
        if(parse!=null)
        {
            TregexMatcher matcher = pattern.matcher(parse);
            while (matcher.find()) {
                //System.out.println("Tree NOT null!");
                Tree match = matcher.getMatch();
                List<Tree> leaves = match.getLeaves();
                //System.out.println(leaves);
                // Some Guava magic.
                String nounPhrase = Joiner.on(" ").join(Lists.transform(leaves, com.google.common.base.Functions.toStringFunction()));
                result.add(nounPhrase);
                List<LabeledWord> labeledYield = match.labeledYield();
                System.out.println("labeledYield: " + labeledYield);
            }
        }
        else
        {

        }
        System.out.println("result= "+result);
        return result;
    }

}
