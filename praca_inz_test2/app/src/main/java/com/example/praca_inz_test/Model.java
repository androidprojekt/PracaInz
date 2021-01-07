package com.example.praca_inz_test;

//----------------------class defining model in the exhibition tab----------------------------------
public class Model {
    private int image;
    private String title;
    private String desc;

    public Model(int image, String title, String desc) {
        this.image = image;
        this.title = title;
        this.desc = desc;
    }

    public int getImage() {
        return image;
    }


    public String getTitle() {
        return title;
    }


    public String getDesc() {
        return desc;
    }

}
