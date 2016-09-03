package general;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by corpi on 2016-06-08.
 */
public class curlexample {
    public static void main(String[] args) throws IOException {
        URL url = new URL("https://search1.opensearchserver.net/indexes/8a4bbe15-a86a-39c2-b531-efd900cdd253/hi");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
            for (String line; (line = reader.readLine()) != null;) {
                System.out.println(line);
            }
        }
    }
}
