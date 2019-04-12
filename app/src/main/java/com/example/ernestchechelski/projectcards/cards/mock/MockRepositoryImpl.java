package com.example.ernestchechelski.projectcards.cards.mock;

import com.example.ernestchechelski.projectcards.cards.Cards;
import com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI.model.Card;
import com.example.ernestchechelski.projectcards.cards.CardColor;
import com.example.ernestchechelski.projectcards.cards.CardRank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;


public class MockRepositoryImpl implements Cards.Repository {

    @Override
    public Observable<Cards.DeckModel> getNewDeck() {
        return Observable.create(new ObservableOnSubscribe<Cards.DeckModel>() {
            @Override
            public void subscribe(ObservableEmitter<Cards.DeckModel> observableEmitter) throws Exception {
                observableEmitter.onNext(new Cards.DeckModel() {

                    int size = 10;
                    @Override
                    public Integer size() {
                        return size;
                    }

                    @Override
                    public Observable<List<Cards.CardModel>> drawCards(Integer count) {
                        List<Cards.CardModel> result = new ArrayList<>();
                        result.addAll(Arrays.asList(new Cards.CardModel(){
                            @Override
                            public CardColor getColor() {
                                return CardColor.HEARTS;
                            }

                            @Override
                            public CardRank getRank() {
                                return CardRank.ACE;
                            }

                            @Override
                            public String getCode() {
                                return "AH";
                            }

                            @Override
                            public String getImageUrl() {
                                return "https://cdn2.vectorstock.com/i/1000x1000/73/91/poker-playing-card-ace-heart-vector-8697391.jpg";
                            }
                        }));
                        return Observable.fromArray(result);
                    }
                });
            }
        });
    }

    @Override
    public Observable<Cards.DeckModel> getPartialDeck(List<Card> cards) {
        return null;
    }
}
