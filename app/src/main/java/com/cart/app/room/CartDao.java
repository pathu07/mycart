package com.cart.app.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

/**
 * Created by padmanabhan on 2/13/18.
 */

@Dao
public interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Cart cart);

    @Query("DELETE from cart_table")
    void deleteAll();

    @Query("DELETE from cart_table where productId = :productId")
    void deleteCart(String productId);

    @Query("SELECT * from cart_table")
    List<Cart> getAllCartItems();

    @Query("SELECT * from cart_table")
    LiveData<List<Cart>> getAllCartsLiveData();

    @Query("SELECT * from cart_table where productId = :productId")
    Cart getCartItem(String productId);

    @Query("SELECT COUNT(*) FROM cart_table")
    int getCount();

    @Query("UPDATE cart_table SET count=:count where productId = :productId")
    void updateItemCount(String productId, int count);

}


