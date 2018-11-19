package com.stackroute.domain;


import java.util.List;

/**
 * POJO class to produce output for Populator service.
 */
public class OutputForDoc {

    public OutputForDoc(int id, String docTitle, List<String> keywords){
        this.id = id;
        this.docTitle = docTitle;
        this.keywords = keywords;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDocTitle() {
        return docTitle;
    }

    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    private int id;

    private String docTitle;

    private List<String> keywords;
}
