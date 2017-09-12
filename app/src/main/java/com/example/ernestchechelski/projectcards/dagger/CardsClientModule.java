package com.example.ernestchechelski.projectcards.dagger;

import android.content.Context;

import com.example.ernestchechelski.projectcards.cardsService.CardsService;
import com.example.ernestchechelski.projectcards.cardsService.CardsServiceApi;
import com.example.ernestchechelski.projectcards.cardsService.RetrofitCardsService;
import com.example.ernestchechelski.projectcards.cardsService.RetrofitCardsServiceFactory;
import com.google.gson.GsonBuilder;

import javax.inject.Named;
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
public class CardsClientModule {

    @Provides
    @Singleton
    CardsService provideCardsService(Context context) {
        return new RetrofitCardsService(context);
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
    RetrofitCardsServiceFactory provideRetrofitCardsServiceFactory(Context context) {
        return new RetrofitCardsServiceFactory(context);
    }

    @Provides
    @Singleton
    CardsServiceApi provideCardsServiceApi(RetrofitCardsServiceFactory factory){
        return factory.create();
    }
}