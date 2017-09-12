package com.example.ernestchechelski.projectcards.cardsService;

import android.util.Log;

import com.example.ernestchechelski.projectcards.model.DeckResponse;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ernest.chechelski on 9/11/2017.
 */

public interface CardsService {
    public void shuffle(Integer decks,final CardsCallback<DeckResponse> callback);
}
