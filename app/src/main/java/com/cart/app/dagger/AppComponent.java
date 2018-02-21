package com.cart.app.dagger;



import com.cart.app.CartMainActivity;
import com.cart.app.retrofit.NetworkService;
import com.cart.app.retrofit.RetroAPIService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by padmanabhan on 2/12/18.
 */


@Singleton
@Component(modules={AppModule.class})
public interface AppComponent {

    void inject(CartMainActivity cartMainActivity);

    RetroAPIService getRetroAPIService();
    NetworkService getNetWorkService();

}
