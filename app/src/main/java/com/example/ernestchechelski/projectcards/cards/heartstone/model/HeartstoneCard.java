package com.example.ernestchechelski.projectcards.cards.heartstone.model;

import com.example.ernestchechelski.projectcards.cards.CardColor;
import com.example.ernestchechelski.projectcards.cards.CardRank;
import com.example.ernestchechelski.projectcards.cards.Cards;

public class HeartstoneCard implements Cards.CardModel{

    CardColor color;
    CardRank rank;
    String code;
    String imageUrl;

    public HeartstoneCard(CardColor color, CardRank rank, String code, String imageUrl) {
        this.color = color;
        this.rank = rank;
        this.code = code;
        this.imageUrl = imageUrl;
    }

    @Override
    public CardColor getColor() {
        return color;
    }

    @Override
    public CardRank getRank() {
        return rank;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }
}
