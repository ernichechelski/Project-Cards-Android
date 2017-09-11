package com.example.ernestchechelski.projectcards;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.example.ernestchechelski.projectcards.Cards.CardsCallback;
import com.example.ernestchechelski.projectcards.Cards.CardsService;
import com.example.ernestchechelski.projectcards.Cards.Model.DeckResponse;

public class MainActivity extends AppCompatActivity {

    public static String TAG = MainActivity.class.getSimpleName();


    CardsService cardsService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        cardsService.shuffle(1, new CardsCallback<DeckResponse>() {
                    @Override
                    public void onResponse(DeckResponse response) {
                        Log.d(TAG,response.toString());
                    }

                    @Override
                    public void onFailure(Throwable cause) {
                        Log.d(TAG,cause.toString());
                    }
                });
        setContentView(R.layout.activity_main);
    }
}
