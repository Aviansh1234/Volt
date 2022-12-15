package com.example.voltmotors.custom;

public class Product {
    String name, desc, url;
    long price;

    public Product() {

    }

    public Product(String name, String desc, String url, long price) {
        this.name = name;
        this.desc = desc;
        this.url = url;
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}
