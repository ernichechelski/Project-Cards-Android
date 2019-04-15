package com.example.ernestchechelski.projectcards.views;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.subjects.PublishSubject;

import android.view.View;

public class OnClickObservable extends Observable<Boolean> implements View.OnClickListener {

    private PublishSubject<Boolean> internalPublishSubject = PublishSubject.create();

    @Override
    public void onClick(View view) {
        internalPublishSubject.onNext(true);
    }

    @Override
    protected void subscribeActual(Observer<? super Boolean> observer) {
        internalPublishSubject.subscribe(observer);
    }
}
