package SentenceGenerator;

/**
 * Created by corpi on 2016-04-24.
 */
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

import com.knowledgebooks.nlp.ExtractNames;
import com.knowledgebooks.nlp.util.ScoredList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.slf4j.spi.LocationAwareLogger.*;
//import com.hp.hpl.jena.query.*;
@Slf4j
public class Dbpedia
{
    private Dbpedia() {}

    public static String tag(String uri)
    {
        String parts[] = uri.split("/");

        switch(parts[parts.length-2])
        {
            case "ontology": return "dbo";
            case "property": return "dbp";
            default: throw new IllegalArgumentException("uri <"+uri+"> neither ontology nor property");
        }
    }

    static final String sparqlHeader = "PREFIX dbo: <http://dbpedia.org/ontology/>" + "PREFIX yago: <http://dbpedia.org/class/yago/> "
            + "PREFIX dbp: <http://dbpedia.org/property/> " + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
            + "PREFIX res: <http://dbpedia.org/resource/> ";

    private static final String endpoint = "http://live.dbpedia.org/sparql";
    //private static final String endpoint = "http://dbpedia.org/sparql";

    public static ResultSet select(String querystring)
    {//System.out.println(endpoint + querystring);
        //log.debug("select query: "+querystring);
        try(QueryEngineHTTP qe = new QueryEngineHTTP(endpoint,querystring))
        {//System.out.println(qe.toString());
            return ResultSetFactory.copyResults(qe.execSelect());}
        //Query query = QueryFactory.create(querystring); //s2 = the query above
        //QueryExecution qExe = QueryExecutionFactory.sparqlService( "http://live.dbpedia.org/sparql", query );
        //ResultSet results = qExe.execSelect();
        //ResultSetFormatter.out(System.out, results, query) ;
        // return ResultSetFactory.copyResults(qExe.execSelect()) ;
    }

	/*public static boolean ask(String query)
	{System.out.printf(endpoint,sparqlHeader + query);
		log.debug("ask query: "+query);
		try(QueryEngineHTTP qe = new QueryEngineHTTP(endpoint,sparqlHeader + query))
		{
			return qe.execAsk();}
	}
	*/

    /** @param rs needs to be zero (one value) or one-dimensional */

