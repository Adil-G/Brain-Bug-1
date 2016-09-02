import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.InvalidFormatException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by swaggerton on 14/08/15.
 */
public class ConversationBot {
    public static List<Parse> nounPhrases= new ArrayList<Parse>();
    public static List<Parse> pastNounPhrases= new ArrayList<Parse>();
    public static List<Parse> adjpPhrases= new ArrayList<Parse>();
    public static List<Parse> NP= new ArrayList<Parse>();
    public static List<Parse> areVBP= new ArrayList<Parse>();
    public static List<Parse> end= new ArrayList<Parse>();

    public static List<String> nouns = new ArrayList<String>();
    public static List<String> adj = new ArrayList<String>();
    public static String parsedPhrase = "";
    public static List<String[]> fruits =  new ArrayList<String[]>();
    public static Parser parser;
    public static String that =  null;
    public static void main(String[] args) throws Exception{
        InputStream is = new FileInputStream("openNLP/en-parser-chunking.bin");

        ParserModel model = new ParserModel(is);

        parser = ParserFactory.create(model);

        //is.close();
        while(true) {
            String input =(new Scanner(System.in)).nextLine();
            if(that!=null) {
                input = input.replace("[tT]hat", that);
            }

            String machine = ops(input,parser);
            pastNounPhrases.clear();
            getNounPhrases(Parse(machine));
            that = pastNounPhrases.get((new Random()).nextInt(pastNounPhrases.size())).toString();
            System.out.println("***(*)))) previous word: "+that);
        }

    }
    public static Parse Parse(String input) throws InvalidFormatException, IOException {
        // http://sourceforge.net/apps/mediawiki/opennlp/index.php?title=Parser#Training_Tool

            System.out.println("its this one");
        String sentence = "Programcreek is a very huge and useful website.";
        sentence = input;
        Parse topParses[] = ParserTool.parseLine(sentence, parser, 1);

        Parse ps = null;
        String label="";
        for (Parse p : topParses) {
            p.show();
            ps = p;
            /*
            for(Parse pa :p.getTagNodes())
            {
                ps = pa;
                label  = pa.getType();
                System.out.println(ps+" => "+label);
            }
            */
        }



        return ps;
	/*
	 * (TOP (S (NP (NN Programcreek) ) (VP (VBZ is) (NP (DT a) (ADJP (RB
	 * very) (JJ huge) (CC and) (JJ useful) ) ) ) (. website.) ) )
	 */
    }
    public static String[] SentenceDetect(String input) throws InvalidFormatException,
            IOException {
        String paragraph = "Hi. How are you? This is Mike.";
        paragraph = input;
        // always start with a model, a model is learned from training data
        InputStream is = new FileInputStream("openNLP/en-sent.bin");
        SentenceModel model = new SentenceModel(is);
        SentenceDetectorME sdetector = new SentenceDetectorME(model);

        String sentences[] = sdetector.sentDetect(paragraph);
        is.close();
        return sentences;
    }
    public static void getNounPhrases(Parse p,String labelA,String labelB) {
        if (p.getType().equals("NP")) {
            nounPhrases.add(p);
            NP.add(p);
            //System.out.println("SFLSKEJF");
        }
        if (p.getType().equals(labelB)||p.getType().equals("VP")||p.getType().equals("PP")) {
            adjpPhrases.add(p);
        } if (p.getType().equals("NNS")) {
            areVBP.add(p);
        }
        if (p.getType().equals(".")) {
            end.add(p);
        }

        for (Parse child : p.getChildren()) {
            getNounPhrases(child,labelA,labelB);
        }
    }

