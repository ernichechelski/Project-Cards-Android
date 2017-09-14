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
import java.util.List;
import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    public static String TAG = MainActivity.class.getSimpleName();

    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private List<Card> cards = new ArrayList<>();

    private Button restartButton;
    private Button getNextCardsButton;

    private String deckId;
    private Integer decks;
    private String decksCountAnswer = "";


    @Inject
    CardsService cardsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
        setUI();
        showDecksCountQuestionAlert();
    }



    private void injectDependencies() {
        ((MyApplication)getApplication()).getAppComponent().inject(this);
    }

    private void setUI() {
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

        gridView = (GridView) findViewById(R.id.gridView);

        gridAdapter = new GridViewAdapter(this, R.layout.card_item_layout, cards);
        gridView.setAdapter(gridAdapter);
    }


    private void startGame(Integer decks) {
        this.decks = decks;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gridAdapter.clear();
            }
        });
        getCards(decks);

        //getPartialDeck();
        String cardsCodes = null;
        cardsCodes = "2H,2C,2D,2S,3C";
        cardsCodes = "JH,KD,8H,3S,6D";

        //getSampleCards(cardsCodes);
    }

    private void getCards(Integer decks) {
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

    private void getPartialDeck() {
        cardsService.getPartialDeck("2C,3C,4C,5C,6C", new CardsCallback<DeckResponse>() {
            @Override
            public void onResponse(DeckResponse response) {
                Log.d(TAG,"getPartialDeckOnResponse");
                Log.d(TAG,response.toString());
                deckId = response.getDeckId();
                Log.d(TAG,"Draw 5 cards from deck");
                drawCardFromDeck(response.getDeckId(),5);
            }

            @Override
            public void onFailure(Throwable cause) {
                Log.d(TAG,"getPartialDeckOnFailure");
                Log.d(TAG,cause.toString());
            }
        });
    }

    private void getSampleCards(String cardsCodes) {

        cardsService.getCardsSample(cardsCodes, new CardsCallback<List<Card>>() {
            @Override
            public void onResponse(List<Card> response) {
                addCards(response);
            }

            @Override
            public void onFailure(Throwable cause) {
                Log.d(TAG,"getCardsSample");
                Log.d(TAG,cause.toString());
            }
        },true);
    }

    private void drawCardFromDeck(String deckId, final Integer cards) {
        cardsService.drawCardFromDeck(deckId, cards, new CardsCallback<DrawResponse>() {
            @Override
            public void onResponse(final DrawResponse response) {
                if(!response.getSuccess()){
                    showWarnAlert();
                }
                Log.d(TAG,"drawCardFromDeckOnResponse");

                addCards(response.getCards());
                Log.d(TAG,response.toString());
            }

            @Override
            public void onFailure(Throwable cause) {
                Log.d(TAG,"drawCardFromDeckOnFailure");
                Log.d(TAG,cause.toString());
            }
        });
    }

    private void addCards(final List<Card> response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gridAdapter.addAll(response);
                for(Card c:response){
                    Log.d(TAG,"Game value of"+c+" is "+c.getGameValue() + " with color:" +c.getCardColor());
                }
                checkCards(MainActivity.this.cards);
                gridView.smoothScrollToPosition(gridAdapter.getCount());
            }
        });
    }

    private void showWin(List<Card> cards,String reason){
        // Prepare grid view
        GridView gridView = new GridView(this);
        GridViewAdapter gridAdapter = new GridViewAdapter(this, R.layout.card_item_layout, cards);
        gridView.setAdapter(gridAdapter);
        gridView.setNumColumns(3);
        gridView.setGravity(Gravity.CENTER);
        gridView.setPadding(10,10,10,10);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(gridView);
        builder.setTitle("You win with this cards");
        builder.setMessage(reason);
        builder.setPositiveButton("Yay!", null);
        builder.show();
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
                decksCountAnswer = input.getText().toString();
                Integer decks1 = Integer.valueOf(decksCountAnswer);
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

    public void checkCards(List<Card> cards){


        List<Card> matchingCards = new ArrayList<>();
        int size = cards.size();
        for(int x=0;x<size;x++){

            //Initial
            Card card = cards.get(x);
            Integer cardValue = card.getGameValue();


            //Prepare for check
            Integer matchingCounter = 0;
            matchingCards.add(card);

            //Check descending
            for(int y=x+1;y<size;y++){
                if(y==cards.size())break;
                Card nextCard = cards.get(y);
                if(nextCard.getGameValue()==(cardValue+(x-y))){
                    matchingCounter++;
                    matchingCards.add(nextCard);

                }
                if(matchingCounter==2){
                    Log.d(TAG,"Next card match descending");
                    MainActivity.this.showWin(matchingCards, "Descending Stairs");
                    return;
                }
            }

            //Prepare for check
            matchingCards.clear();
            matchingCounter = 0;
            matchingCards.add(card);

            //Check rising
            for(int y=x+1;y<size;y++){
                if(y==cards.size())break;
                card = cards.get(y-1);
                Card nextCard = cards.get(y);

                if(nextCard.getGameValue()==(cardValue-(x-y))){
                    matchingCounter++;
                    matchingCards.add(nextCard);

                }
                if(matchingCounter==2){
                    Log.d(TAG,"Next card match rising");
                    MainActivity.this.showWin(matchingCards, "Rising Stairs");
                    return;
                }
            }

            //Prepare for check
            matchingCards.clear();
            matchingCounter = 0;
            matchingCards.add(card);

            //Check ranks
            for(int y=x+1;y<size;y++){
                if(y==cards.size())break;
                Card nextCard = cards.get(y);
                if(nextCard.getGameValue().equals(card.getGameValue())){
                    matchingCounter++;
                    matchingCards.add(nextCard);
                }
                if(matchingCounter==2){
                    Log.d(TAG,"Next card match color");
                    MainActivity.this.showWin(matchingCards,"Same ranks");
                    return;
                }
            }

            //Prepare for check
            matchingCards.clear();
            matchingCounter = 0;
            matchingCards.add(card);

            //Check colors
            for(int y=x+1;y<size;y++){
                if(y==cards.size())break;
                Card nextCard = cards.get(y);
                if(nextCard.getCardColor() == card.getCardColor()){
                    matchingCounter++;
                    matchingCards.add(nextCard);
                }
                if(matchingCounter==2){
                    Log.d(TAG,"Next card match color");
                    MainActivity.this.showWin(matchingCards,"Same color");
                    return;
                }
            }

            //Prepare for check
            matchingCards.clear();
            matchingCounter = 0;

            //Check face cards
            for(int y=x;y<size;y++){
                if(y==cards.size())break;
                Card nextCard = cards.get(y);
                if(nextCard.isFaceCard()){
                    matchingCounter++;
                    matchingCards.add(nextCard);
                }

                if(matchingCounter==3){
                    Log.d(TAG,"Face cards match");
                    MainActivity.this.showWin(matchingCards,"Face cards");
                    return;
                }
            }



            //Stairs, descending or not
            //Rank match, Twins
            //Face cards
            //Color


        }
    }


}
