package com.stackroute;

import edu.stanford.nlp.simple.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetAllDocs {


    public List<String> listFilesForFolder(final File folder) {
        List<String> fileNames = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                fileNames.add(fileEntry.getName());
                //System.out.println(fileEntry.getName());
            }
        }
        return fileNames;
    }
    public Document getText(String fileName) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader("Files/" + fileName));
        try{

            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line!=null){
                sb.append(line);

                line = br.readLine();
            }
//            NGram nGram = new NGram(sb.toString(),2);
//            System.out.println(nGram.list());
            return new Document(sb.toString().replaceAll("[^a-zA-Z ]", "").toLowerCase());
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            br.close();
        }
        return new Document("Unable To Read");
    }

    public List<String> getnGrams(String fileName, int n) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader("Files/" + fileName));
        try{

            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line!=null){
                sb.append(line.replaceAll("[^\\w\\s\\ ]", "").toLowerCase());

                line = br.readLine();
            }
            NGram nGram = new NGram(sb.toString().toLowerCase(),n);
            //System.out.println(nGram.list());
            return nGram.list();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            br.close();
        }
        return Collections.emptyList();
    }

    public List<Document> readAllData(){
        final File folder = new File("Files");
        GetAllDocs getAllDocs = new GetAllDocs();
        List<String> fileNames = getAllDocs.listFilesForFolder(folder);
        List<Document> allTheData = new ArrayList<>();
        try {
            for (String file : fileNames) {
                allTheData.add(getAllDocs.getText(file));
            }
            return allTheData;
        }catch (IOException  e){
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public List<List<String>> gettingAllnGrams(int n){
        final File folder = new File("Files");
        GetAllDocs getAllDocs = new GetAllDocs();
        List<String> fileNames = getAllDocs.listFilesForFolder(folder);
        List<List<String>> allTheNGrams = new ArrayList<>();
        try {
            for(String file: fileNames){
                allTheNGrams.add(getAllDocs.getnGrams(file, n));
            }
            return allTheNGrams;
        }catch (IOException  e){
            e.printStackTrace();
        }
        return Collections.emptyList();
    }


}
