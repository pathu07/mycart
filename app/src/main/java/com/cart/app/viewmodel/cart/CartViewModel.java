package com.cart.app.viewmodel.cart;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.cart.app.room.Cart;
import com.cart.app.room.Favorite;

import java.util.List;

/**
 * Created by padmanabhan on 2/13/18.
 */

public class CartViewModel extends AndroidViewModel {

    private EMartRepository eMartRepository;

    private MutableLiveData<List<Cart>> allCartsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Favorite>> allFavLiveData = new MutableLiveData<>();

    public CartViewModel(@NonNull Application application) {
        super(application);
        eMartRepository = new EMartRepository(application);
        allCartsLiveData = eMartRepository.getAllCarts();
        allFavLiveData  = eMartRepository.getAllFavourites();
    }

    public MutableLiveData<List<Cart>> getAllCarts(){
        return allCartsLiveData;
    }

    public MutableLiveData<List<Favorite>> getAllFavorites() {
        return allFavLiveData;
    }

    public void insertCart(Cart cart){
        eMartRepository.insertCart(cart);
    }

    public void insertFavorite(Favorite favorite){
        eMartRepository.insertFavorite(favorite);
    }

    public void deleteCart(String productId){ eMartRepository.deleteCart(productId);}
    public void deleteFavorite(String productId){ eMartRepository.deleteFavorite(productId);}

    public void updateCartCount(String productId, int count){ eMartRepository.updateCartCount(productId, count);}
}
