package com.example.ernestchechelski.projectcards.cardsService;


import com.example.ernestchechelski.projectcards.model.DeckResponse;
import com.example.ernestchechelski.projectcards.model.DrawResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ernest.chechelski on 9/11/2017.
 */

public interface CardsServiceApi {

    @GET("api/deck/new/shuffle")
    Call<DeckResponse> shuffleDeck(@Query("deck_count") Integer decksCount);

    @GET("api/deck/new")
    Call<DeckResponse> getNewDeck();

    @GET("api/deck/new/shuffle")
    Call<DeckResponse> getPartialDeck(@Query("cards") String cardsCodes);

    @GET("api/deck/{deck_id}/draw")
    Call<DrawResponse> drawCardFromDeck(@Path("deck_id") String deck_id, @Query("count") Integer cardsCount);

    @GET("api/deck/{deck_id}/shuffle")
    Call<DrawResponse> reshuffleDeck(@Path("deck_id") String deck_id);



}
