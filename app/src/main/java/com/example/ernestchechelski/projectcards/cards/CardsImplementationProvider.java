package com.example.ernestchechelski.projectcards.cards;

import com.example.ernestchechelski.projectcards.cards.deckOfCards.DeckOfCardsRepositoryImpl;

public class CardsImplementationProvider {
    private Cards.Repository repository = new DeckOfCardsRepositoryImpl();

    public Cards.Repository getRepository() {
        return repository;
    }
}
