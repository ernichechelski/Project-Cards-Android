package com.example.ernestchechelski.projectcards;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import com.example.ernestchechelski.projectcards.app.MyApplication;
import com.example.ernestchechelski.projectcards.cardsService.CardsCallback;
import com.example.ernestchechelski.projectcards.cardsService.CardsService;
import com.example.ernestchechelski.projectcards.model.Card;
import com.example.ernestchechelski.projectcards.model.DeckResponse;
import com.example.ernestchechelski.projectcards.model.DrawResponse;
import java.util.ArrayList;
import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    public static String TAG = MainActivity.class.getSimpleName();
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private ArrayList<Card> cards = new ArrayList<>();

    Button restartButton;
    Button getNextCardsButton;

    String deckId;
    Integer decks;

    @Inject
    CardsService cardsService;



    private String m_Text = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        restartButton = (Button) findViewById(R.id.restartGameButton);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (decks != null)
                    MainActivity.this.startGame(decks);
            }
        });
        getNextCardsButton = (Button) findViewById(R.id.getNextCardsButton);
        getNextCardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deckId != null)
                    MainActivity.this.drawCardFromDeck(deckId, 1);
            }
        });
        ((MyApplication)getApplication()).getAppComponent().inject(this);
        gridView = (GridView) findViewById(R.id.gridView);

        gridAdapter = new GridViewAdapter(this, R.layout.card_item_layout, cards);
        gridView.setAdapter(gridAdapter);
        showDecksCountQuestionAlert();

        //startGame(1);

    }
    // Prepare some dummy data for gridview



    private void startGame(Integer decks) {
        this.decks = decks;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gridAdapter.clear();
            }
        });
        cardsService.shuffle(decks, new CardsCallback<DeckResponse>() {
            @Override
            public void onResponse(DeckResponse response) {
                Log.d(TAG,"shuffleOnResponse");
                Log.d(TAG,response.toString());
                deckId = response.getDeckId();
                Log.d(TAG,"Draw 5 cards from deck");
                drawCardFromDeck(response.getDeckId(),5);

            }

            @Override
            public void onFailure(Throwable cause) {
                Log.d(TAG,"shuffleOnFailure");
                Log.d(TAG,cause.toString());
            }
        });
    }

    private void drawCardFromDeck(String deckId, Integer cards) {
        cardsService.drawCardFromDeck(deckId, cards, new CardsCallback<DrawResponse>() {
            @Override
            public void onResponse(final DrawResponse response) {
                if(!response.getSuccess()){
                    showWarnAlert();
                }
                Log.d(TAG,"drawCardFromDeckOnResponse");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gridAdapter.addAll(response.getCards());
                    }
                });
                Log.d(TAG,response.toString());
            }

            @Override
            public void onFailure(Throwable cause) {
                Log.d(TAG,"drawCardFromDeckOnFailure");
                Log.d(TAG,cause.toString());
            }
        });
    }


    private void showWarnAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("No cards left");
        alertDialog.setMessage("Restart game to draw another cards");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void showDecksCountQuestionAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("How many decks do you need in game?");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER| InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);
        builder.setCancelable(false);
        input.setGravity(Gravity.CENTER_HORIZONTAL);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                Integer decks1 = Integer.valueOf(m_Text);
                MainActivity.this.startGame(decks1);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }


}
