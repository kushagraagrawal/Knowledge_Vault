package com.stackroute;

import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.ie.util.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.*;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.simple.*;
import java.util.*;


public class DepParseDemo {

    public static String text = "Nadal has won 17 Grand Slam singles titles, the second most in history for a male player, as well as a record 33 ATP World Tour Masters 1000 titles, 20 ATP World Tour 500 tournaments, and the 2008 Olympic gold medal in singles. In addition, Nadal has held the number one ranking for a total of 196 weeks.[4] In majors, Nadal has won a record 11 French Open titles, three US Open titles, two Wimbledon titles, and one Australian Open title. Nadal has also won 57 clay court titles, including his 11 French Open titles, the most of any player in the Open Era. With 81 consecutive match wins on clay, Nadal has the longest single surface win streak of any player in the Open Era.\n" +
            "\n" +
            "He was also a member of the winning Spain Davis Cup team in 2004, 2008, 2009, and 2011. In 2010, he became the seventh male player in history and youngest of five in the Open Era to achieve the Career Grand Slam at age 24. He is the second male player, after Andre Agassi, to complete the singles Career Golden Slam. In 2011, Nadal was named the Laureus World Sportsman of the Year.";
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        props.put("dcoref.score", true);
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        CoreDocument document = new CoreDocument(text);
        // annnotate the document
        pipeline.annotate(document);
        CoreEntityMention originalEntityMention = document.sentences().get(3).entityMentions().get(1);
        System.out.println("Example: original entity mention");
        System.out.println(originalEntityMention);
        System.out.println("Example: canonical entity mention");
        System.out.println(originalEntityMention.canonicalEntityMention().get());
        System.out.println();
        Map<Integer, CorefChain> corefChains = document.corefChains();
        System.out.println("Example: coref chains for document");
        System.out.println(corefChains);
        System.out.println();

        props.clear();
        props.put("annotators", "tokenize, ssplit, pos, lemma, dpparse");
        CoreSentence sentence = document.sentences().get(1);
        SemanticGraph dependencyParse = sentence.dependencyParse();
        System.out.println("Example: dependency parse");
        System.out.println(dependencyParse);
        System.out.println();
    }
}
