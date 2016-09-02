package general.graph.theory;

import SentenceGenerator.ComparePhrases;
import SentenceGenerator.GoogleCSAPI;
import SentenceGenerator.ObtainOpinion;
import general.Client1;
import general.FindKeyWordsTest;
import general.TestTTS;
import general.comparePhrasesold_june7;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.wordnet.SynonymMap;
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
public class WikipediaInfoBoxModel2OldJuly6 {
    final public static boolean IS_CLEANING_QUERY = true;
    final public static int KEYWORD_IMPORTANCE_INDEX = 2;
    public static ArrayList<String> list = new ArrayList<>();
    public static void chatbot(String question, String origionalQuestion) throws Exception {
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
                        result = result.replace(subKeyWord, StringUtils.repeat(" " + subKeyWord + " ", WikipediaInfoBoxModel2OldJuly6.KEYWORD_IMPORTANCE_INDEX).trim());
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
        } catch (Exception k) {
            query = question;
        }
        System.out.println("query>>> " + query);
        //System.exit(-1);
        try {
            Files.write(Paths.get("C:\\Users\\corpi\\Music\\chatbotLog.txt"), ("QUESTION = " + query + "\n###START###>>").getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
        ArrayList<String> results = new ArrayList<>(GoogleCSAPI.getLinks((origionalQuestion).replaceAll("\\s+", "+"), false));//.subList(0,3));
        ArrayList<String> results2 = new ArrayList<>(GoogleCSAPI.getLinks((origionalQuestion).replaceAll("\\s+", "+"), true));//.subList(0,3));
        results.addAll(results2);
        try {
            Files.write(Paths.get("C:\\Users\\corpi\\Documents\\robot\\permutations-december-25\\permutations\\src\\general\\koo"), ("").getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
        TreeMap<Double, HashSet<AnswerPair>> match = new TreeMap<>(Collections.reverseOrder());
        int charsize = 0;
        for (String result : results) {
            try {
                System.out.println(result);
                Document doc = Jsoup.connect(result).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2").get();
                Elements paragraphs = doc.select("p");
                for (Element p : paragraphs) {
                    String biggestText = p.text();//(|mr|ms|mrs)
                    biggestText = biggestText.toLowerCase().replaceAll("dr\\.", "dr");
                    biggestText = biggestText.toLowerCase().replaceAll("mr\\.", "mr");
                    biggestText = biggestText.toLowerCase().replaceAll("ms\\.", "ms");
                    biggestText = biggestText.toLowerCase().replaceAll("mrs\\.", "mrs");
                    String[] sentences = biggestText.split("[\\.\\?!]+");

                    for (String para : sentences) {
                        String p12 = para;
                        try {
                            p12 = p12.substring(p12.indexOf(':'));
                        } catch (Exception k) {//do nothing
                        }
                        if (false || !p12.toLowerCase().replaceAll("[^\\w^\\d^\\s]+", "").trim().startsWith("how ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\d^\\s]+", "").trim().startsWith("why ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\d^\\s]+", "").trim().startsWith("who ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\d^\\s]+", "").trim().startsWith("what ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\d^\\s]+", "").trim().startsWith("where ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\d^\\s]+", "").trim().startsWith("when ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\d^\\s]+", "").trim().startsWith("is ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\d^\\s]+", "").trim().startsWith("which ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\d^\\s]+", "").trim().startsWith("he ")
                                && !p12.toLowerCase().replaceAll("[^\\w^\\d^\\s]+", "").trim().startsWith("she ")
                                && !p12.contains(">>")
                                ) {
                            try {
                                Files.write(Paths.get("C:\\Users\\corpi\\Documents\\robot\\permutations-december-25\\AWS-permutations-june-19-2-current-build-as-of-june-24\\permutations-june-19-2\\permutations\\src\\general\\koo"), (p.text()).getBytes(), StandardOpenOption.APPEND);
                            } catch (IOException e) {
                                //exception handling left as an exercise for the reader
                            }
                            double compare = ComparePhrases.compare2(query, para);
                            charsize += para.length();
                            if (match.containsKey(compare)) {
                                HashSet<AnswerPair> dummy = match.get(compare);
                                if (para.toLowerCase().matches(".*?\\s(it|she|he|they|their|there|him|her|" +
                                        "his|they're|we|we're|that|this|thus)['\\s\\.\\?!].*?"))//thus?
                                    dummy.add(new AnswerPair(result, biggestText));
                                else
                                    dummy.add(new AnswerPair(result, para));
                                match.put(compare, dummy);
                            } else {
                                HashSet<AnswerPair> dummy = new HashSet<>();
                                if (para.toLowerCase().matches(".*?\\s(it|she|he|they|their|there|him|her|" +
                                        "his|they're|we|we're)['\\s\\.\\?!].*?"))
                                    dummy.add(new AnswerPair(result, biggestText));
                                else
                                    dummy.add(new AnswerPair(result, para));
                                match.put(compare, dummy);
                            }
                        }
                    }

                }
            } catch (Exception k) {
            }
        }
        //System.exit(0);
        // results.clear();
        //results.add("https://en.wikipedia.org/wiki/Economics");
        try {
            System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))");
            System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))");
            System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))");
            System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))");
            System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))");
            String speakThis = "";
            boolean isDone = false;
            if (match.firstEntry().getKey() < 1) {
                TestTTS.speak2("man... I really couldn't find any information on that!");
                return;
            }
            System.out.println("92tu83193g: " + match.firstEntry().getKey());
            //System.exit(0);
            String speakfull = new String();
            if ((int) ((match.firstEntry().getKey() / 4000.0) * 100.0) < 20) {
                for (AnswerPair finalResult : match.firstEntry().getValue()) {
                    if (!isDone) {

                        System.out.println(finalResult.getText());

                        speakfull += " " + finalResult.getText();

                        if (speakfull.toLowerCase().contains("list")) {
                            for (Element e : Jsoup.connect(finalResult.getURL()).get().select("p")) {
                                speakfull += e.text();
                            }
                        }

                        isDone = false;
                    }
                }
                String SpeakThisAnswer = new String();
                for (AnswerPair finalResult : match.firstEntry().getValue()) {
                    if (!isDone) {

                        System.out.println(finalResult.getText());

                        SpeakThisAnswer += " " + finalResult.getText();

                        if (SpeakThisAnswer.toLowerCase().contains("list")) {
                            for (Element e : Jsoup.connect(finalResult.getURL()).get().select("p")) {
                                SpeakThisAnswer += e.text();
                            }
                        }

                        isDone = false;
                    }
                }
                TestTTS.speak2("20 percent. " + SpeakThisAnswer + ". I am less than 20 percent sure of this... So " +
                        " I'll add my full source... " + speakfull);
                return;
            }
            if (match.firstEntry().getValue().size() < 10) {
                for (AnswerPair finalResult : match.firstEntry().getValue()) {
                    if (!isDone) {

                        System.out.println(finalResult.getText());

                        speakThis += " " + finalResult.getText();

                        if (speakThis.toLowerCase().contains("list")) {
                            for (Element e : Jsoup.connect(finalResult.getURL()).get().select("p")) {
                                speakThis += e.text();
                            }
                        }

                        isDone = false;
                    }
                }

                System.out.println("#@@#: " + speakThis);

                TestTTS.speak2("I am " + (int) ((match.firstEntry().getKey() / 4000.0) * 100.0) + " percent sure about this answer. " + speakThis);

            } else {
                try {
                    speakThis = ((AnswerPair) match.firstEntry().getValue().toArray()[0]).getText();
                    TestTTS.speak2("I am " + (int) ((match.firstEntry().getKey() / 4000.0) * 100.0) + " percent sure about this answer. " + speakThis);
                    System.out.println("#@@#: " + speakThis);
                } catch (Exception o) {
                    o.printStackTrace();
                    TestTTS.speak2("something failed");
                }
            }
            System.out.println(match.firstEntry());
            try {
                Files.write(Paths.get("C:\\Users\\corpi\\Documents\\robot\\permutations-december-25\\permutations\\src\\general\\koo"), (speakThis).getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                //exception handling left as an exercise for the reader
            }
        } catch (java.net.SocketException l) {
            System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))");
            System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))");
            System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))");
            System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))");
            System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))");

            String speakfull = ((AnswerPair) match.firstEntry().getValue().toArray()[(new Random()).nextInt(match.firstEntry().getValue().size())])
                    .getText();
            System.out.println("Answer --> " + speakfull);
            System.out.print("Continue? (Press Enter) ");
            (new Scanner(System.in)).nextLine();
            //String opinion = ObtainOpinion.getPopularOpinions((String)match.firstEntry().getValue().toArray()[0]);

            System.out.println("My Rating = " + ObtainOpinion.connotationOnInternet((String) match.firstEntry().getValue().toArray()[0]));
            //System.out.println("My reason: " + opinion);
            //System.exit(-1);

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
    public static void start(String input,String origionalQuestion, boolean isQuestioning) throws Exception {
        PrintWriter writer = new PrintWriter(new File("C:\\Users\\corpi\\Documents\\robot\\permutations-december-25\\AWS-permutations-june-19-2-current-build-as-of-june-24\\permutations-june-19-2\\permutations\\src\\general\\koo"));
        writer.print("");
        writer.close();
        chatbot(input,origionalQuestion);
    }
//in what decade was the first modern crossword puzzle published and oreo cookies are introduced
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
        }catch (NullPointerException k) {
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
            }catch (HttpStatusException j) {
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
            }catch (HttpStatusException j) {
                System.out.println("EEROEOREWORPWERWERWOEKOEWR");
                j.printStackTrace();
            }
        }
        return match.firstEntry().getValue();


    }
}


