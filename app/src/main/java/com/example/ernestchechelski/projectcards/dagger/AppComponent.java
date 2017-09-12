package com.example.ernestchechelski.projectcards.dagger;

import com.example.ernestchechelski.projectcards.MainActivity;
import com.example.ernestchechelski.projectcards.cardsService.CardsService;
import com.example.ernestchechelski.projectcards.cardsService.RetrofitCardsService;

import javax.inject.Singleton;
import dagger.Component;

/**
 * Created by ernest.chechelski on 9/11/2017.
 */
@Singleton
@Component(modules = {AppModule.class, CardsClientModule.class})
public interface AppComponent {
    void inject(MainActivity target);
    void inject(CardsService target);
}