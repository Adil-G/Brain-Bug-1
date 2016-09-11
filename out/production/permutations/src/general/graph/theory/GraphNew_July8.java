package general.graph.theory;

import SentenceGenerator.ComparePhrases;
import general.chat.MainGUI;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static SentenceGenerator.ComparePhrases.globalUsedWords;


/**

 * This class models a simple, undirected graph using an

 * incidence list representation. Vertices are identified

 * uniquely by their labels, and only unique vertices are allowed.

 * At most one Edge per vertex pair is allowed in this Graph.

 *

 * Note that the Graph is designed to manage the Edges. You

 * should not attempt to manually add Edges yourself.

 *

 * @author Michael Levet

 * @date June 09, 2015

 */

public class GraphNew_July8 {


    private HashMap<String, Vertex> vertices;

    private HashMap<Integer, Edge> edges;
    //public static HashSet<String> usedWords = new HashSet<>();
    public static int START_GRAPH_SEARCH = 0;
    public GraphNew_July8(){

        this.vertices = new HashMap<String, Vertex>();

        this.edges = new HashMap<Integer, Edge>();

    }


    /**

     * This constructor accepts an ArrayList<Vertex> and populates

     * this.vertices. If multiple Vertex objects have the same label,

     * then the last Vertex with the given label is used.

     *

     * @param vertices The initial Vertices to populate this Graph

     */

    public GraphNew_July8(ArrayList<Vertex> vertices){

        this.vertices = new HashMap<String, Vertex>();

        this.edges = new HashMap<Integer, Edge>();


        for(Vertex v: vertices){

            this.vertices.put(v.getLabel(), v);

        }


    }


    /**

     * This method adds am edge between Vertices one and two

     * of weight 1, if no Edge between these Vertices already

     * exists in the Graph.

     *

     * @param one The first vertex to add

     * @param two The second vertex to add

     * @return true iff no Edge relating one and two exists in the Graph

     */

    public boolean addEdge(Vertex one, Vertex two){

        return addEdge(one, two, 1);

    }

    public static double print(Vertex g1, Vertex g2, double score, int w1, int w2
            ,HashSet<String> usedWords, boolean isKeyWordPath, int depth) {
        String vNode = g1.getLabel();
        System.out.println("GSDSF: " + vNode);

        /*
        // if the question contains this word and was added as a synmap key

        if (ComparePhrases.synMap.containsKey(vNode.toLowerCase()))
        {
            // get a list of synonyms for this related words
            for (String synonym : ComparePhrases.synMap.get(vNode.toLowerCase()))
                // if this path is not yet a "key word" path
                if (!isKeyWordPath)
                    // check if this synonym is truly a key word
                    if (ComparePhrases.keyWords.contains(synonym.toLowerCase()))
                        if (!ComparePhrases.mostcommon.contains(synonym.toLowerCase())) {
                            isKeyWordPath = true;
                            System.out.println("APPROVAL WORD = " + synonym.toLowerCase());
                            ComparePhrases.hasAUniqueKeyWord = true;
                        }
    }
    */
        if(g1.getNeighborCount() == 0 || g2.getNeighborCount() == 0
                || usedWords.contains(g1.getLabel())
                //||globalUsedWords.contains(g1.getLabel())
                )
        {
            return score;
        }
        //score++;
        if(w2<ComparePhrases.EDGE_LENGTH) {

            score += 1 + depth * 10;
            //score += 1 + depth;
        }
        else
        {
            if(isKeyWordPath)
                return score;
            else
                return 0;
        }
        //score+= (1.0/(((double)w1+1.0)+((double)w2+1.0)))*6.0;
        ArrayList<Edge> e1 = g1.getNeighbors();
        ArrayList<Edge> e2 = g2.getNeighbors();

        for(Edge edge1 : g1.getNeighbors())
        {
            Vertex vertex1 = edge1.getTwo();
            //loop throughs second setntece
            for(Edge edge2 : g2.getNeighbors())
            {
                Vertex vertex2 = edge2.getTwo();
                PrintStream dummyStream = new PrintStream(new OutputStream() {
                    public void write(int b) {
                        //NO-OP
                    }
                });

                if (false||!usedWords.contains(vertex1.getLabel())) {
                    System.setOut(MainGUI.originalStream);
                    System.out.println("24g3q4sg: " + vertex1.getLabel()+"\n"+vertex2.getLabel());
                    System.setOut(dummyStream);
                    globalUsedWords.add(g1.getLabel());
                    usedWords.add(g1.getLabel());
                    score += print(vertex1, vertex2, score,
                            edge1.getWeight(),
                            edge2.getWeight(), usedWords
                            , isKeyWordPath, depth + 1);
                }
            /*
                // most common
                if(vertex1.getLabel().equals(
                        vertex2.getLabel()
                )) {

                    // chjeck all synonyms in seccond sentence if they
                    // match wtuh the current lavel
                    //if(WikipediaInfoBoxModel2OldJune14_PERSONAL.statementsFileNameEquals("statements_july6.txt"))
                    if (ComparePhrases.synMap.containsKey(vertex1.getLabel()))
                        for (String synonym : ComparePhrases.synMap.get(vertex1.getLabel())) {
                            if (vertex1.getLabel().equals(
                                    synonym
                            ))
                                if (!usedWords.contains(vertex1.getLabel())) {
                                    usedWords.add(vertex1.getLabel());
                                    score += print(vertex1, vertex2, score,
                                            g1.getNeighbor(v1.indexOf(vertex1)).getWeight(),
                                            g2.getNeighbor(v2.indexOf(vertex2)).getWeight(), new HashSet<>(usedWords)
                                            , isKeyWordPath, depth + 1);
                                }
                        }

                }
                */
            }

          //  System.out.println("__>"+vertex1);
           // Vertex vertex2 = null;
/*
            if(ComparePhrases.synMap.containsKey(vertex1.getLabel()))
            for(String synonym : ComparePhrases.synMap.get(vertex1.getLabel()))
                if(vertex2==null) {
                    System.out.println(vertex1.getLabel()+" -> 2f3fsef: "+synonym);
                    vertex2 = g2.getVertex(synonym);
                }
            if(vertex2!=null&&!usedWords.contains(vertex2.getLabel())){//
                //System.out.println(g1.getVertex(vNode).getNeighborCount());
                //System.out.println(g2.getVertex(vNode).getNeighborCount());


                try {
                    usedWords.add(vNode);

                                score += print(g1, g2, vertex1.getLabel(), score,
                                        g1.getVertex(vNode).getNeighbor(v1.indexOf(vertex1)).getWeight(),
                                        g2.getVertex(vNode).getNeighbor(v2.indexOf(vertex2)).getWeight(),new HashSet<>(usedWords)
                                        ,isKeyWordPath);

                }
                catch (Exception p)
                {
                    p.printStackTrace();
                    if(g2.getVertex(vNode).getNeighborCount()<2) {
                        usedWords.add(vNode);
                        score += print(g1, g2, vertex1.getLabel(), score,
                                g1.getVertex(vNode).getNeighbor(v1.indexOf(vertex1)).getWeight(),
                                g2.getVertex(vNode).getNeighbor(0).getWeight(),new HashSet<>(usedWords),
                                false);
                    }
                    else
                    {
                        System.out.println("ERROR!!! CHECK GRAPH PRINTER");
                        //System.exit(-1);
                    }
                }

            }
*/
        }


        return score;

    }

