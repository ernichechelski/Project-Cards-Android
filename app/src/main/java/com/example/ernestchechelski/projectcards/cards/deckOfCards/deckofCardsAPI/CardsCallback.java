package com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI;

/**
 * Created by ernest.chechelski on 9/11/2017.
 */

public interface CardsCallback<T> {
    void onResponse(T response);
    void onFailure(Throwable cause);
}
