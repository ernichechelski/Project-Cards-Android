package com.example.ernestchechelski.projectcards.dagger;

import android.content.Context;

import com.example.ernestchechelski.projectcards.cards.Cards;
import com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI.DeckOfCardsAPIService;
import com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI.CardsServiceApi;
import com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI.RetrofitDeckOfCardsAPIService;
import com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI.model.Card;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ernest.chechelski on 9/12/2017.
 */


@Module
public class DeckOfCardsAPICardsClientModule {

    @Provides
    @Singleton
    DeckOfCardsAPIService provideCardsService(Context context) {
        return new RetrofitDeckOfCardsAPIService(context);
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Converter.Factory converter) {
        return new Retrofit.Builder()
                .baseUrl("http://deckofcardsapi.com")
                .addConverterFactory(converter)
                .build();
    }

    @Provides
    @Singleton
    Converter.Factory provideGsonConverter() {
        return GsonConverterFactory.create();
    }

    @Provides
    @Singleton
    CardsServiceApi provideCardsServiceApi(Retrofit retrofit){
        return  retrofit.create(CardsServiceApi.class);
    }

    @Provides
    @Singleton
    List<Cards.CardModel> provideCardsList(){
        return new ArrayList<>();
    }
}