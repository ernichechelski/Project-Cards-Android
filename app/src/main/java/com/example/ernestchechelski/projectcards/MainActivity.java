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
    private Integer neededMatches = 9;
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private String deckId;
    private Integer decks;
    private String decksCountAnswer = "";

    @Inject
    List<Card> cards;
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
        Button restartButton = (Button) findViewById(R.id.restartGameButton);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (decks != null)
                    MainActivity.this.startGame(decks);
            }
        });
        Button getNextCardsButton = (Button) findViewById(R.id.getNextCardsButton);
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
        //cardsCodes = "2H,2C,2D,2S,3C";
        cardsCodes = "2H,3C,4D,5C,5D";
        //cardsCodes = "JH,KD,8H,3S,6D";

        //getSampleCards(cardsCodes,true);
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

    private void getSampleCards(String cardsCodes, boolean sortedAscending) {

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
        },sortedAscending);
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
                    Log.d(TAG,"Game value of"+c+" is "+c.getRankValue() + " with color:" +c.getCardColor());
                }
                checkCards(MainActivity.this.cards);
                gridView.smoothScrollToPosition(gridAdapter.getCount());
            }
        });
    }

    private void showWin(List<Card> cards,String reason){
        GridView gridView = new GridView(this);
        GridViewAdapter gridAdapter = new GridViewAdapter(this, R.layout.card_item_layout, cards);
        gridView.setAdapter(gridAdapter);
        gridView.setNumColumns(3);
        gridView.setGravity(Gravity.CENTER);
        gridView.setPadding(10,10,10,10);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(gridView);
        builder.setTitle(R.string.win_alert_title);
        builder.setMessage(reason);
        builder.setPositiveButton(R.string.win_confirm_button_title, null);
        builder.show();
    }
    private void showWarnAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(getString(R.string.no_cards_left));
        alertDialog.setMessage(getString(R.string.restart_game_warning));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
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
        builder.setTitle(R.string.decks_count_question_title);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER| InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);
        builder.setCancelable(false);
        input.setGravity(Gravity.CENTER_HORIZONTAL);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                decksCountAnswer = input.getText().toString();
                Integer decks1 = Integer.valueOf(decksCountAnswer);
                MainActivity.this.startGame(decks1);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }


    public void checkCards(List<Card> cards){

        int size = cards.size();
        for(int x=0;x<size;x++){
            List<Card> matchingCards = new ArrayList<>();
            Card card = cards.get(x);

            matchingCards.clear();
            for(int y=x;y<size;y++){
                Card nextCard = cards.get(y);
                if(card.getRankValue()+(y-x) == nextCard.getRankValue()){
                    matchingCards.add(nextCard);
                } else {
                    break;
                }
                if(matchingCards.size()==neededMatches){
                    showWin(matchingCards,getString(R.string.ascending_cards_match));
                    return;
                }
            }

            matchingCards.clear();
            for(int y=x;y<size;y++){
                Card nextCard = cards.get(y);
                if(card.getRankValue()-(y-x) == nextCard.getRankValue()){
                    matchingCards.add(nextCard);
                }else {
                    break;
                }
                if(matchingCards.size()==neededMatches){
                    showWin(matchingCards,getString(R.string.descending_cards_match));
                    return;
                }
            }

            matchingCards.clear();
            for(int y=x;y<size;y++){
                Card nextCard = cards.get(y);
                if(card.getRankValue().equals(nextCard.getRankValue())){
                    matchingCards.add(nextCard);
                }
                if(matchingCards.size()==neededMatches){
                    showWin(matchingCards,getString(R.string.rank_cards_match));
                    return;
                }
            }

            matchingCards.clear();
            for(int y=x;y<size;y++){
                Card nextCard = cards.get(y);
                if(nextCard.isFaceCard()){
                    matchingCards.add(nextCard);
                }
                if(matchingCards.size()==neededMatches){
                    showWin(matchingCards,getString(R.string.face_cards_match));
                    return;
                }
            }

            matchingCards.clear();
            for(int y=x;y<size;y++){
                Card nextCard = cards.get(y);
                if(card.getCardColor().ordinal() == nextCard.getCardColor().ordinal()){
                    matchingCards.add(nextCard);
                }
                if(matchingCards.size()==neededMatches){
                    showWin(matchingCards,getString(R.string.color_cards_match));
                    return;
                }
            }

        }
    }
}
