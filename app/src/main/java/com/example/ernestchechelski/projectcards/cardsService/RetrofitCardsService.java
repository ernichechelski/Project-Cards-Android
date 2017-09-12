package com.example.ernestchechelski.projectcards.cardsService;

import android.content.Context;
import android.util.Log;

import com.example.ernestchechelski.projectcards.app.MyApplication;
import com.example.ernestchechelski.projectcards.model.DeckResponse;
import com.example.ernestchechelski.projectcards.model.DrawResponse;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ernest.chechelski on 9/11/2017.
 */

public class RetrofitCardsService implements CardsService{

    public static String TAG = RetrofitCardsService.class.getSimpleName();

    @Inject
    CardsServiceApi cardsServiceApi;

    public RetrofitCardsService(Context context) {
        ((MyApplication)context).getAppComponent().inject(this);
    }

    public void shuffle(Integer decks,final CardsCallback<DeckResponse> callback){
        Call<DeckResponse> shuffleResponse = cardsServiceApi.shuffleDeck(decks);
        shuffleResponse.enqueue(new Callback<DeckResponse>() {
            @Override
            public void onResponse(Call<DeckResponse> call, Response<DeckResponse> response) {
                Log.d(TAG,response.toString());
                if (response!=null){
                    callback.onResponse(response.body());
                }
                else {
                    callback.onFailure(new Exception("Null response"));
                }
            }

            @Override
            public void onFailure(Call<DeckResponse> call, Throwable t) {
                Log.w(TAG,"onFailure",t);
                callback.onFailure(t);
            }
        });
    }

    @Override
    public void getNewDeck(final CardsCallback<DeckResponse> callback) {
        Call<DeckResponse> shuffleResponse = cardsServiceApi.getNewDeck();
        shuffleResponse.enqueue(new Callback<DeckResponse>() {
            @Override
            public void onResponse(Call<DeckResponse> call, Response<DeckResponse> response) {
                Log.d(TAG,response.toString());
                if (response!=null){
                    callback.onResponse(response.body());
                }
                else {
                    callback.onFailure(new Exception("Null response"));
                }
            }

            @Override
            public void onFailure(Call<DeckResponse> call, Throwable t) {
                Log.w(TAG,"onFailure",t);
                callback.onFailure(t);
            }
        });
    }

    @Override
    public void drawCardFromDeck(String deckId, Integer cards,final CardsCallback<DrawResponse> callback) {
        Call<DrawResponse> shuffleResponse = cardsServiceApi.drawCardFromDeck(deckId,cards);
        shuffleResponse.enqueue(new Callback<DrawResponse>() {
            @Override
            public void onResponse(Call<DrawResponse> call, Response<DrawResponse> response) {
                Log.d(TAG,response.toString());
                if (response!=null){
                    callback.onResponse(response.body());
                }
                else {
                    callback.onFailure(new Exception("Null response"));
                }
            }

            @Override
            public void onFailure(Call<DrawResponse> call, Throwable t) {
                Log.w(TAG,"onFailure",t);
                callback.onFailure(t);
            }
        });
    }


}