    /**

     * Accepts two vertices and a weight, and adds the edge

     * ({one, two}, weight) iff no Edge relating one and two

     * exists in the Graph.

     *

     * @param one The first Vertex of the Edge

     * @param two The second Vertex of the Edge

     * @param weight The weight of the Edge

     * @return true iff no Edge already exists in the Graph

     */

    public boolean addEdge(Vertex one, Vertex two, int weight){

        if(one.equals(two)){

            return false;

        }


        //ensures the Edge is not in the Graph

        Edge e = new Edge(one, two, weight);

        if(edges.containsKey(e.hashCode())){

            return false;

        }


        //and that the Edge isn't already incident to one of the vertices

        else if(one.containsNeighbor(e) || two.containsNeighbor(e)){

            return false;

        }


        edges.put(e.hashCode(), e);

        one.addNeighbor(e);

        two.addNeighbor(e);

        return true;

    }


    /**

     *

     * @param e The Edge to look up

     * @return true iff this Graph contains the Edge e

     */

    public boolean containsEdge(Edge e){

        if(e.getOne() == null || e.getTwo() == null){

            return false;

        }


        return this.edges.containsKey(e.hashCode());

    }



    /**

     * This method removes the specified Edge from the Graph,

     * including as each vertex's incidence neighborhood.

     *

     * @param e The Edge to remove from the Graph

     * @return Edge The Edge removed from the Graph

     */

    public Edge removeEdge(Edge e){

        e.getOne().removeNeighbor(e);

        e.getTwo().removeNeighbor(e);

        return this.edges.remove(e.hashCode());

    }


    /**

     *

     * @param vertex The Vertex to look up

     * @return true iff this Graph contains vertex

     */

    public boolean containsVertex(Vertex vertex){

        return this.vertices.get(vertex.getLabel()) != null;

    }


    /**

     *

     * @param label The specified Vertex label

     * @return Vertex The Vertex with the specified label

     */

    public Vertex getVertex(String label){

        return vertices.get(label);

    }


    /**

     * This method adds a Vertex to the graph. If a Vertex with the same label

     * as the parameter exists in the Graph, the existing Vertex is overwritten

     * only if overwriteExisting is true. If the existing Vertex is overwritten,

     * the Edges incident to it are all removed from the Graph.

     *

     * @param vertex

     * @param overwriteExisting

     * @return true iff vertex was added to the Graph

     */

    public boolean addVertex(Vertex vertex, boolean overwriteExisting){

        Vertex current = this.vertices.get(vertex.getLabel());

        if(current != null){

            if(!overwriteExisting){

                return false;

            }


            while(current.getNeighborCount() > 0){

                this.removeEdge(current.getNeighbor(0));

            }

        }



        vertices.put(vertex.getLabel(), vertex);

        return true;

    }


    /**

     *

     * @param label The label of the Vertex to remove

     * @return Vertex The removed Vertex object

     */

    public Vertex removeVertex(String label){

        Vertex v = vertices.remove(label);


        while(v.getNeighborCount() > 0){

            this.removeEdge(v.getNeighbor((0)));

        }


        return v;

    }


    /**

     *

     * @return Set<String> The unique labels of the Graph's Vertex objects

     */

    public Set<String> vertexKeys(){

        return this.vertices.keySet();

    }

    /**

     *

     * @return Set<Edge> The Edges of this graph

     */

    public Set<Edge> getEdges(){

        return new HashSet<Edge>(this.edges.values());

    }


}
