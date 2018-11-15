package com.stackroute;

import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;
import com.github.jsonldjava.utils.Obj;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadandWriteToJSON {

    public static void main(String[] args){
        try {// Open a valid json(-ld) input file
            //InputStream inputStream = new FileInputStream("jsonldschema.json");
            //Object jsonObject = JsonUtils.fromInputStream(inputStream);
            Writer outputStream = new BufferedWriter(new FileWriter("output.json"));

            Map context = new HashMap();


            //JsonLdOptions options = new JsonLdOptions();


            //Object compact = JsonLdProcessor.compact(jsonObject, context, options);
            GetDiseasesAndSymptoms diseasesAndSymptoms = new GetDiseasesAndSymptoms();
            String[] terms = {"cancer", "breast", "cell", "tumor", "survival", "prostate", "radiation", "skin","meat", "dna", "vitamin", "lung", "surgery", "metastasis", "hormone", "error", "cure", "chemotherapy", "palliative", "cervical", "original"};
            List<String> diseases = diseasesAndSymptoms.getDiseases();
            List<String> symptoms = diseasesAndSymptoms.getSymptoms();

            for(String term:terms){
                for(String disease: diseases){
                    if(disease.contains(term)){
                        System.out.println("disease " + term);
                    }
                }
                for(String symptom: symptoms){
                    if(symptom.contains(term)){
                        System.out.println("symptom " + term);
                    }
                }
            }


            Map<String, Object> root = new HashMap<>();
            root.put("@context","http://schema.org");
            root.put("@type", "MedicalCondition");

            Map<String, String> code = new HashMap<>();
            code.put("@type", "MedicalCode");
            code.put("code","413");
            code.put("codingSystem","ICD-9");

            root.put("code", code);

            Map<String,String> anatomy = new HashMap<>();
            anatomy.put("@type", "sfa");
            anatomy.put("name", "safa");
            root.put("associatedAnatomy", anatomy);
            Object outputObj = root;

            List<Map<String, Object>> possibleTreatment = new ArrayList<>();
            Map<String, Object> tempMap = new HashMap<>();
            tempMap.put("@type","Drug");
            tempMap.put("name","Cavlam-650");
            List<Map<String, String>> doseSchedule = new ArrayList<>();
            Map<String, String> dosage = new HashMap<>();
            dosage.put("doseUnit","500mg");
            dosage.put("frequency","twice daily");
            doseSchedule.add(dosage);
            tempMap.put("DoseSchedule", doseSchedule);
            possibleTreatment.add(tempMap);

            tempMap.clear();
            tempMap.put("@type","Drug");
            tempMap.put("name","Paracetamol");
            List<Map<String, String>> doseSchedule1 = new ArrayList<>();
            Map<String, String> dosage1 = new HashMap<>();
            dosage1.put("doseUnit","500mg");
            dosage1.put("frequency","twice daily");
            doseSchedule1.add(dosage1);
            tempMap.put("DoseSchedule", doseSchedule1);
            possibleTreatment.add(tempMap);

            root.put("possibleTreatment", possibleTreatment);






            JsonUtils.writePrettyPrint(outputStream, outputObj);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
