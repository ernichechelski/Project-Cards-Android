package com.example.ernestchechelski.projectcards.model;

/**
 * Created by ernest.chechelski on 9/11/2017.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Card {

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("value")
    @Expose
    private String value;

    @SerializedName("suit")
    @Expose
    private String suit;

    @SerializedName("code")
    @Expose
    private String code;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getGameValue(){
        try{
            switch (this.getValue()){
                case "ACE":
                    return 1;
                case "KING":
                    return 12;
                case "QUEEN":
                    return 11;
                case "JACK":
                    return 10;
                default:
                    return Integer.valueOf(this.getValue().toCharArray()[0]+"");
            }
        }
        catch (Exception e){
            return -1;
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
}
