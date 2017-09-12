package com.example.ernestchechelski.projectcards.cardsService;

import android.content.Context;

import com.example.ernestchechelski.projectcards.app.MyApplication;
import com.google.gson.GsonBuilder;

import javax.inject.Inject;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ernest.chechelski on 9/11/2017.
 */

public class RetrofitCardsServiceFactory implements CardsServiceFactory  {

    public RetrofitCardsServiceFactory(Context context) {
        ((MyApplication)context).getAppComponent().inject(this);
    }
    @Inject
    Retrofit retrofit;

    public CardsServiceApi create(){

            /* retrofit =   new Retrofit.Builder()
                .baseUrl("http://deckofcardsapi.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();*/
        return  retrofit.create(CardsServiceApi.class);
    }
}
