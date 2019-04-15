package com.example.ernestchechelski.projectcards.views.mainView;

import com.example.ernestchechelski.projectcards.R;
import com.example.ernestchechelski.projectcards.cards.Cards;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivityPresenter implements MainActivityContract.Presenter {

    private static Integer NEEDED_MATCHES = 9;
    private static Integer DECKS = 5;
    private static Integer CARDS_TO_DRAW = 5;

    private CompositeDisposable ioDisposables = new CompositeDisposable();
    private Cards.DeckModel currentDeck;
    private List<Cards.CardModel> cards = new ArrayList<>();

    private MainActivityContract.View view;
    private Cards.Repository repository;

    public MainActivityPresenter(MainActivityContract.View view, Cards.Repository repository) {
        this.view = view;
        this.repository = repository;
    }

    private Consumer<Throwable> globalErrorConsumer =  new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
            view.showWarning(throwable.getLocalizedMessage());
        }
    };

    @Override
    public void startGame() {
        ioDisposables
                .add(repository
                        .getShuffledDeck(DECKS)
                        .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Cards.DeckModel>() {
            @Override
            public void accept(Cards.DeckModel deckModel) throws Exception {
                currentDeck = deckModel;
                drawCards();
            }
        },globalErrorConsumer));
    }

    @Override
    public void drawCards() {
        ioDisposables
                .add(currentDeck
                        .drawCards(CARDS_TO_DRAW)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<Cards.CardModel>>() {
            @Override
            public void accept(List<Cards.CardModel> cardModels) throws Exception {
                cards.addAll(cardModels);
                view.addCards(cardModels);
                checkCards();
            }
        },globalErrorConsumer));
    }

    @Override
    public void resetGame() {
        view.clearCards();
        cards.clear();
        startGame();
    }

    @Override
    public void dispose() {
        ioDisposables.clear();
    }

    private void checkCards(){
        int size = cards.size();
        for(int x=0;x<size;x++){
            List<Cards.CardModel> matchingCards = new ArrayList<>();
            Cards.CardModel card = cards.get(x);

            matchingCards.clear();
            for(int y=x;y<size;y++){
                Cards.CardModel nextCard = cards.get(y);
                if(card.getRank().rankValue+(y-x) == nextCard.getRank().rankValue){
                    matchingCards.add(nextCard);
                } else {
                    break;
                }
                if(matchingCards.size()==NEEDED_MATCHES){
                    this.view.showWinWith(matchingCards,this.view.getContext().getString(R.string.ascending_cards_match));
                    ioDisposables.clear();
                    return;
                }
            }

            matchingCards.clear();
            for(int y=x;y<size;y++){
                Cards.CardModel nextCard = cards.get(y);
                if(card.getRank().rankValue-(y-x) == nextCard.getRank().rankValue){
                    matchingCards.add(nextCard);
                }else {
                    break;
                }
                if(matchingCards.size()==NEEDED_MATCHES){
                    this.view.showWinWith(matchingCards,this.view.getContext().getString(R.string.descending_cards_match));
                    ioDisposables.clear();
                    return;
                }
            }

            matchingCards.clear();
            for(int y=x;y<size;y++){
                Cards.CardModel nextCard = cards.get(y);
                if(card.getRank().equals(nextCard.getRank())){
                    matchingCards.add(nextCard);
                }
                if(matchingCards.size()==NEEDED_MATCHES){

                    this.view.showWinWith(matchingCards,this.view.getContext().getString(R.string.rank_cards_match));
                    ioDisposables.clear();
                    return;
                }
            }

            matchingCards.clear();
            for(int y=x;y<size;y++){
                Cards.CardModel nextCard = cards.get(y);
                if(nextCard.getRank().isFaceCard()){
                    matchingCards.add(nextCard);
                }
                if(matchingCards.size()==NEEDED_MATCHES){
                    this.view.showWinWith(matchingCards,this.view.getContext().getString(R.string.face_cards_match));
                    ioDisposables.clear();
                    return;
                }
            }

            matchingCards.clear();
            for(int y=x;y<size;y++){
                Cards.CardModel nextCard = cards.get(y);
                if(card.getColor().ordinal() == nextCard.getColor().ordinal()){
                    matchingCards.add(nextCard);
                }
                if(matchingCards.size()==NEEDED_MATCHES){
                    this.view.showWinWith(matchingCards,this.view.getContext().getString(R.string.color_cards_match));
                    ioDisposables.clear();
                    return;
                }
            }
        }
    }
}
