package SentenceGenerator;

import general.LuceneSnowBallTest;
import general.chat.MainGUI;
import general.graph.theory.Edge;
import general.graph.theory.Graph;
import general.graph.theory.GraphNew_July8;
import general.graph.theory.Vertex;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Adil on 2015-06-04.
 */
public class ComparePhrases {
    public static double compare(String phrase0,String phrase1) throws IOException {
        double outcome;//Authority control GND:
        boolean atleastOneAlphaA = phrase0.matches(".*[a-zA-Z]+.*") && !phrase0.toLowerCase().contains("authority control gnd")&&!phrase0.toLowerCase().contains("privacy policy")
                && !phrase0.toLowerCase().contains("sign up");
        boolean atleastOneAlphaB = phrase1.matches(".*[a-zA-Z]+.*") &&!phrase1.toLowerCase().contains("authority control gnd:")&& !phrase1.toLowerCase().contains("privacy policy")
                && !phrase1.toLowerCase().contains("sign up");
        boolean goodLength = Math.abs(phrase1.length() - phrase0.length()) < 50;
        if(atleastOneAlphaA&&atleastOneAlphaB && goodLength)
        {
            //System.out.println("still processing ...");
            String[] arrayA = phrase0.toLowerCase().split("[^\\w]+");
            String[] arrayB = phrase1.toLowerCase().split("[^\\w]+");
            Set<String> setA = new HashSet<String>(Arrays.asList(arrayA));
            Set<String> setB = new HashSet<String>(Arrays.asList(arrayB));
            setA.retainAll(setB);
            outcome = setA.size();
            int max = Math.max(arrayA.length, arrayB.length);
            int min = Math.min(arrayA.length, arrayB.length);
            Pattern pat = Pattern.compile(".*?(\\d+,[\\d,\\s]+).*?");
            Matcher match = pat.matcher(phrase1);
            if(match.matches())
                outcome+=(match.group(1).length());
            //outcome *= (double)min/(double)max;
            //int CLAMP = phrase0.length();
            //outcome = CLAMP*(Math.round(outcome/CLAMP));
        }
        else
        {
            outcome=0;
        }

        return outcome;
    }
    /*for (int i = 0; i < arrayA.length; i++) {
                        Vertex va = new Vertex(arrayA[i]);

                        for (int j = 0; j < arrayA.length; j++) {


                            Vertex vb = new Vertex(arrayA[j]);
                            graph0.addVertex(vb, false);
                            va.addNeighbor(new Edge(va,vb));
                        }
                        graph0.addVertex(va, false);
                    }
                    */
    public static Vertex addVertecies(Vertex vertex,int cuIndex, String[] array, HashSet<String> added)
    {
        if(cuIndex <0) cuIndex =0;
        if(cuIndex>=array.length-1)
            return vertex;


        //added.add(vertex.getLabel());
        // adil is a guy
        // if(Arrays.asList(array).indexOf(vertex.getLabel()) == (array.length - 1))
        //    return;
        for(int j = cuIndex+1;j<array.length;j++) {
            Vertex va = new Vertex(array[j]);
            int indexB = j;

            if(Math.abs(cuIndex - indexB)<CREATION_EDGE_LENGTH)
            if(true  &&cuIndex>=0 && indexB>=0) {
                Vertex sdf = addVertecies(va,j,array,added);
                vertex.addNeighbor(new Edge(va, sdf,
                        Math.abs(cuIndex - indexB)));
                //return vertex;
            }
            else
            {
                //System.out.println("asfasdf");
                //System.exit(-1);
                vertex.addNeighbor(new Edge(vertex, va));
            }
        }

        for(int j = 0;j<array.length;j++) {
            Vertex vTemp = new Vertex(array[j]);
            Edge edgex = new Edge(vertex, vTemp);
            if(!vertex.containsNeighbor(edgex))
                vertex.addNeighbor(edgex);
        }

        return  vertex;
    }
    public static HashSet<String> keyWords = new HashSet<>();
    public static HashSet<String> keyWordsVerbOrAdjective = new HashSet<>();
    public static HashMap<String,Integer> keyWordUniqueness = new HashMap<>();
    public static int textSize = 0;
    public static boolean hasAUniqueKeyWord = false;
    public static ArrayList<String> mostcommon = new ArrayList<>();
    public static HashMap<String, HashSet<String>> synMap = new HashMap();
    public static final int EDGE_LENGTH =100 ;
    public static final int CREATION_EDGE_LENGTH =100 ;
    public static final HashSet<String> globalUsedWords = new HashSet<>();
    public static double compare2(String phrase0,String phrase1) throws IOException {
        globalUsedWords.clear();
        hasAUniqueKeyWord = false;
        //what actress starred in freaky friday
        //Which actress played the role of Mary (adult) in the movie It s a Wonderful Life
        System.out.println("query = "+phrase0);
        String original = phrase1;
        try{
            /*HashMap<String, Parse> phrases = Understanding.go(phrase1);

            // THIS NLP SOFTWARE IS INCACURATE
           phrase1 =  phrases.get("VP").getText();
            phrase1.replace(phrases.get("NP").getText(),"");
            System.out.print("809354035480354");
            //System.out.println("This is the new phrase0: "+phrase0);
           // System.out.println("This is the new phrase1: "+phrase1);
           */
        }
        catch (Exception e)
        {
            return 0.0;
        }
        Graph graph0 = new Graph();
        Graph graph1 = new Graph();
        double outcome = 0.0;//Authority control GND:
        boolean isWrong = phrase1.matches(".*?\\d{4}.\\d{4}.*?");
        if(true||!phrase1.toLowerCase().replaceAll("[^\\w^\\d^\\s]+","").trim().startsWith("how")
                &&!phrase1.toLowerCase().replaceAll("[^\\w^\\d^\\s]+","").trim().startsWith("why")
                &&!phrase1.toLowerCase().replaceAll("[^\\w^\\d^\\s]+","").trim().startsWith("who")
                &&!phrase1.toLowerCase().replaceAll("[^\\w^\\d^\\s]+","").trim().startsWith("what")
                &&!phrase1.toLowerCase().replaceAll("[^\\w^\\d^\\s]+","").trim().startsWith("where")
                &&!phrase1.toLowerCase().replaceAll("[^\\w^\\d^\\s]+","").trim().startsWith("when")
                &&!phrase1.toLowerCase().replaceAll("[^\\w^\\d^\\s]+","").trim().startsWith("is")
                &&!phrase1.toLowerCase().replaceAll("[^\\w^\\d^\\s]+","").trim().startsWith("he")
                &&!phrase1.toLowerCase().replaceAll("[^\\w^\\d^\\s]+","").trim().startsWith("she")

                &&
                !phrase1.toLowerCase().contains("list of")
                &&!isWrong&&phrase1.length()>6&&phrase1.length()<250 )//&& !phrase1.toLowerCase().trim().matches("^\"?\\[\\d+\\]$")
        //if(true)

        {
            try {
                System.out.println("ey34345: " + phrase1 +" :adil");

                String[] arrayAO = phrase0.toLowerCase().split("[^\\w^\\d]+");
                String[] arrayBO = phrase1.toLowerCase().split("[^\\w^\\d]+");
                String[] arrayA = new String[arrayAO.length];
                String[] arrayB = new String[arrayBO.length];
                for(int i = 0;i<arrayAO.length;i++)
                    try {
                        String root  = LuceneSnowBallTest.getStem(arrayAO[i]);
                        if(root!=null)
                            arrayA[i] = root;
                        else
                            arrayA[i] = arrayAO[i];
                    }catch (Exception e)
                    {
                        arrayA[i] = arrayAO[i];
                    }
                for(int i = 0;i<arrayBO.length;i++)
                    try {
                        String root  = LuceneSnowBallTest.getStem(arrayBO[i]);
                        if(root!=null)
                            arrayB[i] = root;
                        else
                            arrayB[i] = arrayBO[i];
                    }catch (Exception e)
                    {
                        arrayB[i] = arrayBO[i];
                    }
                Vertex masterA = new Vertex("vlkvenslvmsevksdv");
                masterA = addVertecies(masterA,0,arrayA,new HashSet<>());

                graph0.addVertex(masterA,false);


                Vertex masterB = new Vertex("vlkvenslvmsevksdv");
                masterB = addVertecies(masterB,0,arrayB,new HashSet<>());
                PrintStream dummyStream = new PrintStream(new OutputStream() {
                    public void write(int b) {
                        //NO-OP
                    }
                });
                System.setOut(MainGUI.originalStream);
                //System.out.println("23ts second last res = " + masterA.getNeighbor(0).getTwo().getNeighbor(0).getTwo()
                 //     .getNeighbor(0).getTwo().getLabel());
                System.setOut(dummyStream);
                graph1.addVertex(masterB,false);

                /*Vertex masterB = new Vertex("");
                for (int i = 0; i < arrayB.length; i++) {
                    Vertex va = new Vertex(arrayB[i]);

                    addVertecies(va,arrayB,new HashSet<>());
                    masterB.addNeighbor(new Edge(masterB,va,i));

                }
                graph1.addVertex(masterB, false);
                */
                System.out.println("graph size = " + Arrays.asList(arrayA));
                for (Edge e : graph1.getEdges()) {
                    System.out.println("lABEL = " + graph1.getVertex(e.getOne().getLabel()) + ", " + graph1.getVertex(e.getTwo().getLabel()));
                    System.out.println(graph1.getVertex(e.getOne().getLabel()).getNeighbors());
                }
                double score = 0;

                hasAUniqueKeyWord =true;
                    double instantaniousScore = 0;
                //if(phrase1.toLowerCase().contains("when u mess up a screenshot".toLowerCase()))
                instantaniousScore += GraphNew_July8.print(graph0.getVertex("vlkvenslvmsevksdv"),
                        graph1.getVertex("vlkvenslvmsevksdv"), Graph.START_GRAPH_SEARCH,0,0,new HashSet<>(),false
                        ,0);


                    score += instantaniousScore;

                System.out.println("" + phrase1 + " = " + score);

                System.setOut(MainGUI.originalStream);
                System.out.println("SCORE res = " + score);
                System.setOut(dummyStream);
                return score;


            }catch (Exception e){e.printStackTrace();
                System.exit(-1);}

        }
        else
        {
            outcome=0;
            /*PrintStream dummyStream = new PrintStream(new OutputStream() {
                public void write(int b) {
                    //NO-OP
                }
            });
            if(phrase1.toLowerCase().contains("kind of service firm whose characteristics have distinctive"
                    .toLowerCase())) {
                System.setOut(MainGUI.originalStream);
                System.out.println("second last res = " + outcome);
                System.setOut(dummyStream);
                System.exit(-99);

            }
            */
        }
        /*PrintStream dummyStream = new PrintStream(new OutputStream() {
            public void write(int b) {
                //NO-OP
            }
        });
        if(phrase1.toLowerCase().contains("kind of service firm whose characteristics have distinctive"
                .toLowerCase())) {
            System.setOut(MainGUI.originalStream);
            System.out.println("last res = " + outcome);
            System.setOut(dummyStream);
            System.exit(-99);
        }
        */
        return outcome;
    }

