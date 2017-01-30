package general.chat;

import com.freebase.json.JSON;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.net.www.http.HttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by adil on 04/07/15.
 */

public class GSAPI {

    public static void main(String[] args) throws Exception {
        //System.out.println(new GSAPI().get(new String[]{"elephant",""}));
        //main2(new String[]{});
        //System.out.println(new GSAPI().get(new String[]{"elephant news article",""}));
        System.out.println(new GSAPI().getSearch(new String[]{"elephant news article",""}));
       /* new GSAPI().postBlog("3672530759325903336","title","<br><br>\n" +
                "The followers are the scheduled events of connection football for the yr 2016 throughout the universe .  (<a href=\"https://en.wikipedia.org/wiki/2016_in_association_football\">https://en.wikipedia.org/wiki/2016_in_association_football</a>)<script data-cfasync='false' type='text/javascript' src='//p189754.clksite.com/adServe/banners?tid=189754_345824_0&amp;tagid=2'></script><br><br><h2><a href=\"http://www.gizbot.com/mobile/features/innovative-smartphones-launched-2016-037146.html\">&nbsp;Source: http://www.gizbot.com/mobile/features/innovative-smartphones-launched-2016-037146.html</a>)</h2>");
    */
    }


    public static void main2 (String args[]) throws IOException {
        String google = "http://www.google.com/search?q=";
        String search = "stackoverflow";
        String charset = "UTF-8";
        String userAgent = "ExampleBot 1.0 (+http://example.com/bot)"; // Change this to your company's name and bot homepage!

        Elements links = Jsoup.connect(google + URLEncoder.encode(search, charset)).userAgent(userAgent).get().select(".g>.r>a");

        for (Element link : links) {
            String title = link.text();
            String url = link.absUrl("href"); // Google returns URLs in format "http://www.google.com/url?q=<url>&sa=U&ei=<someKey>".
            url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), "UTF-8");

            if (!url.startsWith("http")) {
                continue; // Ads/news/etc.
            }

