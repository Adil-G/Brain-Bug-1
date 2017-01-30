package general.chat;

import javax.xml.parsers.DocumentBuilderFactory;
        import javax.xml.parsers.DocumentBuilder;
        import org.w3c.dom.Document;
        import org.w3c.dom.NodeList;
        import org.w3c.dom.Node;

import java.io.InputStream;
import java.util.ArrayList;

public class ReadXML {

    public static ArrayList<String> getASIN8(InputStream argv) {
        String headline = "Carrie Fisher: Star Wars actress suffers heart attack";
        try {

            //File fXmlFile = new File("/Users/mkyong/staff.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(argv);

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            ArrayList<String> linup = new ArrayList<>();
            NodeList list = doc.getDocumentElement().getElementsByTagName("ASIN");
            int ASINCount = 0;
            int MAX_PRODUCT = 8;
            for(int i =0;i<list.getLength();i++) {
                if(ASINCount++<MAX_PRODUCT) {
                    Node node = list.item(i);
                    System.out.println(node.getFirstChild().getNodeValue());
                    linup.add(node.getFirstChild().getNodeValue());
                }
            }
            return linup;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static ArrayList<String> getASIN4(InputStream argv) {
        String headline = "Carrie Fisher: Star Wars actress suffers heart attack";
        try {

            //File fXmlFile = new File("/Users/mkyong/staff.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(argv);

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            ArrayList<String> linup = new ArrayList<>();
            NodeList list = doc.getDocumentElement().getElementsByTagName("ASIN");
            int ASINCount = 0;
            int MAX_PRODUCT = 4;
            for(int i =0;i<list.getLength();i++) {
                if(ASINCount++<MAX_PRODUCT) {
                    Node node = list.item(i);
                    System.out.println(node.getFirstChild().getNodeValue());
                    linup.add(node.getFirstChild().getNodeValue());
                }
            }
            return linup;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}