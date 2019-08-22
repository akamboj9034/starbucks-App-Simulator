package com.nihalkonda.starbuckapp.data;

import com.google.gson.Gson;

public class Transaction extends JsonStructure{
    long id;
    String item_name;
    double item_price;


    public Transaction(){

    }

    public Transaction(long id,String item_name, double item_price) {
        this.id = id;
        this.item_name = item_name;
        this.item_price = item_price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public double getItem_price() {
        return item_price;
    }

    public void setItem_price(double item_price) {
        this.item_price = item_price;
    }


    public String getDateString(){
        return dateString(id);
    }
}
