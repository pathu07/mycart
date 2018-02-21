package com.cart.app.retrofit;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.cart.app.dagger.AppComponentProvider;
import com.squareup.picasso.Picasso;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by padmanabhan on 2/12/18.
 */

public class NetworkService {

    public static final String BASE_URL = "https://api.redmart.com/";
    private static final String MEDIA_URL = "http://media.redmart.com/newmedia/200p";

    public static int currentPage = 0;
    public static final int pageSize = 30;

    public static MutableLiveData<Integer> errorCodeVal = new MutableLiveData<>();
    private RetroAPIService retroAPIService = AppComponentProvider.getInstance().getAppComponent().getRetroAPIService();


    /**
     * @apiNote loads product details for the given product Id
     * @param productId
     * @return
     */
    public Observable<JsonElement> loadProductDetail(String productId) {

        return retroAPIService.getProductAsJson(productId).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).onErrorReturn(throwable -> {
                    throwable.printStackTrace();
                    return null;
                });
    }

    /**
     * @apiNote Loads products list for the given page number and page size.
     * @param page
     * @param pageSize
     * @return
     */
    public Observable<JsonElement> loadProductsList(int page, int pageSize) {

        return retroAPIService.getListAsJsonRx(page, pageSize)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(throwable -> {
                    errorCodeVal.setValue(1);
                    throwable.printStackTrace();
                    return null;
                });
    }

    /**
     * @apiNote wrapper class to call picasso with given imageview and image path.
     * @param context
     * @param imageView
     * @param path
     */
    public void loadImage(Context context, ImageView imageView, String path){
        Picasso.with(context)
                .load(getImageUrl(path))
                .resize(100, 150)
                .centerInside()
                .into(imageView);
    }

    /**
     * @apiNote Method to append base url to image string.
     * @param imagePath
     * @return
     */
    public static String getImageUrl(String imagePath){
        return MEDIA_URL+imagePath;
    }
}
