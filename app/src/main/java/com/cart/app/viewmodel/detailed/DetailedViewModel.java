package com.cart.app.viewmodel.detailed;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.util.ArrayMap;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.cart.app.dagger.AppComponentProvider;
import com.cart.app.model.ProductModel;
import com.cart.app.retrofit.NetworkService;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;

/**
 * Created by padmanabhan on 2/12/18.
 */

public class DetailedViewModel extends AndroidViewModel {

    /**
     * Live Data and extended ViewModel - Android Architectural components
     */
    private MutableLiveData<ArrayMap<String, ProductModel>> productsLiveData = new MutableLiveData<>();

    Subscriber subscriber;

    public MutableLiveData<ArrayMap<String, ProductModel>> getProductsLiveData() {
        return productsLiveData;
    }

    private NetworkService networkService  = AppComponentProvider.getInstance().getAppComponent().getNetWorkService();

    public DetailedViewModel(Application application) {
        super(application);
    }

    /**
     * @apiNote An Observable to request API for get the given product details.
     * it will update productModelLiveData and notify the View.
     * @param productId
     */
    public void loadProductDetail(String productId){

        Observable<JsonElement> observable = networkService.loadProductDetail(productId);
        subscriber = (Subscriber) observable.subscribe(new Observer<JsonElement>() {
            @Override
            public void onCompleted() {
                if(subscriber!=null){
                    subscriber.unsubscribe();
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Toast.makeText(getApplication(), "ERROR while fetching details", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(JsonElement jsonElement) {
                if(jsonElement!=null) {

                    Gson g = new Gson();
                    ArrayMap<String, ProductModel> data = productsLiveData.getValue();
                    if(data == null){
                        data = new ArrayMap<>();
                    }
                    data.put(productId , g.fromJson(jsonElement.getAsJsonObject().get("product"), ProductModel.class));
                    productsLiveData.setValue(data);
                }
            }
        });
    }

    public boolean isAvailable(String productId){
        return (productsLiveData.getValue() != null && productsLiveData.getValue().get(productId) != null);
    }

    public ProductModel getProductModel(String productId){
        if(productsLiveData.getValue() != null){
           return productsLiveData.getValue().get(productId);
        }
        return null;
    }
}