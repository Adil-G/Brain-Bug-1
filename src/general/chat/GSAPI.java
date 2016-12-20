package general.chat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by adil on 04/07/15.
 */

public class GSAPI {
    public static ArrayList<String> main(String[] args) throws Exception {
        String google = "http://www.google.com/search?q=";
        String search = args[0];
        String charset = "UTF-8";
        String userAgent = "ExampleBot 1.0 (+http://example.com/bot)"; // Change this to your company's name and bot homepage!

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
            if(!url.contains("http://www.theweeklynews.ca/news-story/7003792-top-honours-for-st-thomas-coffee-roasters"))
                arlsit.add(url);
        }
        return arlsit;
    }

}

