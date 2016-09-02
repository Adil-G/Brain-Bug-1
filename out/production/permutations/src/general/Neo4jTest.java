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

public class Neo4jTest {
    public static Driver driver;
    public static Session session;
    public static void updateBrain(String phrase, String noun) throws IOException {
        if(driver==null)
            driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "000777" ) );

        if(session==null)
            session = driver.session();
        HashMap<String, Parse> phrases = Understanding.go(phrase);
        System.out.println("subject: " + phrases.get("VP"));
        System.out.println("object: " + phrases.get("NP"));
        StringBuffer message = new StringBuffer();
        message.append("CREATE (a:Idea {");
        message.append("question"+":'"+noun
                .replaceAll("[^\\w]+"," ")
                .replaceAll("'"," ").replaceAll("[()]","").replaceAll("\\s+"," ").trim()+"'");
        if(phrases.size()>0)
            message.append(", ");
        int count =0;
        for(Map.Entry<String,Parse> entry:phrases.entrySet())
        {
            ++count;
            if(entry.getKey().equals("NP"))
                message.append(entry.getKey()
                        .replaceAll("[^\\w]+"," ")
                        .replaceAll("[,\"]+","")
                        .replaceAll("'"," ").replaceAll("[()]","").trim()+":'"+(FindKeyWordsTest.getPlaces(phrase)+" "+FindKeyWordsTest.findName(phrase)+" "+entry.getValue().toString()).replaceAll("[\\[\\],\"]+","")
                        .replaceAll("[^\\w]+"," ")
                        .replaceAll("'"," ").replaceAll("[()]","").replaceAll("\\s+"," ").trim()+"'");
            else
                message.append(entry.getKey()
                        .replaceAll("[^\\w]+"," ")
                        .replaceAll("[,\"]+","")
                        .replaceAll("'"," ").replaceAll("[()]","").trim()+":'"+entry.getValue().toString().replaceAll("[,\"]+","")
                        .replaceAll("[^\\w]+"," ")
                        .replaceAll("'"," ").replaceAll("[()]","").replaceAll("\\s+"," ").trim()+"'");
            if(count<phrases.size())
                message.append(", ");

        }
        message.append(", ");
        message.append("JJ".replaceAll("[,\"]+","")
                .replaceAll("[^\\w]+"," ")
                .replaceAll("'[A-Za-z]"," ").replaceAll("[()]","")+":'"+FindKeyWordsTest.getVerbs(phrase).toString().replaceAll("[,\"]+","")
                .replaceAll("[^\\w]+"," ")
                .replaceAll("'[A-Za-z]"," ").replaceAll("[()]","")+"'");
        message.append("})");
        System.out.println("2312 message sent to database:\n...\t"+ message.toString());
        session.run(message.toString());
        session.close();
    }

    public static void main(String[] args)
    {
        Driver driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "000777" ) );

        Session session = driver.session();



        session.run( "MATCH (n) DETACH\n" +
                "DELETE n" );





    }
    public static void template()
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
