package com.stackroute.domain;


import java.util.Map;

public class JsonLDObject {
    private int id;

    private Map<String, Object> jsonld;

    public JsonLDObject(int id, Map<String, Object> jsonld){
        this.id = id;
        this.jsonld = jsonld;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<String, Object> getJsonld() {
        return jsonld;
    }

    public void setJsonld(Map<String, Object> jsonld) {
        this.jsonld = jsonld;
    }
}
