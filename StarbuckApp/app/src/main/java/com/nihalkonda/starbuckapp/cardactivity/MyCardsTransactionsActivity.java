package com.nihalkonda.starbuckapp.cardactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nihalkonda.starbuckapp.R;
import com.nihalkonda.starbuckapp.data.Card;
import com.nihalkonda.starbuckapp.data.Transaction;

public class MyCardsTransactionsActivity extends NavigationMainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setScreenContextAndLayout(this);

        String card_id = godClass.getActivityString(this,"card_id");

        Card c = godClass.getUser().getCardByCardId(card_id);

        ((TextView) findViewById(R.id.description)).setText("Transactions on "+card_id);

        LinearLayout cardContainer = (LinearLayout) findViewById(R.id.cardContainer);

        for(Transaction t:c.getTransactions()){
            LinearLayout item = (LinearLayout) getLayoutInflater().inflate(R.layout.starbucks_item, cardContainer,false);

            ((TextView)item.findViewById(R.id.item_name)).setText(t.getItem_name());
            ((TextView)item.findViewById(R.id.item_price)).setText("$ "+t.getItem_price());
            ((TextView)item.findViewById(R.id.item_time)).setText(t.getDateString());



            cardContainer.addView(item,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
        }

        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class goTo;
                if(v.getId()==R.id.button_back){
                    goTo=MyCardsActivity.class;
                }else {
                    developingFeature();
                    return;
                }

                startActivity(new Intent(MyCardsTransactionsActivity.this,goTo));
                finish();
            }
        };

        setOnClickListener(buttonClickListener,
                R.id.button_back
        );

    }

}
