package com.example.ernestchechelski.projectcards.cards.deckOfCards.deckofCardsAPI.model;

/**
 * Created by ernest.chechelski on 9/11/2017.
 */
import com.example.ernestchechelski.projectcards.views.ImageGridViewAdapter;
import com.example.ernestchechelski.projectcards.cards.CardColor;
import com.example.ernestchechelski.projectcards.cards.CardRank;
import com.example.ernestchechelski.projectcards.cards.Cards;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Card implements ImageGridViewAdapter.ImageUrlProvider, Cards.CardModel {

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("value")
    @Expose
    private CardRank value;

    @SerializedName("suit")
    @Expose
    private CardColor suit;

    @SerializedName("code")
    @Expose
    private String code;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public CardRank getValue() {
        return value;
    }

    public void setValue(CardRank value) {
        this.value = value;
    }

    public CardColor getSuit() {
        return suit;
    }

    public void setSuit(CardColor suit) {
        this.suit = suit;
    }

    @Override
    public CardColor getColor() {
        return getCardColor();
    }

    @Override
    public CardRank getRank() {
        return getRankValue();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CardRank getRankValue(){
        return this.value;
    }

    public String getFullCode(){
        return getRankValue().text + getCardColor().code;
    }

    public CardColor getCardColor(){
        try{
            String substring = this.getCode().substring(this.getCode().length()-1);
            for (CardColor color:CardColor.values()){
                if (color.code.equals(substring)) {
                    return color;
                }
            }
            return null;
        }
        catch (Exception e){
            return null;
        }
    }
    @Override
    public String toString() {
        return "Card{" +
                "image='" + image + '\'' +
                ", value='" + value + '\'' +
                ", suit='" + suit + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

    @Override
    public String getImageUrl() {
        return getImage();
    }
}
