package com.stackroute;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class StopWordRemoval {




    public static List<String> readStopWords(String stopWordsFilename)
    {
        List<String> stopWords = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(stopWordsFilename));
            String line = br.readLine();

            while (line != null) {
                stopWords.add(line);
                line = br.readLine();
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }


        return stopWords;
    }

    public List<String> removeStopwords(List<String> terms){
        List<String> resultingdoc = new ArrayList<>();

        List<String> stopwords = readStopWords("stopwords.txt");

        for(String term: terms){
            boolean flag = true;
            for(String stopword: stopwords){
                if(term.equalsIgnoreCase(stopword)){
                    flag = false;
                    break;
                }
            }
            if(flag == true)
                resultingdoc.add(term);
        }



        return resultingdoc;
    }

}