            System.out.println("Title: " + title);
            System.out.println("URL: " + url);
        }
    }
    public static String getHTMLPOST(String ID,String TITLE, String content) throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build(); //Use this instead

        try {

            HttpPost request = new HttpPost("https://www.googleapis.com/blogger/v3/blogs/"+ID+"/posts/");
            request.addHeader("Authorization", "Bearer ya29.Ci_EA8kWY8mhy8RArhVkBRyKF_KotD-1-SSc5BOZHp43czn8wpFk1NkWwZO71E9J3Q");
            request.addHeader("Content-Type", "application/json");
            StringEntity params =new StringEntity("{\n" +
                    "  \"kind\": \"blogger#post\",\n" +
                    "  \"blog\": {\n" +
                    "    \"id\": \""+ID+"\"\n" +
                    "  },\n" +
                    "  \"title\": \""+TITLE+"\",\n" +
                    "  \"content\": \""+content+"\"\n" +
                    "}");

            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            System.out.println(response);
            System.exit(0);
            return response.toString();
            //handle response here...

        }catch (Exception ex) {

            //handle exception here

        } finally {
            //Deprecated
            //httpClient.getConnectionManager().shutdown();
        }
        return "error";
    }
    public static String getHTML(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(50);
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }
    public ArrayList<String> getWiki(String[] args) throws Exception {
        String google = "https://en.wikipedia.org/w/api.php" +
                "?action=opensearch" +
                "&";
        String search = "&search="+args[0]+"&limit=1"+"&namespace=0"+"&format=json";
        String charset = "UTF-8";
        String userAgent = "ExampleBot 1.0 (+http://example.com/bot)"; // Change this to your company's name and bot homepage!
        String url = "https://en.wikipedia.org/w/api.php?action=opensearch&search="+args[0]+"&limit=100&namespace=0&format=json";
        System.out.println(url);
        String jsonString = getHTML(url);
        System.out.println(jsonString);
        JSON json = JSON.parse(jsonString);
        JSON arr = json.get(3);
        ArrayList<String> array = new ArrayList<>();
        for(int i = 0; i< arr.length();i++)
        {
            array.add(arr.get(i).string());
        }
        return  array;
    }
    public static String encodeURIComponent(String s)
    {
        String result = null;

        try
        {
            result = URLEncoder.encode(s, "UTF-8")
                    .replaceAll("\\+", "%20")
                    .replaceAll("\\%21", "!")
                    .replaceAll("\\%27", "'")
                    .replaceAll("\\%28", "(")
                    .replaceAll("\\%29", ")")
                    .replaceAll("\\%7E", "~");
        }

        // This exception should never occur.
        catch (UnsupportedEncodingException e)
        {
            result = s;
        }

        return result;
    }
    public ArrayList<String> postBlog(String ID, String TITLE, String CONTENT) throws Exception {
        TITLE = TITLE.replaceAll("[^\\w^\\d^\\.^\\?]+"," ");
        CONTENT = CONTENT.replaceAll("[^\\.^\\?]+"," ");
        String key = "845fa92d482649c28a6165aaaad8d22c";
        //String url  = "https://newsapi.org/v1/articles?source="+args[0]+"&sortBy=latest&apiKey=";
        //System.out.println(url);
        String jsonString = getHTMLPOST(ID,TITLE,CONTENT);
        System.out.println(jsonString);

        //JSON json = JSON.parse(jsonString);
        //JSON arr = json.get("articles");
        ArrayList<String> array = new ArrayList<>();
        //for(int i = 0; i< arr.length();i++)
       // {
           // array.add(arr.get(i).get("url").string());
        //}
        return  array;
    }
    public ArrayList<String> getSearch(String[] args) throws Exception {
        String google = "https://www.search.com/web?q=";
        String search = args[0];
        String charset = "UTF-8";
        System.out.println("search2");
        String userAgent = "ExampleBot 1.0 (+http://example.com/bot)"; // Change this to your company's name and bot homepage!
        try {
            Document links = Jsoup.connect("https://www.search.com/web?q="+URLEncoder.encode(search, charset)+"&qo=homeSearchBox&qsrc=&ot=organic").userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
                    .timeout(100*1000)
                    .ignoreContentType(true)
                    .get();
            //System.out.println("f39s:"+links.html());
            ArrayList<String> arlsit = new ArrayList<>();
            for (Element link : links.select(".web-result-title-link")) {
                String title = link.text();
                String url = link.absUrl("href"); // Google returns URLs in format "http://www.google.com/url?q=<url>&sa=U&ei=<someKey>".
                url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), "UTF-8");

                if (!url.startsWith("http")) {
                    continue; // Ads/news/etc.
                }

                System.out.println("Title: " + title);
                System.out.println("URL: " + url);
                if (!url.contains("http://www.theweeklynews.ca/news-story/7003792-top-honours-for-st-thomas-coffee-roasters")
                        && !url.equals(args[1]))
                    arlsit.add(url);
            }

            return arlsit;

        }catch (Exception e)
        {
            e.printStackTrace();
            getSearch(args);
        }
        return  new ArrayList<>();
    }
    public ArrayList<String> get(String[] args) throws Exception {
        String google = "http://www.google.com/search?q=";
        String search = args[0];
        String charset = "UTF-8";
        String userAgent = "ExampleBot 1.0 (+http://example.com/bot)"; // Change this to your company's name and bot homepage!
        try {
            Elements links = Jsoup.connect(google + URLEncoder.encode(search, charset)).userAgent(userAgent).get().select(".g>.r>a");
            ArrayList<String> arlsit = new ArrayList<>();
            for (Element link : links) {
                String title = link.text();
                String url = link.absUrl("href"); // Google returns URLs in format "http://www.google.com/url?q=<url>&sa=U&ei=<someKey>".
                url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), "UTF-8");

                if (!url.startsWith("http")) {
                    continue; // Ads/news/etc.
                }

                System.out.println("Title: " + title);
                System.out.println("URL: " + url);
                if (!url.contains("http://www.theweeklynews.ca/news-story/7003792-top-honours-for-st-thomas-coffee-roasters")
                        && !url.equals(args[1]))
                    arlsit.add(url);
            }
            return arlsit;
        }catch (Exception e)
        {
            get(args);
        }
        return  new ArrayList<>();
    }

}

