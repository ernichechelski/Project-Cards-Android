package com.example.ernestchechelski.projectcards.cardsService;

import android.content.Context;
import android.util.Log;

import com.example.ernestchechelski.projectcards.app.MyApplication;
import com.example.ernestchechelski.projectcards.model.Card;
import com.example.ernestchechelski.projectcards.model.DeckResponse;
import com.example.ernestchechelski.projectcards.model.DrawResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ernest.chechelski on 9/11/2017.
 */

public class RetrofitCardsService implements CardsService{

    public static String TAG = RetrofitCardsService.class.getSimpleName();

    private static String nullResponseBody = "Null response body";
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
                    callback.onFailure(new Exception(nullResponseBody));
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
                    callback.onFailure(new Exception(nullResponseBody));
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
                    callback.onFailure(new Exception(nullResponseBody));
                }
            }

            @Override
            public void onFailure(Call<DrawResponse> call, Throwable t) {
                Log.w(TAG,"onFailure",t);
                callback.onFailure(t);
            }
        });
    }

    @Override
    public void getPartialDeck(String cardsCodes,final CardsCallback<DeckResponse> callback) {
        Call<DeckResponse> shuffleResponse = cardsServiceApi.getPartialDeck(cardsCodes);
        shuffleResponse.enqueue(new Callback<DeckResponse>() {
            @Override
            public void onResponse(Call<DeckResponse> call, Response<DeckResponse> response) {
                if (response!=null){
                    callback.onResponse(response.body());
                }
                else {
                    callback.onFailure(new Exception(nullResponseBody));
                }
            }

            @Override
            public void onFailure(Call<DeckResponse> call, Throwable t) {
                Log.w(TAG,"onFailure",t);
                callback.onFailure(t);
            }
        });
    }



    private void getCardsSample(String cardsCodes, final CardsCallback<List<Card>> callback,final boolean sorted, final boolean ascending) {
        getPartialDeck(cardsCodes, new CardsCallback<DeckResponse>() {
            @Override
            public void onResponse(DeckResponse response) {
                if(response!=null){
                    drawCardFromDeck(response.getDeckId(), response.getRemaining(), new CardsCallback<DrawResponse>() {
                        @Override
                        public void onResponse(DrawResponse response) {
                            List<Card> cards = new ArrayList<>();

                            cards.addAll(response.getCards());
                            if(sorted)
                            Collections.sort(cards, new Comparator<Card>() {
                                @Override
                                public int compare(Card card, Card t1) {
                                    int value = card.getRankValue() - t1.getRankValue();
                                    if(value==0){
                                        value = card.getCardColor().ordinal() - t1.getCardColor().ordinal();
                                    }
                                    if(!ascending){
                                        value = value*-1;
                                    }
                                    return value;
                                }
                            });

                            callback.onResponse(cards);
                        }

                        @Override
                        public void onFailure(Throwable cause) {
                            callback.onFailure(cause);
                        }
                    });
                }else {
                    callback.onFailure(new Exception(nullResponseBody));
                }
            }

            @Override
            public void onFailure(Throwable cause) {
                callback.onFailure(cause);
            }
        });
    }
    @Override
    public void getCardsSample(String cardsCodes, final CardsCallback<List<Card>> callback, final boolean ascending) {
       getCardsSample(cardsCodes,callback,true,ascending);
    }

    @Override
    public void getCardsSample(String cardsCodes, CardsCallback<List<Card>> callback) {
        getCardsSample(cardsCodes,callback,false,false);
    }
}
