package com.stackroute.knowledgevault.WebScraping.services;

import java.util.List;

public interface webScraping {
    public String getTitle(String url);
    public List<String> getAllParagraphs(String url);
    public String getBody(String url);
}
