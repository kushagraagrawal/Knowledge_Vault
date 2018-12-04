package com.stackroute.knowledgevault.WebScraping.controller;

import com.stackroute.knowledgevault.WebScraping.services.Scores;
import com.stackroute.knowledgevault.WebScraping.services.webScraping;
import com.stackroute.knowledgevault.domain.POJOClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * Controller class, to make GET request.
 */

@RestController
@RequestMapping("/api/v1/webscraper")
public class webScrapingController {

    @Qualifier("webScrapingImpl")
    private webScraping webScraper;

    private String url;
    private static final Logger LOGGER = LoggerFactory.getLogger(webScrapingController.class);

    @Autowired
    public webScrapingController(webScraping webScraper) {
        this.webScraper = webScraper;
    }

    @Autowired
    private KafkaTemplate<String, POJOClass> kafkaTemplate;

    private static final String KafkaTopic ="prod2";

    /*
        Function to produce the processed documents to the Kafka topic "prod2" and as a get request.
        An object of DocumentService is called to process the docs present in the local database.
    */

    @PostMapping()
    public ResponseEntity<?> gettingUrl(@RequestBody String url){
        this.url = url;
        LOGGER.info(url);
        return new ResponseEntity<>(url, HttpStatus.OK);
    }


    @GetMapping()
    public ResponseEntity<?> getAllTerms(){
        ResponseEntity responseEntity;
        webScraper.getURL(url);
        Map<String, Double> scoredTerms = webScraper.getScoredDocs();
        responseEntity = new ResponseEntity<>(scoredTerms, HttpStatus.OK);
        return responseEntity;
    }

}