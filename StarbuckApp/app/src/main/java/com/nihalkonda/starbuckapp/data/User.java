package com.nihalkonda.starbuckapp.data;

import com.google.gson.Gson;
import com.nihalkonda.starbuckapp.FileManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;

public class User extends JsonStructure{

    String user_email;
    String passcode;
    long last_changed;

    ArrayList<Card> cards;

    public User(){
        user_email = "" ;
        passcode = "1234" ;
        last_changed = 0 ;
        cards = new ArrayList<>();
    }

    public User(String user_email,String passcode,long last_changed){
        this();
        this.user_email = user_email ;
        this.passcode = passcode;
        this.last_changed = last_changed;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

    public long getLast_changed() {
        for (Card c:
                cards) {
            if(last_changed < c.getLast_used()){
                last_changed = c.getLast_used() ;
            }
        }
        return last_changed;
    }

    public void setLast_changed(long last_changed) {
        this.last_changed = last_changed;
        updateUser();
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public boolean addCard(Card c){
            if(c==null)
                return false;
            if(cards.add(c)) {
                setLast_changed(c.id);
                return true;
            }
            return false;
    }

    public boolean addTransaction(String card_id,Transaction t){

        for (Card c:
             cards) {
            if(c.getCard_id().equals(card_id)){
                if(c.addTransaction(t)){
                    setLast_changed(t.id);
                    return true;
                }
            }
        }
        return false;
    }

    public Card getCardByCardId(String card_id){
        for (Card c:
                cards) {
            if(c.getCard_id().equals(card_id)){
                return c;
            }
        }
        return null;
    }

    public String getTotalBalance(){
        double d=0;
        for (Card c:
                cards) {
            d+=c.getCard_balance();
        }
        return String.format("%.2f",d);
    }

    public void updateUser(){
        FileManager.writeToFile(getUser_email(),toJSON());
    }

}
