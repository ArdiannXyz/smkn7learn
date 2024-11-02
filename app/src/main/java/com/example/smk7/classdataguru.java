package com.example.smk7;

public class classdataguru {

    private String dataNip;
    private String datanama;
    private String key;
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getDataNip() {
        return dataNip;
    }
    public String getDatanama() {
        return datanama;
    }
    public classdataguru(String dataNip, String datanama) {
        this.dataNip = dataNip;
        this.datanama = datanama;

    }
    public classdataguru(){
    }
}
