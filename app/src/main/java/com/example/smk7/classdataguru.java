package com.example.smk7;

public class classdataguru {

    private String role;
    private String uid;
    private String email;
    private String dataNip;
    private String datanama;
    private String key;
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getDataRole() {
        return role;
    }
    public String getDataUid() {
        return uid;
    }
    public String getDataNip() {
        return dataNip;
    }
    public String getDataEmail() {
        return email;
    }
    public String getDatanama() {
        return datanama;
    }
    public classdataguru(String dataNip, String datanama, String email ,String uid, String role) {
        this.dataNip = dataNip;
        this.datanama = datanama;
        this.email = email;
        this.uid = uid;
        this.role = role;

    }
    public classdataguru(){
    }
}
