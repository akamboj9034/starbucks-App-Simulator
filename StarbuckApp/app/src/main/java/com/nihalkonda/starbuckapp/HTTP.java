package com.nihalkonda.starbuckapp;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HTTP {

    //public final String BUrl="http://18.208.24.34:5000";

    public final String BUrl="http://loadbalancer-1394073147.us-east-1.elb.amazonaws.com";


    public final String ABUrl="https://gentle-plains-60806.herokuapp.com";

    public OkHttpClient client ;

    public HTTP(){
        client = new OkHttpClient();
    }

    public JSONObject POST(String Url,JSONObject jsonObject){

        try {
            return POST(BUrl,Url,jsonObject);
        }catch (Exception e){

        }
        try {
            return POST(ABUrl,Url,jsonObject);
        }catch (Exception e){

        }
        return new JSONObject();
    }

    public JSONObject POST(String BaseUrl,String Url,JSONObject jsonObject) throws Exception{

            Url=BaseUrl+Url;

            Log.i("HTTP_Url",Url);
            Log.i("HTTP_Req",jsonObject.toString());

            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    jsonObject.toString());

            Request request = new Request.Builder()
                    .url(Url)
                    .post(body)
                    .build();

            System.out.println("\n"+jsonObject.toString());

            Call call = client.newCall(request);

            Response response = call.execute();
            String res = response.body().string();

            JSONObject resp=new JSONObject(res);

            Log.i("HTTP_Res","\n"+resp.toString());
            return resp;

    }

    public String GET(String Url){

        Request request = new Request.Builder()
                .url(Url)
                .build();

        System.out.println(request.body());

        Call call = client.newCall(request);

        try {
            Response response = call.execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }

}
