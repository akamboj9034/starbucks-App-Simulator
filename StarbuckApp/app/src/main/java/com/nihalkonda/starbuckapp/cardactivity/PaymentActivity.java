package com.nihalkonda.starbuckapp.cardactivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.nihalkonda.starbuckapp.R;

public class PaymentActivity extends NavigationMainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setScreenContextAndLayout(this);

        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Class goTo;

                if(v.getId()==R.id.button_stores_screen){
                    goTo=StoreActivity.class;
                }else {
                    developingFeature();
                    return;
                }

                startActivity(new Intent(PaymentActivity.this,goTo));
                finish();
            }
        };

        setOnClickListener(buttonClickListener,
                R.id.button_stores_screen,
                R.id.button_enable_payments
        );


    }
}
