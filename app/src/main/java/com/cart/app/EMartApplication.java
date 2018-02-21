package com.cart.app;

import android.app.Application;

import com.cart.app.dagger.AppComponentProvider;

/**
 * Created by padmanabhan on 2/12/18.
 */

public class EMartApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppComponentProvider.getInstance().initiate(this);
    }
}
