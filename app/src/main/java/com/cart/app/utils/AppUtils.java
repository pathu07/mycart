package com.cart.app.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

import com.cart.app.CartMainActivity;
import com.cart.app.R;

/**
 * Created by padmanabhan on 2/14/18.
 */

public class AppUtils {

    public static String prevFragmentTitle;

    public static void updateActionBarTitle(Activity activity, String title, boolean backButton){
        ((CartMainActivity) activity).getSupportActionBar().setTitle(title);
        ((CartMainActivity) activity).getSupportActionBar().setDisplayHomeAsUpEnabled(backButton);
    }

    public static String addDollar(double price){
        String priceString  = String.valueOf(price);
        priceString = "$"+priceString;
        return priceString;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static void showErrorToast(Context context){
        Toast toast = Toast.makeText(context, context.getString(R.string.error_string), Toast.LENGTH_LONG);
        View view = toast.getView();
        view.setBackgroundColor(Color.RED);
        toast.show();
    }
}
