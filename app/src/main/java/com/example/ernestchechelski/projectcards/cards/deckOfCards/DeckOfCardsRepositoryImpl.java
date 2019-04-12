package com.example.ernestchechelski.projectcards.cards.deckOfCards;

import android.content.Context;

import com.example.ernestchechelski.projectcards.app.MyApplication;
import com.example.ernestchechelski.projectcards.cards.Cards;
import com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI.DeckOfCardsAPIService;
import com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI.model.Card;
import com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI.model.DeckResponse;
import com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI.model.DrawResponse;

import java.util.List;


import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


public class DeckOfCardsRepositoryImpl implements Cards.Repository {

    @Inject
    DeckOfCardsAPIService apiService;

    public DeckOfCardsRepositoryImpl(Context context) {
        ((MyApplication)context).getAppComponent().inject(this);
    }

    @Override
    public Observable<Cards.DeckModel> getNewDeck() {
        return apiService.newDeckObservable().map(new Function<DeckResponse, Cards.DeckModel>() {

            @Override
            public Cards.DeckModel apply(final DeckResponse response) throws Exception {
                return new Cards.DeckModel() {
                    int size = response.getRemaining();

                    @Override
                    public Integer size() {
                        return size;
                    }

                    @Override
                    public Observable<List<Cards.CardModel>> drawCards(Integer count) {
                        return apiService.drawCardFromDeckObservable(response.getDeckId(),count).doOnNext(new Consumer<DrawResponse>() {
                            @Override
                            public void accept(DrawResponse response) throws Exception {
                                size = response.getRemaining();
                            }
                        }).map(new Function<DrawResponse, List<Card>>() {
                            @Override
                            public List<Card> apply(DrawResponse response) throws Exception {
                                size = response.getRemaining();
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
                };
            }
        });
    }

    @Override
    public Observable<Cards.DeckModel> getPartialDeck(List<Card> cards) {
        return null;
    }
}
