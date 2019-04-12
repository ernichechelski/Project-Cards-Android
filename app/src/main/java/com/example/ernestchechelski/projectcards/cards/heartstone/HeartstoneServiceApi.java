package com.example.ernestchechelski.projectcards.cards.heartstone;


import com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI.model.DeckResponse;
import com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI.model.DrawResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ernest.chechelski on 9/11/2017.
 */

public interface HeartstoneServiceApi {
    String getCardImageUrl(String locale,String resolution,String cardId,String extension);
}
