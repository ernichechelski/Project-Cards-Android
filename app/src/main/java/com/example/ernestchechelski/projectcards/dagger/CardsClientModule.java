package com.example.ernestchechelski.projectcards.dagger;

import com.example.ernestchechelski.projectcards.cardsService.CardsService;
import com.example.ernestchechelski.projectcards.cardsService.CardsServiceApi;
import com.example.ernestchechelski.projectcards.cardsService.RetrofitCardsService;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ernest.chechelski on 9/12/2017.
 */


@Module
public class CardsClientModule {
    @Provides
    @Singleton
    CardsService provideCardsService() {
        return new RetrofitCardsService();
    }

    @Provides
    @Singleton
    public CardsServiceApi create(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://deckofcardsapi.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GsonBuilder gsonBuilder = new GsonBuilder();
        return  retrofit.create(CardsServiceApi.class);
    }


}