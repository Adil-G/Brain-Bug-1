package general;

import com.gtranslate.Audio;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;

import javax.sound.sampled.*;
import java.applet.AudioClip;
import java.io.*;

/**
 * Created by corpi on 2016-06-10.
 */
public class TestTTS {
    public static void main(String[] args) throws IOException {
        speak("my name is adil");
    }
    public static void speak(String text) throws IOException {
        text = text.replaceAll("\\[.*?\\]","").replaceAll("[^\\w^\\s]+"," ").replaceAll("\\[.*?\\]","");
        try {
            iSpeechTTS h = new iSpeechTTS("5ab37f2a8d571c529fc8744a60b6e0eb", false, "C:\\Users\\corpi\\Music\\");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        Runtime.getRuntime().exec(""+"C:\\Program Files (x86)\\VideoLAN\\VLC\\vlc.exe"+" "+"C:\\Users\\corpi\\Music\\tts.wav"+"");
    }
    // IBM ID = garadadil@hotmaill.com, superfire777 or 000777
    public static void speak2(String text) throws IOException {
        System.out.println("Answer --> "+text);
        /*
        text = text.replaceAll("\\[.*?\\]","").replaceAll("[^\\w^\\s]+"," ");
        TextToSpeech service = new TextToSpeech();
        service.setUsernameAndPassword("a5853bea-0f83-4c4e-8d02-88a0ed86a531", "5XFYo7vxsuZQ");

        File audio = new File("C:\\Users\\corpi\\Music\\test.wav");
        InputStream transcript = service.synthesize(text, Voice.EN_LISA);

        FileOutputStream out = new FileOutputStream(audio);

        byte[] buffer = new byte[2048];
        int read;
        while ((read = transcript.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        Runtime.getRuntime().exec(""+"C:\\Program Files (x86)\\VideoLAN\\VLC\\vlc.exe"+" "+audio.getAbsolutePath()+"");
        */
    }
    public static void speak2NEW(String text) throws IOException {
        text = text.replaceAll("[^\\w^\\s^\\d]+"," ").replaceAll("\\s+"," ");
        TextToSpeech service = new TextToSpeech();
        service.setUsernameAndPassword("a5853bea-0f83-4c4e-8d02-88a0ed86a531", "5XFYo7vxsuZQ");

        File audio = new File("C:\\Users\\corpi\\Music\\test.wav");
        InputStream transcript = service.synthesize(text, Voice.EN_LISA);

        FileOutputStream out = new FileOutputStream(audio);

        byte[] buffer = new byte[2048];
        int read;
        while ((read = transcript.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        Runtime.getRuntime().exec(""+"C:\\Program Files (x86)\\VideoLAN\\VLC\\vlc.exe"+" "+audio.getAbsolutePath()+"");
    }
    public static byte[] getBytes(InputStream is) throws IOException {

        int len;
        int size = 1024;
        byte[] buf;

        if (is instanceof ByteArrayInputStream) {
            size = is.available();
            buf = new byte[size];
            len = is.read(buf, 0, size);
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            buf = new byte[size];
            while ((len = is.read(buf, 0, size)) != -1)
                bos.write(buf, 0, len);
            buf = bos.toByteArray();
        }
        return buf;
    }
}
