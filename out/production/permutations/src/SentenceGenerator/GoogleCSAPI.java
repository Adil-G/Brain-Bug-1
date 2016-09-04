package SentenceGenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by corpi on 2016-04-30.
 */

public class GoogleCSAPI {
    final static String REDDIT = "https://www.googleapis.com/customsearch/v1?key=AIzaSyDW9LbJgsvwGwVIXV2g_flvTeCKjn6S_io&cx=009861299925025910586:nu8l6aww458&q=";

    final static String GOOGLE = "https://www.googleapis.com/customsearch/v1?key=AIzaSyDtyVvXYRsfKqgHJM1olTrq1z2ORagxNzg&cx=009861299925025910586:zvig9m12x1a&q=";
    // final static String GOOGLE = "https://www.googleapis.com/customsearch/v1?key=AIzaSyDtyVvXYRsfKqgHJM1olTrq1z2ORagxNzg&cx=009861299925025910586:zvig9m12x1a&q=";
    //old//https://www.googleapis.com/customsearch/v1?key=AIzaSyCMGfdDaSfjqv5zYoS0mTJnOT3e9MURWkU&cx=009861299925025910586:c8kxlwje9du&q=
    final static String DATA ="https://www.googleapis.com/customsearch/v1?key=AIzaSyCMGfdDaSfjqv5zYoS0mTJnOT3e9MURWkU&cx=009861299925025910586:c8kxlwje9du&q=";
    //final static String DATA = "https://www.googleapis.com/customsearch/v1?key=AIzaSyDtyVvXYRsfKqgHJM1olTrq1z2ORagxNzg&cx=009861299925025910586:zvig9m12x1a&q=";
    public static void main(String[] args) throws Exception {


    }

    public static ArrayList<String> getLinks(String query, boolean isDataBase) throws IOException {
        String source;
        if(isDataBase)
            source = DATA;
        else
            source = GOOGLE;
        URL url = new URL(
                source
                 + query);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

        String output;
        ArrayList<String> links = new ArrayList<>();
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {

            if (output.contains("\"link\": \"")) {
                String link = output.substring(output.indexOf("\"link\": \"") + ("\"link\": \"").length(), output.indexOf("\","));
                System.out.println(link);       //Will print the google search links
                links.add(link);
            }
        }
        conn.disconnect();
        return links;
    }
}
