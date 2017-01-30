package general.chat; /**
 * Created by corpi on 2016-07-09.
 */

import general.graph.theory.ParagraphInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;

public class MainGUI {

    String      appName     = "Colt Chat v0.1";
    MainGUI     mainGUI;
    JFrame      newFrame    = new JFrame(appName);
    JButton     sendMessage;
    JTextField  messageBox;
    JTextArea   chatBox;
    JTextField  usernameChooser;
    JFrame      preFrame;
    String      chatbotName = "Petricia";
    int         size        = (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()*0.7);
    boolean IS_DEEP = false;
    public static String personal = "personalChat.txt";
    public static String web = "webData.txt";
    public static String local = "local.txt";
    public static boolean useNewData = false;
    //public static String directory = "C:\\Users\\corpi\\Documents\\robot\\permutations-december-25\\AWS-permutations-june-19-2-current-build-as-of-july-17\\AWS-permutations-june-19-2-current-build-as-of-july-15\\AWS-permutations-june-19-2-current-build-as-of-june-24\\permutations-june-19-2\\permutations\\openNLP\\";
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager
                            .getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                MainGUI mainGUI = new MainGUI();


                mainGUI.dataChooser(mainGUI);
            }
        });
    }
    public static File chooseFile()
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        JFrame frame0 = new JFrame("Choose a file");
        int result = fileChooser.showOpenDialog(frame0);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return selectedFile;
        }
        return  null;
    }
    public void dataChooser(MainGUI mainGUI)
    {

        JFrame currFrame = new JFrame(appName);
        currFrame.setLayout(new GridLayout(1,3));
        Button button1 = new Button("Personal Bot");
        button1.setFont(new Font("Serif", Font.PLAIN, 25));
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currFrame.setVisible(false);
                mainGUI.preDisplay();
            }
        } );

        currFrame.add(button1);
        currFrame.setSize(size, size);
        currFrame.setVisible(true);

    }
    public void preDisplay() {
        newFrame.setVisible(false);
        preFrame = new JFrame(appName);
        usernameChooser = new JTextField(15);
        usernameChooser.setFont(new Font("Serif", Font.PLAIN, 25));
        JLabel chooseUsernameLabel = new JLabel("Pick a username:");
        JButton enterServer = new JButton("Enter Chat Server");
        enterServer.addActionListener(new enterServerButtonListener());
        JPanel prePanel = new JPanel(new GridBagLayout());

        GridBagConstraints preRight = new GridBagConstraints();
        preRight.insets = new Insets(0, 0, 0, 10);
        preRight.anchor = GridBagConstraints.EAST;
        GridBagConstraints preLeft = new GridBagConstraints();
        preLeft.anchor = GridBagConstraints.WEST;
        preLeft.insets = new Insets(0, 10, 0, 10);
        // preRight.weightx = 2.0;
        preRight.fill = GridBagConstraints.HORIZONTAL;
        preRight.gridwidth = GridBagConstraints.REMAINDER;

        prePanel.add(chooseUsernameLabel, preLeft);
        prePanel.add(usernameChooser, preRight);
        preFrame.add(BorderLayout.CENTER, prePanel);
        preFrame.add(BorderLayout.SOUTH, enterServer);
        preFrame.setSize(size, size);
        preFrame.setVisible(true);

    }

    public void display() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.BLUE);
        southPanel.setLayout(new GridBagLayout());

        messageBox = new JTextField(30);
        messageBox.setFont(new Font("Serif", Font.PLAIN, 25));
        messageBox.requestFocusInWindow();

        sendMessage = new JButton("Send Message");
        sendMessage.addActionListener(new sendMessageButtonListener());

        chatBox = new JTextArea();
        chatBox.setEditable(false);
        chatBox.setFont(new Font("Serif", Font.PLAIN, 25));
        chatBox.setLineWrap(true);

        mainPanel.add(new JScrollPane(chatBox), BorderLayout.CENTER);

        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.LINE_START;
        left.fill = GridBagConstraints.HORIZONTAL;
        left.weightx = 512.0D;
        left.weighty = 1.0D;

        GridBagConstraints right = new GridBagConstraints();
        right.insets = new Insets(0, 10, 0, 0);
        right.anchor = GridBagConstraints.LINE_END;
        right.fill = GridBagConstraints.NONE;
        right.weightx = 1.0D;
        right.weighty = 1.0D;

        southPanel.add(messageBox, left);
        southPanel.add(sendMessage, right);

        mainPanel.add(BorderLayout.SOUTH, southPanel);

        newFrame.add(mainPanel);
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.setSize(size, size);
        newFrame.setVisible(true);
    }
    public void setSendErrorMessageToUser(String errorMessage) throws Exception {
        chatBox.append("<" + chatbotName + ">:  " +  errorMessage
                + "\n");

    }
    public static PrintStream originalStream = System.out;
    public void setSendMessageToUser(String userResponse, boolean isDeep) throws Exception {


        PrintStream dummyStream    = new PrintStream(new OutputStream(){
            public void write(int b) {
                //NO-OP
            }
        });

        System.setOut(dummyStream);

        ParagraphInfo answer = new TestChatBot().getAnswerWithGUI(userResponse, isDeep);

        System.setOut(originalStream);
/*
.split("- \\(  \\)|" +
                "------------------------------------------------")[0]
 */
        chatBox.append("<" + chatbotName + ">:  " +  answer
                + "\n");
        //new FreeTTS(answer).speak();
        /*
        if(samp==null)
        samp = new SampleIvonaSpeechCloudCreateSpeech();
        if(answer.contains(".")
                || answer.contains("?")
                || answer.contains("!")) {
            ArrayList<String> total = new ArrayList<>();
            int targetWordCount = 20;
            int currentWordCount = 0;
            String targetString = new String();
            for (String sentence : answer.split("\\s+")) {
                targetString += sentence + " ";
                if (currentWordCount++ > targetWordCount || sentence.contains(".")
                        || sentence.contains("?")
                        || sentence.contains("!")) {
                    total.add(targetString);
                    targetString = new String();
                    currentWordCount = 0;
                }
            }
            for(String saythis :total) {
                //samp.text = saythis;
                //samp.speak();
            }
        }
        else
        {
            //samp.text = answer;
            //samp.speak();
        }
        */

    }
    class sendMessageButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            Thread hilo = new Thread(new Runnable() {

                @Override
                public void run() {
                    //messageBox.setFont(new Font());
                    //here your code
                    if (messageBox.getText().length() < 1) {
                        // do nothing
                        messageBox.requestFocusInWindow();
                    } else if (messageBox.getText().equals(".clear")) {
                        chatBox.setText("Cleared all messages\n");
                        messageBox.setText("");
                        messageBox.requestFocusInWindow();
                    } else {
                        String question = messageBox.getText();
                        chatBox.append("<" + username + ">:  " + messageBox.getText()
                                + "\n");

                        messageBox.setText("");
                        messageBox.requestFocusInWindow();
                        try {
                            setSendMessageToUser(question,IS_DEEP);
                        } catch (Exception e) {
                            e.printStackTrace();
                            try {
                                setSendErrorMessageToUser("... Error...");
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            });
            hilo.start();



        }
    }

    String  username;

    class enterServerButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {

            username = usernameChooser.getText();
            if (username.length() < 1) {
                System.out.println("No!");
            } else {
                preFrame.setVisible(false);
                display();
            }
        }

    }
}