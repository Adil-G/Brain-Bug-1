package SentenceGenerator;

import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by swaggerton on 22/08/15.
 */
public class Parsit {
    public static Map<Parse, Integer> parseMap = new TreeMap<Parse, Integer>();
    public static List<Parse> nounPhrases= new ArrayList<Parse>();
    public static List<Parse> parses = new ArrayList<Parse>();
    public static String TOK_NODE = "";
    public static String parsed = "";
    public static List<String> pp = new ArrayList<String>();
    public static List<String> pps = new ArrayList<String>();
    static public void main(String[] argv) throws IOException {
        //Parse((new Scanner(System.in)).nextLine());
        Parse("Among the 34 states in January 1861, seven Southern slave states individually declared their secession from the United States and formed the Confederate States of America. The Confederacy, often simply called the South, grew to include eleven states, and although they claimed thirteen states and additional western territories, the Confederacy was never diplomatically recognized by a foreign country. The states that remained loyal and did not declare secession were known as the Union or the North.");
        System.out.println("\nAmong the 34 states in January 1861, seven Southern slave states\n individually declared their secession from the United States and formed the Confederate\n States of America. The Confederacy, often simply called the South, \ngrew to include eleven states, and although they claimed thirteen states and additional\n western territories, the Confederacy was never diplomatically recognized by a foreign country. \nThe states that remained loyal and did not declare secession were known as the Union or the North.");
        //System.out.println("$$$$$$$$$$$$$$$$$");
        System.out.println(parsed);
        System.out.println("$$$$$$$$$$$$$$$$$");
        for(Parse s:nounPhrases)
        {
        //    System.out.println(s.toString());
        }
        List<String> k = new ArrayList<String>();
        getAll();
        for(String y:pps)
        {
            System.out.println("AAASSSSFFFFF: "+y);
            y = y.replaceAll("\\([A-Z\\$]{1,2}","").replaceAll("\\)","");
            k.add(y);

        }
        for(String x:k)
        {
            System.out.println("KEOPOIOPOIP: "+x);
        }

    }
    public static void getAll()
    {
        getPP(parsed);
        String newString = "";
        for (String x : pp) {
            System.out.println(x);
            newString += x;
        }
        pps.add(newString);
        String n = "\\";
        //newString = newString.replaceAll("\\(","(").replaceAll("\\)", ")");
        parsed = parsed.substring(0,parsed.lastIndexOf("(PP")+1);
        System.out.println("EWIOUFOIFJOFSD: " + parsed);
        //parsed = parsed.replaceAll(newString,"");
        if(parsed.contains("(PP")) {
            pp.clear();
            getAll();
        }
    }
    public static void getNounPhrases(Parse p,String labelA) {
        if (p.getType().equals(labelA)) {
            nounPhrases.add(p);
        }
        for (Parse child : p.getChildren()) {
            getNounPhrases(child,labelA);
        }
    }
    public static String getPP(String parsePP)
    {
        if(parsePP.contains("(PP"))
        {
            parsePP = parsePP.substring(parsePP.indexOf("(PP")+1, parsePP.length());
            Matcher m = Pattern.compile("(\\()").matcher(parsePP);
            int count = 0;
            while (m.find()) {
                count++;
            }
            System.err.format("Found %1$s matches\n", count);
            String end = "";
            for(int i=0;i<count;i++)
            {
                end+=")";
            }
            if(parsePP.contains("(PP")) {
                String substringPP = parsePP.substring(parsePP.lastIndexOf("(PP"), parsePP.length()-1);
                // this is the final stage
                // CUT OFF AT THE CLOSING BRACKETS
                if(substringPP.indexOf(end)>=0) {
                    substringPP = substringPP.substring(0, substringPP.indexOf(end) - 1);
                }
                    pp.add(substringPP);
                    System.out.println("EWIOJEWIORUOEWIRU: " + substringPP);
                    parsePP = parsePP.substring(parsePP.indexOf(end) + 1, parsePP.length());
                    getPP(parsePP);

            }
        }
        return "";
    }
    private static void show(Parse p) {
             int start;
             start = p.getSpan().getStart();
              if (true||!p.getType().equals(TOK_NODE)) {
                  parsed+="("+p.getType();
                    System.out.print("(");
                    System.out.print(p.getType());
            if (parseMap.containsKey(p)) {
                parsed+="#"+parseMap.get(p);
                          System.out.print("#"+parseMap.get(p));
                       }
                    //System.out.print(p.hashCode()+"-"+parseMap.containsKey(p));
                  parsed+=" ";
                            System.out.print(" ");
                  }
              Parse[] children = p.getChildren();
              for (int pi=0,pn=children.length;pi<pn;pi++) {
                    Parse c = children[pi];
                    Span s = c.getSpan();
                    if (start < s.getStart()) {
                          System.out.print(p.getText().substring(start, s.getStart()));
                        parsed+=p.getText().substring(start, s.getStart());
                        }
                   show(c);
                  start = s.getEnd();
        }
        parsed+=p.getText().substring(start, p.getSpan().getEnd());
             System.out.print(p.getText().substring(start, p.getSpan().getEnd()));
              if (true||!p.getType().equals(TOK_NODE)) {
                  parsed+=")";
                    System.out.print(")");
                  }
    }

    public static void Parse(String sentence) throws InvalidFormatException, IOException {
        // http://sourceforge.net/apps/mediawiki/opennlp/index.php?title=Parser#Training_Tool
        InputStream is = new FileInputStream("openNLP/en-parser-chunking.bin");

        ParserModel model = new ParserModel(is);

        Parser parser = ParserFactory.create(model);

        //String sentence = "Programcreek is a very huge and useful website.";
        Parse topParses[] = ParserTool.parseLine(sentence, parser, 1);

        for (Parse p : topParses) {
            show(p);
            nounPhrases.clear();
            getNounPhrases(p,"PP");
        }

        is.close();

	/*
	 * (TOP (S (NP (NN Programcreek) ) (VP (VBZ is) (NP (DT a) (ADJP (RB
	 * very) (JJ huge) (CC and) (JJ useful) ) ) ) (. website.) ) )
	 */
    }

}
