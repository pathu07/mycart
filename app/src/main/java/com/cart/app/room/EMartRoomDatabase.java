package com.cart.app.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by padmanabhan on 2/13/18.
 */

@Database(entities = {Cart.class, Favorite.class}, version = 1)
public abstract class EMartRoomDatabase extends RoomDatabase {

    private static EMartRoomDatabase INSTANCE;
    private static final String dbName = "red_database";

    public abstract FavoriteDao favDao();
    public abstract CartDao cartDao();

    public static EMartRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (EMartRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            EMartRoomDatabase.class, dbName)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
