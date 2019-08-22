package com.nihalkonda.starbuckapp.cardactivity;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nihalkonda.starbuckapp.QRScannerActivity;
import com.nihalkonda.starbuckapp.R;
import com.nihalkonda.starbuckapp.data.Card;

import org.json.JSONObject;

public class AddCardActivity extends NavigationMainActivity {

    private final int QR_REQUEST_CODE = 500 ;

    EditText CardId,CardCode,CardBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setScreenContextAndLayout(this);

        CardId=(EditText)findViewById(R.id.edittext_card_id);
        CardCode=(EditText)findViewById(R.id.edittext_card_code);
        CardBalance=(EditText)findViewById(R.id.edittext_card_balance);

        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class goTo;

                if(v.getId()==R.id.button_add_card){

                    String inputCardId=CardId.getText().toString().replace("-","");
                    String inputCardCode=CardCode.getText().toString();
                    String inputCardBalance=CardBalance.getText().toString();

                    if(Double.parseDouble(inputCardBalance)<=0){
                        Toast.makeText(AddCardActivity.this,"Please Enter Valid Card Balance.",Toast.LENGTH_LONG).show();
                        return;
                    }

                    if(inputCardId.length()==9 && inputCardCode.length()==3 && inputCardBalance.length()>0 ){
                        addCard(inputCardId,inputCardCode,inputCardBalance);
                        return;
                    }else{
                        Toast.makeText(AddCardActivity.this,"Please Enter Valid CardID and CardCode.",Toast.LENGTH_LONG).show();
                        return;
                    }

                }else if(v.getId()==R.id.button_scan_card){
                    startActivityForResult(new Intent(AddCardActivity.this,QRScannerActivity.class),QR_REQUEST_CODE);
                    return;

                }else if(v.getId()==R.id.button_cancel){

                    Toast.makeText(AddCardActivity.this,"Card Creation Cancelled.",Toast.LENGTH_LONG).show();

                    goTo=SettingsActivity.class;

                }else {
                    developingFeature();
                    return;
                }

                startActivity(new Intent(AddCardActivity.this,goTo));
                finish();
            }
        };

        setOnClickListener(buttonClickListener,
                R.id.button_add_card,
                R.id.button_scan_card,
                R.id.button_cancel
        );


        /*CardId.addTextChangedListener(new TextWatcher() {

            private final String delimiter="-";
            private final int delimiterAfter=3;
            private boolean backspacePressed;
            private boolean actedAlready;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                backspacePressed=count==0;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }



            @Override
            public void afterTextChanged(Editable s) {

                if(backspacePressed||actedAlready){
                    if(actedAlready)actedAlready=false;
                    return;
                }
                if(s.length()>1 && (s.toString().replace(delimiter,"").length() % delimiterAfter == 1)){
                    actedAlready=true;
                    s.insert(s.length()-1,delimiter);
                }

            }
        });*/

    }

    private void addCard(final String inputCardId,final String inputCardCode,final String inputCardBalance) {

        setContentView(R.layout.activity_contacting_server);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject response =
                            godClass.getHttp().POST("/linkCard",new JSONObject()
                                    .putOpt("user_email",godClass.getUser().getUser_email())
                                    .putOpt("passcode",godClass.getUser().getPasscode())
                                    .putOpt("card_id",inputCardId)
                                    .putOpt("card_code",inputCardCode)
                                    .putOpt("card_balance",inputCardBalance)
                            );

                    if(response.getBoolean("success")){
                        godClass.getUser().addCard(new Card(response.getLong("newTime"),inputCardId,inputCardCode,Double.parseDouble(inputCardBalance)));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AddCardActivity.this,"New Card Created Successfully.",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(AddCardActivity.this,MyCardsActivity.class));
                                finish();
                            }
                        });
                    }else{
                        final String error = response.getString("error");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setScreenContextAndLayout(AddCardActivity.this);
                                Toast.makeText(AddCardActivity.this,error,Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                }catch (Exception e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setScreenContextAndLayout(AddCardActivity.this);
                            Toast.makeText(AddCardActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();

    }

    @Override
    public void next(View v){
        ((Button)findViewById(R.id.button_add_card)).performClick();
    }

    @Override
    public void prev(View v){
        ((Button)findViewById(R.id.button_cancel)).performClick();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == QR_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String returnedResult = data.getData().toString();
                System.out.println("QR_RESULT:"+returnedResult);

                try {
                    JSONObject jsonObject = new JSONObject(returnedResult);

                    CardId.setText(jsonObject.getString("card_id"));
                    CardCode.setText(jsonObject.getString("card_code"));
                    CardBalance.setText(jsonObject.getDouble("card_balance")+"");

                    Toast.makeText(this,"Card Info Loaded",Toast.LENGTH_LONG).show();

                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }

            }
        }
    }
}
