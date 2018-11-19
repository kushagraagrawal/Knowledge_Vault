package com.stackroute;

import com.stackroute.domain.JsonLDObject;
import com.stackroute.domain.OutputForDoc;

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

    private double tf(List<String> doc, String term){
        double result = 0;
        for(String word: doc){
            if(term.equalsIgnoreCase(word))
                result++;
        }

        return Math.log(1 + result);
    }

    private double idf(List<List<String>> docs, String term) {
        double n = 0;
        for (List<String> doc : docs) {
            for (String word : doc) {
                if (term.equalsIgnoreCase(word)) {
                    n++;
                    break;
                }
            }
        }
        return Math.log((docs.size()) / (n+1));
    }

    private List<String> tfIdf(int index, List<List<String>> docs) {

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
        List<List<String>> NGramList = getAllDocs.gettingAllnGrams(2);

        NGramTFIDF nGramTFIDF = new NGramTFIDF();
        List<OutputForDoc> relevantTerms = new ArrayList<>();

        for(int i=0;i<6;i++){
            List<String> terms = nGramTFIDF.tfIdf(i, NGramList);
            relevantTerms.add(new OutputForDoc(i,"new title", terms));
        }
        List<JsonLDObject> getJson = nGramTFIDF.convertTermsToJsonLD(relevantTerms);
        for(JsonLDObject json: getJson){
            System.out.println(json.getJsonld());
        }
    }
    private List<JsonLDObject> convertTermsToJsonLD(List<OutputForDoc> outputForDocs) {
        /*
            {
            "@context": "http://schema.org",
            "@type": "MedicalCondition",
            "alternateName": "angina pectoris",
            "associatedAnatomy": {
                "@type": "AnatomicalStructure",
                "name": "heart"
                }
            }
         */
        GetDiseasesAndSymptoms getDiseasesAndSymptoms = new GetDiseasesAndSymptoms();
        List<String> diseases = getDiseasesAndSymptoms.getDiseases();
        List<String> symptoms = getDiseasesAndSymptoms.getSymptoms();
        List<JsonLDObject> jsonLDObjects = new ArrayList<>();
        for(OutputForDoc documents: outputForDocs){
            Map<String, Object> root = new HashMap<>();
            root.put("@context","http://schema.org");
            root.put("@type", "MedicalCondition");
            for(String keyword: documents.getKeywords()){

                for(String disease: diseases){
                    if(disease.contains(keyword) || keyword.contains(disease)){
                        root.put("alternateName", disease);
                    }
                }
                Map<String, String> Anatomy = new HashMap<>();
                for(String bodypart: symptoms){
                    if(bodypart.contains(keyword) || keyword.contains(bodypart)){
                        Anatomy.put("@type", "AnatomicalStructure");
                        Anatomy.put("name", bodypart);

                    }
                }
                    root.put("associatedAnatomy", Anatomy);

            }
            jsonLDObjects.add(new JsonLDObject(documents.getId(), root));
        }
        return jsonLDObjects;
    }

}
