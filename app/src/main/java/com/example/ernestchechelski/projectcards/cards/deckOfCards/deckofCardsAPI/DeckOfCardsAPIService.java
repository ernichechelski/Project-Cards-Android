package com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI;

import com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI.model.Card;
import com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI.model.DeckResponse;
import com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI.model.DrawResponse;

import java.util.List;


import io.reactivex.Observable;

/**
 * Created by ernest.chechelski on 9/11/2017.
 */

public interface DeckOfCardsAPIService {
    Observable<DeckResponse> getShuffledDeck(Integer decks);
    Observable<DeckResponse> newDeckObservable();
    Observable<DrawResponse> drawCardFromDeckObservable(String deckId, Integer cards);
    Observable<DeckResponse> partialDeckObservable(String cardsCodes);
    Observable<List<Card>> cardsObservable(String cardsCodes);
}
