package com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI;

import android.content.Context;
import android.util.Log;

import com.example.ernestchechelski.projectcards.app.MyApplication;
import com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI.model.Card;
import com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI.model.DeckResponse;
import com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI.model.DrawResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.subjects.BehaviorSubject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ernest.chechelski on 9/11/2017.
 */

public class RetrofitDeckOfCardsAPIService implements DeckOfCardsAPIService {

    public static final String TAG = RetrofitDeckOfCardsAPIService.class.getSimpleName();

    private static String nullResponseBody = "Null response body";

    private CardsServiceApi cardsServiceApi = new Retrofit.Builder()
            .baseUrl("http://deckofcardsapi.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(CardsServiceApi .class);

    @Override
    public Observable<DeckResponse> getShuffledDeck(final Integer decks) {
        return BehaviorSubject.create(new ObservableOnSubscribe<DeckResponse>() {
            @Override
            public void subscribe(final ObservableEmitter<DeckResponse> observableEmitter) throws Exception {
                shuffle(decks, new CardsCallback<DeckResponse>() {
                    @Override
                    public void onResponse(DeckResponse response) {
                        observableEmitter.onNext(response);
                    }

                    @Override
                    public void onFailure(Throwable cause) {
                        observableEmitter.onError(cause);
                    }
                });
            }
        });
    }

    @Override
    public Observable<DeckResponse> newDeckObservable() {
        return BehaviorSubject.create(new ObservableOnSubscribe<DeckResponse>() {
            @Override
            public void subscribe(final ObservableEmitter<DeckResponse> observableEmitter) throws Exception {
                getNewDeck(new CardsCallback<DeckResponse>() {
                    @Override
                    public void onResponse(DeckResponse response) {
                        observableEmitter.onNext(response);
                    }

                    @Override
                    public void onFailure(Throwable cause) {
                        observableEmitter.onError(cause);
                    }
                });
            }
        });
    }

    @Override
    public Observable<DrawResponse> drawCardFromDeckObservable(final String deckId, final Integer cards) {
        return BehaviorSubject.create(new ObservableOnSubscribe<DrawResponse>() {
            @Override
            public void subscribe(final ObservableEmitter<DrawResponse> observableEmitter) throws Exception {
                drawCardFromDeck(deckId, cards, new CardsCallback<DrawResponse>() {
                    @Override
                    public void onResponse(DrawResponse response) {
                        observableEmitter.onNext(response);
                    }

                    @Override
                    public void onFailure(Throwable cause) {
                        observableEmitter.onError(cause);
                    }
                });
            }
        });
    }

    @Override
    public Observable<DeckResponse> partialDeckObservable(final List<Card> cards) {
        return BehaviorSubject.create(new ObservableOnSubscribe<DeckResponse>() {
            @Override
            public void subscribe(final ObservableEmitter<DeckResponse> observableEmitter) throws Exception {
                getPartialDeck(cards, new CardsCallback<DeckResponse>() {
                    @Override
                    public void onResponse(DeckResponse response) {
                        observableEmitter.onNext(response);
                    }

                    @Override
                    public void onFailure(Throwable cause) {
                        observableEmitter.onError(cause);
                    }
                });
            }
        });
    }

    @Override
    public Observable<List<Card>> cardsObservable(String cardsCodes) {
        return null;
    }

    private void shuffle(Integer decks,final CardsCallback<DeckResponse> callback){
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



    private void getNewDeck(final CardsCallback<DeckResponse> callback) {
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

    private void drawCardFromDeck(String deckId, Integer cards,final CardsCallback<DrawResponse> callback) {
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

    private void getPartialDeck(List<Card> cards,CardsCallback<DeckResponse> callback){
        StringBuilder builder = new StringBuilder();
        for (Card c:cards) {
            builder.append(c.getCode()).append(",");
        }
        builder.deleteCharAt(builder.length()-1);
        getPartialDeck(builder.toString(),callback);
    }

    private void getPartialDeck(String cardsCodes,final CardsCallback<DeckResponse> callback) {
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
}
