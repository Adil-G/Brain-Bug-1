package SentenceGenerator;
//    Copyright 2013 Petter Törnberg
//
//    This demo code has been kindly provided by Petter Törnberg <pettert@chalmers.se>
//    for the SentiWordNet website.
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.

import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

import java.io.*;
import java.util.*;

public class SentiWordNetDemoCode {

    private Map<String, Double> dictionary;

    public SentiWordNetDemoCode(String pathToSWN) throws IOException {
        // This is our main dictionary representation
        dictionary = new HashMap<String, Double>();

        // From String to list of doubles.
        HashMap<String, HashMap<Integer, Double>> tempDictionary = new HashMap<String, HashMap<Integer, Double>>();

        BufferedReader csv = null;
        try {
            csv = new BufferedReader(new FileReader(pathToSWN));
            int lineNumber = 0;

            String line;
            while ((line = csv.readLine()) != null) {
                lineNumber++;

                // If it's a comment, skip this line.
                if (!line.trim().startsWith("#")) {
                    // We use tab separation
                    String[] data = line.split("\t");
                    String wordTypeMarker = data[0];

                    // Example line:
                    // POS ID PosS NegS SynsetTerm#sensenumber Desc
                    // a 00009618 0.5 0.25 spartan#4 austere#3 ascetical#2
                    // ascetic#2 practicing great self-denial;...etc

                    // Is it a valid line? Otherwise, through exception.
                    if (data.length != 6) {
                        throw new IllegalArgumentException(
                                "Incorrect tabulation format in file, line: "
                                        + lineNumber);
                    }

                    // Calculate synset score as score = PosS - NegS
                    Double synsetScore = Double.parseDouble(data[2])
                            - Double.parseDouble(data[3]);

                    // Get all Synset terms
                    String[] synTermsSplit = data[4].split(" ");

                    // Go through all terms of current synset.
                    for (String synTermSplit : synTermsSplit) {
                        // Get synterm and synterm rank
                        String[] synTermAndRank = synTermSplit.split("#");
                        String synTerm = synTermAndRank[0] + "#"
                                + wordTypeMarker;

                        int synTermRank = Integer.parseInt(synTermAndRank[1]);
                        // What we get here is a map of the type:
                        // term -> {score of synset#1, score of synset#2...}

                        // Add map to term if it doesn't have one
                        if (!tempDictionary.containsKey(synTerm)) {
                            tempDictionary.put(synTerm,
                                    new HashMap<Integer, Double>());
                        }

                        // Add synset link to synterm
                        tempDictionary.get(synTerm).put(synTermRank,
                                synsetScore);
                    }
                }
            }

            // Go through all the terms.
            for (Map.Entry<String, HashMap<Integer, Double>> entry : tempDictionary
                    .entrySet()) {
                String word = entry.getKey();
                Map<Integer, Double> synSetScoreMap = entry.getValue();

                // Calculate weighted average. Weigh the synsets according to
                // their rank.
                // Score= 1/2*first + 1/3*second + 1/4*third ..... etc.
                // Sum = 1/1 + 1/2 + 1/3 ...
                double score = 0.0;
                double sum = 0.0;
                for (Map.Entry<Integer, Double> setScore : synSetScoreMap
                        .entrySet()) {
                    score += setScore.getValue() / (double) setScore.getKey();
                    sum += 1.0 / (double) setScore.getKey();
                }
                score /= sum;

                dictionary.put(word, score);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (csv != null) {
                csv.close();
            }
        }
    }

    public double extract(String word, String pos) {
        return dictionary.get(word + "#" + pos);
    }
    public static String POSTag(String input) throws IOException {
        POSModel model = new POSModelLoader()
                .load(new File("openNLP/en-pos-maxent.bin"));
        PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
        POSTaggerME tagger = new POSTaggerME(model);

        ;
        ObjectStream<String> lineStream = new PlainTextByLineStream(
                new StringReader(input));

        perfMon.start();
        String line;
        String result = "";
        while ((line = lineStream.read()) != null) {

            String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE
                    .tokenize(line);
            String[] tags = tagger.tag(whitespaceTokenizerLine);

            POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
            System.out.println(sample.toString());
            result += sample.toString();
            perfMon.incrementCounter();
        }
        perfMon.stopAndPrintFinalResult();
        return result;
    }
    public static void main(String [] args) throws IOException {
        connotation("I am a bit upset");
        /*if(args.length<1) {
            System.err.println("Usage: java SentiWordNetDemoCode <pathToSentiWordNetFile>");
            return;
        }*/


       /* System.out.println("fantastic#a"+sentiwordnet.extract("fantastic", "a"));
        System.out.println("bad#a "+sentiwordnet.extract("bad", "a"));
        System.out.println("blue#a "+sentiwordnet.extract("blue", "a"));
        System.out.println("blue#n "+sentiwordnet.extract("blue", "n"));
        */
    }

    public static double connotation(String input) throws IOException {
        String s = input;
        s = s.replaceAll("[^\\w]+"," ");
        String posTags  = POSTag(s);
        String pathToSWN = "C:\\Users\\corpi\\Documents\\robot\\permutations-december-25\\permutations\\src\\SentiWordNet_3.0.0_20130122.txt";
        SentiWordNetDemoCode sentiwordnet = new SentiWordNetDemoCode(pathToSWN);
        String[] words = posTags.split("\\s+");
        double total = 0.0;
        for(String wordData : words)
        {
            // fomat = "word_tag"
            try{
                String tag = wordData.split("_")[1];
                String word =wordData.split("_")[0];
                if(tag.toLowerCase().contains("n"))
                {
                    double value = sentiwordnet.extract(word, "n");
                    //System.out.println(word+": "+value);
                    total += value;
                }
                else if(tag.toLowerCase().contains("j"))
                {
                    double value = sentiwordnet.extract(word, "a");
                    //System.out.println(word+": "+ value);
                    total += value;
                }
            }catch(Exception e)
            {
                continue;
            }
        }
        System.out.println("total: " + total);
        return total;
    }
    public static ArrayList<String> bestPhrases(ArrayList<String> sentences,int NumberOfFacts) throws IOException {
        Map<Double, String> mostCommonWords = new TreeMap<Double, String>(Collections.reverseOrder());
        Map<String, Double> noRepetition = new HashMap<String, Double>();
        // compare list to itself
        for (int i = 0; i < sentences.size(); i++) {
            double factorSum = 0;
            for (int j = 0; j < sentences.size(); j++) {
                double currentFactor;
                // check to see if there is repetition
                String assumeCombo = sentences.get(i) + sentences.get(j);
                if (noRepetition.containsKey(assumeCombo)) {

                    // use previous comparison factor
                    currentFactor = noRepetition.get(assumeCombo);
                    // accumulate maching factors for coparison later on
                    factorSum += currentFactor;
                }
                // if there is no repetition, register combo in memory and continue as normal.
                else {
                    // compare the two objects
                    currentFactor = ComparePhrases.compare(sentences.get(i), sentences.get(j));
                    String anticipateCombo = sentences.get(j) + sentences.get(i);
                    noRepetition.put(anticipateCombo, currentFactor);

                    // accumulate maching factors for coparison later on
                    factorSum += currentFactor;
                }
            }
            // sort sentences by matching factor
            mostCommonWords.put(factorSum, sentences.get(i));
                    /*
                    if(factorSum>bestFactor)
                    {
                        bestSentence = sentences.get(i);
                        bestFactor=factorSum;
                    }*/

        }
        ///System.out.println("#####################################################################################");

        Map<Double, String> bestResults = new TreeMap<Double, String>(Collections.reverseOrder());
        ArrayList<String>list = new ArrayList<String>();
        int i = 0;
        boolean stop = true;
        for (Map.Entry entry : mostCommonWords.entrySet()) {
            if (i++ < NumberOfFacts&&stop) {
                list.add(entry.getValue().toString());
                //System.out.println("Added: "+entry.getValue().toString());
                //System.out.println(entry);
                stop = true;
                //double factor  = ComparePhrases.compare(input, entry.getValue().toString());
                //bestResults.put(factor,entry.getValue().toString());
            }
        }
        return list;
    }
    public static double connotationGeneral(String input) throws IOException {
        String[] s = input.split("[^\\w]+");

        String pathToSWN = "C:\\Users\\corpi\\Documents\\robot\\permutations-december-25\\permutations\\src\\SentiWordNet_3.0.0_20130122.txt";
        SentiWordNetDemoCode sentiwordnet = new SentiWordNetDemoCode(pathToSWN);
        double total = 0.0;
        for(String wordData : s)
        {
            // fomat = "word_tag"
            try{
                String word = wordData;
                double value = sentiwordnet.extract(word, "n");
                value += sentiwordnet.extract(word, "a");
                //System.out.println(word+": "+value);
                total += value;
            }catch(Exception e)
            {
            }
        }
        System.out.println("total: " + total);
        return total;
    }
}