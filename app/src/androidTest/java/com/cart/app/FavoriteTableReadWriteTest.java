package com.cart.app;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.cart.app.room.EMartRoomDatabase;
import com.cart.app.room.Favorite;
import com.cart.app.room.FavoriteDao;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.IOException;


/**
 * Created by padmanabhan on 2/14/18.
 */


@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FavoriteTableReadWriteTest {
    private static FavoriteDao favoriteDao;
    private static EMartRoomDatabase eMartRoomDatabase;

    @BeforeClass
    public static void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        eMartRoomDatabase = Room.inMemoryDatabaseBuilder(context, EMartRoomDatabase.class).build();
        favoriteDao = eMartRoomDatabase.favDao();
    }


    @Test
    public void A_insertTest() throws Exception {
        Favorite favorite = new Favorite();
        favorite.productId = "678910";
        favorite.price = "4.2";
        favorite.productName = "eMart Meat";
        favorite.image = "http://www.emart.api.com/image.png";
        try {
            favoriteDao.insert(favorite);
            int count = favoriteDao.getCount();
            assert(count == 1);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void B_getTest() throws Exception {
        try{
            Favorite favorite = favoriteDao.getFavItem("678910");
            assert (favorite.productName.equals("eMart Meat"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void C_clearTest() throws Exception {
        try{
            favoriteDao.deleteFav("678910");
            int count = favoriteDao.getCount();
            assert(count == 0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @AfterClass
    public static void closeDb() throws IOException {
        eMartRoomDatabase.close();
    }
}