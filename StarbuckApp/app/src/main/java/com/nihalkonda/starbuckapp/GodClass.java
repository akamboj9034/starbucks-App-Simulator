package com.nihalkonda.starbuckapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.nihalkonda.starbuckapp.cardactivity.*;
import com.nihalkonda.starbuckapp.data.User;

import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.crypto.NoSuchPaddingException;

public class GodClass {

    private static GodClass ch;

    private Class[] AllClasses;
    private Integer[] AllLayouts;
    private Integer[] AllNavIds;
    private String[] AllTitles;

    private CryptLib cryptLib;

    private String email;

    private User user;

    private HTTP http;

    UserLoader userLoader;

    Gson gson;

    private GodClass(){

        try {
            cryptLib = new CryptLib("SDTrocks");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        gson = new Gson();

        email = "" ;

        userLoader = new UserLoader();

        http = new HTTP();

        navInit();

    }

    public void navInit(){
        AllClasses = new Class[]{
                MyCardsActivity.class,
                PaymentActivity.class,
                RewardsActivity.class,
                StoreActivity.class,
                SettingsActivity.class,
                MyCardsPayActivity.class,
                MyCardsOptionsActivity.class,
                MyCardsMoreOptionsActivity.class,
                MyCardsTransactionsActivity.class,
                AddCardActivity.class
        };

        AllLayouts = new Integer[]{
                R.layout.activity_my_cards,
                R.layout.activity_payment,
                R.layout.activity_rewards,
                R.layout.activity_store,
                R.layout.activity_settings,
                R.layout.activity_my_cards_pay,
                R.layout.activity_my_cards_options,
                R.layout.activity_my_cards_more_options,
                R.layout.activity_my_cards_transactions,
                R.layout.activity_add_card
        };

        AllNavIds = new Integer[]{
                R.id.navigation_my_cards,
                R.id.navigation_payments,
                R.id.navigation_my_rewards,
                R.id.navigation_stores,
                R.id.navigation_settings,
                R.id.navigation_my_cards,
                R.id.navigation_my_cards,
                R.id.navigation_my_cards,
                R.id.navigation_my_cards,
                R.id.navigation_settings
        };

        AllTitles = new String[]{
                "My Cards" ,
                "Payment" ,
                "Rewards" ,
                "Store" ,
                "Settings" ,
                "My Cards Pay" ,
                "Options" ,
                "More Options" ,
                "Transactions" ,
                "Add Card"
        };
    }

    public static GodClass getInstance(){
        if(ch==null)ch=new GodClass();
        return ch;
    }

    public Class getClassByNavId(int id){
        return AllClasses[getIndex(AllNavIds,id)];
    }

    public int getNavIdByClass(Class activity){
        return AllNavIds[getIndex(AllClasses,activity)];
    }

    public int getLayoutByActivity(Class activity){
        return AllLayouts[getIndex(AllClasses,activity)];
    }

    public String getTitleByClass(Class activity){
        return AllTitles[getIndex(AllClasses,activity)];
    }

    public int getIndex(Object[] arr,Object elem){
        for (int i = 0; i < arr.length; i++) {
            if(elem.equals(arr[i]))
                return i;
        }
        return -1;
    }

    public HTTP getHttp(){
        return http;
    }

    public void activityTravel(Context a,Class c){
        activityTravel(a,c,null);
    }

    public void activityTravel(Context a,Class c,String[][] vals){
        Intent intent = new Intent(a, c);

        if(vals!=null){
            for (int i = 0; i < vals.length ; i++) {
                intent.putExtra(vals[i][0], vals[i][1]);
            }
        }

        a.startActivity(intent);
    }

    public String getActivityString(Activity a ,String key){
        try {
            return a.getIntent().getStringExtra(key);
        }catch (Exception e){
            return "";
        }
    }

    public String encrypt(String str){
        try {
            return cryptLib.encrypt(str);
        }catch (Exception e){
            return str;
        }
    }

    public String decrypt(String str){
        try {
            return cryptLib.decrypt(str);
        }catch (Exception e){
            return str;
        }
    }

    public String getEmail(){
        if(user==null){
            return email;
        }
        return user.getUser_email();
    }

    public User getUser(){
        return user;
    }

    public void createUser(User user){
        this.user = user;
        user.updateUser();
    }

    public boolean loadUserIfCan(JSONObject jsonObject){
        try {
            user = gson.fromJson(jsonObject.toString(),User.class);
            user.updateUser();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean loadUserIfAny(String email){
        try {

            user = gson.fromJson(FileManager.readFromFile(email),User.class);

            user.updateUser();

            return user!=null;

        }catch (Exception e){

        }
        return false;
    }

    public void loadUser( Activity activity , String email){

        this.email = email;

        userLoader.loadUser(activity,email);

    }

}
