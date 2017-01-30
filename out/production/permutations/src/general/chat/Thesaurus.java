package general.chat;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Scanner;

import org.json.simple.*; // json package, download at http://code.google.com/p/json-simple/

public class Thesaurus {
    public static void main(String[] args) {
// NOTE: replace test_only with your own key 
        SendRequest request = new SendRequest((new Scanner(System.in)).nextLine(), "en_US", "xTuRi7OxPOyuqHaMBFkJ", "json");
        System.out.println(request.synonyms);
    }
    public HashSet<String> getSynonyms(String input, boolean isDeep)
    {
        if(!isDeep)
            return new HashSet<String>();
        else
        {

            SendRequest request = new SendRequest(input, "en_US", "xTuRi7OxPOyuqHaMBFkJ", "json");
            System.out.println(request.synonyms);
            return request.synonyms;

        }
    }
} // end of Thesaurus 

class SendRequest {
    final String endpoint = "http://thesaurus.altervista.org/thesaurus/v1";
    HashSet<String> synonyms;
    public SendRequest(String word, String language, String key, String output) {
        synonyms = new HashSet<>();
        try {
            URL serverAddress = new URL(endpoint + "?word="+URLEncoder.encode(word, "UTF-8")+"&language="+language+"&key="+key+"&output="+output);
            HttpURLConnection connection = (HttpURLConnection)serverAddress.openConnection();
            connection.connect();
            int rc = connection.getResponseCode();
            if (rc == 200) {
                String line = null;
                BufferedReader br = new BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null)
                    sb.append(line + '\n');
                JSONObject obj = (JSONObject) JSONValue.parse(sb.toString());
                JSONArray array = (JSONArray)obj.get("response");
                for (int i=0; i < array.size(); i++) {
                    JSONObject list = (JSONObject) ((JSONObject)array.get(i)).get("list");
                    String[] lines = list.get("synonyms").toString().split("\\|");

                    for(String linex : lines)
                    {

                        if(linex.contains(" (similar term)")) {
                            //System.out.println(linex.replaceAll(" \\(similar term\\)", "").trim());
                            this.synonyms.add(linex.replaceAll(" \\(similar term\\)", "").trim());
                        }
                    }

                    System.out.println();
                }

            } else System.out.println("HTTP error:"+rc);
            connection.disconnect();
        } catch (java.net.MalformedURLException e) {
            e.printStackTrace();
        } catch (java.net.ProtocolException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
} // end of SendRequest