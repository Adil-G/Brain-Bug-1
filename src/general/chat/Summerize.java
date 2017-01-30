package general.chat;

import com.freebase.json.JSON;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.RequestBodyEntity;
import com.sun.xml.internal.fastinfoset.Encoder;
import general.graph.theory.ParagraphInfo;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.regex.Matcher;

import static com.mashape.unirest.http.utils.URLParamEncoder.encode;
import static sun.net.www.ParseUtil.decode;

/**
 * Created by corpi on 2016-12-20.
 */
public class Summerize {
    public static void main(String[] args) throws Exception {
        ArrayList<ParagraphInfo> fullDoc = new Summerize().articleDataFromUrl("https://monetizepros.com/display-advertising/average-cpm-rates/"
        ,"service trip planned guatemala");
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        for(ParagraphInfo hello : fullDoc)
            System.out.println("\n\n"+hello.getInfo());
       /* Summerize sum = new Summerize();

        String contents = sum.sum(sum.webContents("http://insidetech.monster.com/benefits/articles/8537-10-best-tech-blogs"));
        ArrayList<ParagraphInfo> topics = new ArrayList<>();
        for(String idea : contents.split("[\\.\\?!]+")) {

            String question = idea;
            ParagraphInfo answer = TestChatBot.getAnswerBlog(question, false, TestChatBot.NO_SUM);

            System.out.println(answer.getInfo());
            topics.add(answer);
        }
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        ArrayList<ParagraphInfo> fullDoc = new ArrayList<>();
        for(ParagraphInfo sentence : topics)
        {
            ParagraphInfo paragraphInfo = new ParagraphInfo(Paraphrase1.paraphrase(sentence.getText()),sentence.getPub(),
                    Jsoup.connect(sentence.getPub()).get().title());
            System.out.println(paragraphInfo.getInfo());
            fullDoc.add(paragraphInfo);
        }
        System.out.println("\n\n"+fullDoc);
        */
    }
    public ArrayList<ParagraphInfo> articleDataFromUrl(final String URL, final String TITLE) throws Exception {
        Summerize sum = new Summerize();

        String contents = sum.sum(sum.webContents(URL));
        //String contentsOne = sum.sumOne(sum.webContents(URL));
        ArrayList<ParagraphInfo> topics = new ArrayList<>();
        HashSet<String> usedURLs = new HashSet<>();
        usedURLs.add(URL);
        ArrayList<UrlFileConnector> sentences =  new DMiningGoogleOnlyChunksUnordered().excecute(TITLE, URL);
        for(String idea : contents.split("[\\.\\?!]+")) {

            String question = idea;
            ParagraphInfo answer = new TestChatBot().getAnswerBlog(sentences,URL, usedURLs,contents,question, false, TestChatBot.NO_SUM);

            System.out.println("3fw0pfa30"+answer.getInfo());
            topics.add(answer);
            usedURLs.add(answer.getPub());
        }

        ArrayList<ParagraphInfo> fullDoc = new ArrayList<>();

        for(ParagraphInfo sentence : topics)
        {
            if(sentence.getText().trim().isEmpty()||sentence.getPub().trim().isEmpty()
                    ||contents.toLowerCase().contains(sentence.getText().toLowerCase().trim()))
                continue;
            ParagraphInfo paragraphInfo = new ParagraphInfo(new general.chat.Paraphrase2().paraphrase(sentence.getText()),sentence.getPub(),"");
            //ParagraphInfo paragraphInfo = new ParagraphInfo(sentence.getText(),sentence.getPub(),"");
            // Jsoup.connect(sentence.getPub()).get().title()
            System.out.println(paragraphInfo.getInfo());
            fullDoc.add(paragraphInfo);
        }

        return fullDoc;
    }
    public String webContents(String url) throws IOException {
        String wholeText = "";
        Document doc = Jsoup.connect(url).get();
        System.out.println("WAITING FOR JSOUP");
        // Select the <p> Elements from the document
        Elements paragraphs = doc.select("p");
        System.out.println("WAITING FOR SELECT(P)");
        // For each selected <p> element, print out its text

        for (Element p : paragraphs) {
            //System.out.println(e.text());
            if (!p.text().contains(":") && !p.text().toLowerCase().contains("welcome") && !p.text().toLowerCase().contains("is a song")) {

                wholeText += p.text().replaceAll("\\[[0-9]+\\]", "");
                System.out.println("Adding more data..." + p.text());
            }
        }
        return wholeText;
    }
    public String sum(String rawData) throws Exception {
        new general.chat.Paraphrase2().init();
        String xml11pattern = "[|&;$%@\"<>\\(\\)\\+,]+";
        rawData = rawData.replaceAll("[^\\w^\\d^\\.^\\?]+"," ");
        final HttpClient httpClient = new HttpClient();
        final PostMethod postMethod = new PostMethod("https://textanalysis-text-summarization.p.mashape.com/text-summarizer");
        postMethod.addRequestHeader("X-Mashape-Key", "rSRchG7r8QmshCMVF5zTVA5YZ4QUp1PYMyVjsnsV8fq3c1Sa6c");
        postMethod.addRequestHeader("Content-Type", "application/json");
        postMethod.addRequestHeader("Accept", "application/json");
        //Charset.forName("UTF-8").encode(rawData);
        String pattern = "\"";
        System.out.println(((rawData)));
        postMethod.setRequestBody("{\"url\":\"\",\"text\":\""+rawData+"\",\"sentnum\":3}");

        httpClient.executeMethod(postMethod);
        String jsonResponse = IOUtils.toString(postMethod.getResponseBodyAsStream(), Encoder.UTF_8);
        System.out.println(jsonResponse);;
        JSON jsonObj = JSON.parse(jsonResponse);
        JSON arr = jsonObj.get("sentences");
        ArrayList<String> answers = new ArrayList<>();
//        String originalArticle = new String(Files.readAllBytes(Paths.get("D:\\permutations-june-19-2-aug-25\\permutations\\src\\general\\chat\\article.txt")));
        for(int i =0;i< arr.length();i++)
        {
            /*String answer = TestChatBot.getAnswerSummary(String.valueOf(arr.get(i)), false, TestChatBot.SUM
            ,rawData);
            String idea = answer.replaceAll("\\n+"," ").trim();
            idea = idea.substring(idea.indexOf("<f39j8f9sa9jf>"));
            if(idea.length() < 10)
                continue;
                */
            answers.add(String.valueOf(arr.get(i)) +".");

        }
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        String summary = "";
        for(String ans : answers)
        {
            System.out.println(ans);
            summary += ans;
        }
        //String answer = TestChatBot.getAnswerBlog(question, false, NO_SUM);
        postMethod.releaseConnection();
        return summary;
    }
    public String sumOne(String rawData) throws Exception {
        new general.chat.Paraphrase2().init();
        String xml11pattern = "[|&;$%@\"<>\\(\\)\\+,]+";
        rawData = rawData.replaceAll("[^\\w^\\d^\\.^\\?]+"," ");
        final HttpClient httpClient = new HttpClient();
        final PostMethod postMethod = new PostMethod("https://textanalysis-text-summarization.p.mashape.com/text-summarizer");
        postMethod.addRequestHeader("X-Mashape-Key", "rSRchG7r8QmshCMVF5zTVA5YZ4QUp1PYMyVjsnsV8fq3c1Sa6c");
        postMethod.addRequestHeader("Content-Type", "application/json");
        postMethod.addRequestHeader("Accept", "application/json");
        //Charset.forName("UTF-8").encode(rawData);
        String pattern = "\"";
        System.out.println(((rawData)));
        postMethod.setRequestBody("{\"url\":\"\",\"text\":\""+rawData+"\",\"sentnum\":1}");

        httpClient.executeMethod(postMethod);
        String jsonResponse = IOUtils.toString(postMethod.getResponseBodyAsStream(), Encoder.UTF_8);
        System.out.println(jsonResponse);;
        JSON jsonObj = JSON.parse(jsonResponse);
        JSON arr = jsonObj.get("sentences");
        ArrayList<String> answers = new ArrayList<>();
//        String originalArticle = new String(Files.readAllBytes(Paths.get("D:\\permutations-june-19-2-aug-25\\permutations\\src\\general\\chat\\article.txt")));
        for(int i =0;i< arr.length();i++)
        {
            /*String answer = TestChatBot.getAnswerSummary(String.valueOf(arr.get(i)), false, TestChatBot.SUM
            ,rawData);
            String idea = answer.replaceAll("\\n+"," ").trim();
            idea = idea.substring(idea.indexOf("<f39j8f9sa9jf>"));
            if(idea.length() < 10)
                continue;
                */
            answers.add(String.valueOf(arr.get(i)) +".");

        }
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        String summary = "";
        for(String ans : answers)
        {
            System.out.println(ans);
            summary += ans;
        }
        //String answer = TestChatBot.getAnswerBlog(question, false, NO_SUM);
        postMethod.releaseConnection();
        return summary;
    }
}
