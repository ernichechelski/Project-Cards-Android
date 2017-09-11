package com.example.ernestchechelski.projectcards.cardsService;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ernest.chechelski on 9/11/2017.
 */

public class RetrofitCardsServiceFactory implements CardsServiceFactory  {
    public CardsServiceApi create(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://deckofcardsapi.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GsonBuilder gsonBuilder = new GsonBuilder();
        return  retrofit.create(CardsServiceApi.class);
    }
}
