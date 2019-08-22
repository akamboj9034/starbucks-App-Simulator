package com.nihalkonda.starbuckapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nihalkonda.starbuckapp.cardactivity.MyCardsActivity;
import com.nihalkonda.starbuckapp.cardactivity.SettingsActivity;
import com.nihalkonda.starbuckapp.data.User;

import org.json.JSONObject;

import me.philio.pinentry.PinEntryView;

public class PasscodeActivity extends AppCompatActivity {

    PinEntryView pinEntryView;

    public static final String SET_PIN = "SET_PIN" ;
    public static final String RESET_PIN = "RESET_PIN" ;
    public static final String CHECK_PIN = "CHECK_PIN" ;

    int turn = 0 ;
    String[] pins ;
    String[] banners ;
    TextView banner;

    String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);

        pinEntryView = (PinEntryView) findViewById(R.id.pin_entry_simple);

        banner = ((TextView)findViewById(R.id.textViewBanner));

        pins = new String[]{"","",""};

        mode = GodClass.getInstance().getActivityString(this,"mode");

        if(mode.equals(SET_PIN)){
            banners = new String[]{"Set New Passcode","Re-Enter New Passcode",""};
        }else if(mode.equals(RESET_PIN)){
            banners = new String[]{"Enter Old Passcode","Enter New Passcode","Re-Enter New Passcode"};
        }else if(mode.equals(CHECK_PIN)){
            banners = new String[]{"Enter Passcode","",""};
        }

        setBannerText(banners[turn]);

    }



    private void setBannerText(String str) {
        banner.setText(str);
    }

    private void validPin() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(PasscodeActivity.this,MyCardsActivity.class));
                finish();
            }
        });
    }

    private void invalidPin() {
        setBannerText("Invalid Passcode");
        setPin("");
        resetPins();
    }

    public void keypadPress(View v){
        try {
            setBannerText(banners[turn]);

            String cmd = ((Button)v).getText().toString().replace("_"," ").trim();

            if(cmd.equals("<")){
                if(pins[turn].length()>0)
                    pins[turn]=removeLastChar(pins[turn]);
            }else {
                if (pins[turn].length() < 4)
                    pins[turn] += cmd;
            }

            setPin(pins[turn]);

            if(pins[turn].length()==4){
                fullPinEntered();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void fullPinEntered() {
        if(mode.equals(CHECK_PIN)){
            checkPin(pins[0]);
            return;
        }else if(mode.equals(SET_PIN)){
            if(turn == 1){
                if(pins[0].equals(pins[1])){
                    setNewPin(pins[1]);
                    return;
                }else{
                    Toast.makeText(this,"Retry",Toast.LENGTH_LONG).show();
                    resetPins();
                    turn=0;
                    setPin("");
                    return;
                }
            }
        }else if(mode.equals(RESET_PIN)){
            if(turn == 0){
                if(pins[turn].equals(GodClass.getInstance().getUser().getPasscode())){
                    turn++;
                    setBannerText(banners[turn]);
                    setPin("");
                    return;
                }else{
                    invalidPin();
                    return;
                }
            }else if(turn == 2){
                if(pins[1].equals(pins[2])){
                    resetNewPin(pins[2]);
                    return;
                }else{
                    Toast.makeText(this,"Retry",Toast.LENGTH_LONG).show();
                    resetPins();
                    turn=0;
                    setPin("");
                    return;
                }
            }
        }
        turn++;
        setBannerText(banners[turn]);
        setPin("");
    }

    private void resetPins(){
        pins = new String[]{"","",""};
    }

    private void checkPin(final String pin){
        try {
            if(pins[turn].equals(GodClass.getInstance().getUser().getPasscode())){
                validPin();
            }else{
                invalidPin();
            }
            return;
        }catch (Exception e){

        }
        turn=-1;
        setBannerText("Please Wait");
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    GodClass godClass = GodClass.getInstance();

                    JSONObject Response=
                            GodClass.getInstance().getHttp()
                                    .POST("/checkUserDiff",
                                            new JSONObject()
                                                    .putOpt("user_email",godClass.getEmail())
                                                    .putOpt("passcode",pin)
                                                    .putOpt("last_changed",0)
                                    );

                    if(Response.getBoolean("success")){
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
                        validPin();
                    }else{
                        errorMessage("Error Adding User");
                    }

                }catch (Exception e){

                }
            }
        }).start();
    }

    private void setNewPin(final String pin) {
        turn=-1;
        setBannerText("Please Wait");
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                GodClass godClass = GodClass.getInstance();

                JSONObject Response=
                        GodClass.getInstance().getHttp()
                                .POST("/addUser",
                                        new JSONObject()
                                                .putOpt("user_email",godClass.getEmail())
                                                .putOpt("passcode",pin)
                                                .putOpt("last_changed",0)
                                );

                if(Response.getBoolean("success")){
                    godClass.createUser(new User(godClass.getEmail(),pin,Response.getLong("newTime")));
                    validPin();
                }else{
                    errorMessage("Error Adding User");
                }

                }catch (Exception e){

                }
            }
        }).start();
    }

    private void resetNewPin(final String pin) {
        turn=-1;
        setBannerText("Please Wait");
        new Thread(new Runnable() {
            @Override
            public void run() {


                try {

                    GodClass godClass = GodClass.getInstance();
                    JSONObject Response =
                            godClass.getHttp().POST("/setPin",

                                    new JSONObject()
                                            .putOpt("user_email",godClass.getEmail())
                                            .putOpt("new_passcode",pin)
                                            .putOpt("old_passcode",godClass.getUser().getPasscode())
                            );

                    if(Response.getBoolean("success")){
                        godClass.getUser().setPasscode(pin);
                        godClass.getUser().setLast_changed(Response.getLong("newTime"));
                        validPin();
                    }else{
                        errorMessage("Error Setting Pin");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void errorMessage(final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PasscodeActivity.this,error,Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setPin(String str){
        pinEntryView.setText(str);
    }

    public String removeLastChar(String s) {
        return (s == null || s.length() == 0)
                ? null
                : (s.substring(0, s.length() - 1));
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(mode.equals(RESET_PIN)){
            GodClass.getInstance().activityTravel(this,SettingsActivity.class);
            finish();return;
        }
        Toast.makeText(this,"Sorry, This Feature Isn't Available Right Now.",Toast.LENGTH_SHORT).show();
    }


}
