package SentenceGenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by swaggerton on 18/08/15.
 */
public class InterruptThread implements Runnable {

    HttpURLConnection con;
    String word;
    String broken=null;
    List<String> x = new ArrayList<String>();
    public InterruptThread(HttpURLConnection con,String mword) {
        this.con = con;
        this.word = mword;
    }
    public List<String> getList()
    {
        return this.x;
    }
    public void run() {
        x=  new ArrayList<String>();
        /*
        try {
            Thread.sleep(5000); // or Thread.sleep(con.getConnectTimeout())
        } catch (InterruptedException e) {

        }
        */
        con.getConnectTimeout();
        con.disconnect();
        System.out.println("Timer thread forcing to quit connection for: " + word);
        this.broken = "";
        /*
        try {
            this.x = listRymes(word);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }
    public static List<String> listSynonymsBACKUP(String lastWord) throws IOException {
        String url = "http://words.bighugelabs.com/" + lastWord;
        String html = getUrlSourceBACKUP(url);
        //System.out.println(html);

        Pattern p = Pattern.compile(".*?(rhymes with</h3>.*?.*?$).*?");
        Matcher m = p.matcher(html);
        if(m.matches()){
            html = m.group(1);
            //System.out.println("adsgasgoijaov"+html);
        }
        p = Pattern.compile(">([a-z]+)</a>");
        m = p.matcher(html);
        List<String> rjhymes = new ArrayList<String>();
        while (m.find()) {
            String rhyme1 = m.group(1);
            System.out.println("Rhyme = "+rhyme1);
            rjhymes.add(rhyme1);
        }
        //TreeMap<Integer,String> set = new TreeMap<Integer,String>(Collections.reverseOrder());
        if (rjhymes.size() > 1) {
            int maxSize = 10000;
            if(rjhymes.size()<maxSize) {
                maxSize = rjhymes.size();
            }

            rjhymes = pickNRandomBACKUP(rjhymes, maxSize-1);
        } else {
            //System.out.println(usedPhrases7.get(i));
            //System.out.println(usedPhrases5.get(i + 1));
        }
        return rjhymes;
    }
    public static List<String> pickNRandomBACKUP(List<String> lst, int n) {
        if(lst.size()>=n) {
            List<String> copy = new LinkedList<String>(lst);
            Collections.shuffle(copy);
            return copy.subList(0, n);
        }
        else
        {
            return lst;
        }
    }
    private static String getUrlSourceBACKUP(String url) throws IOException {
        URL yahoo = new URL(url);
        URLConnection yc = yahoo.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                yc.getInputStream(), "UTF-8"));
        String inputLine;
        StringBuilder a = new StringBuilder();
        while ((inputLine = in.readLine()) != null)
            a.append(inputLine);
        in.close();

        return a.toString();
    }
}