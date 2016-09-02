package SentenceGenerator;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.VCARD;
import ch.qos.logback.classic.LoggerContext;
import org.apache.xerces.util.XMLChar;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.jena.iri.IRIException;
import org.apache.jena.iri.IRIFactory;
import org.apache.jena.ext.com.google.common.cache.RemovalNotification;
/**
 * Created by corpi on 2016-04-24.
 */

public class SparqlQueryExecuter {
    public static void main(String[] args)
    {
        System.out.println(mostrarTodo());
        /*
        String filename = "example3.rdf";

        // Create an empty model
        Model model = ModelFactory.createDefaultModel();

        // Use the FileManager to find the input file
        InputStream in = FileManager.get().open(filename);

        if (in == null)
            throw new IllegalArgumentException("File: "+filename+" not found");

        // Read the RDF/XML file
        model.read(in, null);

        // List all the resources with the property "vcard:FN"
        String queryString =
                "PREFIX vcard: <" + VCARD.getURI() + "> " +
                        "SELECT ?Subject "+
                        "WHERE { ?Subject vcard:FN ?FullName.} ";
        com.hp.hpl.jena.query.Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model) ;
        ResultSet results = qexec.execSelect() ;

        while (results.hasNext())
        {
            QuerySolution binding = results.nextSolution();
            Resource subj = (Resource) binding.get("Subject");
            System.out.println("Subject: "+subj.getURI());
        }
        */
    }

    public static List<String> mostrarTodo(){

        Model model = ModelFactory.createDefaultModel();
        String queryString =
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                        "PREFIX type: <http://dbpedia.org/class/yago/>\n" +
                        "PREFIX prop: <http://dbpedia.org/property/>\n" +
                        "SELECT ?country_name ?population\n" +
                        "WHERE {\n" +
                        " ?country a type:LandlockedCountries ;\n" +
                        " rdfs:label ?country_name ;\n" +
                        " prop:populationEstimate ?population .\n" +
                        " FILTER (?population > 15000000) .\n" +
                        "}";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model) ;
        ResultSet results = qexec.execSelect() ;


        ArrayList<String> resultadoConsulta = new ArrayList<String>();

        while (results.hasNext())
        {
            QuerySolution binding = results.nextSolution();
            System.out.println(">>>>>"+binding.toString());
            Resource subj = (Resource) binding.get("Subject");
            String categoria=subj.getProperty(VCARD.CATEGORIES).getLiteral().toString();
            if(categoria.length()>22)
                categoria=categoria.substring(23);
            else
                categoria="actividad";
            resultadoConsulta.add("Evento: "+subj.getProperty(VCARD.TITLE).getLiteral()+"\n Fecha: "+subj.getProperty(VCARD.Other).getLiteral()+"  Categoría: "+categoria+"\n Biblioteca: "+subj.getProperty(VCARD.Locality).getLiteral());
        }

        return resultadoConsulta;

    }
}
