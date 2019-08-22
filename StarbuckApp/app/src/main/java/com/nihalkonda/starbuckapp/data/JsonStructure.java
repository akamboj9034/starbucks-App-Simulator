package com.nihalkonda.starbuckapp.data;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class JsonStructure {

    public String toJSON(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String toRequestJSON(){
        try {
            JSONObject jsonObject = new JSONObject(toJSON());

            Iterator<String> keys = jsonObject.keys();

            while(keys.hasNext()){
                String key = keys.next();
                try {
                    JSONArray jsonArray=
                            jsonObject.getJSONArray(key);
                    jsonArray.length();
                    jsonObject.remove(key);
                }catch (Exception e){

                }
            }

            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "{}";
        }
    }

    public String dateString(long l){
        String pattern = "MM/dd/yyyy hh:mm:ss a";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        return simpleDateFormat.format(new Date(l));
    }
}
