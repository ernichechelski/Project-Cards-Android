package com.example.ernestchechelski.projectcards.app;

import android.app.Application;

import com.example.ernestchechelski.projectcards.dagger.AppComponent;
import com.example.ernestchechelski.projectcards.dagger.AppModule;
import com.example.ernestchechelski.projectcards.dagger.DaggerAppComponent;

/**
 * Created by ernest.chechelski on 9/11/2017.
 */

public class MyApplication extends Application {

    private AppComponent appComponent;

    public AppComponent getAppComponent() {
        return appComponent;
    }
    //https://www.raywenderlich.com/146804/dependency-injection-dagger-2
    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = initDagger(this);
    }
    protected AppComponent initDagger(MyApplication application) {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(application))
                .build();
    }
}