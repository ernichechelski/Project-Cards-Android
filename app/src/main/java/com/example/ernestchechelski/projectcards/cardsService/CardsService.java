package com.example.ernestchechelski.projectcards.cardsService;

import android.util.Log;

import com.example.ernestchechelski.projectcards.model.Card;
import com.example.ernestchechelski.projectcards.model.DeckResponse;
import com.example.ernestchechelski.projectcards.model.DrawResponse;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ernest.chechelski on 9/11/2017.
 */

public interface CardsService {
    void shuffle(Integer decks,final CardsCallback<DeckResponse> callback);
    void getNewDeck(final CardsCallback<DeckResponse> callback);
    void drawCardFromDeck(String deckId, Integer cards, final CardsCallback<DrawResponse> callback);
    void getPartialDeck(String cardsCodes,final CardsCallback<DeckResponse> callback);
    void getCardsSample(String cardsCodes, final CardsCallback<List<Card>> callback, boolean sortedAscending);
    void getCardsSample(String cardsCodes, final CardsCallback<List<Card>> callback);
}
