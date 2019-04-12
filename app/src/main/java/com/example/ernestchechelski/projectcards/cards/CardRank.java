package com.example.ernestchechelski.projectcards.cards;

import com.google.gson.annotations.SerializedName;

public enum CardRank implements Cards.FaceStatus {
    @SerializedName("2") TWO(2,"2"),
    @SerializedName("3") THREE(3,"3"),
    @SerializedName("4") FOUR(4,"4"),
    @SerializedName("5") FIVE(5,"5"),
    @SerializedName("6") SIX(6,"6"),
    @SerializedName("7") SEVEN(7,"7"),
    @SerializedName("8") EIGHT(8,"8"),
    @SerializedName("9") NINE(9,"9"),
    @SerializedName("10") TEN(10,"10"),
    @SerializedName("JACK") JACK(11,"J"),
    @SerializedName("QUEEN") QUEEN(12,"Q"),
    @SerializedName("KING") KING(13,"K"),
    @SerializedName("ACE") ACE(1,"A"),
    @SerializedName("JOKER") JOKER(0,"J");
    public int rankValue;
    public String text;
    CardRank(int rankValue,String text) {
        this.rankValue = rankValue;
        this.text = text;
    }

    @Override
    public boolean isFaceCard() {
        switch (this.rankValue){
            case 1:
            case 11:
            case 12:
            case 13:
                return true;
            default:
                return false;
        }
    }
}