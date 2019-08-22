package com.nihalkonda.starbuckapp.cardactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.nihalkonda.starbuckapp.GodClass;
import com.nihalkonda.starbuckapp.PasscodeActivity;
import com.nihalkonda.starbuckapp.R;

public class SettingsActivity extends NavigationMainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setScreenContextAndLayout(this);

        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(v.getId()==R.id.button_add_card_screen){
                    startActivity(new Intent(SettingsActivity.this,AddCardActivity.class));
                    finish();
                }else if(v.getId()==R.id.button_passcode){

                    GodClass.getInstance().activityTravel(SettingsActivity.this,PasscodeActivity.class,
                            new String[][]{
                                    {"mode",PasscodeActivity.RESET_PIN}
                            }
                    );

                    finish();
                }else {
                    developingFeature();
                    return;
                }

            }
        };

        setOnClickListener(buttonClickListener,
                R.id.button_add_card_screen,
                R.id.button_billing,
                R.id.button_passcode,
                R.id.button_about_terms,
                R.id.button_help
        );


    }

}