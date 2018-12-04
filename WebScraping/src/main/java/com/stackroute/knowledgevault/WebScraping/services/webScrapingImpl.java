package com.stackroute.knowledgevault.WebScraping.services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
@Primary
public class webScrapingImpl implements webScraping {

    private static final Logger LOGGER = LoggerFactory.getLogger(webScrapingImpl.class);
    private HashMap<String, Double> TermWeights = new HashMap<>();
    private NGram nGram;
    private Map<String, Long> tagWeights = new HashMap<>();
    private Document document;

    /*
    Function to calculate term frequency(tf) score of a term in a document. Takes all the terms in a doc and term to be calculated.
    Returns normalized tf score, i.e. dividing the term frequency with the total number of terms to keep the score < 1.
     */

    @Override
    public double tf(List<String> doc, String term){
        double result = 0;
        for(String word: doc){
            if(term.equalsIgnoreCase(word))
                result++;
        }

        return Math.log(1 + result);
    }

    /*
    Helper function to sort the terms by their tfidf value.
     */

    private static HashMap sortByValues(HashMap map) {

        List list = new LinkedList(map.entrySet());
        // Defined Custom Comparator here
        list.sort((Object o1, Object o2)->(((Comparable)(((Map.Entry)o2).getValue())).compareTo((((Map.Entry)o1).getValue()))));

        // Here I am copying the sorted list in HashMap
        // using LinkedHashMap to preserve the insertion order
        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }


    @Override
    public void getMap() {
        TermWeights = sortByValues(TermWeights);
        LOGGER.info(TermWeights.toString());
    }

    @Override
    public void readJSON() {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("scoringscheme.json")){
            Object obj = jsonParser.parse(reader);
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(obj);
            LOGGER.info(jsonArray.toString());
            jsonArray.forEach( score -> parseScore( (JSONObject) score ) );


        }catch (FileNotFoundException e){
            LOGGER.info(e.getMessage());
        }catch (IOException e){
            LOGGER.info(e.getMessage());
        }catch (ParseException e){
            LOGGER.info(e.getMessage());
        }
    }

    private void parseScore(JSONObject score)
    {
        //Get employee object within list
        String tagName = (String) score.get("title");
        Long value = (Long) score.get("child.weight");
        tagWeights.put(tagName, value);

        JSONArray childObject = (JSONArray) score.get("child");

        for(int i=0;i<childObject.size();i++){
            JSONObject jsonObject = (JSONObject) childObject.get(i);
            String tag = (String) jsonObject.get("title");
            Long values = (Long) jsonObject.get("child.weight");
            tagWeights.put(tag, values);

            JSONArray innerscores = (JSONArray) jsonObject.get("child");

            for(int j=0;j<innerscores.size();j++){
                JSONObject jsonObject1 = (JSONObject) innerscores.get(j);
                String tags = (String) jsonObject1.get("title");
                Long value1 = (Long) jsonObject1.get("child.weight");
                tagWeights.put(tags, value1);
           }
        }
    }

    /**
     * Make generic for head, title and meta tags
     * @param url
     * @return
     */


    @Override
    public String getTitle(String url) {
        try{
            document = Jsoup.connect(url).get();
            String title = document.title().toLowerCase();

            TermWeights.clear();
            nGram = new NGram(title, 2);
            List<String> ngrams = nGram.list();
            for(String terms:ngrams){
                double score = tf(ngrams, terms);
                if(TermWeights.get(terms) != null){
                    double tempscore = TermWeights.get(terms) + tagWeights.get("title")*score;
                    TermWeights.put(terms, tempscore);
                }else{
                    TermWeights.put(terms, tagWeights.get("title")*score);
                }
            }
            return document.title();
        }catch (IOException e){
            LOGGER.info(e.getMessage());
        }
        return null;
    }


    @Override
    public List<String> getAllParagraphs() {
        try{

            List<String> paras = document.getElementsByTag("p").eachText();
            for(String paragraph: paras) {
                paragraph = paragraph.toLowerCase();;
                nGram = new NGram(paragraph, 2);
                List<String> ngrams = nGram.list();
                for (String terms : ngrams) {
                    double score = tf(ngrams, terms);
                    if (TermWeights.get(terms) != null) {
                        double tempscore = TermWeights.get(terms) + tagWeights.get("p") * score;
                        TermWeights.put(terms, tempscore);
                    } else {
                        TermWeights.put(terms, tagWeights.get("p") * score);
                    }
                }
            }
            return document.getElementsByTag("p").eachText();
        }catch (Exception e){
            LOGGER.info(e.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public String getBody() {
        try{

            String body = document.getElementsByTag("body").text().toLowerCase();
            nGram = new NGram(body, 2);
            List<String> ngrams = nGram.list();
            for (String terms : ngrams) {
                double score = tf(ngrams, terms);
                if (TermWeights.get(terms) != null) {
                    double tempscore = TermWeights.get(terms) + tagWeights.get("body") * score;
                    TermWeights.put(terms, tempscore);
                } else {
                    TermWeights.put(terms, tagWeights.get("body") * score);
                }
            }
            return document.getElementsByTag("body").text();
        }catch (Exception e){
            LOGGER.info(e.getMessage());
        }
        return null;
    }

    @Override
    public String geth1Score(){
        try{
            List<String> h1docs = document.getElementsByTag("h1").eachText();
            for(String h1: h1docs){
                nGram = new NGram(h1,2);
                List<String> ngrams = nGram.list();
                for(String terms: ngrams){
                    double score = tf(ngrams, terms);
                    if (TermWeights.get(terms) != null) {
                        double tempscore = TermWeights.get(terms) + tagWeights.get("h1") * score;
                        TermWeights.put(terms, tempscore);
                    } else {
                        TermWeights.put(terms, tagWeights.get("h1") * score);
                    }
                }
            }
        }catch (Exception e){
            LOGGER.info(e.getMessage());
        }
        return null;
    }
}
