package com.example.ernestchechelski.projectcards.cards.heartstone.model;

import com.example.ernestchechelski.projectcards.cards.Cards;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;

public class HeartstoneDeck implements Cards.DeckModel{

    List<Cards.CardModel> cards;

    public HeartstoneDeck(List<Cards.CardModel> cards) {
        this.cards = cards;
    }

    @Override
    public Integer size() {
        return cards.size();
    }

    @Override
    public Observable<List<Cards.CardModel>> drawCards(Integer count) {
        int counter = 0;
        Iterator<Cards.CardModel> it = cards.iterator();
        List<Cards.CardModel> result = new ArrayList<>();
        while (it.hasNext() && counter != count) {
            Cards.CardModel item = it.next();
            result.add(item);
            it.remove();
            count++;
        }
        return Observable.fromArray(result);
    }
}
