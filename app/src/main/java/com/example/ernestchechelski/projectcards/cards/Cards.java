package com.example.ernestchechelski.projectcards.cards;

import com.example.ernestchechelski.projectcards.views.ImageGridViewAdapter;
import com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI.model.Card;

import java.util.List;

import io.reactivex.Observable;


public interface Cards {

    interface Repository {
        Observable<DeckModel> getNewDeck();
        Observable<DeckModel> getShuffledDeck(Integer fromDecks);
        Observable<DeckModel> getPartialDeck(List<Card> cards);
    }




    interface CardModel extends ImageGridViewAdapter.ImageUrlProvider {
        CardColor getColor();
        CardRank getRank();
        String getCode();
        String getImageUrl();
    }

    interface DeckModel {
        Integer size();
        Observable<List<CardModel>> drawCards(Integer count);
    }

    interface FaceStatus {
        boolean isFaceCard();
    }
}
