package com.example.ernestchechelski.projectcards.Cards;

/**
 * Created by ernest.chechelski on 9/11/2017.
 */

public interface CardsCallback<T> {
    public void onResponse(T response);
    public void onFailure(Throwable cause);
}