    public static boolean getRandomBoolean() {
        return Math.random() < 0.5;
        //I tried another approaches here, still the same result
    }
    public static void getNounPhrases(Parse p) {
        if (p.getType().equals("NP")) {
            pastNounPhrases.add(p);
            System.out.println("EEEEEEEEE " + p + " EEEEEEEEE " + p.getType());
            //NP.add(p);
            //System.out.println("SFLSKEJF");
        }
        for (Parse child : p.getChildren()) {
            System.out.println("IIIIIIII "+child+" IIIIII "+child.getType());
            getNounPhrases(child);
        }
    }
    public static String ops(String par,Parser parser) throws Exception {

        nounPhrases.clear();
        String paragraph = "Ubisoft is a very huge and useful website.";
        //paragraph = (new Scanner(System.in)).nextLine();
        paragraph = par;
        String[] sentences = SentenceDetect(paragraph);
        String response = "* no comment *";
        String result = "";
        for(String input: sentences) {
            System.out.println("\n$$$ "+input);
            System.out.println("%%%%%%%%%%%#GGSDG");
            getNounPhrases(Parse(input), "NP", "VP");
            int max = Math.min(nounPhrases.size(),adjpPhrases.size());
            if(max>0) {
                max = 1;
                for (int i = 0; i < max; i++) {
                    String np = nounPhrases.get(i).toString();
                    String adjp = adjpPhrases.get(i).toString();
                    String china = null;//ops2(np);
                    if(true||np.toLowerCase().contains("china")) {
                        if(true||china==null) {
                            String predicate = "               "+adjp.split(",")[0].toLowerCase()+"                ";
                            String me = predicate;
                            String you = predicate;
                            me = me.replaceAll(" my", " your").replaceAll(" i ", " you ").replaceAll(" me ", " you ")
                            .replaceAll(" mine ", " yours ");
                            you  = you.replaceAll(" your", "  my").replaceAll(" you ", " i ")
                                    .replaceAll(" yours ", " mine ");
                            System.out.println(predicate);
                            predicate = me;
                            predicate = predicate.trim();
                            /*
                            List<String> no = new TestOpenNLP().ops3(predicate, parser,false);
                            //response = "I suppose that " + n.get((new Random()).nextInt(n.size()));
                            response = no.get((new Random()).nextInt(no.size()));
                            result = response;
                            System.out.println("answer: "+result);
                            System.exit(0);
                            */
                            if(true||input.toLowerCase().startsWith("what")||input.toLowerCase().startsWith("why")
                                    ||input.toLowerCase().startsWith("who")
                                    ||input.toLowerCase().startsWith("where")
                                    ||input.toLowerCase().startsWith("when")
                                    ||input.toLowerCase().startsWith("how"))
                            {
                                if(false&&NP.size()>0&&areVBP.size()>0&&end.size()>0) {
                                    response= NP.get(0).toString()+" " +areVBP.get(0).toString()+" " + end.get(0).toString()+" because " + new TestOpenNLP().ops3(predicate,parser,false);
                                    NP.clear();areVBP.clear();end.clear();
                                }
                                else
                                {
                                    boolean isPersonal = false;
                                    if(predicate.toLowerCase().contains("think")) {
                                        isPersonal = true;
                                    }
                                    if(!isPersonal) {
                                        if (areVBP.size() > 0) {
                                            List<String> n = new TestOpenNLP().ops3(predicate, parser,isPersonal);
                                            //response = "I suppose that they " + n.get((new Random()).nextInt(n.size()));
                                            response = n.get((new Random()).nextInt(n.size()));
                                            result = response;
                                            areVBP.clear();
                                        } else {
                                            List<String> n = new TestOpenNLP().ops3(predicate, parser,isPersonal);
                                            //response = "I suppose that " + n.get((new Random()).nextInt(n.size()));
                                            response = n.get((new Random()).nextInt(n.size()));
                                            result = response;
                                        }
                                    }
                                    else
                                    {
                                        if(getRandomBoolean()) {
                                            List<String> n = new TestOpenNLP().ops3(predicate, parser,isPersonal);
                                            result = n.get((new Random()).nextInt(n.size()));
                                            response = "Yes, I think "+result;

                                        }
                                        else
                                        {
                                            List<String> n = new TestOpenNLP().ops3(predicate, parser,isPersonal);
                                            result = n.get((new Random()).nextInt(n.size()));
                                            response = "No, this is not the case "+result;
                                        }

                                    }
                                }
                                System.out.println("\n@ "+response);
                            }
                            else if(input.toLowerCase().startsWith("you"))
                            {
                                if (getRandomBoolean()) {
                                    response = "\n@ No, it is not the case that I" + predicate;
                                    result = predicate;
                                    System.out.println(response);
                                } else {
                                    result = predicate;
                                    System.out.println("\n@ Of course! I " + predicate);
                                }
                            }
                            else if(false){
                                if (getRandomBoolean()) {
                                    result =  new TestOpenNLP().ops4(predicate);
                                    response = "Yes, " + predicate + " that are " +result;
                                    System.out.println(response);
                                }
                                else
                                {
                                    result= new TestOpenNLP().ops4(predicate);
                                    response = "I personaly hate " + predicate + " specifically because they are " +result;
                                    System.out.println(response);
                                }
                                /*
                                if (getRandomBoolean()) {
                                    System.out.println("\n@ No, I do not " + predicate);
                                } else {
                                    System.out.println("\n@ Of course! I'd love to " + predicate);
                                }
                                */
                            }
                            else
                            {
                                if(predicate.toLowerCase().contains("think")) {
                                    if(getRandomBoolean()) {
                                        List<String> n = new TestOpenNLP().ops3(predicate, parser,true);
                                        result = n.get((new Random()).nextInt(n.size()));
                                        response = "Yes, I think "+result;

                                    }
                                    else
                                    {
                                        List<String> n = new TestOpenNLP().ops3(predicate, parser,true);
                                        result = n.get((new Random()).nextInt(n.size()));
                                        response = "No, this is not the case "+result;
                                    }
                                    System.out.println("\n@ "+response);
                                }
                                else
                                {
                                    result = adjp.split(",")[0];
                                    response = "\n@ "+china+"--> " + adjp.split(",")[0];
                                    System.out.println(response);
                                }
                            }
                        }
                        else
                        {
                            result = adjp.split(",")[0];
                            response = "\n@ "+china+"--> " + adjp.split(",")[0];
                            System.out.println(response);
                        }
                    }
                }
            }
            nounPhrases.clear();
            adjpPhrases.clear();
            areVBP.clear();

        }
        return result;
    }

}
