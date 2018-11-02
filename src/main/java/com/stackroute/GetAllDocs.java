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
            return new Document(sb.toString());
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            br.close();
        }
        return new Document("Unable To Read");
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


}
