package com.cart.app.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by padmanabhan on 2/13/18.
 */


@Dao
public interface FavoriteDao {

    @Insert
    void insert(Favorite cart);

    @Query("DELETE from fav_table")
    void deleteAll();

    @Query("DELETE from fav_table where productId = :productId")
    void deleteFav(String productId);

    @Query("SELECT * from fav_table")
    List<Favorite> getAllFavItems();

    @Query("SELECT * from fav_table where productId = :productId")
    Favorite getFavItem(String productId);

    @Query("SELECT COUNT(*) FROM fav_table")
    int getCount();



}


