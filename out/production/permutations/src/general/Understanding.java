package general;

import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.util.InvalidFormatException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by corpi on 2016-05-07.
 */
public class Understanding {
    public static POSModel model;
    public static PerformanceMonitor perfMon;
    public static POSTaggerME tagger;
    public static int MAX_SEARCHES = 100;
    public static ArrayList<String> info = new ArrayList<String>();
    public static Parser parser;
    public static void initialize() throws IOException {
        System.out.println("WFIWOFWOEFIJ");
        POSModel model = new POSModelLoader()
                .load(new File("openNLP/en-pos-maxent.bin"));
        perfMon = new PerformanceMonitor(System.err, "sent");
        tagger = new POSTaggerME(model);
        InputStream is = new FileInputStream("openNLP/en-parser-chunking.bin");

        ParserModel parserModel1 = new ParserModel(is);

        parser = ParserFactory.create(parserModel1);
        perfMon.start();
        System.out.println("DONE INIT");

    }
    static HashMap<String,Parse> phrases = new HashMap<String,Parse>();

    public static void getAnyPhraseSS(Parse p, final String TAG) {
        if(p!=null) {

            if (p.getType().equals(TAG)) {
                if(phrases.containsKey(TAG)) {
                    if (phrases.get(TAG).getText().length() < p.getText().length()) {
                        phrases.put(TAG, p);
                    }

                }
                else{
                    phrases.put(TAG, p);
                }

            }
            for (Parse child : p.getChildren()) {
                getAnyPhraseSS(child, TAG);

            }
        }
    }
    static HashMap<String,TreeMap<Integer,HashSet<Parse>>> phrasesDataBase =
            new HashMap<>();
    public static void getAnyPhraseSSDB(Parse p, final String TAG) {
        if(p!=null) {
            int len = p.getText().length();
            if(phrasesDataBase.containsKey(TAG)) {
                TreeMap<Integer,HashSet<Parse>> y
                        = new TreeMap<Integer,HashSet<Parse>>(Collections.reverseOrder());
                y.putAll(phrasesDataBase.get(TAG));
                if(phrasesDataBase.get(TAG).containsKey(len))
                {
                    HashSet<Parse> x = new HashSet<>(phrasesDataBase.get(TAG).get(len));
                    x.add(p);
                    y.put(len,x);
                    phrasesDataBase.put(TAG,y);
                }
                else
                {
                    HashSet<Parse> x = new HashSet<>();
                    x.add(p);
                    y.put(len,x);
                    phrasesDataBase.put(TAG,y);
                }


            }
            else{
                HashSet<Parse> x = new HashSet<>();
                x.add(p);
                TreeMap<Integer,HashSet<Parse>> y=
                        new TreeMap<>(Collections.reverseOrder());
                y.put(len,x);
                phrasesDataBase.put(TAG, y);
            }


            for (Parse child : p.getChildren()) {
                getAnyPhraseSSDB(child, TAG);

            }
        }
    }
    public static Parse Parse(String input,Parser parserTemp) throws InvalidFormatException, IOException {
        // http://sourceforge.net/apps/mediawiki/opennlp/index.php?title=Parser#Training_Tool
        System.out.println("GOGOOG");
        String sentence = "Programcreek is a very huge and useful website.";
        sentence = input;
        Parse[] topParses;
        /*try {

            topParses = ParserTool.parseLine(sentence, parserTemp, 1);
        }catch (StringIndexOutOfBoundsException e)
        {
            topParses = new Parse[0];

            InputStream is = new FileInputStream("openNLP/en-parser-chunking.bin");

            ParserModel model = new ParserModel(is);

            Parser parser = ParserFactory.create(model);
            return Parse(input,parser);

        }
        */
        topParses = ParserTool.parseLine(sentence, parserTemp, 1);
        Parse ps = null;
        String label="";
        System.out.println(topParses.length);
        for (Parse p : topParses) {
            //System.out.print("asdfadsfasdf\n");
            //p.show();
            ps = p;
            //getAnyPhraseSS(p,"PP");
            /*
            for(Parse pa :p.getTagNodes())
            {
                ps = pa;
                label  = pa.getType();
                System.out.println(ps+" => "+label);
            }
            */
        }

        //is.close();

        return ps;
	/*
	 * (TOP (S (NP (NN Programcreek) ) (VP (VBZ is) (NP (DT a) (ADJP (RB
	 * very) (JJ huge) (CC and) (JJ useful) ) ) ) (. website.) ) )
	 */
    }
    public static void refresh()
    {
        phrases.clear();
    }
    public static HashMap<String, TreeMap<Integer, HashSet<Parse>>> parseIdea(String query) throws IOException {
        initialize();
        getAnyPhraseSSDB(Parse(query,parser),"VP");
        getAnyPhraseSSDB(Parse(query,parser),"NP");
        getAnyPhraseSSDB(Parse(query,parser),"JJ");
        System.out.println("subject: "+phrasesDataBase.get("VP"));
        System.out.println("object: "+phrasesDataBase.get("NP"));
        System.out.println("adjectives: "+phrasesDataBase.get("JJ"));
        System.out.println(phrasesDataBase);
        return phrasesDataBase;
    }
    public static HashMap<String,Parse> goIdea(String query) throws IOException {
        initialize();
        getAnyPhraseSS(Parse(query,parser),"VP");
        getAnyPhraseSS(Parse(query,parser),"NP");
        getAnyPhraseSS(Parse(query,parser),"JJ");
        System.out.println("subject: "+phrases.get("VP"));
        System.out.println("object: "+phrases.get("NP"));
        System.out.println("adjective: "+phrases.get("JJ"));
        System.out.println(phrases);
        return phrases;
    }
    public static HashMap<String,Parse> go(String query) throws IOException {
        phrases.clear();
        if(parser==null) {
            initialize();
        }

        getAnyPhraseSS(Parse(query,parser),"VP");
        getAnyPhraseSS(Parse(query,parser),"NP");

       // System.out.println(phrases);
        if(phrases.get("VP").getText().length()<phrases.get("NP").getText().length())
        {
            Parse temp = phrases.get("VP");
            phrases.put("VP", phrases.get("NP"));
            phrases.put("NP", temp);
        }
        //System.out.println("subject: "+phrases.get("VP"));
        //System.out.println("object: "+phrases.get("NP"));
        return phrases;
    }
}
