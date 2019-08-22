package com.nihalkonda.starbuckapp.cardactivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.nihalkonda.starbuckapp.GodClass;
import com.nihalkonda.starbuckapp.R;
import com.nihalkonda.starbuckapp.data.Card;

public class MyCardsActivity extends NavigationMainActivity {


    TextView description;
    LinearLayout cardContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setScreenContextAndLayout(this);

        description = (TextView) findViewById(R.id.description);

        description.setText("Your Current Balance is $"+godClass.getUser().getTotalBalance());

        cardContainer = (LinearLayout) findViewById(R.id.cardContainer);

        View.OnClickListener cardClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout card = (LinearLayout)v;

                PopupMenu popupMenu = new PopupMenu(MyCardsActivity.this,card);

                popupMenu.getMenu().add("Make Payment");
                popupMenu.getMenu().add("View Transactions");

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if(item.getTitle().equals("Make Payment")){
                            godClass.activityTravel(MyCardsActivity.this,MyCardsPayActivity.class,new String[][]{
                                    {"card_id",((TextView)card.findViewById(R.id.cardId)).getText().toString()}
                            });
                            finish();
                        }else{
                            godClass.activityTravel(MyCardsActivity.this,MyCardsTransactionsActivity.class,new String[][]{
                                    {"card_id",((TextView)card.findViewById(R.id.cardId)).getText().toString()}
                            });
                            finish();
                        }

                        return false;
                    }
                });

                popupMenu.show();

            }
        };

        for(Card c:godClass.getUser().getCards()){
            LinearLayout card = (LinearLayout) getLayoutInflater().inflate(R.layout.starbucks_card, cardContainer,false);

            ((TextView)card.findViewById(R.id.cardBalance)).setText(c.getCardBalance());
            ((TextView)card.findViewById(R.id.cardId)).setText(c.getCard_id());
            ((TextView)card.findViewById(R.id.cardCode)).setText(c.getCard_code());
            ((TextView)card.findViewById(R.id.cardTime)).setText(c.getDateString());

            card.setOnClickListener(cardClickListener);



            cardContainer.addView(card,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
        }

        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class goTo;
                if(v.getId()==R.id.button_options_screen){
                    goTo=MyCardsOptionsActivity.class;
                }else {
                    developingFeature();
                    return;
                }

                startActivity(new Intent(MyCardsActivity.this,goTo));
                finish();
            }
        };

        setOnClickListener(buttonClickListener,
                    R.id.button_options_screen
                );

    }

}
