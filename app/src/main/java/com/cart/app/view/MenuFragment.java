package com.cart.app.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cart.app.R;
import com.cart.app.model.ProductModel;
import com.cart.app.model.ThumbData;
import com.cart.app.retrofit.NetworkService;
import com.cart.app.utils.AppUtils;
import com.cart.app.view.adapter.ProductsListAdapter;
import com.cart.app.view.adapter.ProductsListView;
import com.cart.app.viewmodel.cart.CartViewModel;
import com.cart.app.viewmodel.listing.ListingViewModel;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by padmanabhan on 2/13/18.
 */

public class MenuFragment extends Fragment implements ProductsListAdapter.adapterInterface, ProductsListView.ListViewInterface {

    ProductsListAdapter productsListAdapter;
    private ListingViewModel listingViewModel;
    private CartViewModel cartViewModel;
    DetailFragment secondFragment;
    private AVLoadingIndicatorView avi;
    private boolean pulled = false;

    /**
     * @param savedInstanceState
     * @apiNote ListingViewModel is LiveData & extends Android ViewModel. Here we will observe for any change in ListingData.
     * Any changes in that will trigger UI refresh.
     * It is safe to Orientation change as we are following ViewModel
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listingViewModel = ViewModelProviders.of(getActivity()).get(ListingViewModel.class);
        cartViewModel = ViewModelProviders.of(getActivity()).get(CartViewModel.class);

        if (listingViewModel.getListLiveData().getValue() == null || listingViewModel.getListLiveData().getValue().size() == 0) {
            listingViewModel.loadProductsLists(NetworkService.currentPage, NetworkService.pageSize);
        }
        listingViewModel.getListLiveData().observe(this, this::updateRecyclerView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        avi = getActivity().findViewById(R.id.avi);
        if (!listingViewModel.isListLoaded()) {
            avi.show();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    /**
     * @param productId String
     * @apiNote Method to open Detailed Fragment for showing product details
     */
    @Override
    public void showDetailedPage(String productId) {

        secondFragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString("productId", productId);
        secondFragment.setArguments(args);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.trans_left_in, R.anim.trans_left_out, R.anim.trans_right_in, R.anim.trans_right_out)
                .add(R.id.flContainer2, secondFragment)
                .addToBackStack("DETAILED")
                .commit();
    }

    /**
     * @param productModelList
     * @apiNote To update Recycler view with API response. remove loader & update actionbar title.
     */
    public void updateRecyclerView(List<ProductModel> productModelList) {
        try {

            List<ThumbData> thumbData = extractThumbData(productModelList);
            if (productsListAdapter == null) {
                productsListAdapter = new ProductsListAdapter(this, thumbData, cartViewModel);

                ProductsListView recyclerView = getActivity().findViewById(R.id.recycler_view);
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(productsListAdapter);
                recyclerView.myInterface = this;
                avi.hide();
                listingViewModel.setListLoaded(true);
                AppUtils.updateActionBarTitle(this.getActivity(), getString(R.string.groceries), false);
            } else {
                productsListAdapter.getThumbDataList().addAll(thumbData);
                productsListAdapter.notifyDataSetChanged();
                pulled = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        AppUtils.updateActionBarTitle(this.getActivity(), getString(R.string.groceries), false);
    }


    /**
     * @apiNote scrollEnd will trigger next set of data. Basically PAGINATION.
     */
    @Override
    public void scrollEndAction() {
        if (!pulled) {
            NetworkService.currentPage++;
            listingViewModel.loadProductsLists(NetworkService.currentPage, NetworkService.pageSize);
            pulled = true;
        }
    }

    /**
     * @param productModelList
     * @return List<ThumbData>
     * @apiNote To Extract View specif Data from ProductModel. Refer ThumbData.kt for Data Model.
     */
    private List<ThumbData> extractThumbData(List<ProductModel> productModelList) {
        List<ThumbData> thumbData = new ArrayList<>();
        Observable.from(productModelList)
                .map(productModel -> new ThumbData(productModel.getId(), productModel.getTitle(), productModel.getImg().getName(), productModel.getPricing())).subscribe(thumbData::add);

        return thumbData;
    }
}