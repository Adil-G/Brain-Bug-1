package SentenceGenerator;

import com.gtranslate.Audio;
import com.gtranslate.Language;
import javazoom.jl.decoder.JavaLayerException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by swaggerton on 07/10/15.
 */
public class TextToSpeech {
    private static final String TEXT_TO_SPEECH_SERVICE =
            "http://translate.google.com/translate_tts";
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:11.0) " +
                    "Gecko/20100101 Firefox/11.0";
    public static void main(String[] args) throws Exception {
        textToSpeech("hi", 20);
    }
    public static void go(String language, String text) throws Exception {
        // Create url based on input params
        String strUrl = TEXT_TO_SPEECH_SERVICE + "?" +
                "tl=" + language + "&q=" + text;
        URL url = new URL(strUrl);

        // Etablish connection
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // Get method
        connection.setRequestMethod("GET");
        // Set User-Agent to "mimic" the behavior of a web browser. In this
        // example, I used my browser's info
        connection.addRequestProperty("User-Agent", USER_AGENT);
        connection.connect();

        // Get content
        BufferedInputStream bufIn =
                new BufferedInputStream(connection.getInputStream());
        byte[] buffer = new byte[1024];
        int n;
        ByteArrayOutputStream bufOut = new ByteArrayOutputStream();
        while ((n = bufIn.read(buffer)) > 0) {
            bufOut.write(buffer, 0, n);
        }

        // Done, save data
        File output = new File("output.mp3");
        BufferedOutputStream out =
                new BufferedOutputStream(new FileOutputStream(output));
        out.write(bufOut.toByteArray());
        out.flush();
        out.close();
        System.out.println("Done");
    }
    public static void textToSpeech(String bigText, int maxWords) throws Exception {
        Audio audio = Audio.getInstance();
        bigText = bigText.replaceAll("\\W", " ");
        String[] bigWords = bigText.split(" ");
        List<String[]> sayThis = splitArray(bigWords, maxWords);
        for (String[] list : sayThis) {
            String whatToSay = "";
            for (String word : list) {
                whatToSay += word + " ";
            }
            whatToSay = whatToSay.trim().replaceAll("'s", "s").replaceAll(" s ", "s ").replaceAll("\\[.*?\\]", "");

            go(Language.ENGLISH, whatToSay);
        }
    }

    public static <T> List<T[]> splitArray(String[] items, int maxSubArraySize) {
        List<T[]> result = new ArrayList<T[]>();
        if (items == null || items.length == 0) {
            return result;
        }

        int from = 0;
        int to = 0;
        int slicedItems = 0;
        while (slicedItems < items.length) {
            to = from + Math.min(maxSubArraySize, items.length - to);
            T[] slice = Arrays.copyOfRange((T[]) items, from, to);
            result.add(slice);
            slicedItems += slice.length;
            from = to;
        }
        return result;
    }
}
