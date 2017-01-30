package general.chat;

import com.freebase.json.JSON;
import org.json.simple.parser.ParseException;

import java.net.*;
        import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Request {
    public static void main(String[] args) throws Exception {
        IFramePair iFrame = new Request().getAds("computers");
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.out.println(iFrame);
        System.exit(0);

        //System.out.println("shoes news".replaceAll("\\bnews\\b",""));

    }
    public String[] getIFrame8(String productName) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        productName = productName.replaceAll("[^\\w^\\d^\\.^\\?]+"," ").replaceAll("\\s","+");

        SignedRequestHelper help = new SignedRequestHelper();
        Map<String, String> params = new TreeMap<>();
        params.put("Service","AWSECommerceService");
        params.put("Operation","ItemSearch");
        params.put("AWSAccessKeyId","AKIAJZCA7JRA4JM533CQ");
        params.put("AssociateTag","addyapps-20");
        params.put("SearchIndex","All");
        params.put("Keywords",productName+"\n");
        URL yahoo = new URL(help.sign(params));
        URLConnection yc = yahoo.openConnection();
        String bottomHalf = "";
        String topHalf = "";
        ArrayList<String> productList =  ReadXML.getASIN8(yc.getInputStream());
        int count = 0;
        for(String ASIN : productList) {
            String iframe =
                    "<iframe style=\"width:120px;height:240px;\" marginwidth=\"0\" marginheight=\"0\" scrolling=\"no\" frameborder=\"0\" src=\"//ws-na.amazon-adsystem.com/widgets/q?ServiceVersion=20070822&OneJS=1&Operation=GetAdHtml&MarketPlace=US&source=ac&ref=qf_sp_asin_til&ad_type=product_link&tracking_id=addyapps-20&marketplace=amazon&region=US&placement=" + ASIN + "&asins=" + ASIN + "&linkId=4069cf5c911c691d6fa51cd45b7ea2e8&show_border=false&link_opens_in_new_window=false&price_color=333333&title_color=0066c0&bg_color=ffffff\">\n" +
                            "    </iframe>";
            //System.out.println(iframe);
            if(count++<4)
                bottomHalf+=iframe;
            else
                topHalf+=iframe;
        }
        if(topHalf.trim().isEmpty()&&bottomHalf.trim().isEmpty())
            return null;
        else
            return  new String[]{topHalf,bottomHalf};
    }
    public String getIFrame4(String productName) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        productName = productName.replaceAll("[^\\w^\\d^\\.^\\?]+"," ").replaceAll("\\s","+");

        SignedRequestHelper help = new SignedRequestHelper();
        Map<String, String> params = new TreeMap<>();
        params.put("Service","AWSECommerceService");
        params.put("Operation","ItemSearch");
        params.put("AWSAccessKeyId","AKIAJZCA7JRA4JM533CQ");
        params.put("AssociateTag","addyapps-20");
        params.put("SearchIndex","All");
        params.put("Keywords",productName+"\n");
        URL yahoo = new URL(help.sign(params));
        URLConnection yc = yahoo.openConnection();
        String bottomHalf = "";
        ArrayList<String> productList =  ReadXML.getASIN4(yc.getInputStream());
        for(String ASIN : productList) {
            String iframe =
            "<iframe style=\"width:120px;height:240px;\" marginwidth=\"0\" marginheight=\"0\" scrolling=\"no\" frameborder=\"0\" src=\"//ws-na.amazon-adsystem.com/widgets/q?ServiceVersion=20070822&OneJS=1&Operation=GetAdHtml&MarketPlace=US&source=ac&ref=qf_sp_asin_til&ad_type=product_link&tracking_id=addyapps-20&marketplace=amazon&region=US&placement=" + ASIN + "&asins=" + ASIN + "&linkId=4069cf5c911c691d6fa51cd45b7ea2e8&show_border=false&link_opens_in_new_window=false&price_color=333333&title_color=0066c0&bg_color=ffffff\">\n" +
                    "    </iframe>";
            //System.out.println(iframe);
            bottomHalf+=iframe;
        }
        if(!bottomHalf.isEmpty()) {
            return  bottomHalf;
        }
        else
            return null;
    }
    public IFramePair getAds(String title) throws IOException, NoSuchAlgorithmException, InvalidKeyException, ParseException {
        ArrayList<String> iFrames = new ArrayList<>();
        String input = title;
        input = input.replaceAll("[^\\w^\\d^\\.^\\?]+", " ").replaceAll("\\s", "+");
        try {
            String[] iFrameOriginal = new String[]{};//getIFrame8(input);
            if (false && iFrameOriginal != null) {
                if(!iFrameOriginal[0].trim().isEmpty())
                    iFrames.add(iFrameOriginal[0]);
                if(!iFrameOriginal[1].trim().isEmpty())
                    iFrames.add(iFrameOriginal[1]);
                IFramePair iFramePair = new IFramePair("", "");

                if (iFrames.size() == 0) {
                    // Do nothing.
                } else if (iFrames.size() == 1) {
                    iFramePair.setTop(iFrames.get(0));
                } else if (iFrames.size() > 1) {
                    iFramePair.setTop(iFrames.get(0));
                    iFramePair.setBottom(iFrames.get(1));
                }
                return iFramePair;
            } else {
                System.out.println("using haven");
                String url = "https://api.havenondemand.com/1/api/sync/extractconcepts/v1";
                SignedRequestHelperHPE help = new SignedRequestHelperHPE();
                Map<String, String> params = new TreeMap<>();
                params.put("text", input);
                params.put("apikey", "9119a2a4-d399-46cf-914d-18177075a9d9");
                URL yahoo = new URL(help.sign(params));
                URLConnection yc = yahoo.openConnection();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                yc.getInputStream()));
                String inputLine;
                String output = "";
                while ((inputLine = in.readLine()) != null)
                    output += inputLine;
                in.close();
                System.out.println(output);
                JSON jsonObject = JSON.parse(output);

                for (int i = 0; i < jsonObject.get("concepts").length(); i++) {
                    try {
                        System.out.println(jsonObject.get("concepts").get(i).get("concept"));
                        String concept = jsonObject.get("concepts").get(i).get("concept").toString();
                        String iFrame = getIFrame4(concept);
                        if (iFrame != null) {
                            iFrames.add(iFrame);
                        }
                    } catch (Exception e) {

                    }
                }

                IFramePair iFramePair = new IFramePair("", "");

                if (iFrames.size() == 0) {
                    // Do nothing.
                } else if (iFrames.size() == 1) {
                    iFramePair.setTop(iFrames.get(0));
                } else if (iFrames.size() > 1) {
                    iFramePair.setTop(iFrames.get(0));
                    iFramePair.setBottom(iFrames.get(1));
                }
                return iFramePair;
            }
        }catch(Exception o)
        {
            o.printStackTrace();
            return new IFramePair("","");
        }
    }
}