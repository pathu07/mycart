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
import android.widget.RelativeLayout;

import com.cart.app.R;
import com.cart.app.room.Cart;
import com.cart.app.room.Favorite;
import com.cart.app.utils.AppUtils;
import com.cart.app.view.adapter.FavListAdapter;
import com.cart.app.viewmodel.cart.CartViewModel;

import java.util.List;

/**
 * Created by padmanabhan on 2/13/18.
 */

public class FavFragment extends Fragment implements FavListAdapter.FavoriteInterface{

    private CartViewModel cartViewModel;
    FavListAdapter adapter;
    RelativeLayout emptyView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cartViewModel = ViewModelProviders.of(getActivity()).get(CartViewModel.class);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LiveData<List<Favorite>> cartList  = cartViewModel.getAllFavorites();
        cartList.observe(this, this::favDataModified);

        adapter = new FavListAdapter(getContext());
        adapter.favoriteInterface = this;

        RecyclerView recyclerView = getView().findViewById(R.id.recyclerview2);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        emptyView = getView().findViewById(R.id.EmptyMsg);

    }

    private void favDataModified(List<Favorite> favorites) {
        adapter.setFavorite(favorites);
        if (favorites.size() > 0){
            emptyView.setVisibility(View.INVISIBLE);
        }else{
            emptyView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        AppUtils.updateActionBarTitle(this.getActivity(), getString(R.string.my_fav), true);
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

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_cart);
        item.setVisible(false);
        MenuItem item1 = menu.findItem(R.id.action_fav);
        item1.setVisible(false);
    }

    @Override
    public void removeItem(String productId) {
        cartViewModel.deleteFavorite(productId);
        Snackbar.make(this.emptyView, R.string.item_removed_fav, Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void moveItem(Favorite favorite) {
        cartViewModel.deleteFavorite(favorite.productId);
        cartViewModel.insertCart(getCart(favorite));
        Snackbar.make(this.emptyView, R.string.item_moved, Snackbar.LENGTH_LONG).show();
    }

    private Cart getCart(Favorite favorite){
        Cart cart = new Cart();
        cart.productId = favorite.productId;
        cart.productName = favorite.productName;
        cart.count  = 1;//default count
        cart.image = favorite.image;
        cart.price = favorite.price;
        return cart;
    }
}