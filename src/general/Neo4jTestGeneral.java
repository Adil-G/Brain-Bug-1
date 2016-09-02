package general; /**
 * Created by corpi on 2016-05-14.
 */
import opennlp.tools.parser.Parse;
import org.neo4j.driver.v1.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

public class Neo4jTestGeneral {
    public static Driver driver;
    public static Session session;
    public static void updateBrain(String phrase, String noun) throws IOException {
        if(driver==null)
            driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "000777" ) );

        if(session==null)
            session = driver.session();
        HashMap<String, TreeMap<Integer, HashSet<Parse>>> phrases = Understanding.parseIdea(phrase);
        System.out.println("subject: " + phrases.get("VP"));
        System.out.println("object: " + phrases.get("NP"));
        StringBuffer message = new StringBuffer();
        message.append("CREATE (a:Idea {");
        message.append("question"+":'"+noun+"'");
        if(phrases.size()>0)
            message.append(", ");
        int count =0;
        HashMap<String,String> ph = new HashMap<>();
        final int MAX_ARITFACTS = 2;

        for(Map.Entry<String, TreeMap<Integer, HashSet<Parse>>> entry : phrases.entrySet())
        {
            try {
                String TAG = entry.getKey().toString();
                StringBuffer body = new StringBuffer();
                int artifactCount =0;
                for (Parse artifact : entry.getValue().firstEntry().getValue())
                {
                    if(artifactCount++<MAX_ARITFACTS)
                        body.append(artifact.toString() + " ");
                }
                System.out.println("876857: "+TAG+": " + body.toString().trim());
                ph.put(TAG,body.toString().trim());
            }catch (Exception e)
            {
                System.out.println("ERROR: COULD NOT FIND INFORMATION");
            }
        }
        for(Map.Entry<String,String> entry:ph.entrySet())
        {
            ++count;
            message.append(entry.getKey().replaceAll("[,\"]+","")
                    .replaceAll("'[A-Za-z]"," ").replaceAll("[()]","")+":'"+entry.getValue().toString().replaceAll("[,\"]+","")
                    .replaceAll("'[A-Za-z]"," ").replaceAll("[()]","")+"'");
            if(count<phrases.size())
                message.append(", ");

        }
        message.append("})");
        System.out.println("2312 message sent to database:\n...\t"+ message.toString());
        session.run(message.toString());
        session.close();
    }
    public static void main(String[] args)
    {




        Driver driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "000777" ) );

        Session session = driver.session();



        session.run( "CREATE (a:Person {name:'Arthur', title:'King'})" );



        StatementResult result = session.run( "MATCH (a:Person) WHERE a.name = 'Arthur' RETURN a.name AS name, a.title AS title" );

        while ( result.hasNext() )

        {

            Record record = result.next();

            System.out.println( record.get( "title" ).asString() + " " + record.get("name").asString() );

        }



        session.close();

        driver.close();
    }
}
