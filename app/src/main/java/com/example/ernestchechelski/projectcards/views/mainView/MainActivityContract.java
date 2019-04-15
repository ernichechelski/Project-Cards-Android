package com.example.ernestchechelski.projectcards.views.mainView;


import android.content.Context;

import com.example.ernestchechelski.projectcards.cards.Cards;

import java.util.List;
import java.util.Observable;

public interface MainActivityContract {
    interface View {
        void clearCards();
        void addCards(List<Cards.CardModel> cards);
        void showWinWith(List<Cards.CardModel> cards,String reason);
        void showWarning(String message);
        Context getContext();
    }

    interface Presenter {
        void startGame();
        void drawCards();
        void resetGame();
        void dispose();
    }
}
