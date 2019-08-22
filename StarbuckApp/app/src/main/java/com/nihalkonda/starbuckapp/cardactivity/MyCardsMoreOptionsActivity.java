package com.nihalkonda.starbuckapp.cardactivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.nihalkonda.starbuckapp.R;

public class MyCardsMoreOptionsActivity extends NavigationMainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setScreenContextAndLayout(this);

        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Class goTo;

                if(v.getId()==R.id.button_cancel){
                    goTo=MyCardsOptionsActivity.class;
                }else {
                    developingFeature();
                    return;
                }

                startActivity(new Intent(MyCardsMoreOptionsActivity.this,goTo));
                finish();
            }
        };

        setOnClickListener(buttonClickListener,
                R.id.button_refresh,
                R.id.button_reload,
                R.id.button_auto_reload,
                R.id.button_transactions,
                R.id.button_cancel
        );

    }
}
