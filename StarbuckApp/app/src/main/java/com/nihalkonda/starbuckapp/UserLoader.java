package com.nihalkonda.starbuckapp;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;

public class UserLoader {

    public void loadUser(Activity activity, String email){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject Response=
                            GodClass.getInstance().getHttp()
                                    .POST("/isUser",
                                            new JSONObject()
                                            .putOpt("user_email",email)
                                    );

                    if(!Response.getBoolean("success")){
                        freshUser(activity,email);
                        return;
                    }else{
                        existingUser(activity,email);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void existingUser(Activity activity, String email) throws Exception {

        GodClass godClass = GodClass.getInstance();

        String passcode="";
        long last_changed=0;

        if(godClass.loadUserIfAny(email)){
            passcode = godClass.getUser().getPasscode();
            last_changed = godClass.getUser().getLast_changed();
        }else{
            checkPin(activity);
            return;
        }

        try {
            JSONObject Response=
                    GodClass.getInstance().getHttp()
                            .POST("/checkUserDiff",
                                    new JSONObject()
                                            .putOpt("user_email",email)
                                            .putOpt("passcode",passcode)
                                            .putOpt("last_changed",last_changed)
                            );

            if(!Response.getBoolean("success")){
                System.out.println("checkUserDiff is false");
                return;
            }else{
                if(Response.has("data")) {
                    JSONObject data = Response.getJSONObject("data");
                    if(godClass.loadUserIfCan(data)){
                        System.out.println("User Updated");
                    }else{
                        System.out.println("User Data Updation Failed");
                    }
                }else{
                    System.out.println("User Data is Up-To-Date");
                }

                checkPin(activity);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void checkPin(Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GodClass.getInstance().activityTravel(activity,PasscodeActivity.class,
                        new String[][]{
                                {"mode",PasscodeActivity.CHECK_PIN}
                        }
                );
            }
        });
    }

    private void freshUser(Activity activity, String email) throws Exception {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GodClass.getInstance().activityTravel(activity,PasscodeActivity.class,
                        new String[][]{
                                {"mode",PasscodeActivity.SET_PIN}
                        }
                );
            }
        });
    }

}
