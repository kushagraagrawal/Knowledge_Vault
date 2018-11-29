package com.stackroute.knowledgevault.WebScraping.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
@Primary
public class webScrapingImpl implements webScraping {

    private static final Logger LOGGER = LoggerFactory.getLogger(webScrapingImpl.class);

    @Override
    public String getTitle(String url) {
        try{
            Document document = Jsoup.connect(url).get();
            return document.title();
        }catch (IOException e){
            LOGGER.info(e.getMessage());
        }
        return null;
    }

    @Override
    public List<String> getAllParagraphs(String url) {
        try{
            Document document = Jsoup.connect(url).get();
            return document.getElementsByTag("p").eachText();
        }catch (IOException e){
            LOGGER.info(e.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public String getBody(String url) {
        try{
            Document document = Jsoup.connect(url).get();
            return document.getElementsByTag("body").text();
        }catch (IOException e){
            LOGGER.info(e.getMessage());
        }
        return null;
    }
}
