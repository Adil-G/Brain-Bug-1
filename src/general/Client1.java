package general;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


public class Client1 {

    private String apikey = "9119a2a4-d399-46cf-914d-18177075a9d9";
    private String url = "https://api.havenondemand.com/1/api/sync/extractconcepts/v1";

    public static org.json.JSONArray extractConcept(String input){
        input = input.replaceAll("\\s+","+");
        HttpClient httpclient = new DefaultHttpClient();
        //url += "?apikey="+apikey+"&text="+input+"";
        String url = "https://api.havenondemand.com/1/api/sync/extractconcepts/v1?text="+input+"&apikey=9119a2a4-d399-46cf-914d-18177075a9d9";
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = null;

        try {

            response = (CloseableHttpResponse) httpclient.execute(httpget);
            StatusLine statusLine = response.getStatusLine();
            System.out.println("StatusLine: "+statusLine.toString());
            //System.out.println("StatusLine(Code Phrase): "+statusLine.getStatusCode()+" "+statusLine.getReasonPhrase());

            HttpEntity entity = response.getEntity();
            if (entity != null) {

                long len = entity.getContentLength();
                if (len != -1 && len < 2048) {
                    String body = EntityUtils.toString(entity);
                    System.out.println(body.toString());
                    JSONObject obj = new JSONObject(body);
                    org.json.JSONArray pageName = obj.getJSONArray("concepts");
                    return pageName;
                } else {

                }
                //entity.writeTo(System.out);
            }
        }catch(ClientProtocolException cpe){
            cpe.printStackTrace();
        }catch(IOException ioe){
            ioe.printStackTrace();
        } finally {
            /*try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            */
        }
        return new  org.json.JSONArray();
    }
    public static String gogo(String input){
        input = input.replaceAll("\\s+","+");
        HttpClient httpclient = new DefaultHttpClient();
        //url += "?apikey="+apikey+"&text="+input+"";
        String url = "https://api.havenondemand.com/1/api/sync/extractconcepts/v1?text="+input+"&apikey=9119a2a4-d399-46cf-914d-18177075a9d9";
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = null;

        try {

            response = (CloseableHttpResponse) httpclient.execute(httpget);
            StatusLine statusLine = response.getStatusLine();
            System.out.println("StatusLine: "+statusLine.toString());
            //System.out.println("StatusLine(Code Phrase): "+statusLine.getStatusCode()+" "+statusLine.getReasonPhrase());

            HttpEntity entity = response.getEntity();
            if (entity != null) {

                long len = entity.getContentLength();
                if (len != -1 && len < 2048) {
                    String body = EntityUtils.toString(entity);
                    JSONObject obj = new JSONObject(body);
                    org.json.JSONArray pageName = obj.getJSONArray("concepts");
                    return pageName.getJSONObject(0).getString("concept");
                } else {

                }
                //entity.writeTo(System.out);
            }
        }catch(ClientProtocolException cpe){
            cpe.printStackTrace();
        }catch(IOException ioe){
            ioe.printStackTrace();
        } finally {
            /*try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            */
        }
        return new  String();
    }
    public static void main(String[] args) {
        //Client1 cl1 = new Client1();
        Client1.extractConcept("wolf");
        Client1.extractConcept("elephant");
        //String string = cl1.extractConcept("what is the highest mountain in the world");
        //System.out.print(string);

        //gson.toJson(123, System.out);
    }
}