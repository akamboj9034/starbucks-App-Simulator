package com.nihalkonda.starbuckapp.cardactivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.nihalkonda.starbuckapp.R;

public class MyCardsOptionsActivity extends NavigationMainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setScreenContextAndLayout(this);

        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Class goTo;
                if(v.getId()==R.id.button_more_options_screen){
                    goTo=MyCardsMoreOptionsActivity.class;
                }else if(v.getId()==R.id.button_cancel){
                    goTo=MyCardsActivity.class;
                }else {
                    developingFeature();
                    return;
                }

                startActivity(new Intent(MyCardsOptionsActivity.this,goTo));
                finish();
            }
        };

        setOnClickListener(buttonClickListener,
                R.id.button_reload,
                R.id.button_refresh,
                R.id.button_more_options_screen,
                R.id.button_cancel
        );

    }
}
