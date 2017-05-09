package com.example.sads.honeycontrol.models;

/**
 * Created by sads on 14/04/17.
 */
public class Products {

    private int id;
    private String size;
    private String price;




    public Products(){

    }

    public Products(int id, String size, String price) {
        this.id = id;
        this.size = size;
        this.price = price;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


}
