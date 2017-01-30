package general.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class AmazonLInks {

    private final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws Exception {

        AmazonLInks http = new AmazonLInks();

        System.out.println("Testing 1 - Send Http GET request");
        http.sendGet();

        System.out.println("\nTesting 2 - Send Http POST request");
        http.sendPost();

    }

    // HTTP GET request
    private void sendGet() throws Exception {

        String url = "http://webservices.amazon.com/onca/xml?\n" +
                "Service=AWSECommerceService&\n" +
                "Operation=ItemSearch&\n" +
                "AWSAccessKeyId=AKIAJZCA7JRA4JM533CQ&\n" +
                "AssociateTag=addyapps-20&\n" +
                "SearchIndex=Apparel&\n" +
                "Keywords=Shirt\n" +
                "&Timestamp=[2016-12-22T12:00:00Z]\n" +
                "&Signature=4NeGOAZ76Dua1K+NFlYT1raUAt0zd7/CKb1wE1BR";
        SignedRequestHelper help = new SignedRequestHelper();
        Map<String, String> params = new TreeMap<>();
        params.put("Service","AWSECommerceService");
        params.put("Operation","ItemSearch");
        params.put("AWSAccessKeyId","AKIAJZCA7JRA4JM533CQ");
        params.put("AssociateTag","addyapps-20");
        params.put("SearchIndex","Apparel");
        params.put("Keywords","Blue+Shirt\n");
        url = help.sign(params);


        //HttpRequest re = new H
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);

        // add request header
        request.addHeader("User-Agent", USER_AGENT);

        HttpResponse response = client.execute(request);

        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " +
                response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        System.out.println(result.toString());

    }

    // HTTP POST request
    private void sendPost() throws Exception {

        String url = "https://selfsolve.apple.com/wcResults.do";

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        // add header
        post.setHeader("User-Agent", USER_AGENT);

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("sn", "C02G8416DRJM"));
        urlParameters.add(new BasicNameValuePair("cn", ""));
        urlParameters.add(new BasicNameValuePair("locale", ""));
        urlParameters.add(new BasicNameValuePair("caller", ""));
        urlParameters.add(new BasicNameValuePair("num", "12345"));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + post.getEntity());
        System.out.println("Response Code : " +
                response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        System.out.println(result.toString());

    }

}