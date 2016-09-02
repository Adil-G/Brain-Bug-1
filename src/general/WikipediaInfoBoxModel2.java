package general;
import SentenceGenerator.*;
import general.graph.theory.WikipediaInfoBoxModel2OldJune14;
import opennlp.tools.parser.Parse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.lucene.wordnet.SynonymMap;
import org.json.JSONArray;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by corpi on 2016-04-30.
 */
public class WikipediaInfoBoxModel2 {
    final public static boolean IS_CLEANING_QUERY = true;
    final public static int KEYWORD_IMPORTANCE_INDEX = 8;
    public static ArrayList<String> list = new ArrayList<>();
    public static int MAX_ANSWERS_PER_QUESTION = 20;
    public static int  ANSWER_MIN = 30;
    public static int ANSWER_MAX = 250;
    public static void chatbot(String question) throws Exception {
        //noun = question + " " + noun;
        //query = question + " " + query;
        PrintStream c = System.out;
        String query = question;//.toLowerCase().replace("known as","");
        ExecutorService es = Executors.newCachedThreadPool();
        final String finalQuery = query;


        org.json.JSONArray keywords = Client1.extractConcept(finalQuery);
        ArrayList<String> words = new ArrayList<>();
        for (int i = 0; i < keywords.length(); i++) {
            final int ii = i;
            es.execute(new Runnable() {
                @Override
                public void run() {
                    String result = finalQuery;
                    String keyword = keywords.getJSONObject(ii).get("concept").toString();
                    System.out.println("WORD BEFORE= " + keyword);

                    for (String usedWord : words)
                        keyword = keyword.replaceAll(usedWord, "");
                    keyword = Client1.gogo(keyword);
                    System.out.println("WORD AFTER= " + keyword);
                    for (String subKeyWord : keyword.split("\\s+"))
                        result = result.replace(subKeyWord, StringUtils.repeat(" " + subKeyWord + " ", WikipediaInfoBoxModel2.KEYWORD_IMPORTANCE_INDEX).trim());
                    words.add(keyword.trim());
                    System.out.println(">>> " + words);
                    list.add(result);
                }
            } /*  your task */);


        }
        es.shutdown();
        boolean finshed = es.awaitTermination(1, TimeUnit.MINUTES);
// all tasks have finished or the time has been reached.
        if (finshed) {

            System.out.println("query>>> " + list);
            //System.exit(0);
            //System.exit(-1);
        }
        try {
            query = list.get(0);
        }
        catch (Exception m){

        }
        System.out.println("query>>> " + query);
        //System.exit(-1);
        try {
            Files.write(Paths.get("C:\\Users\\corpi\\Music\\chatbotLog.txt"), ("QUESTION = " + query + "\n###START###>>").getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
        //ArrayList<String> results = GoogleCSAPI.getLinks((question).replaceAll("\\s+", "+"), false);
        // results.clear();
        //results.add("https://en.wikipedia.org/wiki/Economics");
        TreeMap<Double, HashSet<String>> match = new TreeMap<>(Collections.reverseOrder());
        //for (String result : results) {
        try {
            int count = 0;
            //Document doc = Jsoup.connect(result).get();
            //Elements paragraphs = doc.select("p");
            //for(Element p : paragraphs) {
            //   String[] sentences = p.text().split("[\\.\\?!]+");
            BufferedReader br = new BufferedReader(new FileReader(new File("C:\\Users\\corpi\\Documents\\robot\\permutations-december-25\\AWS-permutations-june-19-2-current-build-as-of-june-24\\permutations-june-19-2\\permutations\\src\\general\\statements2")));
            String line = null;
            ArrayList<String> sentences = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                sentences.add(line);
            }

            for (String paraFull : sentences)
               for (String paras : paraFull.split("[\\.\\?!]+"))
              //         for (String para : paras.split(" {2,}"))
                //for (String para : paraFull.split("                       "))
            {String para = paras;
                    //para = paraFull;
                    //String para = paraFull;
                    System.out.println(count++);
                    String phrase1 = para;
                System.out.println("Evaluating: "+para);
                    String phrase1Para = para;
                    if (true||!phrase1.toLowerCase().replaceAll("[^\\w^\\d^\\s]+", "").trim().startsWith("how")
                            && !phrase1.toLowerCase().replaceAll("[^\\w^\\d^\\s]+", "").trim().startsWith("why")
                            && !phrase1.toLowerCase().replaceAll("[^\\w^\\d^\\s]+", "").trim().startsWith("who")
                            && !phrase1.toLowerCase().replaceAll("[^\\w^\\d^\\s]+", "").trim().startsWith("what")
                            && !phrase1.toLowerCase().replaceAll("[^\\w^\\d^\\s]+", "").trim().startsWith("where")
                            && !phrase1.toLowerCase().replaceAll("[^\\w^\\d^\\s]+", "").trim().startsWith("when")
                            && !phrase1.toLowerCase().replaceAll("[^\\w^\\d^\\s]+", "").trim().startsWith("is")
                            && !phrase1.toLowerCase().replaceAll("[^\\w^\\d^\\s]+", "").trim().startsWith("he")
                            && !phrase1.toLowerCase().replaceAll("[^\\w^\\d^\\s]+", "").trim().startsWith("she")
                            && !phrase1.toLowerCase().replaceAll("[^\\w^\\d^\\s]+", "").trim().startsWith("do ")
                            //&& !phrase1.toLowerCase().replaceAll("[^\\w^\\d^\\s]+", "").trim().startsWith("are ")
                            &&
                            !phrase1.toLowerCase().contains("list of")
                           // && phrase1.length() > WikipediaInfoBoxModel2.ANSWER_MIN && phrase1.length() <  WikipediaInfoBoxModel2.ANSWER_MAX)
                            )
                if (!para.toLowerCase().contains("the following")
                                && !para.toLowerCase().contains("please see")
                                && !para.toLowerCase().contains("this list")
                                && !para.toLowerCase().contains("this category")
                                && !para.toLowerCase().contains("let us")
                                && !para.toLowerCase().contains("follow us")
                                && !para.toLowerCase().contains("our newsletter")
                                && !para.toLowerCase().contains("here are")
                                && !para.toLowerCase().contains("loading ")
                                && !para.toLowerCase().startsWith("do ")
                                && para.length() > 10) {
                            System.out.println("Considered: "+para);
                            //the ancient lion of nimrud went missing from what city national museum in 2003
                            //hedgehogs are covered with quills or spines which are hollow hairs made stiffby what protein
                            //para = para.replaceAll("(who|what|where|why|how|when|does|did)","");
                            double compare = ComparePhrases.compare2(query, para);
                            System.out.println("final res = " + compare);
                            if(compare>20) {
                                System.out.println(query);
                                //System.exit(0);
                            }
                            if (match.containsKey(compare)) {
                                HashSet<String> dummy = match.get(compare);
                                dummy.add(phrase1Para);
                                match.put(compare, dummy);
                            } else {
                                HashSet<String> dummy = new HashSet<>();
                                dummy.add(phrase1Para);
                                match.put(compare, dummy);
                            }
                        }

                }

            System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))");
            System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))");
            System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))");
            System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))");
            System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))");
            String speakThis = "";
            boolean isDone = false;
            try {
                if (match.firstEntry().getValue().size() < WikipediaInfoBoxModel2.MAX_ANSWERS_PER_QUESTION) {
                    for (String finalResult : match.firstEntry().getValue()) {
                        if (!isDone) {
                            System.out.println(finalResult);

                            speakThis += " " + finalResult + " ";
                            isDone = false;
                        }
                    }

                    System.out.println("#@@#: " + match.firstEntry().getValue().toArray()[
                            (new Random()).nextInt(match.firstEntry().getValue().size())].toString());
                } else {
                    speakThis = (String) match.firstEntry().getValue().toArray()[0];
                    System.out.println("#@@#: " + match.firstEntry().getValue().toArray()[
                            (new Random()).nextInt(match.firstEntry().getValue().size())].toString());
                }
                String it =match.firstEntry().getValue().toArray()[
                        (new Random()).nextInt(match.firstEntry().getValue().size())].toString();
                System.out.println("4327fhdjhh5: "+it);
                TestTTS.speak2NEW(it);
            }catch (Exception k){k.printStackTrace();}
            System.out.println(match.firstEntry());

            ComparePhrases.compare2(query,speakThis);
            //System.out.wait();
            //System.out.print("Continue? (Press Enter) ");
            //(new Scanner(System.in)).nextLine();
            //WikipediaInfoBoxModel2OldJune14.start(question,false);

            TestChatBotMain.main(new String[]{});
            // System.out.println("#@@#: "+speakThis);
                /*JSONArray keyWords = Client1.extractConcept(speakThis);
                for(int i = 0;i<keyWords.length();i++) {
                    String sayit = keyWords.getJSONObject(i).get("concept").toString();
                    System.out.println("#@@#: "+sayit);

                }
*/
            /*System.out.print("Continue? (Press Enter) ");
            (new Scanner(System.in)).nextLine();
            //String opinion = ObtainOpinion.getPopularOpinions((String)match.firstEntry().getValue().toArray()[0]);

            System.out.println("My Rating = " + ObtainOpinion.connotationOnInternet((String) match.firstEntry().getValue().toArray()[0]));
            //System.out.println("My reason: " + opinion);
            System.exit(-1);
            */
        } catch (Exception j) {
j.printStackTrace();
            System.exit(-100);
        }

    }

    public static ArrayList<String> listSynonymsJJ2(String lastWord) throws IOException {
        ArrayList<String> rjhymes = new ArrayList<String>();
        System.out.println("... Getting source");
        //String url = "http://www.thesaurus.com/browse/"+ lastWord+"?s=t";
        String url = "http://www.wordhippo.com/what-is/another-word-for/" + lastWord + ".html";
        //String url = "http://www.getanotherwordfor.com/"+lastWord;
        String html = "";
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
            html = doc.html();
        } catch (HttpStatusException e) {
            System.out.println("No internet");
        }
        //System.out.println(html);
        if (html.equals("") || html.contains("No words found.")) {
            System.out.println("REWORDING FAILED.");
            return new ArrayList<String>();//listSynonymsBACKUP(lastWord);
        } else {
            System.out.println("... Got source succeeded");
            if (!html.equals("") && !html.contains("no thesaurus results")) {
                System.out.println("GOt the HTML");
                String pattern = "";
                for (String type : new String[]{"v", "j", "r"}){
                    if (type.toLowerCase().startsWith("v")) {
                        pattern = "<div class=\"wordtype\">Verb</div>(.*?)";
                    } else if (type.toLowerCase().startsWith("j")) {
                        pattern = "<div class=\"wordtype\">Adjective</div>(.*?)";
                    } else if (type.toLowerCase().startsWith("r")) {
                        pattern = "<div class=\"wordtype\">Adverb</div>(.*?)";
                    }
                    Pattern p = Pattern.compile(pattern);
                    Matcher m = p.matcher(html);
                    while (m.find()) {
                        if (m.groupCount() > 0) {
                            html = m.group(1);
                        }
                    }
                    /////////////////////////////////////////////////////////////
                    pattern = "/what-is/another-word-for/.*?\">([a-z\\s]+)</a>";
                    p = Pattern.compile(pattern);
                    m = p.matcher(html);
                    final int WORD_BANK_SIZE = 4;
                    int h = 0;
                    while (h++ < WORD_BANK_SIZE && m.find()) {
                        String rhyme1 = m.group(1);
                        System.out.println("Synonym = " + rhyme1);
                        if (!rhyme1.equals(lastWord)) {
                            rjhymes.add(rhyme1);
                        }
                    }
                    //TreeMap<Integer,String> set = new TreeMap<Integer,String>(Collections.reverseOrder());
                    if (rjhymes.size() > 1) {
                        int maxSize = 4;
                        if (rjhymes.size() < maxSize) {
                            maxSize = rjhymes.size();
                        }

                        //rjhymes = pickNRandom(rjhymes, maxSize - 1);
                    } else {
                        //System.out.println(usedPhrases7.get(i));
                        //System.out.println(usedPhrases5.get(i + 1));
                    }
                }
            }
        }

        System.out.println("DFSIUGHENSVJLNEIONVS last word = <" + lastWord + ">");
        System.out.println("DFSIUGHENSVJLNEIONVS synonym = <"+rjhymes+">");
        return rjhymes;
    }
    public static String getBestFromCollection(HashSet<String> list, String search,final String TYPE) throws IOException {
        double awsome = 0;
        //String best = getRandomFromCollection(list);
        String best = "NOTHING FOUND";
        for(String x : list)
        {
            double current = comparePhrasesold_june7.compare2Type(search,x,false,TYPE);
            if(current>awsome)
            {
                best = x;
                awsome = current;
            }
        }
        return best;
    }
    public static String getRandomFromCollection(Collection list)
    {
        Random generator = new Random(list.size());
        int number = generator.nextInt();
        if(number >=list.size())
            number = list.size()-1;
        else if(number<0)
            number =0;
        return list.toArray()[0].toString();
    }
    public static void start(String input, boolean isQuestioning) throws Exception {

        chatbot(input);
    }

    public static String getNouns(String raw) throws IOException {
        ArrayList<String> rawParsed = FindKeyWordsTest.POSTag2(raw);
        ArrayList<String> noun = new ArrayList<>();
        ArrayList<String> adjective = new ArrayList<>();
        ArrayList<String> nnp = new ArrayList<>();
        ArrayList<String> properNoun = new ArrayList<>();
        ArrayList<String> adverb = new ArrayList<>();
        ArrayList<String> verb = new ArrayList<>();
        for(String word : rawParsed)
        {
            try {
                String tag = word.substring(word.indexOf('_')).toLowerCase();
                String partOfSpeech = word.substring(0, word.indexOf('_'));
                if (tag.contains("n") && !tag.contains("p") && !tag.contains("i")) {
                    // this is a regular noun
                    noun.add(partOfSpeech);
                    System.out.println("regular noun = " + partOfSpeech);
                } else if (tag.contains("n") && tag.contains("p")) {
                    // this is a proper noun WITH THE NAME ID e.g. Japan
                    nnp.add(partOfSpeech);
                    System.out.println("proper noun = " + partOfSpeech);
                } else if (!tag.contains("n") && tag.contains("prp")) {
                    // this is a proper noun WITHOUT ID e.g. I, She, He
                    properNoun.add(partOfSpeech);
                    System.out.println("proper noun = " + partOfSpeech);
                } else if (tag.contains("rb")) {
                    // This is an ADVERB
                    adverb.add(partOfSpeech);
                    System.out.println("adverb = " + partOfSpeech);
                } else if (tag.contains("jj")) {
                    // This is an Adjective
                    adjective.add(partOfSpeech);
                    System.out.println("adjective = " + partOfSpeech);
                } else if (tag.contains("vb")) {
                    // This is an VERB
                    verb.add(partOfSpeech);
                    System.out.println("verb = " + partOfSpeech);
                }
            }catch (Exception l)
            {}
        }
        String result =  "";
        for(String x : noun)
            result += x + " ";
        result.trim();
        return result;
    }
    public static HashSet<String> getDataWHO_WHERE(String RAW, String NAME, String nouns
                                              /*
                                              String adjectives,
                                              String nnps, String properNouns,
                                              String adverbs, String verbs
                                              */
    ) throws IOException {
        PrintStream c = System.out;
        boolean big = false;
        String noun = (NAME + " " + RAW).replaceAll("\\s+","+");
        String query = nouns;
        // ADD SYNONYMS TOO
        String[] words = query.toLowerCase().split("[^\\w]+");
        SynonymMap map = new SynonymMap(new FileInputStream("openNLP/wn_s.pl"));
        for (int i = 0; i < words.length; i++) {
            String[] synonyms = map.getSynonyms(words[i]);
            for(String x : synonyms)
            {
                query += " " + x;
            }
        }
        query = getNouns(query);
        System.out.println("ALL QUERIES USED: " + query.replaceAll("\\s",", "));
        ArrayList<String> results = GoogleCSAPI.getLinks(noun,true);
        TreeMap<Double, HashSet<String>> match = new TreeMap<>(Collections.reverseOrder());
        for (String result : results) {
            try {
                //if(!result.getUrl().toLowerCase().contains("wikipedia"))
                //    continue;
                Connection.Response res = Jsoup.connect(result)
                        .execute();

                String html = res.body();

                Document doc = Jsoup.parseBodyFragment(html);
                Element body = doc.body();
                Elements tables = body.getElementsByTag("table");// hasClass("infobox bordered");
                //System.out.println(tables.get(0));
                String text = "";

                for (int k = 0; k < tables.size(); k++)
                    for (int i = 0; i < tables.get(k).children().size(); i++) {
                        ;
                        c.println(">>> " + tables.get(k).children().get(i).text());
                        for (int j = 0; j < tables.get(k).children().get(i).children().size(); j++) {
                            if (true||!(RAW.toLowerCase().contains("where")||RAW.toLowerCase().contains("who") || RAW.toLowerCase().contains("name"))) {
                                String line = tables.get(k).children().get(i).children().get(j).text();
                                line = line.replaceAll("\\([sS]\\)","");
                                //c.println("-->" + line);
                                text += "\n" + line + "\n";//
                                String parent = tables.get(k).children().get(i).text();
                                String superParent = tables.get(k).text();
                                //double comparisonFactor = ComparePhrases.compare2(query, line, big);
                                double comparisonFactor = comparePhrasesold_june7.compare2SHORT(query, line, big);
                                if (match.containsKey(comparisonFactor)) {
                                    HashSet<String> addON = new HashSet<String>(match.get(comparisonFactor));
                                    //addON.add(parent);
                                    addON.add(line);
                                    match.put(comparisonFactor, addON);
                                } else {
                                    HashSet<String> newLine = new HashSet<>();
                                    newLine.add(line);
                                    match.put(comparisonFactor, newLine);
                                }

                            } else {
                                String line = tables.get(k).children().get(i).children().get(j).text();

                                double comparisonFactor = comparePhrasesold_june7.compare(query, line);
                                c.println("cc::"+comparisonFactor+"->"+line);
                                if (match.containsKey(comparisonFactor)) {
                                    HashSet<String> addON = new HashSet<String>(match.get(comparisonFactor));
                                    //addON.add(parent);
                                    addON.add(line);
                                    match.put(comparisonFactor, addON);
                                } else {
                                    HashSet<String> newLine = new HashSet<>();
                                    newLine.add(line);
                                    match.put(comparisonFactor, newLine);
                                }
                            }
                        }
                        //if (match.size() > 0 && match.firstEntry().getKey() != 0)
                        //    System.out.println(match.firstEntry().getKey() + "RESULT:::" + match.firstEntry().getValue());
                    }
            }catch (Exception j) {

            }
        }
        HashSet<String> first = new HashSet<>(match.firstEntry().getValue());
        TreeMap<Double, HashSet<String>> second = new TreeMap<>(Collections.reverseOrder());

        for(String line : first)
        {

            if (line.length()<40&&FindKeyWordsTest.findName(line).size() > 0) {
                // THIS IS VALID
                System.out.println("FOUND SOEMTHING! >> "+ line);
                double comparisonFactor = comparePhrasesold_june7.compare(query, line);
                if (second.containsKey(comparisonFactor)) {
                    HashSet<String> addON = new HashSet<String>(second.get(comparisonFactor));
                    //addON.add(parent);
                    addON.add(line);
                    second.put(comparisonFactor, addON);
                } else {
                    HashSet<String> newLine = new HashSet<>();
                    newLine.add(line);
                    second.put(comparisonFactor, newLine);
                }
            }
        }
        try {
            first = new HashSet<>(second.firstEntry().getValue());
            System.out.println("seccon-->" + first);
            return first;
        }catch (java.lang.NullPointerException k) {
            System.out.println("YOUMESSEDUP");
            if ((RAW.toLowerCase().contains("place")||RAW.toLowerCase().contains("where")||RAW.toLowerCase().contains("who") || RAW.toLowerCase().contains("name"))) {

                for (String result : results) {
                    try {
                        String x = result.substring(result.lastIndexOf('/')).replaceAll("_", " ");
                        if (x.split("\\s").length == 2 && FindKeyWordsTest.findName(x).size() > 0)
                            return new HashSet<String>(FindKeyWordsTest.findName(x));
                    } catch (Exception o) {
                    }
                }
            }
            else
            {
            }
            //first.add("I'm not sure... is the answer ");
            //
            return new HashSet<String>();
        }


    }
    public static HashSet<String> getDataWHAT_WHEN_NUMBER(String RAW, String NAME, String nouns
                                              /*
                                              String adjectives,
                                              String nnps, String properNouns,
                                              String adverbs, String verbs
                                              */
    ) throws IOException {
        PrintStream c = System.out;
        boolean big = RAW.toLowerCase().contains("population");
        String noun = (NAME + " " + RAW).replaceAll("\\s+","+");
        //noun = NAME.replaceAll("\\s+","+");
        String query = nouns;
        // ADD SYNONYMS TOO
        String[] words = query.toLowerCase().split("[^\\w]+");
        SynonymMap map = new SynonymMap(new FileInputStream("openNLP/wn_s.pl"));
        for (int i = 0; i < words.length; i++) {
            String[] synonyms = map.getSynonyms(words[i]);
            for(String x : synonyms)
            {
                query += " " + x;
            }
        }
        query = getNouns(query);
        System.out.println("ALL QUERIES USED: " + query.replaceAll("\\s",", "));
        ArrayList<String> results = GoogleCSAPI.getLinks(noun,true);
        TreeMap<Double, HashSet<String>> match = new TreeMap<>(Collections.reverseOrder());
        for (String result : results) {
            try {
                //if(!result.getUrl().toLowerCase().contains("wikipedia"))
                //    continue;
                Connection.Response res = Jsoup.connect(result)
                        .execute();

                String html = res.body();

                Document doc = Jsoup.parseBodyFragment(html);
                Element body = doc.body();
                Elements tables = body.getElementsByTag("table");// hasClass("infobox bordered");
                //System.out.println(tables.get(0));
                String text = "";

                for (int k = 0; k < tables.size(); k++) {
                    c.println("~~~~"+tables.get(k).text());
                    for (int i = 0; i < tables.get(k).children().size(); i++) {
                        ;
                        c.println(">>> " + tables.get(k).children().get(i).text());
                        for (int j = 0; j < tables.get(k).children().get(i).children().size(); j++) {
                            if (true || !(RAW.toLowerCase().contains("where") || RAW.toLowerCase().contains("who") || RAW.toLowerCase().contains("name"))) {
                                String line = tables.get(k).children().get(i).children().get(j).text();

                                line = line.replaceAll("\\([sS]\\)", "");
                                //c.println("-->" + line);
                                text += "\n" + line + "\n";//
                                String parent = tables.get(k).children().get(i).text();
                                String superParent = tables.get(k).text();
                                //double comparisonFactor = ComparePhrases.compare2(query, line, big);
                                double comparisonFactor = comparePhrasesold_june7.compare2SHORT(query, line, big);
                                if (match.containsKey(comparisonFactor)) {
                                    HashSet<String> addON = new HashSet<String>(match.get(comparisonFactor));
                                    //addON.add(parent);
                                    addON.add(line);
                                    match.put(comparisonFactor, addON);
                                } else {
                                    HashSet<String> newLine = new HashSet<>();
                                    newLine.add(line);
                                    match.put(comparisonFactor, newLine);
                                }

                            } else {
                            }
                        }
                        //if (match.size() > 0 && match.firstEntry().getKey() != 0)
                        //    System.out.println(match.firstEntry().getKey() + "RESULT:::" + match.firstEntry().getValue());
                    }
                }
            }catch (org.jsoup.HttpStatusException j) {
                System.out.println("EEROEOREWORPWERWERWOEKOEWR");
                j.printStackTrace();
            }
        }
        HashSet<String> first = new HashSet<>(match.firstEntry().getValue());
        try{
            HashSet<String> second = new HashSet<>();
            Pattern pat = Pattern.compile(".*?\\d+.*?");
            for(String sec : first) {
                Matcher matcher = pat.matcher(sec);
                if(matcher.matches())
                    second.add(sec);
            }
            return  second;
        }catch (Exception l)
        {
            l.printStackTrace();
            return  first;
        }

        //return match.firstEntry().getValue();


    }
    public static HashSet<String> getDataWHAT_WHEN_NAME(String RAW, String NAME, String nouns
                                              /*
                                              String adjectives,
                                              String nnps, String properNouns,
                                              String adverbs, String verbs
                                              */
    ) throws IOException {
        PrintStream c = System.out;
        boolean big = RAW.toLowerCase().contains("population");
        String noun = (NAME + " " + RAW).replaceAll("\\s+","+");
        //noun = NAME.replaceAll("\\s+","+");
        String query = nouns;
        // ADD SYNONYMS TOO
        String[] words = query.toLowerCase().split("[^\\w]+");
        SynonymMap map = new SynonymMap(new FileInputStream("openNLP/wn_s.pl"));
        for (int i = 0; i < words.length; i++) {
            String[] synonyms = map.getSynonyms(words[i]);
            for(String x : synonyms)
            {
                query += " " + x;
            }
        }
        query = getNouns(query);
        System.out.println("ALL QUERIES USED: " + query.replaceAll("\\s",", "));
        ArrayList<String> results = GoogleCSAPI.getLinks(noun,true);
        TreeMap<Double, HashSet<String>> match = new TreeMap<>(Collections.reverseOrder());
        for (String result : results) {
            try {
                //if(!result.getUrl().toLowerCase().contains("wikipedia"))
                //    continue;
                Connection.Response res = Jsoup.connect(result)
                        .execute();

                String html = res.body();

                Document doc = Jsoup.parseBodyFragment(html);
                Element body = doc.body();
                Elements tables = body.getElementsByTag("table");// hasClass("infobox bordered");
                //System.out.println(tables.get(0));
                String text = "";

                for (int k = 0; k < tables.size(); k++) {
                    c.println("~~~~"+tables.get(k).text());
                    for (int i = 0; i < tables.get(k).children().size(); i++) {
                        ;
                        c.println(">>> " + tables.get(k).children().get(i).text());
                        String pLine = tables.get(k).children().get(i).text();
                        double comparisonFactor = comparePhrasesold_june7.compare2SHORT(query, pLine, big);
                        //double comparisonFactor = ComparePhrases.compare2SHORT(RAW.toLowerCase(), pLine, big);
                        pLine = pLine.replaceAll("\\([sS]\\)", "");
                        //c.println("-->" + line);
                        text += "\n" + pLine + "\n";//
                        String parent = tables.get(k).children().get(i).text();
                        String superParent = tables.get(k).text();
                        //double comparisonFactor = ComparePhrases.compare2(query, line, big);

                        if (match.containsKey(comparisonFactor)) {
                            HashSet<String> addON = new HashSet<String>(match.get(comparisonFactor));
                            //addON.add(parent);
                            addON.add(pLine);
                            match.put(comparisonFactor, addON);
                        } else {
                            HashSet<String> newLine = new HashSet<>();
                            newLine.add(pLine);
                            match.put(comparisonFactor, newLine);
                        }
                        for (int j = 0; j < tables.get(k).children().get(i).children().size(); j++) {
                            if (true || !(RAW.toLowerCase().contains("where") || RAW.toLowerCase().contains("who") || RAW.toLowerCase().contains("name"))) {
                                String line = tables.get(k).children().get(i).children().get(j).text();
                                comparisonFactor = comparePhrasesold_june7.compare2SHORT(query, line, big);
                                //comparisonFactor = ComparePhrases.compare2SHORT(RAW.toLowerCase(), line, big);
                                line = line.replaceAll("\\([sS]\\)", "");
                                //c.println("-->" + line);
                                text += "\n" + line + "\n";//
                                parent = tables.get(k).children().get(i).text();
                                superParent = tables.get(k).text();
                                //double comparisonFactor = ComparePhrases.compare2(query, line, big);

                                if (match.containsKey(comparisonFactor)) {
                                    HashSet<String> addON = new HashSet<String>(match.get(comparisonFactor));
                                    //addON.add(parent);
                                    addON.add(line);
                                    match.put(comparisonFactor, addON);
                                } else {
                                    HashSet<String> newLine = new HashSet<>();
                                    newLine.add(line);
                                    match.put(comparisonFactor, newLine);
                                }

                            } else {
                            }
                        }
                        //if (match.size() > 0 && match.firstEntry().getKey() != 0)
                        //    System.out.println(match.firstEntry().getKey() + "RESULT:::" + match.firstEntry().getValue());
                    }
                }
            }catch (org.jsoup.HttpStatusException j) {
                System.out.println("EEROEOREWORPWERWERWOEKOEWR");
                j.printStackTrace();
            }
        }
        return match.firstEntry().getValue();


    }
}



