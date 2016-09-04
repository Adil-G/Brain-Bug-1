package SentenceGenerator;

import general.LuceneSnowBallTest;
import general.graph.theory.Edge;
import general.graph.theory.Graph;
import general.graph.theory.GraphNew_July8;
import general.graph.theory.Vertex;

import java.io.IOException;
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
    public static void addVertecies(Vertex vertex, String[] array, HashSet<String> added)
    {
        added.add(vertex.getLabel());
        // adil is a guy
        // if(Arrays.asList(array).indexOf(vertex.getLabel()) == (array.length - 1))
        //    return;
        for(int j = 0;j<array.length;j++) {
            Vertex va = new Vertex(array[j]);
            int indexA = Arrays.asList(array).indexOf(vertex.getLabel());
            int indexB = Arrays.asList(array).indexOf(va.getLabel());

            if(true &&indexA>=0 && indexB>=0) {
                vertex.addNeighbor(new Edge(vertex, va,
                        Math.abs(indexA - indexB)));
            }
            else
            {
                //System.out.println("asfasdf");
                //System.exit(-1);
                vertex.addNeighbor(new Edge(vertex, va));
            }
        }
        //int index = Arrays.asList(array).indexOf(vertex.getLabel());
        for(int i = 0;i<array.length;i++)
        {
            Vertex va = new Vertex(array[i]);
            if(!added.contains(va.getLabel()))
                addVertecies(va,array,added);
        }
    }
    public static HashSet<String> keyWords = new HashSet<>();
    public static HashSet<String> keyWordsVerbOrAdjective = new HashSet<>();
    public static HashMap<String,Integer> keyWordUniqueness = new HashMap<>();
    public static int textSize = 0;
    public static boolean hasAUniqueKeyWord = false;
    public static ArrayList<String> mostcommon = new ArrayList<>();
    public static HashMap<String, HashSet<String>> synMap = new HashMap();
    public static double compare2(String phrase0,String phrase1) throws IOException {
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
                for (int i = 0; i < arrayA.length; i++) {
                    Vertex va = new Vertex(arrayA[i]);

                    addVertecies(va,arrayA,new HashSet<>());
                    graph0.addVertex(va, false);
                }
                for (int i = 0; i < arrayB.length; i++) {
                    Vertex va = new Vertex(arrayB[i]);

                    addVertecies(va,arrayB,new HashSet<>());
                    graph1.addVertex(va, false);;
                }
                System.out.println("graph size = " + Arrays.asList(arrayA));
                for (Edge e : graph1.getEdges()) {
                    System.out.println("lABEL = " + graph1.getVertex(e.getOne().getLabel()) + ", " + graph1.getVertex(e.getTwo().getLabel()));
                    System.out.println(graph1.getVertex(e.getOne().getLabel()).getNeighbors());
                }
                double score = 0;
                double highestScore = 0;
                for (String label : arrayA) {
//
                    hasAUniqueKeyWord = false;
                    double instantaniousScore = 0;
                    if ( graph0.getVertex(label) != null && graph1.getVertex(label) != null) {
                        //System.out.println("309i30---->> "+graph0.getVertex(label).getNeighbors());
                        //System.out.println("309i31---->> "+Arrays.asList(arrayA));
                        instantaniousScore += GraphNew_July8.print(graph0.getVertex(label), graph1.getVertex(label), label, Graph.START_GRAPH_SEARCH,0,0,new HashSet<>(),false
                                ,0);

                    }
                    else //if(WikipediaInfoBoxModel2OldJune14_PERSONAL.statementsFileNameEquals("statements_july6.txt"))
                        if(ComparePhrases.synMap.containsKey(label))
                            for(String synonym : ComparePhrases.synMap.get(label))
                                if (
                                        graph0.getVertex(label) != null && graph1.getVertex(synonym) != null) {
                                    System.out.println("309i30---->> "+graph0.getVertex(label).getNeighbors());
                                    System.out.println("309i31---->> "+Arrays.asList(arrayA));
                                    instantaniousScore += GraphNew_July8.print(graph0.getVertex(label), graph1.getVertex(synonym), label, Graph.START_GRAPH_SEARCH,0,0,new HashSet<>(),false
                                            ,0);

                                }
                    if(!hasAUniqueKeyWord)
                        instantaniousScore = 0;

                    score += instantaniousScore;

                    /*if(instantaniousScore>highestScore)
                    {
                        highestScore = instantaniousScore;
                    }
*/


                }
                //score = highestScore;

                System.out.println("" + phrase1 + " = " + score);

                System.out.println("res = "+score);
                return score;


            }catch (Exception e){e.printStackTrace();
                System.exit(-1);}
                /*
            //System.out.println("still processing ...");
            String[] arrayA = phrase0.toLowerCase().split("[^\\w^\\d]+");
            String[] arrayB = phrase1.toLowerCase().split("[^\\w^\\d]+");
                System.out.println("08960786: " + Arrays.asList(arrayB));
            Set<String> setA = new HashSet<String>(Arrays.asList(arrayA));
            Set<String> setB = new HashSet<String>(Arrays.asList(arrayB));
            setA.retainAll(setB);

                try {
                    for (int i = 0; i < arrayA.length; i++)
                        for (int j = 0; j < arrayA.length; j++) {
                            graph0.addEdge(new Vertex(arrayA[i]), new Vertex(arrayA[j]), Math.abs(i - j));
                        }
                    for (int i = 0; i < arrayB.length; i++)
                        for (int j = 0; j < arrayB.length; j++) {

                            graph1.addEdge(new Vertex(arrayB[i]), new Vertex(arrayB[j]), Math.abs(i - j));

                        }

                    // vertex order doesn't matter
                    //(A,B),(A,B)
                    // (B,A),(B,A)

                    // (B,A),(A,B)
                    // (A,B),(B,A)
                    boolean seen = false;
                    for (Edge e0 : graph0.getEdges())
                        for (Edge e1 : graph1.getEdges()) {
                            //System.out.println("2034923: " + e1.getTwo().getLabel());
                            for(String one : synonyms.get(e0.getOne().getLabel()))
                            for(String two : synonyms.get(e0.getTwo().getLabel()))
                            if ((one.equals(e1.getOne().getLabel()) && two.equals(e1.getTwo().getLabel()))
                                    || (one.equals(e1.getTwo().getLabel()) && two.equals(e1.getOne().getLabel()))) {
                                //System.out.println("TRUE");
                                double total = Math.pow(((double) arrayA.length - 1.0), 2) + Math.pow(((double) arrayB.length - 1.0), 2);
                                double diff0 = (double) e0.getWeight();
                                double diff1 = (double) e1.getWeight();
                                double diff = Math.abs(diff0 - diff1) / total;
                                if(e0.getWeight() < 3 && e1.getWeight()<3) {
                                    outcome ++;
                                    System.out.println(outcome + "{}-->" + phrase1);
                                    System.out.println("with edge: " + e0.toString() + ", " + e1.toString());
                                    seen = true;
                                }
                            }
                        }
                    if(!seen)
                    System.out.println("Not understood phrase1 = " + phrase1);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                */
                /*
                outcome += setA.size();
            int max = Math.max(arrayA.length, arrayB.length);
            int min = Math.min(arrayA.length, arrayB.length);
            String populationMoney =".*?(\\d+,[\\d,\\-\\s]+).*?";
            String anyNumber = "\\d+";
            Pattern pat;
            if(isBig)
                pat = Pattern.compile(populationMoney);
            else
                pat = Pattern.compile(anyNumber);
            Matcher match = pat.matcher(phrase1);
            if(match.matches())
            {
                if(isBig)
                    outcome+=(match.group(1).length());
                else
                    outcome+=2;
            }
                //Which artist is credited with developing linear perspective
                // search Brunelleschi.*linear perspective
            System.out.println(outcome+"-->" + phrase1);
            /*int wordcount = phrase0.split("[^\\w]+").length;
            if(wordcount>4) {
                if (outcome < 6.0)
                    outcome = 0.0;
            }
            */
            //System.out.println(outcome + "{}-->" + phrase0);

        }
        else
        {
            outcome=0;
        }

        return outcome;
    }


}
