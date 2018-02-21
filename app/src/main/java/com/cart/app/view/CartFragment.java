package com.cart.app.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cart.app.R;
import com.cart.app.room.Cart;
import com.cart.app.utils.AppUtils;
import com.cart.app.view.adapter.CartListAdapter;
import com.cart.app.viewmodel.cart.CartViewModel;

import java.util.List;

/**
 * Created by padmanabhan on 2/13/18.
 */

public class CartFragment extends Fragment implements CartListAdapter.CartInterface{

    private CartViewModel cartViewModel;
    CartListAdapter adapter;
    RelativeLayout emptyView;
    public Button buyNow;
    public TextView tPriceEle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        cartViewModel = ViewModelProviders.of(getActivity()).get(CartViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, parent, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LiveData<List<Cart>> cartList  = cartViewModel.getAllCarts();
        cartList.observe(this, this::cartDataModified);

        adapter = new CartListAdapter(getContext());
        adapter.cartInterface = this;

        RecyclerView recyclerView = getView().findViewById(R.id.recyclerview1);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tPriceEle = view.findViewById(R.id.totalPrice);
        buyNow  = view.findViewById(R.id.buyNow);
        emptyView = getView().findViewById(R.id.EmptyMsg);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        AppUtils.updateActionBarTitle(this.getActivity(), getString(R.string.my_cart), true);
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onStop(){
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(getFragmentManager().getBackStackEntryCount() == 0){
            AppUtils.updateActionBarTitle(this.getActivity(), getString(R.string.groceries), false);
        }else{
            AppUtils.updateActionBarTitle(this.getActivity(), AppUtils.prevFragmentTitle, true);
        }
    }

    private void cartDataModified(List<Cart> cartList){
        adapter.setCarts(cartList);
        if(cartList.size() >0){
            emptyView.setVisibility(View.INVISIBLE);
        }else{
            emptyView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_cart);
        item.setVisible(false);
        MenuItem item1 = menu.findItem(R.id.action_fav);
        item1.setVisible(false);
    }

    @Override
    public void removeItem(String productId) {
        cartViewModel.deleteCart(productId);
        Snackbar.make(this.emptyView, R.string.item_removed, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void updateByNowButton(boolean enable) {
        buyNow.setEnabled(enable);
        buyNow.setClickable(enable);
    }

    @Override
    public void updateTotalPrice(double price) {
        tPriceEle.setText(getString(R.string.total_price, AppUtils.round(price,2)));
    }

    @Override
    public void updateCount(String productId, int count) {
        cartViewModel.updateCartCount(productId, count);
    }

}