import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.james.mime4j.message.SingleBody;
import org.apache.commons.logging.LogFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
public class HttpFileUpload {
    public static void main(String args[]) throws Exception {
        String htto = search("/home/adil/Desktop/IMG_20150625_144534737.jpg");
        //System.out.println(htto);
        //UrlReader.main(new String[]{"http://www.bing.com/images/search?q=payne&go=&form=QBLH&scope=images&filt=all&first=10"});
        URL url = new URL(htto);
        URLConnection hc = url.openConnection();
        hc.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.0.3; ko-kr; LG-L160L Build/IML74K) AppleWebkit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");

        //System.out.println(hc.getContentType());
        BufferedReader in = new BufferedReader(new InputStreamReader(
                hc.getInputStream()));
        String wholeHtml = "";
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            wholeHtml += inputLine;
            System.out.println(inputLine);
        }
        in.close();
        Pattern p = Pattern.compile(".*?\"(http://.*?)\".*?");
        Matcher m = p.matcher(wholeHtml);
        List<String> links= new ArrayList<String>();
        while(m.find())
        {
            if(!m.group(1).toLowerCase().contains("google")) {
                links.add(m.group(1));
                System.out.println("ADSFASFA=" + m.group(1));
            }
        }
        /*URL yahoo = new URL(htto);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        yahoo.openStream()));

        String inputLine;

        while ((inputLine = in.readLine()) != null)
            System.out.println(inputLine);

        in.close();
*/

    }
    public static String search(String imgPath)
    {
        String searchPage = "";
        try {
            HttpClient client = new DefaultHttpClient();
            String url="https://www.google.co.in/searchbyimage/upload";
            String imageFile=imgPath;
            HttpPost post = new HttpPost(url);

            MultipartEntity entity = new MultipartEntity();
            entity.addPart("encoded_image", new FileBody(new File(imageFile)));
            entity.addPart("image_url", new StringBody(""));
            entity.addPart("image_content", new StringBody(""));
            entity.addPart("filename", new StringBody(""));
            entity.addPart("h1", new StringBody("en"));
            entity.addPart("bih", new StringBody("179"));
            entity.addPart("biw", new StringBody("1600"));

            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            //System.out.println("RESPONSE " + response.getAllHeaders()[0]);
            searchPage = response.getAllHeaders()[0].toString();



            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            boolean searched = false;
            String line = "";
            while ((line = rd.readLine()) != null) {
                if (line.indexOf("HREF") > 0)
                    if (!searched) {
                        //System.out.println(line.substring(8));
                        searchPage = line.substring(8);
                    }
            }
            Pattern p = Pattern.compile(".*?\"(.*?)\".*?");
            Matcher m  = p.matcher(searchPage);
            if(m.matches()) {
                searchPage = m.group(1);
                //searchPage = searchPage.split("-")[0];
            }


        }catch (ClientProtocolException cpx){
            cpx.printStackTrace();
        }catch (IOException ioex){
            ioex.printStackTrace();
        }
        return searchPage;
    }
}