package com.stackroute;

import edu.stanford.nlp.simple.*;
import java.util.Properties;

public class corefexample {
    public static void main(String[] args) throws Exception {
        Document document = new Document("Barack obama is the pres. He is best. he was elected in 2008");
        System.out.println(document.coref());
    }
}
