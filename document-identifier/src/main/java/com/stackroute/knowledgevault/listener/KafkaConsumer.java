package com.stackroute.knowledgevault.listener;

import com.stackroute.knowledgevault.domain.ExtractedFileData;
import com.stackroute.knowledgevault.services.DocumentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * Class for Kafka consumer. consumes data from Extractor service and saves it local database (MongoDB)
 */

@Service
//@Component
public class KafkaConsumer {

    private Logger logger;



    @Autowired
    private DocumentServiceImpl documentService;

    /*
        The function consumejson receives a list of documents (Class: ExtractedFIleData) from extractor service and saves to
        MongoDb
    */

    @KafkaListener(topics="document",groupId = "group_json", containerFactory= "documentKafkaListenerFactory")
    public void consumejson(ExtractedFileData extractedFileData){
        System.out.println("consumed message"+ extractedFileData.toString());
        documentService.saveDocuments(extractedFileData);
    }

}