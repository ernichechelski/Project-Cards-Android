package com.example.ernestchechelski.projectcards;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.ernestchechelski.projectcards.app.MyApplication;
import com.example.ernestchechelski.projectcards.cardsService.CardsCallback;
import com.example.ernestchechelski.projectcards.cardsService.CardsService;
import com.example.ernestchechelski.projectcards.model.DeckResponse;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    public static String TAG = MainActivity.class.getSimpleName();


    @Inject
    CardsService cardsService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((MyApplication)getApplication()).getAppComponent().inject(this);

        cardsService.shuffle(1, new CardsCallback<DeckResponse>() {
            @Override
            public void onResponse(DeckResponse response) {
                Log.d(TAG,"shuffleOnResponse");
                Log.d(TAG,response.toString());
            }

            @Override
            public void onFailure(Throwable cause) {
                Log.d(TAG,"shuffleOnFailure");
                Log.d(TAG,cause.toString());
            }
        });

        cardsService.getNewDeck(new CardsCallback<DeckResponse>() {
            @Override
            public void onResponse(DeckResponse response) {
                Log.d(TAG,"getNewDeckOnResponse");
                Log.d(TAG,response.toString());
            }

            @Override
            public void onFailure(Throwable cause) {
                Log.d(TAG,"getNewDeckOnFailure");
                Log.d(TAG,cause.toString());
            }
        });

    }
}
