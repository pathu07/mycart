package com.cart.app.viewmodel.cart;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;

import com.cart.app.room.Cart;
import com.cart.app.room.CartDao;
import com.cart.app.room.Favorite;
import com.cart.app.room.FavoriteDao;
import com.cart.app.room.EMartRoomDatabase;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by padmanabhan on 2/13/18.
 */

public class EMartRepository {

    private CartDao cartDao;
    private FavoriteDao favoriteDao;
    private MutableLiveData<List<Cart>> allCartsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Favorite>> allFavLiveData = new MutableLiveData<>();

    EMartRepository(Application application) {
        EMartRoomDatabase db = EMartRoomDatabase.getDatabase(application);
        cartDao = db.cartDao();
        favoriteDao = db.favDao();
        getAllCartItems();
        getAllFavoriteItems();
    }

    MutableLiveData<List<Cart>> getAllCarts() {
        return allCartsLiveData;
    }

    MutableLiveData<List<Favorite>> getAllFavourites() {
        return allFavLiveData;
    }

    //INSERT
    void insertCart(Cart cart) {
        rx.Observable.just(cartDao)
                .subscribeOn(Schedulers.newThread())
                .subscribe(cartDao1 -> {
                    try {
                        Cart cart1 = cartDao1.getCartItem(cart.productId);
                        if (cart1 == null)
                            cartDao1.insert(cart);
                        getAllCartItems();//refresh LiveData
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });
    }

    void insertFavorite(Favorite favorite) {
        rx.Observable.just(favoriteDao)
                .subscribeOn(Schedulers.newThread())
                .subscribe(favoriteDao1 -> {
                    try {
                        Favorite favorite1 = favoriteDao1.getFavItem(favorite.productId);
                        if (favorite1 == null)
                            favoriteDao1.insert(favorite);
                        getAllFavoriteItems();//refresh LiveData.
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    //FETCH
    void getAllCartItems() {
        Observable.just(cartDao)
                .map(CartDao::getAllCartItems)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(obj -> {
                    try {
                        allCartsLiveData.setValue(obj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    void getAllFavoriteItems() {
        Observable.just(favoriteDao)
                .map(FavoriteDao::getAllFavItems)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(obj -> {
                    try {
                        allFavLiveData.setValue(obj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    public void deleteCart(String productId) {
        Observable.just(cartDao)
                .subscribeOn(Schedulers.newThread())
                .subscribe(cartDao1 -> {
                    try {
                        cartDao1.deleteCart(productId);
                        getAllCartItems();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    public void deleteFavorite(String productId) {
        Observable.just(favoriteDao)
                .subscribeOn(Schedulers.newThread())
                .subscribe(favoriteDao1 -> {
                    try {
                        favoriteDao1.deleteFav(productId);
                        getAllFavoriteItems();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    public void updateCartCount(String productId, int count) {
        Observable.just(cartDao)
                .subscribeOn(Schedulers.newThread())
                .subscribe(cartDao1 -> {
                    try {
                        cartDao.updateItemCount(productId, count);
                        getAllCartItems();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}