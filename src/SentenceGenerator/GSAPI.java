package SentenceGenerator;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adil on 04/07/15.
 */

public class GSAPI {
    public static List<GoogleResults2.Result> main(String[] args) throws Exception {
        String address = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
        String query = args[0];
        String charset = "UTF-8";

        URL url = new URL(address + URLEncoder.encode(query, charset));
        Reader reader = new InputStreamReader(url.openStream(), charset);
        GoogleResults2 results = new Gson().fromJson(reader, GoogleResults2.class);
        try {
            int total = results.getResponseData().getResults().size();
            System.out.println("total: " + total);

            // Show title and URL of each results
            for (int i = 0; i <= total - 1; i++) {
                System.out.println("Title: " + results.getResponseData().getResults().get(i).getTitle());
                System.out.println("URL: " + results.getResponseData().getResults().get(i).getUrl() + "\n");

            }
            return results.getResponseData().getResults();
        }catch (NullPointerException e)
        {
            /*List<String> what = StanfordParser.findNoun2(args[0]);
            String whats ="";
            for(String word:what)
            {
                whats+=word+" ";
            }
            main(new String[]{whats.trim()});
            */
            System.out.println("null pointer exception!\n" +
                    "ERROR: Google Search API not responding");
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}

