package com.example.luongtiendat.htw_projekt.Model;

/**
 * Created by Luong Tien Dat on 30.12.2017.
 */

public class User {

    private String name;
    private String phone;
    private String email;
    private String bewertung_count;
    private String auftrag_count;
    private String image;
    private String thumb_image;

    public User() {
    }

    public User(String name, String phone, String email, String bewertung_count, String auftrag_count, String image, String thumb_image) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.bewertung_count = bewertung_count;
        this.auftrag_count = auftrag_count;
        this.image = image;
        this.thumb_image = thumb_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBewertung_count() {
        return bewertung_count;
    }

    public void setBewertung_count(String bewertung_count) {
        this.bewertung_count = bewertung_count;
    }

    public String getAuftrag_count() {
        return auftrag_count;
    }

    public void setAuftrag_count(String auftrag_count) {
        this.auftrag_count = auftrag_count;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }
}

