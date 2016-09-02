package general.graph.theory;

import SentenceGenerator.ComparePhrases;

import java.io.IOException;
import java.util.*;

/**

 *

 * @author Michael Levet

 * @date June 09, 2015

 */

public class DemoGraph {


    public static void main(String[] args) throws IOException {
        String phrase0 = "Which artist is credited with developing linear perspective";
        String phrase1 = "Brunelleschi is famous for two panel paintings illustrating geometric optical linear perspective made in the early 15th century";
        HashMap<String, ArrayList<String>> synonyms = new HashMap<>();
       /* String[] words = phrase0.toLowerCase().split("[^\\w]+");
        //String full = new String();
        for (String lastWord : words) {
            ArrayList<String> arr   = general.WikipediaInfoBoxModel2.listSynonymsJJ2(lastWord);
            arr.add(lastWord);
            synonyms.put(lastWord,arr);
        }
        */
        Graph graph0 = new Graph();
        Graph graph1 = new Graph();
        double outcome = 0.0;//Authority control GND:
        String[] arrayA = phrase0.toLowerCase().split("[^\\w^\\d]+");
        String[] arrayB = phrase1.toLowerCase().split("[^\\w^\\d]+");
        System.out.println("08960786: " + Arrays.asList(arrayB));
        Set<String> setA = new HashSet<String>(Arrays.asList(arrayA));
        Set<String> setB = new HashSet<String>(Arrays.asList(arrayB));
        setA.retainAll(setB);

        try {

            for (int i = 0; i < arrayA.length; i++) {
                Vertex va = new Vertex(arrayA[i]);
                graph0.addVertex(va,false);
                for (int j = 0; j < arrayA.length; j++) {
                    Vertex vb = new Vertex(arrayA[j]);
                    graph0.addVertex(vb,false);
                    graph0.addEdge(va, vb, Math.abs(i - j));
                }
            }
            for (int i = 0; i < arrayB.length; i++) {
                Vertex va = new Vertex(arrayB[i]);
                graph1.addVertex(va,false);
                for (int j = 0; j < arrayB.length; j++) {
                    Vertex vb = new Vertex(arrayB[j]);
                    graph1.addVertex(vb,false);
                    graph1.addEdge(va,vb , Math.abs(i - j));

                }
            }
            System.out.println("graph size = " + graph0.getEdges());
            for(Edge e :graph0.getEdges())
            {
                System.out.println("lABEL = "+graph0.getVertex(e.getOne().getLabel())+", "+graph0.getVertex(e.getTwo().getLabel()));
            }
            int score= 0;
            for(String label : arrayA)
            {

                //if(!Graph.usedWords.contains(label)&&graph0.getVertex(label)!=null && graph1.getVertex(label)!=null) {

                    //score += Graph.print(graph0, graph1, label, Graph.START_GRAPH_SEARCH);
                //}
                //what company is IBM's new personal robot

            }
            double score2 = ComparePhrases.compare2(phrase0,phrase1);
            //score2-=score2%2;
            System.out.println("score2 = " +score2) ;

            // vertex order doesn't matter
            //(A,B),(A,B)
            // (B,A),(B,A)

            // (B,A),(A,B)
            // (A,B),(B,A)
                    /*N: Most of the Canadian Prairie Provinces and the US midwest are on square mile grids for surveying purposes
match: = ({Vertex mile, Vertex square}, 1) with ({Vertex mile, Vertex square}, */
            /*boolean seen = false;
            for (Edge e0 : graph0.getEdges())//vertex linear, vertex perspective}, 1), ({Vertex linear, Vertex perspective}, 1)
                for (Edge e1 : graph1.getEdges()) {
                    System.out.println(outcome + "{}-->" + phrase1);
                    System.out.println("with edge: " + e0.toString() + ", " + e1.toString());
                    for(String one : synonyms.get(e0.getOne().getLabel()))
                        for(String two : synonyms.get(e0.getTwo().getLabel()))
                            if ((one.equals(e1.getOne().getLabel()) && two.equals(e1.getTwo().getLabel()))
                                    || (one.equals(e1.getTwo().getLabel()) && two.equals(e1.getOne().getLabel()))) {
                                System.out.println("TRUE");
                                double total = Math.pow(((double) arrayA.length - 1.0), 2) + Math.pow(((double) arrayB.length - 1.0), 2);
                                double diff0 = (double) e0.getWeight();
                                double diff1 = (double) e1.getWeight();
                                double diff = Math.abs(diff0 - diff1) / total;
                                if(e0.getWeight() < 3 && e1.getWeight()<3) {
                                        /*if (diff != 0) {
                                            outcome += 1.0 / diff;

                                            //System.out.println("{}{"+outcome+"}--> "+phrase1);

                                        } else {
                                            outcome += 1.0 / ((0.25) / total);
                                        }

                                    outcome ++;

                                    seen = true;
                                }
                            }
                }
            if(!seen)
                System.out.println("Not understood phrase1 = " + phrase1);
                */


        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }

}
