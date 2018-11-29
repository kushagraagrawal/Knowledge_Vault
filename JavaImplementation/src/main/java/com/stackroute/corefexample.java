package com.stackroute;

import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.ie.util.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.*;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.trees.*;
import java.util.*;

import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.traverse.*;

public class corefexample {
    public static String text = "Lung cancer, also known as lung carcinoma, is a malignant lung tumor characterized by" +
            "uncontrolled cell growth in tissues of the lung. This growth can spread beyond the lung by the process" +
            " of metastasis into nearby tissue or other parts of the body. Most cancers that start in the lung," +
            " known as primary lung cancers, are carcinomas. The two main types are small-cell lung carcinoma (SCLC)" +
            " and non-small-cell lung carcinoma (NSCLC). The most common symptoms are coughing" +
            " (including coughing up blood), weight loss, shortness of breath, and chest pains.\n";
    public static void main(String[] args) throws Exception {
//        Properties props = new Properties();
//        // set the list of annotators to run
//        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,parse");
//        // set a property for an annotator, in this case the coref annotator is being set to use the neural algorithm
//        props.setProperty("coref.algorithm", "neural");
//        // build pipeline
//        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
//        // create a document object
//        CoreDocument document = new CoreDocument(text);
//        // annnotate the document
//        pipeline.annotate(document);
//        // examples
//
//        // 10th token of the document
//        CoreLabel token = document.tokens().get(10);
//        System.out.println("Example: token");
//        System.out.println(token);
//        System.out.println();
//
//        // text of the first sentence
//        String sentenceText = document.sentences().get(0).text();
//        System.out.println("Example: sentence");
//        System.out.println(sentenceText);
//        System.out.println();
//
//        // second sentence
//        CoreSentence sentence = document.sentences().get(0);
//
//
//        // list of the part-of-speech tags for the second sentence
//        List<String> posTags = sentence.posTags();
//        System.out.println("Example: pos tags");
//        System.out.println(posTags);
//        System.out.println();
//
//        // list of the ner tags for the second sentence
////        List<String> nerTags = sentence.nerTags();
////        System.out.println("Example: ner tags");
////        System.out.println(nerTags);
////        System.out.println();
//        Tree constituencyParse = sentence.constituencyParse();
//        System.out.println("Example: constituency parse");
//        System.out.println(constituencyParse);
//        System.out.println();
//        GetAllDocs getAllDocs = new GetAllDocs();
//        List<Document> something = getAllDocs.readAllData();
        //Document document = new Document(text.replaceAll("[^a-zA-Z ]", "").toLowerCase());
 //       System.out.println(something.get(1).sentences());
        //System.out.println(document.sentences().get(1).coref());

        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        props.put("dcoref.score", true);
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        //text.replaceAll("[^a-zA-Z ]", "").toLowerCase();

        CoreDocument document = new CoreDocument(text);
        pipeline.annotate(document);

        Map<Integer, CorefChain> corefChains = document.corefChains();
        System.out.println("Example: coref chains for document");
        System.out.println(corefChains);
        System.out.println();

        props.clear();
        props.put("annotators", "tokenize, ssplit, pos, lemma, dpparse");
        CoreSentence sentence1 = document.sentences().get(0);
        SemanticGraph dependencyParser = sentence1.dependencyParse();
        for(int i=0;i<document.sentences().size();i++){
            CoreSentence sentence = document.sentences().get(i);
            SemanticGraph dependencyParse = sentence.dependencyParse();
            System.out.println("Example: dependency parse");
            System.out.println(dependencyParse);
            System.out.println();
        }



        //dependencyParser.descendants((IndexedWord)root);


    }
}

