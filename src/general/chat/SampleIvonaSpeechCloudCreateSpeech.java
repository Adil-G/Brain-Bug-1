package general.chat;
/*
 * Copyright 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 *    http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.net.URL;
        import java.util.ArrayList;

        import com.amazonaws.ClientConfiguration;
        import com.amazonaws.auth.AWSCredentials;
        import com.amazonaws.auth.AWSCredentialsProvider;
        import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
        import com.ivona.services.tts.IvonaSpeechCloudClient;
        import com.ivona.services.tts.model.CreateSpeechRequest;
        import com.ivona.services.tts.model.CreateSpeechResult;
        import com.ivona.services.tts.model.Input;
        import com.ivona.services.tts.model.Voice;import com.amazonaws.auth.AWSCredentials;
        import com.amazonaws.auth.AWSCredentialsProvider;
        import com.sun.media.jfxmedia.Media;
        import com.sun.media.jfxmedia.MediaPlayer;

        import javax.sound.sampled.AudioInputStream;
        import javax.sound.sampled.AudioSystem;
        import javax.sound.sampled.Clip;


/**
 * Class that generates sample synthesis and retrieves audio stream.
 */
public class SampleIvonaSpeechCloudCreateSpeech {

    private static IvonaSpeechCloudClient speechCloud;
    public String text;
    public MP3 mp3;
    public SampleIvonaSpeechCloudCreateSpeech()
    {
        this.text = "hello";
        init();
    }
    private static void init() {
        speechCloud = new IvonaSpeechCloudClient(new IvonaCredentials("XNDOm+scllfB39gvWDkMmeOdWopN9DfT7zQZguNS", "GDNAJOTK6K6CDF27MFMQ"));
        speechCloud.setEndpoint("https://tts.eu-west-1.ivonacloud.com");
    }

    public static void main(String[] args) throws Exception {
        SampleIvonaSpeechCloudCreateSpeech samp = new SampleIvonaSpeechCloudCreateSpeech();
        samp.text = "hello";
        samp.speak();

    }

    public void speak() throws IOException {

        String bestURL = "";
        String outputFileName = "speech.mp3";
        CreateSpeechRequest createSpeechRequest = new CreateSpeechRequest();
        Input input = new Input();
        Voice voice = new Voice();

        voice.setName("Salli");
        input.setData(this.text);

        createSpeechRequest.setInput(input);
        createSpeechRequest.setVoice(voice);
        InputStream in = null;
        FileOutputStream outputStream = null;

        try {

            CreateSpeechResult createSpeechResult = speechCloud.createSpeech(createSpeechRequest);
            //createSpeechResult.setContentType();
            System.out.println("\nSuccess sending request:");
            System.out.println(" content type:\t" + createSpeechResult.getContentType());
            System.out.println(" request id:\t" + createSpeechResult.getTtsRequestId());
            System.out.println(" request chars:\t" + createSpeechResult.getTtsRequestCharacters());
            System.out.println(" request units:\t" + createSpeechResult.getTtsRequestUnits());

            System.out.println("\nStarting to retrieve audio stream:");

            in = createSpeechResult.getBody();
            outputStream = new FileOutputStream(new File(outputFileName));

            byte[] buffer = new byte[2 * 1024];
            int readBytes;

            while ((readBytes = in.read(buffer)) > 0) {
                // In the example we are only printing the bytes counter,
                // In real-life scenario we would operate on the buffer
                System.out.println(" received bytes: " + readBytes);
                outputStream.write(buffer, 0, readBytes);
            }
            bestURL = new File(outputFileName).getAbsolutePath();
            System.out.println("\nFile saved: " + bestURL);

        } finally {
            if (in != null) {

                in.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }

        useMP3(bestURL);
    }
    public void useMP3(String sayThis)
    {
        String filename = sayThis;
        mp3 = new MP3(filename);
        mp3.play();
        while(!mp3.player.isComplete()){}
    }
    public static ArrayList<String> getParts(String string, int partitionSize) {
        ArrayList<String> parts = new ArrayList<String>();
        int len = string.length();
        for (int i=0; i<len; i+=partitionSize)
        {
            parts.add(string.substring(i, Math.min(len, i + partitionSize)));
        }
        return parts;
    }
    public static class IvonaCredentials implements AWSCredentialsProvider{

        public IvonaCredentials(String mSecretKey, String mAccessKey) {
            super();
            this.mSecretKey = mSecretKey;
            this.mAccessKey = mAccessKey;
        }

        private String mSecretKey;
        private String mAccessKey;

        @Override
        public AWSCredentials getCredentials() {
            AWSCredentials awsCredentials = new AWSCredentials() {

                @Override
                public String getAWSSecretKey() {
                    // TODO Auto-generated method stub
                    return mSecretKey;
                }

                @Override
                public String getAWSAccessKeyId() {
                    // TODO Auto-generated method stub
                    return mAccessKey;
                };
            };
            return awsCredentials;
        }

        @Override
        public void refresh() {
            // TODO Auto-generated method stub

        }



    }
}