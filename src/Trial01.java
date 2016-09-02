import SentenceGenerator.UnderstandSentence;

import java.io.*;
import java.util.*;

/**
 * Created by adil on 14/07/15.
 */
public class Trial01 {
    public static String ROOT_PATH = "PartsOfSpeech/";
    public static String EXT =  ".txt";
    public static String RESEVOIR = ROOT_PATH + "SentenceOrders" + EXT;
    public  static  List<String> nounTypes;
    public static void stripDuplicatesFromFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        Set<String> lines = new HashSet<String>(10000); // maybe should be bigger
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        reader.close();
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        for (String unique : lines) {
            writer.write(unique);
            writer.newLine();
        }
        writer.close();
    }
    static public void main(String[] argv) throws IOException {
        String[] l = new String[]{"SentenceOrders","CC","CD","DT","EX","FW","IN","JJ","JJR","JJS","LS","MD","NN","NNP","NNPS","NNS","PDT",
                "POS","PRP","PRP$","RB","RBR","RBS","RP","SYM","TO","UH","VB","VBD","VBG","VBN","VBP",
                "VBZ","WDT","WP","WP$","WRB"};
        for(String path:l)
        {
            stripDuplicatesFromFile(ROOT_PATH + path + EXT);
        }
        System.out.print("Enter random sentence: ");
        String input = (new Scanner(System.in)).nextLine();
        //String input = argv[0];
        UnderstandSentence sentence = new StanfordParser().findNoun3(input);
        Map<Integer,String[]> sentenceOrder = sentence.getSentenceOrder();
        String sentenceMap = "";
        String wordsInSentence = "";
        for(Map.Entry<Integer,String[]> entry:sentenceOrder.entrySet())
        {
            System.out.print(entry.getValue()[0]+" ");
            sentenceMap +=entry.getValue()[0]+" ";
            wordsInSentence += entry.getValue()[1]+" ";
        }
        for(Map.Entry<Integer,String[]> entry:sentenceOrder.entrySet()) {
            String part = entry.getValue()[0];
            String word = entry.getValue()[1];
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(ROOT_PATH + part + EXT, true)))) {
                out.println(word);
            } catch (IOException e) {
            }
        }
        String resivoir = ROOT_PATH + "SentenceOrders" + EXT;
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(resivoir, true)))) {
            out.println(sentenceMap);
        } catch (IOException e) {
        }
        /*List<String> nounTypes = Arrays.asList(new String[]{"NN","NNS","NNP","NNPS",
                "PDT","POS","PRP"});*/
        nounTypes = Arrays.asList(new String[]{"CC","CD","DT","EX","FW","IN","JJ","JJR","JJS","LS","MD","NN","NNP","NNPS","NNS","PDT",
                "POS","PRP","RB","RBR","RBS","RP","SYM","TO","UH","VB","VBD","VBG","VBN","VBP",
                "VBZ","WDT","WP","WP$","WRB","PRP$"
        //,"VB","VBD","VBG","VBN","VBP","VBZ"
        });
        List<String> allMap = Arrays.asList(sentenceMap.split("\\s+"));
        Map<String,String> nounAndWord = new HashMap<String,String>();
        List<String> allWords = Arrays.asList(wordsInSentence.split("\\s+"));
        for(int i = 0;i<allMap.size();i++)
        {
            if(nounTypes.contains(allMap.get(i)))
            {
                String noun = allMap.get(i);
                String word = allWords.get(i);
                nounAndWord.put(noun,word);
            }
        }
        System.out.println("Computer: \"" + generateSentence(new File(resivoir), nounAndWord) + "\"");
    }
    public static String trial(File f,Map<String,String> nounAndWord) throws IOException {
        // get random sentence structure based on previous ones
        String sentenceOrder = choose(f);
        Set<String> a = new HashSet<String>(nounTypes);
        Set<String> b = new HashSet<String>(Arrays.asList(sentenceOrder.split("\\s+")));
        a.retainAll(b);
        Set<String> c = new HashSet<String>(nounAndWord.keySet());
        a.retainAll(c);
        if (!(a.size() > 0||Arrays.asList(sentenceOrder.split("\\s+")).size()<=6)) {
            return trial(f, nounAndWord);
        }
        else
        {
            return sentenceOrder;
        }


    }
    public static String generateSentence(File f,Map<String,String> nounAndWord) throws IOException {

        String sentenceOrder =trial(f,nounAndWord);
        //String sentenceOrder = choose(f);
        String resultantSentence = "";
        String[] order = sentenceOrder.split("\\s+");
        for(String type:order)
        {
            System.out.println("#####>>> "+type+":"+nounAndWord.containsKey(type));
            if(!nounAndWord.containsKey(type)) {
                // INSTEAD OF FINDING WORDS RANDOMLY, USE RELATIVITY TO FIND THE NEXT WORD! (REMEMBER ITS A WORK IN PROGRESS)
                // REMEMBER TO MAKE A GRAPH OF ADJECTIVES, NOUNS AND VERBS AND KNOW HOW TO CONSTRUCT A SENTENCE ORGANICALLY
                String path = ROOT_PATH + type + EXT;
                String currentWord = choose(new File(path));
                resultantSentence += currentWord + " ";
            }//An article is a word
            else
            {
                System.out.println("RESERVED WORD ACTIVE&&&&&&&&&&&&&    "+nounAndWord.get(type)+"     &&&&&&&&&&&&&&&&&&&&&");
                // THIS IS A RESERVED NOUN
                String currentWord = nounAndWord.get(type);
                resultantSentence += currentWord + " ";
                // KEEP FROM REPEATING NOUNS AND OTHER WORDS
                nounAndWord.remove(type);
            }
        }
        return resultantSentence;
    }
    public static String lastLine(File f) throws IOException {

        String sCurrentLine;

        BufferedReader br = new BufferedReader(new FileReader(f.getAbsolutePath()));
        String lastLine = "";
        while ((sCurrentLine = br.readLine()) != null)
        {
            //System.out.println(sCurrentLine);
            lastLine = sCurrentLine;
        }
        return lastLine;
    }
    public static String choose(File f) throws FileNotFoundException
    {
        String result = null;
        Random rand = new Random();
        int n = 0;
        for(Scanner sc = new Scanner(f); sc.hasNext(); )
        {
            ++n;
            String line = sc.nextLine();
            if(rand.nextInt(n) == 0)
                result = line;
        }
        return result;
    }
}
