package com.example.ernestchechelski.projectcards.cards.deckOfCards;

import android.content.Context;

import com.example.ernestchechelski.projectcards.app.MyApplication;
import com.example.ernestchechelski.projectcards.cards.Cards;
import com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI.CardsServiceApi;
import com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI.DeckOfCardsAPIService;
import com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI.RetrofitDeckOfCardsAPIService;
import com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI.model.Card;
import com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI.model.DeckResponse;
import com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI.model.DrawResponse;

import java.util.List;


import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DeckOfCardsRepositoryImpl implements Cards.Repository {

    private DeckOfCardsAPIService apiService = new RetrofitDeckOfCardsAPIService();
    private static int RETRY_COUNT = 3;

    @Override
    public Observable<Cards.DeckModel> getNewDeck() {
        return apiService.newDeckObservable().retry(RETRY_COUNT).map(map);
    }

    @Override
    public Observable<Cards.DeckModel> getShuffledDeck(Integer decks) {
        return apiService.getShuffledDeck(decks).retry(RETRY_COUNT).map(map);
    }

    @Override
    public Observable<Cards.DeckModel> getPartialDeck(List<Card> cards) {
       return apiService.partialDeckObservable(cards).retry(RETRY_COUNT).map(map);
    }

    private Function<DeckResponse,Cards.DeckModel> map = new Function<DeckResponse, Cards.DeckModel>() {
        @Override
        public Cards.DeckModel apply(final DeckResponse response) throws Exception {
            return new DefaultDeckModel(response,apiService);
        }
    };

    private class DefaultDeckModel implements Cards.DeckModel{

        public DefaultDeckModel(DeckResponse response, DeckOfCardsAPIService apiService) {
            this.response = response;
            this.apiService = apiService;
        }

        DeckResponse response;
        DeckOfCardsAPIService apiService;


        @Override
        public Integer size() {
            return response.getRemaining();
        }

        @Override
        public Observable<List<Cards.CardModel>> drawCards(Integer count) {
            return apiService
                    .drawCardFromDeckObservable(response.getDeckId(),count)
                    .retry(RETRY_COUNT)
                    .map(new Function<DrawResponse, List<Card>>() {
                @Override
                public List<Card> apply(DrawResponse response) throws Exception {
                    return response.getCards();
                }
            }).flatMap(new Function<List<Card>, ObservableSource<List<Cards.CardModel>>>() {
                @Override
                public ObservableSource<List<Cards.CardModel>> apply(List<Card> cards) throws Exception {
                    return Observable.fromIterable(cards).map(new Function<Card, Cards.CardModel>() {
                        @Override
                        public Cards.CardModel apply(Card card) throws Exception {
                            return card;
                        }
                    }).toList().toObservable();
                }
            });
        }

        public String toString(){
            return response.toString();
        }
    }
}
