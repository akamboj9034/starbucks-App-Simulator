package com.nihalkonda.starbuckapp.cardactivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nihalkonda.starbuckapp.GodClass;
import com.nihalkonda.starbuckapp.R;

public class NavigationMainActivity extends AppCompatActivity {

    private LinearLayout layoutContainer;

    private Context screenContext;

    private BottomNavigationView navigation;

    public GodClass godClass;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            //Toast.makeText(screenContext,item.getTitle(),Toast.LENGTH_LONG).show();

            System.out.println(item.getTitle());

            startActivity(new Intent(
                    screenContext,
                    godClass.getClassByNavId(item.getItemId())
            ));
            finish();

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refresh();

        godClass = GodClass.getInstance();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


    }

    public void refresh(){
        setContentView(R.layout.activity_navigation_main);
        layoutContainer = (LinearLayout) findViewById(R.id.layout_container);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    public void setScreenContextAndLayout(Context screenContext) {

        refresh();

        this.screenContext = screenContext;

        LayoutInflater inflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myView = inflater.inflate(godClass.getLayoutByActivity(screenContext.getClass()), null);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(godClass.getNavIdByClass(screenContext.getClass()));
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        layoutContainer.removeAllViews();
        layoutContainer.addView(myView);

        ((TextView)findViewById(R.id.actionbartitle)).setText(GodClass.getInstance().getTitleByClass(screenContext.getClass()));

    }

    public void prev(View v){
        Toast.makeText(screenContext,"Sorry, No Previous Screen for this Screen.",Toast.LENGTH_SHORT).show();
    }

    public void next(View v){
        Toast.makeText(screenContext,"Sorry, No Next Screen for this Screen.",Toast.LENGTH_SHORT).show();
    }


    public void setOnClickListener(View.OnClickListener buttonClickListener,int... buttonIds){
        for (int i = 0; i < buttonIds.length ; i++) {
            ((Button)findViewById(buttonIds[i])).setOnClickListener(buttonClickListener);
        }
    }

    public void developingFeature(){
        Toast.makeText(screenContext,"Sorry, This Feature Isn't Available Right Now.",Toast.LENGTH_SHORT).show();
    }

    public void toastMessage(final String error){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(screenContext,error,Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Toast.makeText(screenContext,"Sorry, This Feature Isn't Available Right Now.",Toast.LENGTH_SHORT).show();
    }
}
