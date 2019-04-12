package com.example.ernestchechelski.projectcards.cards;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ernest.chechelski on 9/13/2017.
 * Helper enum for Card class.
 */

public enum CardColor {
    HEARTS("H"),
    DIAMONDS("D"),
    CLUBS("C"),
    SPADES("S");
    public String code;
    CardColor(String code) {
        this.code = code;
    }
}