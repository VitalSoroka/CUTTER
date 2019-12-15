package com.mycompany.model.entity;

public enum ImgFormat{
    PNG("png"),
    BMP("bmp");
    String format;
    ImgFormat(String  format){
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    @Override
    public String toString() {
        return format;
    }
}
