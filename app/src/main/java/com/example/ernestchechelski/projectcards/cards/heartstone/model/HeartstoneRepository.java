package com.example.ernestchechelski.projectcards.cards.heartstone.model;

import com.example.ernestchechelski.projectcards.cards.Cards;
import com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI.model.Card;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class HeartstoneRepository implements Cards.Repository{

    enum HeartstoneCardType {

    }



    @Override
    public Observable<Cards.DeckModel> getNewDeck() {
        Cards.DeckModel model = new HeartstoneDeck(new ArrayList<Cards.CardModel>());
        return Observable.just(model);
    }

    @Override
    public Observable<Cards.DeckModel> getPartialDeck(List<Card> cards) {
        return null;
    }
}