    public static ArrayList<String> rankAnswers(String query, ArrayList<String> unsortedParagraphs, HashSet<String[]> keyWords)
    {
        TreeMap<Double, HashSet<String>> sorted = new TreeMap<>();

        for(String paragraph : unsortedParagraphs)
        {
            double score = Math.abs(computeParagraph(paragraph,keyWords) - computeParagraph(query,keyWords));
            if(sorted.containsKey(score))
            {
                HashSet<String> set = sorted.get(score);
                set.add(paragraph);
                sorted.put(score,set);
            }
            else
            {
                HashSet<String> set = new HashSet<>();
                set.add(paragraph);
                sorted.put(score,set);
            }
        }
        ArrayList<String> sortedList = new ArrayList<>();
        for(Map.Entry<Double,HashSet<String>> entry : sorted.entrySet())
        {
            for(String para : entry.getValue())
                sortedList.add(para);
        }
        return  sortedList;
    }
    public static double computeParagraph(String paragraph, HashSet<String[]> keyWords)
    {
        HashSet<Integer> inteciesOfKeyWords = new HashSet<>();
        for(String[] words : keyWords)
        {
            for(String word : words) {
                int index = paragraph.toLowerCase().indexOf(word.toLowerCase());
                inteciesOfKeyWords.add(index);
            }
        }
        int total = 0;
        for(int index : inteciesOfKeyWords)
        {
            total += index;
        }
        double average = (double) total / (double) inteciesOfKeyWords.size();

        double distanceFromAverage = 0.0;
        for(int index : inteciesOfKeyWords)
        {
            distanceFromAverage += Math.abs(index - average);
        }

        return distanceFromAverage;
    }

}
