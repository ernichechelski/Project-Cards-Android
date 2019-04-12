package com.example.ernestchechelski.projectcards;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import com.example.ernestchechelski.projectcards.app.MyApplication;
import com.example.ernestchechelski.projectcards.cards.Cards;
import com.example.ernestchechelski.projectcards.cards.deckOfCards.DeckOfCardsRepositoryImpl;
import com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI.DeckOfCardsAPIService;
import com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI.model.Card;
import com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI.model.DeckResponse;
import com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI.model.DrawResponse;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    public static String TAG = MainActivity.class.getSimpleName();
    private Integer neededMatches = 9;
    private GridView gridView;
    private ImageGridViewAdapter<Cards.CardModel> gridAdapter;
    private String deckId;
    private Integer decks;
    private String decksCountAnswer = "";

    private CompositeDisposable ioDisposables = new CompositeDisposable();
    private CompositeDisposable uiDisposables = new CompositeDisposable();
    private OnClickObservable nextCardButtonClickedObservable = new OnClickObservable();
    private OnClickObservable restartButtonClickedObservable = new OnClickObservable();

    private Consumer<Throwable> globalErrorConsumer =  new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
            showWarn(throwable.getLocalizedMessage());
        }
    };

    @Inject
    List<Cards.CardModel> cards;

    @Inject
    DeckOfCardsAPIService cardsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
        setUI();
        showDecksCountQuestionAlert();
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiDisposables.add(restartButtonClickedObservable.subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (decks != null) startGame(decks);
            }
        }));
        uiDisposables.add(nextCardButtonClickedObservable.subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (deckId != null) drawCardFromDeck(deckId, 1);
            }
        }));
    }

    @Override
    protected void onPause() {
        super.onPause();
        uiDisposables.clear();
        ioDisposables.clear();
    }

    private void injectDependencies() {
        ((MyApplication)getApplication()).getAppComponent().inject(this);
    }


    private void setUI() {
        setContentView(R.layout.activity_main);
        Button restartButton = (Button) findViewById(R.id.restartGameButton);
        restartButton.setOnClickListener(restartButtonClickedObservable);
        Button getNextCardsButton = (Button) findViewById(R.id.getNextCardsButton);
        getNextCardsButton.setOnClickListener(nextCardButtonClickedObservable);
        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new ImageGridViewAdapter<>(this, R.layout.card_item_layout, cards);
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
        Cards.Repository cardsRepo = new DeckOfCardsRepositoryImpl(getApplicationContext());
        ioDisposables.add(cardsRepo.getNewDeck().subscribe(new Consumer<Cards.DeckModel>() {
            @Override
            public void accept(Cards.DeckModel deckModel) throws Exception {
                Log.d(TAG,deckModel.toString());
                ioDisposables.add(deckModel.drawCards(5).subscribe(new Consumer<List<Cards.CardModel>>() {
                    @Override
                    public void accept(List<Cards.CardModel> cardModels) throws Exception {
                        addCards(cardModels);
                    }
                }));
            }
        }));

        ioDisposables.add(cardsService
                .getShuffledDeck(decks)
                .subscribe(new Consumer<DeckResponse>() {
                    @Override
                    public void accept(DeckResponse response) throws Exception {
                        Log.d(TAG, "shuffleOnResponse");
                        Log.d(TAG, response.toString());
                        deckId = response.getDeckId();
                        Log.d(TAG, "Draw 5 cards from deck");
                        drawCardFromDeck(response.getDeckId(), 5);
                    }
                }, globalErrorConsumer));
    }

    private void getPartialDeck() {
       /* cardsService.getPartialDeck("2C,3C,4C,5C,6C", new CardsCallback<DeckResponse>() {
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
        });*/
    }


    private void drawCardFromDeck(String deckId, final Integer cards) {
        ioDisposables.add(cardsService
                .drawCardFromDeckObservable(deckId,cards)
                .subscribe(new Consumer<DrawResponse>() {
            @Override
            public void accept(DrawResponse response) throws Exception {
                if(!response.getSuccess()){
                    showWarnAlert();
                }
                Log.d(TAG,"drawCardFromDeckOnResponse");

                //addCards(response.getCards());
                Log.d(TAG,response.toString());
            }
        },globalErrorConsumer));
    }

    private void addCards(final List<Cards.CardModel> response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gridAdapter.addAll(response);
                for(Cards.CardModel c:response){
                    Log.d(TAG,"Game value of"+c+" is: "+c.getRank() + " with color: " +c.getCode() + " with generated code: " + c.getCode());
                }
                checkCards(MainActivity.this.cards);
                gridView.smoothScrollToPosition(gridAdapter.getCount());
            }
        });
    }

    private void showWin(List<Cards.CardModel> cards,String reason){
        ioDisposables.clear();
        GridView gridView = new GridView(this);
        ImageGridViewAdapter gridAdapter = new ImageGridViewAdapter<Cards.CardModel>(this, R.layout.card_item_layout, cards);
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

    private void showWarn(String text) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Warning");
        alertDialog.setMessage(text);
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


    public void checkCards(List<Cards.CardModel> cards){

        int size = cards.size();
        for(int x=0;x<size;x++){
            List<Cards.CardModel> matchingCards = new ArrayList<>();
            Cards.CardModel card = cards.get(x);

            matchingCards.clear();
            for(int y=x;y<size;y++){
                Cards.CardModel nextCard = cards.get(y);
                if(card.getRank().rankValue+(y-x) == nextCard.getRank().rankValue){
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
                Cards.CardModel nextCard = cards.get(y);
                if(card.getRank().rankValue-(y-x) == nextCard.getRank().rankValue){
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
                Cards.CardModel nextCard = cards.get(y);
                if(card.getRank().equals(nextCard.getRank())){
                    matchingCards.add(nextCard);
                }
                if(matchingCards.size()==neededMatches){
                    showWin(matchingCards,getString(R.string.rank_cards_match));
                    return;
                }
            }

            matchingCards.clear();
            for(int y=x;y<size;y++){
                Cards.CardModel nextCard = cards.get(y);
                if(nextCard.getRank().isFaceCard()){
                    matchingCards.add(nextCard);
                }
                if(matchingCards.size()==neededMatches){
                    showWin(matchingCards,getString(R.string.face_cards_match));
                    return;
                }
            }

            matchingCards.clear();
            for(int y=x;y<size;y++){
                Cards.CardModel nextCard = cards.get(y);
                if(card.getColor().ordinal() == nextCard.getColor().ordinal()){
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
