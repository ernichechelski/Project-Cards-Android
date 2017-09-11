package com.example.ernestchechelski.projectcards.Cards.Model;

/**
 * Created by ernest.chechelski on 9/11/2017.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DrawResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("cards")
    @Expose
    private List<Card> cards = null;
    @SerializedName("deck_id")
    @Expose
    private String deckId;
    @SerializedName("remaining")
    @Expose
    private Integer remaining;

    /**
     * No args constructor for use in serialization
     *
     */
    public DrawResponse() {
    }

    /**
     *
     * @param remaining
     * @param deckId
     * @param success
     * @param cards
     */
    public DrawResponse(Boolean success, List<Card> cards, String deckId, Integer remaining) {
        super();
        this.success = success;
        this.cards = cards;
        this.deckId = deckId;
        this.remaining = remaining;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public String getDeckId() {
        return deckId;
    }

    public void setDeckId(String deckId) {
        this.deckId = deckId;
    }

    public Integer getRemaining() {
        return remaining;
    }

    public void setRemaining(Integer remaining) {
        this.remaining = remaining;
    }

    @Override
    public String toString() {
        return "DrawResponse{" +
                "success=" + success +
                ", cards=" + cards +
                ", deckId='" + deckId + '\'' +
                ", remaining=" + remaining +
                '}';
    }

}