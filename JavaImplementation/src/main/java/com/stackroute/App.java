package com.stackroute;

import edu.stanford.nlp.simple.*;

import java.util.*;



public class App 
{

    private static List<String> diseases = new ArrayList<>();
    private static List<String> symptoms = new ArrayList<>();

    private static HashMap sortByValues(HashMap map) {
        List list = new LinkedList(map.entrySet());
        // Defined Custom Comparator here
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o2)).getValue())
                        .compareTo(((Map.Entry) (o1)).getValue());
            }
        });

        // Here I am copying the sorted list in HashMap
        // using LinkedHashMap to preserve the insertion order
        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

    public double tf(List<String> doc, String term){
        double result = 0;
        for(String word: doc){
            if(term.equalsIgnoreCase(word))
                result++;
        }
        return result / doc.size();
    }

    public double idf(List<List<String>> docs, String term) {
        double n = 0;
        for (List<String> doc : docs) {
            for (String word : doc) {
                if (term.equalsIgnoreCase(word)) {
                    n++;
                    break;
                }
            }
        }
        return Math.log(docs.size() / n);
    }

    public List<String> tfIdf(int index, List<List<String>> docs) {

        LinkedHashMap<String, Double> weightMap = new LinkedHashMap<>();
        List<String> relevantWords = new ArrayList<>();
        int numberOfWords = 20;

        //System.out.println(docs.get(index));
        for(String words: docs.get(index)){
            double relevance = tf(docs.get(index), words) * idf(docs, words);
            weightMap.put(words, relevance);


        }
        //System.out.println(weightMap);

        Map<String, Double> sortedMap = sortByValues(weightMap);
        //System.out.println(sortedMap);




        Iterator<Map.Entry<String, Double>> itr = sortedMap.entrySet().iterator();
        int i=0;
        while(itr.hasNext() && i<=numberOfWords){
            relevantWords.add(itr.next().getKey());
            i++;
        }
        //System.out.println(relevantWords);



        return relevantWords;

    }

    public static void main( String[] args )
    {
        GetAllDocs getAllDocs = new GetAllDocs();
        List<Document> allTheText = getAllDocs.readAllData();

        List<List<String>> docs = new ArrayList<>();
        StopWordRemoval stopWordRemoval = new StopWordRemoval();
        App app = new App();

        for(Document document: allTheText){
            List<String> terms = new ArrayList<>();
            for(Sentence sentence: document.sentences()){
                for(int i=0;i<sentence.length();i++){
                    terms.add(sentence.lemma(i).toLowerCase());
//                    Optional<int> dep = sentence.governor(i);
                    //System.out.println(sentence.coref());//sentence.coref();

                }
            }
            terms = stopWordRemoval.removeStopwords(terms);
            docs.add(terms);
        }




        long startTime = System.currentTimeMillis();
        for (int i=0;i<5;i++){
            List<String> relevantTerms = app.tfIdf(i, docs);
            System.out.println(relevantTerms);
        }
        long stopTime = System.currentTimeMillis();
        System.out.println(stopTime - startTime);








    }
}
