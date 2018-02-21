package com.cart.app.viewmodel.listing;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.cart.app.dagger.AppComponentProvider;
import com.cart.app.model.ProductModel;
import com.cart.app.retrofit.NetworkService;
import com.cart.app.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;

/**
 * Created by padmanabhan on 2/12/18.
 */

public class ListingViewModel extends AndroidViewModel {

    /**
     * Live Data and extended ViewModel - Android Architectural components
     */
    private MutableLiveData<Boolean> listLoaded = new MutableLiveData<>();
    private MutableLiveData<List<ProductModel>> listLiveData = new MutableLiveData<>();

    private NetworkService networkService  = AppComponentProvider.getInstance().getAppComponent().getNetWorkService();
    private Subscriber subscriber;

    public MutableLiveData<List<ProductModel>> getListLiveData() {
        return listLiveData;
    }

    public void setListLoaded(boolean listLoaded) {
        this.listLoaded.setValue(listLoaded);
    }

    /**
     * @apiNote  A Boolean to check whether the list is loaded or not.
     * This is to avoid multiple request trigger
     * @return
     */
    public Boolean isListLoaded() {
        if(listLoaded.getValue() != null){
            return listLoaded.getValue();
        }
        return false;
    }
    public ListingViewModel(Application application) {
        super(application);
    }

    /**
     * @apiNote An Observable to request API and get response.
     * @param page
     * @param pageSize
     */
    public void loadProductsLists(int page, int pageSize){
        Observable<JsonElement> observable = networkService.loadProductsList(page, pageSize);
        subscriber = (Subscriber) observable.subscribe(new Observer<JsonElement>() {
            @Override
            public void onCompleted() {
                subscriber.unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                AppUtils.showErrorToast(getApplication().getApplicationContext());
            }

            @Override
            public void onNext(JsonElement jsonElement) {
                if(jsonElement!=null) {
                    updateProductModelList(jsonElement.getAsJsonObject().getAsJsonArray("products"));
                }else{
                    AppUtils.showErrorToast(getApplication().getApplicationContext());
                }
            }
        });
    }

    /**
     * @apiNote Based on API's response, this will update listLiveData object. On scroll down event,
     * it will add to the existing listLiveData and throw event to update Views.
     * @param products
     */
    private void updateProductModelList(JsonArray products){

        Gson s = new Gson();
        List<ProductModel> newData = new ArrayList<>();

        Observable.from(products)
                .map(product -> s.fromJson(product, ProductModel.class)).subscribe(newData::add).unsubscribe();

        if(listLiveData.getValue() != null) {
            List<ProductModel> value = listLiveData.getValue();
            value.addAll(newData);
            listLiveData.setValue(value);
        }else{
            listLiveData.setValue(newData);
        }
    }
}
