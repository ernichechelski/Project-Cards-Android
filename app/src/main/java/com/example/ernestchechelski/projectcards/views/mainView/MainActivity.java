package com.example.ernestchechelski.projectcards.views.mainView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.GridView;

import com.example.ernestchechelski.projectcards.views.OnClickObservable;
import com.example.ernestchechelski.projectcards.R;
import com.example.ernestchechelski.projectcards.cards.Cards;
import com.example.ernestchechelski.projectcards.cards.CardsImplementationProvider;
import com.example.ernestchechelski.projectcards.views.ImageGridViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View{

    public static String TAG = MainActivity.class.getSimpleName();

    private CompositeDisposable uiDisposables = new CompositeDisposable();
    private OnClickObservable nextCardButtonClickedObservable = new OnClickObservable();
    private OnClickObservable restartButtonClickedObservable = new OnClickObservable();

    private GridView gridView;
    private ImageGridViewAdapter<Cards.CardModel> gridAdapter;
    private MainActivityContract.Presenter presenter;

    private Consumer<Throwable> globalErrorConsumer =  new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
            showWarning(throwable.getLocalizedMessage());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setUI();
        this.presenter = new MainActivityPresenter(this,new CardsImplementationProvider().getRepository());
        this.presenter.startGame();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startRespondingToActions();
    }

    private void startRespondingToActions() {
        this.uiDisposables.add(restartButtonClickedObservable
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
               presenter.resetGame();
            }
        },globalErrorConsumer));
        this.uiDisposables.add(nextCardButtonClickedObservable
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
              presenter.drawCards();
            }
        },globalErrorConsumer));
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopRespondingToActions();
    }

    private void stopRespondingToActions() {
        this.uiDisposables.clear();
        this.presenter.dispose();
    }


    private void setUI() {
        setContentView(R.layout.activity_main);
        Button restartButton = (Button) findViewById(R.id.restartGameButton);
        restartButton.setOnClickListener(restartButtonClickedObservable);
        Button getNextCardsButton = (Button) findViewById(R.id.getNextCardsButton);
        getNextCardsButton.setOnClickListener(nextCardButtonClickedObservable);
        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new ImageGridViewAdapter<>(this, R.layout.card_item_layout,
                new ArrayList<Cards.CardModel>());
        gridView.setAdapter(gridAdapter);
    }

    @Override
    public void clearCards() {
        gridAdapter.clear();
    }

    public void addCards(final List<Cards.CardModel> response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gridAdapter.addAll(response);
                debugPrintCards(response);
                gridView.smoothScrollToPosition(gridAdapter.getCount());
            }
        });
    }

    private void debugPrintCards(List<Cards.CardModel> response) {
        for(Cards.CardModel c:response){
            Log.d(TAG,c.toString());
        }
    }

    @Override
    public void showWinWith(List<Cards.CardModel> cards,String reason) {
        stopRespondingToActions();
        GridView gridView = new GridView(this);

        ImageGridViewAdapter gridAdapter =
                new ImageGridViewAdapter<>(this, R.layout.card_item_layout, cards);
        gridView.setAdapter(gridAdapter);
        gridView.setNumColumns(3);
        gridView.setGravity(Gravity.CENTER);
        gridView.setPadding(10,10,10,10);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(gridView);
        builder.setTitle(R.string.win_alert_title);
        builder.setMessage(reason);
        builder.setPositiveButton(R.string.win_confirm_button_title, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startRespondingToActions();
            }
        });
        builder.show();
    }

    @Override
    public void showWarning(String message) {
        stopRespondingToActions();
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(getString(R.string.warning));
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startRespondingToActions();
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }
}
