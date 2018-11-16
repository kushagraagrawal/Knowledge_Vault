package com.stackroute;

import java.util.*;


public class NGramTFIDF {

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
        return Math.log((docs.size() + 1) / (n+1));
    }

    public List<String> tfIdf(int index, List<List<String>> docs) {

        LinkedHashMap<String, Double> weightMap = new LinkedHashMap<>();
        List<String> relevantWords = new ArrayList<>();
        int numberOfWords = 20;

        double denominator = 0.0;

        //System.out.println(docs.get(index));
        for(String words: docs.get(index)){
            double relevance = tf(docs.get(index), words) * idf(docs, words);
            denominator += (relevance * relevance);
            weightMap.put(words, relevance);


        }
        denominator = Math.sqrt(denominator);
        //System.out.println(weightMap);
        Iterator it = weightMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            weightMap.put((String)pair.getKey(),((double)pair.getValue())/denominator);
        }
        Map<String, Double> sortedMap = sortByValues(weightMap);

        System.out.println(sortedMap);


        Iterator<Map.Entry<String, Double>> itr = sortedMap.entrySet().iterator();
        int i=0;
        while(itr.hasNext() && i<=numberOfWords){
            String term = itr.next().getKey().trim();
            if(!term.isEmpty()) {
                relevantWords.add(term);
                i++;
            }
        }



        return relevantWords;

    }

    public static void main(String[] args){
        GetAllDocs getAllDocs = new GetAllDocs();
        List<List<String>> NGramList = getAllDocs.gettingAllnGrams();

        NGramTFIDF nGramTFIDF = new NGramTFIDF();

        List<String> terms = nGramTFIDF.tfIdf(1, NGramList);
        System.out.println(terms);

        GetDiseasesAndSymptoms diseasesAndSymptoms = new GetDiseasesAndSymptoms();
        List<String> diseases = diseasesAndSymptoms.getDiseases();
        List<String> symptoms = diseasesAndSymptoms.getSymptoms();

        for (String term: terms){
            for(String symptom: diseases){
                if(symptom.contains(term)){
                    System.out.println(term);
                }
            }

        }
    }
}