    public static Set<RDFNode> nodeSet(ResultSet rs)
    {
        String var = rs.getResultVars().get(0);
        Set<RDFNode> nodes = new HashSet<>();
        while(rs.hasNext())
        {
            nodes.add(rs.next().get(var));
        }
        return nodes;
    }
    public static void know_everything() throws IOException {
        PrintStream c = System.out;
        c.println("ENTER INPUT: ");
        String input = (new Scanner(System.in)).nextLine();
        ExtractNames extractNames = new ExtractNames();
        // initialize everything, before printing any output - trying to see what is taking so long!
        ScoredList[] ret = extractNames.getProperNames(input);
        System.out.println("Human names: " + ret[1].getValuesAsString());
        if (ret[1].getValuesAsString().isEmpty()) {
            c.println("NAME NOT FOUND");
            System.exit(0);
        }
        String human = ret[1].getValuesAsString().replaceAll("\\s", "_").substring(0, ret[1].getValuesAsString().indexOf(':'));
        System.out.println("input = " + human);
        System.out.println("Place names: " + ret[1].getValuesAsString());
        //File file = new File("C:\\Users\\corpi\\Documents\\robot\\permutations-december-25\\permutations\\src\\SentenceGenerator\\owl");
        File file = new File("C:\\Users\\corpi\\Documents\\robot\\permutations-december-25\\permutations\\src\\SentenceGenerator\\place");
        LineIterator it = FileUtils.lineIterator(file, "UTF-8");
        //TreeMap<Integer, String> definitionTree = new TreeMap<>(Collections.reverseOrder());
        ArrayList<String> definitionTree = new ArrayList<>();
        int i = 0;
        try {
            while (it.hasNext()) {
                String line = it.nextLine();
                if (line.indexOf("/") != -1)
                    line = line.split("[^\\w]+")[line.split("[^\\w]+").length - 1];
                definitionTree.add(line);
            }
        } finally {
            it.close();
        }
        String everything = "";
        for (String x : definitionTree)
        {
            String bestMatch =x;
            bestMatch = bestMatch.split("[^\\w]+")[bestMatch.split("[^\\w]+").length - 1];
            c.println("SUPERRESTUL = " + bestMatch);

            // Choose operation
            //"results":
            // Find birthday based on people
            try {
                ResultSet set = select("SELECT DISTINCT *\n" +
                        "WHERE {\n" +
                        "   ?city rdf:type schema:City ;\n" +
                        "         rdfs:label ?label ;\n" +
                        "         dbpedia-owl:abstract ?abstract ;\n" +
                        "         dbpedia-owl:country ?country ;\n" +
                        "         dbpprop:website ?website ;\n" +
                        "         dbpedia-owl:populationTotal ?pop .\n" +
                        "   ?country dbpprop:countryCode \"USA\"@en .\n" +
                        "   FILTER ( lang(?abstract) = 'en' and regex(?label, \"New York City\"))\n" +
                        "}");
                if (set.hasNext()) {
                    //System.out.println("RESULT:::" +  + ":::RESULT");
                    everything += set.nextSolution().toString() + "\n";
                }
            }
            catch (Exception e){}
        }
        System.out.println(everything);
    }
public static void op() throws IOException {
    PrintStream c = System.out;
    c.println("ENTER INPUT: ");
    String input  =(new Scanner(System.in)).nextLine();
    ExtractNames extractNames = new ExtractNames();
    // initialize everything, before printing any output - trying to see what is taking so long!
    ScoredList[] ret = extractNames.getProperNames(input);
    System.out.println("Human names: " + ret[0].getValuesAsString());
    if(ret[0].getValuesAsString().isEmpty()) {
        c.println("NAME NOT FOUND");
        System.exit(0);
    }
    String human = ret[0].getValuesAsString().replaceAll("\\s","_").substring(0,ret[0].getValuesAsString().indexOf(':'));
    System.out.println("input = " + human);
    System.out.println("Place names: " + ret[1].getValuesAsString());
    File file  = new File("C:\\Users\\corpi\\Documents\\robot\\permutations-december-25\\permutations\\src\\SentenceGenerator\\owl");
    LineIterator it = FileUtils.lineIterator(file, "UTF-8");
    TreeMap<Integer, String> definitionTree = new TreeMap<>(Collections.reverseOrder());
    try {
        while (it.hasNext()) {
            String line = it.nextLine();
            if(line.indexOf("/")!=-1)
                line = line.split("[^\\w]+")[line.split("[^\\w]+").length-1];
            String[] words = input.split("[^\\w]+");
            int score = 0;
            for(String word : words)
            {
                if(line.toLowerCase().contains(word.toLowerCase()))
                {
                    score += word.length();
                }
            }
            definitionTree.put(score, line);
            //c.println(words[0] + ":" + bestScore);
            // do something with line
        }
    } finally {
        it.close();
    }
    boolean foundOne = false;
    if(definitionTree.firstEntry().getKey()!=0)
        for(Map.Entry<Integer, String> entry :definitionTree.entrySet()) {
            if(!foundOne) {
                String bestMatch =entry.getValue();
                bestMatch = bestMatch.split("[^\\w]+")[bestMatch.split("[^\\w]+").length - 1];
                c.println("SUPERRESTUL = " + bestMatch);

                // Choose operation
                //"results":
                // Find birthday based on people
                ResultSet set = select("PREFIX dbpedia: <http://dbpedia.org/resource/>\n" +
                        "PREFIX dbpedia-owl: <http://dbpedia.org/ontology/>\n" +
                        "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                        "SELECT ?givenName ?surname ?birth ?ocsdfc\n" +
                        "WHERE\n" +
                        "{      \n" +
                        "  ?person dbpedia-owl:" + bestMatch + " ?ocsdfc .      \n" +//
                        //"  ?person dbpedia-owl:birthPlace ?occ .      \n" +
                        // "  ?person foaf:givenName ?givenName .      \n" +
                        //"  ?person foaf:surname ?surname .      \n" +//
                        "  VALUES ?person { dbpedia:" + human + "}\n" +
                        "}");
                if (set.hasNext()) {
                    System.out.println("RESULT:::" + set.nextSolution().toString() + ":::RESULT");
                    foundOne = true;
                }
            }
        }
    else
        c.println("NO SUCH DATA!");
}
    public static ResultSet printSet(ResultSet set)
    {
        if(set.hasNext()) {
            System.out.println("RESULT:::" + set.nextSolution().get("property"));
            return printSet(set);
        }
        else {
            return set;
        }
    }
public static void main(String[] args) throws IOException {

    ResultSet set = select("SELECT DISTINCT *\n" +
            "WHERE {\n" +
            "   ?city rdf:type schema:City ;\n" +
            "         rdfs:label ?label ;\n" +
            "         dbpedia-owl:abstract ?abstract ;\n" +
            "         dbpedia-owl:country ?country ;\n" +
            "         dbpprop:website ?website ;\n" +
            "         dbpedia-owl:populationTotal ?pop .\n" +
            "   ?country dbpprop:countryCode \"USA\"@en .\n" +
            "   FILTER ( lang(?abstract) = 'en' and regex(?label, \"New York City\"))\n" +
            "}");
    /*
   ResultSet set = select("select ?property where {\n" +
           "    ?property <http://open.vocab.org/terms/describes> <http://dbpedia.org/ontology/weapon>\n" +
           "}");
           */
   printSet(set);
know_everything();
    // Find people based on birthday:
    /*
    System.out.println(select("PREFIX ont: <http://dbpedia.org/ontology/> \n" +
            "PREFIX foaf: <http://xmlns.com/foaf/0.1/> \n" +
            "PREFIX xsd:    <http://www.w3.org/2001/XMLSchema#> \n" +
            "SELECT ?name ?date WHERE { \n" +
            "  ?person ont:birthDate ?date; \n" +
            "    foaf:name ?name . \n" +
            "  FILTER( \n" +
            "    ( ( datatype(?date) = xsd:date ) || ( datatype(?date) = xsd:dateTime ) ) && \n" +
            "    ( ?date <= \"2010-07-03\"^^xsd:dateTime ) && \n" +
            "    ( regex(str(?date), \"[0-9]{4}-07-03\") ) \n" +
            "  ) \n" +
            "}\n" +
            "LIMIT 10"));
            */
}
}