package com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI.model;

/**
 * Created by ernest.chechelski on 9/11/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DeckResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("deck_id")
    @Expose
    private String deckId;
    @SerializedName("shuffled")
    @Expose
    private Boolean shuffled;
    @SerializedName("remaining")
    @Expose
    private Integer remaining;

    /**
     * No args constructor for use in serialization
     *
     */
    public DeckResponse() {
    }

    /**
     *
     * @param remaining
     * @param shuffled
     * @param deckId
     * @param success
     */
    public DeckResponse(Boolean success, String deckId, Boolean shuffled, Integer remaining) {
        super();
        this.success = success;
        this.deckId = deckId;
        this.shuffled = shuffled;
        this.remaining = remaining;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getDeckId() {
        return deckId;
    }

    public void setDeckId(String deckId) {
        this.deckId = deckId;
    }

    public Boolean getShuffled() {
        return shuffled;
    }

    public void setShuffled(Boolean shuffled) {
        this.shuffled = shuffled;
    }

    public Integer getRemaining() {
        return remaining;
    }

    public void setRemaining(Integer remaining) {
        this.remaining = remaining;
    }

    @Override
    public String toString() {
        return "DeckResponse{" +
                "success=" + success +
                ", deckId='" + deckId + '\'' +
                ", shuffled=" + shuffled +
                ", remaining=" + remaining +
                '}';
    }
}



