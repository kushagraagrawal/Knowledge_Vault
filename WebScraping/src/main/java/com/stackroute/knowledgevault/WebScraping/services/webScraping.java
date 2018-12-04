package com.stackroute.knowledgevault.WebScraping.services;

import java.util.List;

public interface webScraping {
    public String getTitle(String url);
    public List<String> getAllParagraphs();
    public String getBody();
    public String geth1Score();
    public double tf(List<String> doc, String term);
    public void getMap();
    public void readJSON();

}
