package com.cart.app.retrofit;

import com.google.gson.JsonElement;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by padmanabhan on 2/12/18.
 */

public interface RetroAPIService {

    @GET("v1.6.0/catalog/search")
    Observable<JsonElement> getListAsJsonRx(@Query("page") int page, @Query("pageSize") int pSize);

    @GET("v1.6.0/catalog/products/{product_id}")
    Observable<JsonElement> getProductAsJson(@Path(value = "product_id") String pathId);

}

