package general.graph_old.theory;

import general.graph_old.theory.*;

import java.util.*;


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

public class Graph {


    private HashMap<String, Vertex> vertices;

    private HashMap<Integer, general.graph_old.theory.Edge> edges;
    public static HashSet<String> usedWords = new HashSet<>();
    public static int START_GRAPH_SEARCH = 0;
    public Graph(){

        this.vertices = new HashMap<String, Vertex>();

        this.edges = new HashMap<Integer, general.graph_old.theory.Edge>();

    }


    /**

     * This constructor accepts an ArrayList<Vertex> and populates

     * this.vertices. If multiple Vertex objects have the same label,

     * then the last Vertex with the given label is used.

     *

     * @param vertices The initial Vertices to populate this Graph

     */

    public Graph(ArrayList<Vertex> vertices){

        this.vertices = new HashMap<String, Vertex>();

        this.edges = new HashMap<Integer, general.graph_old.theory.Edge>();


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

    public static double print(Graph g1, Graph g2,String vNode, double score, int w1, int w2)
    {
        System.out.println("GSDSF: "+vNode);
        if(Graph.usedWords.contains(vNode))
        {

            return  score;
        }
        score+= (1.0/(((double)w1+1.0)+((double)w2+1.0)))*6.0;
        ArrayList<general.graph_old.theory.Edge> e1 = g1.getVertex(vNode).getNeighbors();
        ArrayList<general.graph_old.theory.Edge> e2 = g2.getVertex(vNode).getNeighbors();

        ArrayList<Vertex> v1 = new ArrayList<Vertex>();
        ArrayList<Vertex> v2 =  new ArrayList<Vertex>();
        for(general.graph_old.theory.Edge edge : e1)
            v1.add(edge.getTwo());
        for(general.graph_old.theory.Edge edge : e2)
            v2.add(edge.getTwo());
        for(Vertex vertex1 : v1)
        {
            System.out.println("__>"+vertex1);
            Vertex vertex2 = g2.getVertex(vertex1.getLabel());
            if(vertex2!=null) {
                Graph.usedWords.add(vNode);
                score += print(g1, g2, vertex1.getLabel(), score,
                        g1.getVertex(vNode).getNeighbor(v1.indexOf(vertex1)).getWeight(),
                        g2.getVertex(vNode).getNeighbor(v2.indexOf(vertex2)).getWeight());
            }

        }
        /*
        if(g1.getLabel().equals(g2.getLabel()))
            score++;
        else
            return score;
        ArrayList<Edge> e1 = g1.getNeighbors();
        ArrayList<Edge> e2 = g2.getNeighbors();
        ArrayList<Vertex> v1 = new ArrayList<Vertex>();
        ArrayList<Vertex> v2 =  new ArrayList<Vertex>();
        for(Edge edge : e1)
            v1.add(edge.getTwo());
        for(Edge edge : e2)
            v2.add(edge.getTwo());
        Graph temp = new Graph();
        for(Vertex vertex1 : v1)
        {
            Vertex vertex2 = v
            if(vertex1.getLabel().equals(vertex2.getLabel()))
                temp.addEdge(vertex1,vertex2,score);
        }


        for(Edge edge : temp.getEdges())
        {
            score += print(edge.getOne(),edge.getTwo(),edge.getWeight());
        }
        */

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

        general.graph_old.theory.Edge e = new general.graph_old.theory.Edge(one, two, weight);

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

    public boolean containsEdge(general.graph_old.theory.Edge e){

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

    public general.graph_old.theory.Edge removeEdge(general.graph_old.theory.Edge e){

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

    public Set<general.graph_old.theory.Edge> getEdges(){

        return new HashSet<general.graph_old.theory.Edge>(this.edges.values());

    }


}
