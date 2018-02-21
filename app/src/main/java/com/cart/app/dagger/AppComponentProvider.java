package com.cart.app.dagger;

import android.app.Application;

import com.cart.app.dagger.DaggerAppComponent;

/**
 * Created by padmanabhan on 2/12/18.
 */

public class AppComponentProvider {

    private static final AppComponentProvider ourInstance = new AppComponentProvider();
    private AppComponent appComponent;

    private AppComponentProvider() {

    }

    public static AppComponentProvider getInstance() {
        return ourInstance;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public void initiate(Application application) {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(application))
                .build();

    }
}
