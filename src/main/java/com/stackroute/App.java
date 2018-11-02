package com.stackroute;

import edu.stanford.nlp.simple.*;

import java.util.*;



public class App 
{
    private static List<List<String>> uniqueTerms = new ArrayList<>();
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

    public List<String> tfIdf(List<String> doc, List<List<String>> docs) {

        LinkedHashMap<String, Double> weightMap = new LinkedHashMap<>();
        List<String> relevantWords = new ArrayList<>();
        int numberOfWords = 20;


        for(String words: uniqueTerms.get(0)){
            double relevance = tf(doc, words) * idf(docs, words);
            weightMap.put(words, relevance);


        }
        System.out.println(weightMap);

        Map<String, Double> sortedMap = sortByValues(weightMap);
        //System.out.println(sortedMap);




        Iterator<Map.Entry<String, Double>> itr = sortedMap.entrySet().iterator();
        int i=0;
        while(itr.hasNext() && i<=numberOfWords){
            relevantWords.add(itr.next().getKey());
            i++;
        }



        return relevantWords;

    }

    public static void main( String[] args )
    {
        GetAllDocs getAllDocs = new GetAllDocs();
        List<Document> allTheText = getAllDocs.readAllData();
        List<String> terms = new ArrayList<>();
        List<List<String>> docs = new ArrayList<>();
        StopWordRemoval stopWordRemoval = new StopWordRemoval();
        App app = new App();

        for(Document document: allTheText){
            for(Sentence sentence: document.sentences()){
                for(int i=0;i<sentence.length();i++){
                    terms.add(sentence.lemma(i));
                }
            }
            terms = stopWordRemoval.removeStopwords(terms);
            docs.add(terms);
            uniqueTerms.add(terms);
            terms.clear();
        }


        List<String> relevantTerms = app.tfIdf(docs.get(0), docs);
        System.out.println(relevantTerms);

        GetDiseasesAndSymptoms diseasesAndSymptoms = new GetDiseasesAndSymptoms();
        //System.out.println(diseasesAndSymptoms.getDiseases());
        //System.out.println(diseasesAndSymptoms.getSymptoms());

        List<String> diseases = diseasesAndSymptoms.getDiseases();
        List<String> symptoms = diseasesAndSymptoms.getSymptoms();

        boolean diseaseContains = false;
        boolean symptomContains = false;

        for(String word: relevantTerms){
            for(String disease: diseases){
                if(disease.contains(word)){
//                    System.out.println(disease);
//                    System.out.println("*******disease*******");
                    diseaseContains = true;
                }
            }
            for(String symptom: symptoms){
                if(symptom.contains(word)){
//                    System.out.println(symptom);
//                    System.out.println("*********symptom********");
                    symptomContains = true;
                }
            }
        }
        if(diseaseContains || symptomContains){
//            System.out.println(diseaseContains);
//            System.out.println(symptomContains);
            System.out.println("relevant");
        }else{
            System.out.println("irrelevant");
        }
    }
}
