package com.cart.app.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

/**
 * Created by padmanabhan on 2/13/18.
 */

@Entity(tableName = "fav_table")
public class Favorite {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "productId")
    public String productId;

    @NonNull
    @ColumnInfo(name = "productName")
    public String productName;

    @NotNull
    @ColumnInfo(name = "price")
    public String price;

    @NonNull
    @ColumnInfo(name = "image")
    public String image;

}
