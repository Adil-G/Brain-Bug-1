package general;

import SentenceGenerator.ComparePhrases;
import com.knowledgebooks.nlp.ExtractNames;
import com.knowledgebooks.nlp.util.ScoredList;
import general.chat.Thesaurus;
import general.graph.theory.KeyWordPattern;
import general.graph.theory.WikipediaInfoBoxModel2OldJune14_PERSONAL_CB;
import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Pattern;

/**
 * Created by corpi on 2016-05-01.
 */
public class FindKeyWordsTest {
    public static HashMap<String, HashSet<KeyWordPattern>> getSynonyms(HashSet<KeyWordPattern> keywords)
    {
        HashMap<String, HashSet<KeyWordPattern>> synonymMap = new HashMap<>();
        for(KeyWordPattern keys : keywords)
        {
            String word = keys.getKeyWords()[1];
            HashSet<String> syns = Thesaurus.getSynonyms(word);
            syns.add(word);
            HashSet<KeyWordPattern> copy = new HashSet<>();
            for (String keyword : syns) {
                // find the root word
                String rootWord = LuceneSnowBallTest.getStem(keyword);
                // Check if root word of key word is an ACTUAL WORD
                if (rootWord.length() > 2 && !(rootWord.isEmpty())&&!(keyword.isEmpty())) {//
                    // add the root word
                    copy.add(new KeyWordPattern(new String[]{rootWord.toLowerCase(),keyword.toLowerCase()}));
                }
                else if (!(keyword.isEmpty())){//
                    // no, add the key word
                    copy.add(new KeyWordPattern(new String[]{keyword.toLowerCase(),keyword.toLowerCase()}));
                }
            }
            synonymMap.put(word, copy);
        }
        return synonymMap;
    }
    public static ArrayList<String> findName(String input) throws IOException {
        InputStream is = new FileInputStream(
                WikipediaInfoBoxModel2OldJune14_PERSONAL_CB.statementsDirectoryName+"en-ner-person.bin");

        TokenNameFinderModel model = new TokenNameFinderModel(is);
        is.close();

        NameFinderME nameFinder = new NameFinderME(model);

        String []sentence = input.split("[^\\w]+");

        Span nameSpans[] = nameFinder.find(sentence);

        ArrayList<String> names  = new ArrayList<>();

        for(Span s: nameSpans) {
            //System.out.println(s.toString());
            String name = "";
            for(int i = s.getStart(); i< s.getEnd(); i++)
                name += sentence[i] + " ";
            names.add(name.trim());
        }
        return names;
    }
    public static ArrayList<String> getPlaces(String input)
    {
        PrintStream c = System.out;
        ExtractNames extractNames = new ExtractNames();
        // initialize everything, before printing any output - trying to see what is taking so long!
        ScoredList[] ret = extractNames.getProperNames(input);
        System.out.println("Place names: " + ret[1].getValuesAsString());
        if (ret[1].getValuesAsString().isEmpty()) {
            c.println("PLACE NOT FOUND");
            return new ArrayList<String>();
        }
        String place =ret[1].getValuesAsString().substring(0, ret[1].getValuesAsString().lastIndexOf(':'));// ret[1].getValuesAsString().substring(0, ret[1].getValuesAsString().indexOf(':'));
        String[] places = place.split(":\\d,\\s?");
        return new ArrayList<String>(Arrays.asList(places));
    }
    public static void main(String[] args) throws IOException {
        System.out.println(findName("who is Hillary"));
    }
    public static ArrayList<String> POSTag2(String input) throws IOException {
        /*POSModel model = new POSModelLoader()
                .load(new File(WikipediaInfoBoxModel2OldJune14_PERSONAL.statementsDirectoryName+"en-pos-maxent.bin"));
                */
        POSModel model = new POSModel(new FileInputStream(WikipediaInfoBoxModel2OldJune14_PERSONAL_CB.statementsDirectoryName+"en-pos-maxent.bin")
        );
        PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
        POSTaggerME tagger = new POSTaggerME(model);

        //String input = "Hi. How are you? This is Mike.";
        ObjectStream<String> lineStream = new PlainTextByLineStream(
                new StringReader(input));
        String samp = "";
        perfMon.start();
        String line;
        while ((line = lineStream.read()) != null) {

            String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE
                    .tokenize(line);
            String[] tags = tagger.tag(whitespaceTokenizerLine);

            POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
            System.out.println(sample.toString());
            samp = sample.toString();

            perfMon.incrementCounter();
        }
        perfMon.stopAndPrintFinalResult();
        return new ArrayList<String>(Arrays.asList(samp.split("\\s+")));
    }
    public static String getPropernoun(String raw) throws IOException {

        ArrayList<String> rawParsed = POSTag2(raw);
        ArrayList<String> noun = new ArrayList<>();
        ArrayList<String> adjective = new ArrayList<>();
        ArrayList<String> nnp = new ArrayList<>();
        ArrayList<String> properNoun = new ArrayList<>();
        ArrayList<String> adverb = new ArrayList<>();
        ArrayList<String> verb = new ArrayList<>();
        for(String word : rawParsed)
        {
            String tag = word.substring(word.indexOf('_')).toLowerCase();
            String partOfSpeech = word.substring(0,word.indexOf('_'));
            if(tag.contains("n") && !tag.contains("p")&& !tag.contains("i"))
            {
                // this is a regular noun
                noun.add(partOfSpeech);
                System.out.println("regular noun = " + partOfSpeech);
            }
            else if (tag.contains("n") && tag.contains("p"))
            {
                // this is a proper noun WITH THE NAME ID e.g. Japan
                nnp.add(partOfSpeech);
                System.out.println("proper noun = " + partOfSpeech);
            }
            else if(!tag.contains("n") && tag.contains("prp"))
            {
                // this is a proper noun WITHOUT ID e.g. I, She, He
                properNoun.add(partOfSpeech);
                System.out.println("proper noun = " + partOfSpeech);
            }
            else if(tag.contains("rb"))
            {
                // This is an ADVERB
                adverb.add(partOfSpeech);
                System.out.println("adverb = " + partOfSpeech);
            }
            else if(tag.contains("jj"))
            {
                // This is an Adjective
                adjective.add(partOfSpeech);
                System.out.println("adjective = " + partOfSpeech);
            }
            else if(tag.contains("vb"))
            {
                // This is an VERB
                verb.add(partOfSpeech);
                //System.out.println("verb = " + partOfSpeech);
            }
        }
        //ArrayList<String> names = findName(raw);
        //ArrayList<String> places = getPlaces(raw);
        String nounsText = "";
        String name = "";
        for(String word : nnp)
            name += word + " ";
        return name;
    }
    public static String getVerbs(String raw) throws IOException {

        ArrayList<String> rawParsed = POSTag2(raw);
        ArrayList<String> noun = new ArrayList<>();
        ArrayList<String> adjective = new ArrayList<>();
        ArrayList<String> nnp = new ArrayList<>();
        ArrayList<String> properNoun = new ArrayList<>();
        ArrayList<String> adverb = new ArrayList<>();
        ArrayList<String> verb = new ArrayList<>();
        for(String word : rawParsed)
        {
            String tag = word.substring(word.indexOf('_')).toLowerCase();
            String partOfSpeech = word.substring(0,word.indexOf('_'));
            if(tag.contains("n") && !tag.contains("p")&& !tag.contains("i"))
            {
                // this is a regular noun
                noun.add(partOfSpeech);
                System.out.println("regular noun = " + partOfSpeech);
            }
            else if (tag.contains("n") && tag.contains("p"))
            {
                // this is a proper noun WITH THE NAME ID e.g. Japan
                nnp.add(partOfSpeech);
                System.out.println("proper noun = " + partOfSpeech);
            }
            else if(!tag.contains("n") && tag.contains("prp"))
            {
                // this is a proper noun WITHOUT ID e.g. I, She, He
                properNoun.add(partOfSpeech);
                System.out.println("proper noun = " + partOfSpeech);
            }
            else if(tag.contains("rb"))
            {
                // This is an ADVERB
                adverb.add(partOfSpeech);
                System.out.println("adverb = " + partOfSpeech);
            }
            else if(tag.contains("jj"))
            {
                // This is an Adjective
                adjective.add(partOfSpeech);
                System.out.println("adjective = " + partOfSpeech);
            }
            else if(tag.contains("vb"))
            {
                // This is an VERB
                verb.add(partOfSpeech);
                System.out.println("verb = " + partOfSpeech);
            }
        }
        //ArrayList<String> names = findName(raw);
        //ArrayList<String> places = getPlaces(raw);
        String nounsText = "";
        String name = "";
        for(String word : verb)
            name += word + " ";
        for(String word : adjective)
            name += word + " ";
        for(String word : adverb)
            name += word + " ";
        return name;
    }
    public static String getNonNouns(String raw) throws IOException {
                String name = "";
                ArrayList<String> rawParsed = POSTag2(raw);
                ArrayList<String> noun = new ArrayList<>();
                ArrayList<String> adjective = new ArrayList<>();
                ArrayList<String> nnp = new ArrayList<>();
                ArrayList<String> properNoun = new ArrayList<>();
                ArrayList<String> adverb = new ArrayList<>();
                ArrayList<String> verb = new ArrayList<>();
                for(String word : rawParsed)
                {
                    String tag = word.substring(word.indexOf('_')).toLowerCase();
                    String partOfSpeech = word.substring(0,word.indexOf('_'));
                    if(false &&tag.contains("n") && !tag.contains("p")&& !tag.contains("i"))
                    {
                        // this is a regular noun
                        noun.add(partOfSpeech);
                System.out.println("regular noun = " + partOfSpeech);
            }
            else if (tag.contains("n") && tag.contains("p"))
            {
                // this is a proper noun WITH THE NAME ID e.g. Japan
                nnp.add(partOfSpeech);
                System.out.println("proper noun = " + partOfSpeech);
            }
            else if(!tag.contains("n") && tag.contains("prp"))
            {
                // this is a proper noun WITHOUT ID e.g. I, She, He
                properNoun.add(partOfSpeech);
                System.out.println("proper noun = " + partOfSpeech);
            }
            else
            {
                name += word.replaceAll("_[A-Z]+$","") + " ";
                System.out.println("86786: " + name);
            }
        }

        return name.trim();
    }
    public static String getMixedPOS(String raw) throws IOException {

        ArrayList<String> rawParsed = POSTag2(raw);
        ArrayList<String> noun = new ArrayList<>();
        ArrayList<String> adjective = new ArrayList<>();
        ArrayList<String> nnp = new ArrayList<>();
        ArrayList<String> properNoun = new ArrayList<>();
        ArrayList<String> adverb = new ArrayList<>();
        ArrayList<String> verb = new ArrayList<>();
        ArrayList<String> numbers = new ArrayList<>();
        for(String word : rawParsed)
        {
            String tag = word.substring(word.indexOf('_')).toLowerCase();
            String partOfSpeech = word.substring(0,word.indexOf('_'));
            if(tag.contains("cd"))
            {
                // this is a regular number
                numbers.add(partOfSpeech);
                System.out.println("number = " + partOfSpeech);
            }
            if(tag.contains("n") && !tag.contains("p")&& !tag.contains("i"))
            {
                // this is a regular noun
                noun.add(partOfSpeech);
                System.out.println("regular noun = " + partOfSpeech);
            }
            else if (tag.contains("n") && tag.contains("p"))
            {
                // this is a proper noun WITH THE NAME ID e.g. Japan
                nnp.add(partOfSpeech);
                System.out.println("proper noun = " + partOfSpeech);
            }
            else if(!tag.contains("n") && tag.contains("prp"))
            {
                // this is a proper noun WITHOUT ID e.g. I, She, He
                properNoun.add(partOfSpeech);
                System.out.println("proper noun = " + partOfSpeech);
            }
            else if(tag.contains("rb"))
            {
                // This is an ADVERB
                adverb.add(partOfSpeech);
                System.out.println("adverb = " + partOfSpeech);
            }
            else if(tag.contains("jj"))
            {
                // This is an Adjective
                adjective.add(partOfSpeech);
                System.out.println("adjective = " + partOfSpeech);
            }
            else if(tag.contains("vb") && !tag.contains("VBZ".toLowerCase())
                    //&& !tag.contains("VBP".toLowerCase())
                    &&!tag.contains("VBD".toLowerCase()))
            {
                // This is an VERB
                verb.add(partOfSpeech);
                System.out.println("verb = " + partOfSpeech);
            }
        }
        //ArrayList<String> names = findName(raw);
        //ArrayList<String> places = getPlaces(raw);
        String nounsText = "";
        String name = "";
        for(String word : numbers)
            name += word + " ";
        for(String word : noun)
            name += word + " ";
        //for(String word : nnp)
        //name += word + " ";
        for(String word : adjective)
            name += word + " ";
        for(String word : verb)
            if(!word.toLowerCase().equals("be"))
                name += word + " ";

        return name;
    }
    public static String getNouns(String raw) throws IOException {

        ArrayList<String> rawParsed = POSTag2(raw);
        ArrayList<String> noun = new ArrayList<>();
        ArrayList<String> adjective = new ArrayList<>();
        ArrayList<String> nnp = new ArrayList<>();
        ArrayList<String> properNoun = new ArrayList<>();
        ArrayList<String> adverb = new ArrayList<>();
        ArrayList<String> verb = new ArrayList<>();
        ArrayList<String> numbers = new ArrayList<>();
        for(String word : rawParsed)
        {
            String tag = word.substring(word.indexOf('_')).toLowerCase();
            String partOfSpeech = word.substring(0,word.indexOf('_'));
            if(tag.contains("cd"))
            {
                // this is a regular number
                numbers.add(partOfSpeech);
                System.out.println("number = " + partOfSpeech);
            }
            if(tag.contains("n") && !tag.contains("p")&& !tag.contains("i"))
            {
                // this is a regular noun
                noun.add(partOfSpeech);
                System.out.println("regular noun = " + partOfSpeech);
            }
            else if (tag.contains("n") && tag.contains("p")
                    &&!partOfSpeech.toLowerCase().equals("i")
                    &&!partOfSpeech.toLowerCase().equals("you")
                    &&!partOfSpeech.toLowerCase().equals("your")
                    &&!partOfSpeech.toLowerCase().equals("am")
                    &&!partOfSpeech.toLowerCase().equals("are")
                    &&!partOfSpeech.toLowerCase().equals("my")
                    &&!partOfSpeech.toLowerCase().equals("me"))
            {
                // this is a proper noun WITH THE NAME ID e.g. Japan
                nnp.add(partOfSpeech);
                System.out.println("proper noun = " + partOfSpeech);
            }
            else if(!tag.contains("n") && tag.contains("prp")
                    &&!partOfSpeech.toLowerCase().equals("i")
                    &&!partOfSpeech.toLowerCase().equals("you")
                    &&!partOfSpeech.toLowerCase().equals("your")
                    &&!partOfSpeech.toLowerCase().equals("am")
                    &&!partOfSpeech.toLowerCase().equals("are")
                    &&!partOfSpeech.toLowerCase().equals("my")
                    &&!partOfSpeech.toLowerCase().equals("me"))
            {
                // this is a proper noun WITHOUT ID e.g. I, She, He
                properNoun.add(partOfSpeech);
                System.out.println("proper noun = " + partOfSpeech);
            }
            else if(tag.contains("rb"))
            {
                // This is an ADVERB
                adverb.add(partOfSpeech);
                System.out.println("adverb = " + partOfSpeech);
            }
            else if(tag.contains("jj"))
            {
                // This is an Adjective
                adjective.add(partOfSpeech);
                System.out.println("adjective = " + partOfSpeech);
            }
            else if(tag.contains("vbg"))
            {
                // This is an VERB
                verb.add(partOfSpeech);
                //System.out.println("verb = " + partOfSpeech);
            }
        }
        //ArrayList<String> names = findName(raw);
        //ArrayList<String> places = getPlaces(raw);
        String nounsText = "";
        String name = "";
        for(String word : numbers)
            name += word + " ";
        for(String word : noun)
            name += word + " ";

        for(String word : nnp)
            name += word + " ";
        /*for(String word : adjective)
            name += word + " ";
        for(String word : verb)
            if(!word.toLowerCase().equals("be"))
                name += word + " ";
*/
        return name;
    }
    public static String getVerbOrAdjective(String raw) throws IOException {

        ArrayList<String> rawParsed = POSTag2(raw);
        ArrayList<String> noun = new ArrayList<>();
        ArrayList<String> adjective = new ArrayList<>();
        ArrayList<String> nnp = new ArrayList<>();
        ArrayList<String> properNoun = new ArrayList<>();
        ArrayList<String> adverb = new ArrayList<>();
        ArrayList<String> verb = new ArrayList<>();
        ArrayList<String> numbers = new ArrayList<>();
        ArrayList<String> in = new ArrayList<>();
        for(String word : rawParsed)
        {
            String tag = word.substring(word.indexOf('_')).toLowerCase();
            String partOfSpeech = word.substring(0,word.indexOf('_'));
            if(tag.contains("cd"))
            {
                // this is a regular number
                numbers.add(partOfSpeech);
                System.out.println("number = " + partOfSpeech);
            }
            if(tag.contains("n") && !tag.contains("p")&& !tag.contains("i"))
            {
                // this is a regular noun
                noun.add(partOfSpeech);
                System.out.println("regular noun = " + partOfSpeech);
            }
            else if (tag.contains("n") && tag.contains("p"))
            {
                // this is a proper noun WITH THE NAME ID e.g. Japan
                nnp.add(partOfSpeech);
                System.out.println("proper noun = " + partOfSpeech);
            }
            else if(!tag.contains("n") && tag.contains("prp"))
            {
                // this is a proper noun WITHOUT ID e.g. I, She, He
                properNoun.add(partOfSpeech);
                System.out.println("proper noun = " + partOfSpeech);
            }
            else if(tag.contains("rb") && !tag.contains("w"))
            {
                // This is an ADVERB
                adverb.add(partOfSpeech);
                System.out.println("adverb = " + partOfSpeech);
            }
            else if(tag.contains("jj"))
            {
                // This is an Adjective
                adjective.add(partOfSpeech);
                System.out.println("adjective = " + partOfSpeech);
            }
            else if(tag.contains("vb") && !tag.contains("VBZ".toLowerCase())
                    &&!partOfSpeech.toLowerCase().equals("i")
                    &&!partOfSpeech.toLowerCase().equals("you")
                    &&!partOfSpeech.toLowerCase().equals("your")
                    &&!partOfSpeech.toLowerCase().equals("am")
                    &&!partOfSpeech.toLowerCase().equals("are")
                    &&!partOfSpeech.toLowerCase().equals("my")
                    &&!partOfSpeech.toLowerCase().equals("me")
                    && !tag.contains("VBP".toLowerCase())
                    &&!tag.contains("VBD".toLowerCase()))
            {
                // This is an VERB
                verb.add(partOfSpeech);
                System.out.println("verb = " + partOfSpeech);
            }
            else if(tag.contains("in"))
            {
                // This is an VERB
                /*if(partOfSpeech.toLowerCase().equals("like"))
                in.add(partOfSpeech);
                System.out.println("in = " + partOfSpeech);
                */
            }
        }
        //ArrayList<String> names = findName(raw);
        //ArrayList<String> places = getPlaces(raw);
        String nounsText = "";
        String name = "";
        /*for(String word : noun)
            name += word + " ";
            */
        //for(String word : nnp)
        //name += word + " ";
        for(String word : in)
            name += word + " ";
        for(String word : numbers)
            name += word + " ";
        for(String word : adjective)
            name += word + " ";
        for(String word : verb)
            if(!word.toLowerCase().equals("be")
                    &&!word.toLowerCase().equals("do"))
                name += word + " ";
        for(String word : adverb)
            if(!word.toLowerCase().equals("be")
                    &&!word.toLowerCase().equals("do"))
                name += word + " ";

        return name;
    }

}
