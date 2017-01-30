package general.graph.theory;

import general.chat.MainGUI;
import general.chat.UrlFileConnector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by corpi on 2016-10-23.
 */
public class Scan {
    public void scanit(TreeMap<Integer, HashSet<HashSet<String>>> closestMatchedQueries,  TreeMap<Integer, HashSet<ParagraphInfo>> totalFinalfinalSetOfWordsTree
    , String[] listOfFiles, HashMap<KeyWordPattern, HashSet<KeyWordPattern>> keyword2SynonymMap
    , HashSet<KeyWordPattern> newKeyWordsFullNouns)
    {
        Pattern ignorePagesWithThis = KeyWordPattern.superMatcher(keyword2SynonymMap);
        System.setOut(MainGUI.originalStream);
        System.out.println(ignorePagesWithThis.toString());
        ;
        for (String result : listOfFiles) {


            try {
                //newContentPane.progressBar.setValue((int) (100.0 * (((double) index++) / listOfFiles.length)));
                if ( true) {

                    int charsize = 0;


                    String biggestText = "";
                    //String[] sentences = biggestText.split("[\\.\\?!\\n]+");
                    boolean isRelevant = false;
                    /*for(Map.Entry<KeyWordPattern, HashSet<KeyWordPattern>> subKeyWord :keyword2SynonymMap.entrySet())
                    {
                        if(isRelevant)
                            break;
                        for(KeyWordPattern keyWord : subKeyWord.getValue()) {
                            if (biggestText.toLowerCase().toLowerCase().contains(keyWord.getKeyWords()[0])
                                    ||biggestText.toLowerCase().toLowerCase().contains(keyWord.getKeyWords()[1])


                                    ) {

                                isRelevant = true;
                                break;
                            }
                        }
                    }
                    */
                    if(!ignorePagesWithThis.matcher(biggestText).find())
                        continue;
                    HashSet<ParagraphInfo> sentsNouns = new HashSet<>();
                    HashSet<ParagraphInfo> sentsVerbsOrAdjFinal = new HashSet<>();


                    String publicationName = "";
                    int parNum = 0;
                    // split by pages first
                    int pageNum = 0;

                    for (String page : biggestText.split("========\\d+========|<-----page \\d+----->"))//
                    {
                        ++pageNum;
                        // then split by sentence
                        /*isRelevant = false;
                        for(Map.Entry<KeyWordPattern, HashSet<KeyWordPattern>> subKeyWord :keyword2SynonymMap.entrySet())
                        {
                            if(isRelevant)
                                break;
                            for(KeyWordPattern keyWord : subKeyWord.getValue()) {
                                if (page.toLowerCase().toLowerCase().contains(keyWord.getKeyWords()[0])
                                        ||page.toLowerCase().toLowerCase().contains(keyWord.getKeyWords()[1])


                                        ) {

                                    isRelevant = true;
                                    break;
                                }
                            }
                        }
                        */
                        if(!ignorePagesWithThis.matcher(page).find()) {
                            continue;
                        }
                        parNum = 0;
                        for (String y : page.split("[\\.\\?!]+"))
                        {

                        /*
                            */
                            parNum++;
                            /*isRelevant = false;
                            for(Map.Entry<KeyWordPattern, HashSet<KeyWordPattern>> subKeyWord :keyword2SynonymMap.entrySet())
                            {
                                if(isRelevant)
                                    break;
                                for(KeyWordPattern keyWord : subKeyWord.getValue()) {
                                    if (
                                            y.toLowerCase().toLowerCase().contains(keyWord.getKeyWords()[0])
                                                    || y.toLowerCase().toLowerCase().contains(keyWord.getKeyWords()[1])


                                            ) {

                                        isRelevant = true;
                                        break;
                                    }
                                }
                            }
                            if(!isRelevant)
                                continue;
                                */
                            HashSet<String> usedKeywords = new HashSet<>();
                            int keyWordCount = 0;
                            HashSet<String> usedKeywords_2 = new HashSet<>();
                            int keyWordCount_2 = 0;
                            int exactKeyWordCount = 0;
                            System.setOut(MainGUI.originalStream);

                            for (KeyWordPattern keyWord : newKeyWordsFullNouns) {
                                Pattern wb0 = keyWord.getPattern1();
                                Pattern wb1 = keyWord.getPattern2();

                                Pattern wb3 = keyWord.getPattern3();
                                if (wb0.matcher(y.toLowerCase()).find()||
                                        wb1.matcher(y.toLowerCase()).find()

                                        /*||

                                        y.toLowerCase().contains(keyWord[0].toLowerCase())
                                                ||
                                                y.toLowerCase().contains(keyWord[1].toLowerCase())
                                        */
                                        ) {

                                    keyWordCount++;
                                    usedKeywords.add(keyWord.getKeyWords()[1]);
                                    exactKeyWordCount += 1;
                                    if(wb3.matcher(y.toLowerCase()).find())
                                        exactKeyWordCount += 1;
                                }
                            }
                            System.setOut(MainGUI.originalStream);
                            System.out.println("23fj8aw98j3a"+new general.graph.theory.WikipediaInfoBoxModel2OldJune14_PERSONAL_CB().DELIMITER+"23fj8aw98j3a");
                            Matcher match = ignorePagesWithThis.matcher(y.toLowerCase());
                            if(match.find())
                            {

                                keyWordCount_2++;
                                usedKeywords_2.add(match.group());
                            }
                            /*
                            for(Map.Entry<KeyWordPattern, HashSet<KeyWordPattern>> subKeyWord :keyword2SynonymMap.entrySet())
                            {

                                Pattern wba = subKeyWord.getKey().getPattern1();
                                Pattern wbb = subKeyWord.getKey().getPattern2();
                                if (wba.matcher(y.toLowerCase()).find()||
                                        wbb.matcher(y.toLowerCase()).find()

                                        ) {

                                    keyWordCount_2++;
                                    usedKeywords_2.add(subKeyWord.getKey().getKeyWords()[1]);
                                    continue;
                                }
                                for(KeyWordPattern keyWord : subKeyWord.getValue()) {
                                    Pattern wb0 = keyWord.getPattern1();
                                    Pattern wb1 = keyWord.getPattern2();
                                    if (wb0.matcher(y.toLowerCase()).find()||
                                            wb1.matcher(y.toLowerCase()).find()

                                            ) {

                                        keyWordCount_2++;
                                        usedKeywords_2.add(keyWord.getKeyWords()[1]);
                                        break;
                                    }
                                }

                            }
                            */

                            //keyWordCount = 0;
                            System.setOut(new general.graph.theory.WikipediaInfoBoxModel2OldJune14_PERSONAL_CB().dummyStream);
                            ParagraphInfo info = new ParagraphInfo(y, publicationName, String.valueOf(pageNum));
                            //newKeyWordsFullNouns.size()
                            System.setOut(MainGUI.originalStream);
                            //System.out.println("f32s8:"+usedKeywords);
                            //System.out.println("f32s9:"+newKeyWordsFullNouns);
                            if (usedKeywords.size() > new  general.graph.theory.WikipediaInfoBoxModel2OldJune14_PERSONAL_CB().clamp(newKeyWordsFullNouns.size() - 1, 0)) {
                                keyWordCount = keyWordCount + exactKeyWordCount;
                                System.setOut(MainGUI.originalStream);
                                if (totalFinalfinalSetOfWordsTree.containsKey(keyWordCount)) {
                                    HashSet<ParagraphInfo> dummy = totalFinalfinalSetOfWordsTree.get(keyWordCount);
                                    dummy.add(info);
                                    totalFinalfinalSetOfWordsTree.put(keyWordCount, dummy);

                                } else {
                                    HashSet<ParagraphInfo> dummy = new HashSet<>();
                                    dummy.add(info);
                                    totalFinalfinalSetOfWordsTree.put(keyWordCount, dummy);

                                }
                                //totalFinalfinalSetOfWords.add(info);
                                System.out.println("298gj2f keyword count " + info.getText());
                                System.out.println("298gj2f size " + newKeyWordsFullNouns.size());
                                System.out.println("298gj2f" + info.getText());
                                System.setOut(new general.graph.theory.WikipediaInfoBoxModel2OldJune14_PERSONAL_CB().dummyStream);
                            } else {
                                if (closestMatchedQueries.size() > 0) {
                                    if (keyWordCount >closestMatchedQueries.firstKey()) {
                                        HashSet<HashSet<String>> currentQueries = new HashSet<>();
                                        currentQueries.add(usedKeywords);
                                        closestMatchedQueries.put(keyWordCount, currentQueries);
                                    } else if (keyWordCount == closestMatchedQueries.firstKey()) {
                                        HashSet<HashSet<String>> currentQueries = closestMatchedQueries.get(keyWordCount);
                                        currentQueries.add(usedKeywords);
                                        closestMatchedQueries.put(keyWordCount, currentQueries);
                                    }
                                } else {
                                    HashSet<HashSet<String>> currentQueries = new HashSet<>();
                                    currentQueries.add(usedKeywords);
                                    closestMatchedQueries.put(keyWordCount, currentQueries);
                                }
                                if ( closestMatchedQueries.size() > 0) {
                                    if (keyWordCount_2 >  closestMatchedQueries.firstKey()) {
                                        HashSet<HashSet<String>> currentQueries = new HashSet<>();
                                        currentQueries.add(usedKeywords_2);
                                       closestMatchedQueries.put(keyWordCount_2, currentQueries);
                                    } else if (keyWordCount_2 ==  closestMatchedQueries.firstKey()) {
                                        HashSet<HashSet<String>> currentQueries =  closestMatchedQueries.get(keyWordCount_2);
                                        currentQueries.add(usedKeywords_2);
                                        closestMatchedQueries.put(keyWordCount_2, currentQueries);
                                    }
                                } else {
                                    HashSet<HashSet<String>> currentQueries = new HashSet<>();
                                    currentQueries.add(usedKeywords_2);
                                    closestMatchedQueries.put(keyWordCount_2, currentQueries);
                                }
                            }


                        /*if (y.length() < 400)
                            if (totalFinalfinalSetOfWordsTree.containsKey(keyWordCount)) {
                                HashSet<ParagraphInfo> dummy = totalFinalfinalSetOfWordsTree.get(keyWordCount);
                                dummy.add(info);
                                totalFinalfinalSetOfWordsTree.put(keyWordCount, dummy);

                            } else {
                                HashSet<ParagraphInfo> dummy = new HashSet<>();
                                dummy.add(info);
                                totalFinalfinalSetOfWordsTree.put(keyWordCount, dummy);

                            }
                            */

                        }

                        System.out.println(sentsNouns.size());

                        //totalFinalfinalSetOfWordsTree.addAll(sentsNouns);
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }
    }
}
