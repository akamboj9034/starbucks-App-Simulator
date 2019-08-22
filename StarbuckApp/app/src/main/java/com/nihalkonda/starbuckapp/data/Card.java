package com.nihalkonda.starbuckapp.data;

import com.google.gson.Gson;

import org.apache.commons.lang3.Validate;

import java.util.ArrayList;

public class Card extends JsonStructure{

    long id;

    String card_id;
    String card_code;
    double card_balance;

    long last_used;

    ArrayList<Transaction> transactions;

    public Card(){

    }

    public Card(long id,String card_id,String card_code,double card_balance){
        this.id=id;

        Validate.isTrue(card_id.length()==9, "Card ID has to be 9 digits."+card_id);
        Validate.isTrue(card_code.length()==3, "Card Code has to be 3 digits."+card_code);
        Validate.isTrue(card_balance>=0, "Card Balance cannot be negative."+card_balance);

        this.card_id = card_id ;
        this.card_code = card_code ;
        this.card_balance = card_balance ;
        this.last_used = 0 ;
        transactions = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getCard_code() {
        return card_code;
    }

    public void setCard_code(String card_code) {
        this.card_code = card_code;
    }

    public double getCard_balance() {
        return card_balance;
    }

    public String getCardBalance() {
        return "$"+String.format("%.2f",card_balance);
    }

    public void setCard_balance(double card_balance) {
        this.card_balance = card_balance;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public long getLast_used() {
        return last_used;
    }

    public void setLast_used(long last_used) {
        this.last_used = last_used;
    }

    public boolean addTransaction(Transaction t){

        if(t.item_price>card_balance){
            return false;
        }

        card_balance-=t.item_price;

        if(transactions.add(t)){
            setLast_used(t.id);
            return true;
        }else{
            return false;
        }

    }

    public String getDateString(){
        return dateString(id);
    }

}
