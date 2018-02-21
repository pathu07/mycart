package com.cart.app;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.cart.app.room.Cart;
import com.cart.app.room.CartDao;
import com.cart.app.room.EMartRoomDatabase;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.Objects;


/**
 * Created by padmanabhan on 2/14/18.
 */

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CartTableReadWriteTest {
    private static CartDao cartDao;
    private static EMartRoomDatabase eMartRoomDatabase;

    @BeforeClass
    public static void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        eMartRoomDatabase = Room.inMemoryDatabaseBuilder(context, EMartRoomDatabase.class).build();
        cartDao = eMartRoomDatabase.cartDao();
    }


    @Test
    public void A_insertTest() throws Exception {
        Cart cart = new Cart();
        cart.productId = "12345";
        cart.count = 1;
        cart.price = "3.2";
        cart.productName = "eMart Chicken";
        cart.image = "http://www.emart.api.com/image.png";
        try {
            cartDao.insert(cart);
            int count = cartDao.getCount();
            assert(count == 1);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void B_getTest() throws Exception {
        try{
            Cart cart = cartDao.getCartItem("12345");
            assert (Objects.equals(cart.productName, "eMart Chicken"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Test
    public void C_updateCountTest() throws Exception{
        try{
            cartDao.updateItemCount("12345", 2);
            Cart cart = cartDao.getCartItem("12345");
            assert(cart.count == 2);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void D_clearTest() throws Exception {
        try{
            cartDao.deleteCart("12345");
            int count = cartDao.getCount();
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