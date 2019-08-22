package com.nihalkonda.starbuckapp.cardactivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nihalkonda.starbuckapp.GodClass;
import com.nihalkonda.starbuckapp.QRScannerActivity;
import com.nihalkonda.starbuckapp.R;
import com.nihalkonda.starbuckapp.data.Transaction;

import org.json.JSONObject;

public class MyCardsPayActivity extends NavigationMainActivity {

    private final int QR_REQUEST_CODE = 500 ;

    String card_id="";

    EditText item_name,item_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setScreenContextAndLayout(this);

        card_id = godClass.getActivityString(this,"card_id");

        ((TextView)findViewById(R.id.cardId)).setText(card_id);

        item_name = (EditText) findViewById(R.id.edittext_item_name);
        item_price = (EditText) findViewById(R.id.edittext_item_price);

        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg="";

                if(v.getId()==R.id.button_pay_now){
                    processPayment();
                    return;

                }else if(v.getId()==R.id.button_scan_code){
                    startActivityForResult(new Intent(MyCardsPayActivity.this,QRScannerActivity.class),QR_REQUEST_CODE);
                    return;

                }else if(v.getId()==R.id.button_cancel){
                    msg="Payment is Cancelled.";
                }else {
                    developingFeature();
                    return;
                }



                Toast.makeText(MyCardsPayActivity.this,msg,Toast.LENGTH_LONG).show();

                startActivity(new Intent(MyCardsPayActivity.this,MyCardsActivity.class));
                finish();
            }
        };

        setOnClickListener(buttonClickListener,
                R.id.button_pay_now,
                R.id.button_scan_code,
                R.id.button_cancel
        );
    }

    private void processPayment() {
        if(item_name.getText().toString().length()*item_price.getText().toString().length()==0){
            Toast.makeText(this,"Insufficient Parameters",Toast.LENGTH_LONG).show();
            return;
        }
        String item_name1=item_name.getText().toString();
        Double item_price1=Double.parseDouble(item_price.getText().toString());

        if(item_price1<=0){
            Toast.makeText(this,"Invalid Price",Toast.LENGTH_LONG).show();
            return;
        }
        setContentView(R.layout.activity_contacting_server);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {



                    JSONObject response=
                    godClass.getHttp().POST("/linkItem",new JSONObject()
                            .putOpt("user_email",godClass.getUser().getUser_email())
                            .putOpt("passcode",godClass.getUser().getPasscode())
                            .putOpt("card_id",card_id)
                            .putOpt("item_name",item_name1)
                            .putOpt("item_price",item_price1)
                    );

                    final Boolean success = response.getBoolean("success");

                    if(success){
                        godClass.getUser().addTransaction(card_id,new Transaction(response.getLong("newTime"),item_name1,item_price1));
                    }else{
                        toastMessage(response.getString("error"));
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (success){
                            startActivity(new Intent(MyCardsPayActivity.this,MyCardsActivity.class));
                            finish();
                            }else{
                                setScreenContextAndLayout(MyCardsPayActivity.this);
                                ((TextView)findViewById(R.id.cardId)).setText(card_id);
                            }
                        }
                    });

                }catch (Exception e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setScreenContextAndLayout(MyCardsPayActivity.this);
                            Toast.makeText(MyCardsPayActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == QR_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String returnedResult = data.getData().toString();
                System.out.println("QR_RESULT:"+returnedResult);

                try {
                    JSONObject jsonObject = new JSONObject(returnedResult);

                    item_name.setText(jsonObject.getString("item_name"));
                    item_price.setText(jsonObject.getString("item_price"));

                    Toast.makeText(this,"Item Info Loaded",Toast.LENGTH_LONG).show();

                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }

            }
        }
    }

}
