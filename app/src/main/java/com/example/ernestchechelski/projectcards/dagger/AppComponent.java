package com.example.ernestchechelski.projectcards.dagger;

import com.example.ernestchechelski.projectcards.MainActivity;
import com.example.ernestchechelski.projectcards.cards.deckOfCards.DeckOfCardsRepositoryImpl;
import com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI.DeckOfCardsAPIService;
import com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI.RetrofitDeckOfCardsAPIService;

import javax.inject.Singleton;
import dagger.Component;

/**
 * Created by ernest.chechelski on 9/11/2017.
 */
@Singleton
@Component(modules = {AppModule.class, DeckOfCardsAPICardsClientModule.class})
public interface AppComponent {
    void inject(MainActivity target);
    void inject(DeckOfCardsAPIService target);
    void inject(RetrofitDeckOfCardsAPIService target);
    void inject(DeckOfCardsRepositoryImpl target);
}