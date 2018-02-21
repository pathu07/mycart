package com.cart.app.view.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.cart.app.R;
import com.cart.app.dagger.AppComponentProvider;
import com.cart.app.retrofit.NetworkService;
import com.cart.app.room.Favorite;
import com.cart.app.utils.AppUtils;

import java.util.List;

/**
 * Created by padmanabhan on 2/13/18.
 */

public class FavListAdapter extends RecyclerView.Adapter<FavListAdapter.FavViewHolder> {

    public FavoriteInterface favoriteInterface;
    private NetworkService networkService =  AppComponentProvider.getInstance().getAppComponent().getNetWorkService();
    private final LayoutInflater mInflater;
    private List<Favorite> mFavorites;

    public FavListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public FavViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.fav_item, parent, false);
        return new FavViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(FavViewHolder holder, int position) {
        if (mFavorites != null) {

            Favorite current = mFavorites.get(position);
            holder.productNameEle.setText(current.productName);
            holder.productPrice.setText(AppUtils.addDollar(Double.parseDouble(current.price)));
            holder.productId = current.productId;

            networkService.loadImage(holder.productImage.getContext(), holder.productImage, current.image);

            RxView.clicks(holder.deleteFav)
                    .subscribe(doThis -> favoriteInterface.removeItem(holder.productId));
            RxView.clicks(holder.moveToCart)
                    .subscribe(doThis -> {
                        favoriteInterface.moveItem(current);
                    });
        }
    }

    public void setFavorite(List<Favorite> favorites){
        mFavorites = favorites;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mFavorites != null)
            return mFavorites.size();
        else return 0;
    }

    /*** Interface to interact with Fragment ***/
    public interface FavoriteInterface{
        void removeItem(String productId);
        void moveItem(Favorite favorite);
    }


    /*** Holder class ***/
    class FavViewHolder extends RecyclerView.ViewHolder {

        private final TextView productNameEle;
        private final ImageView productImage;
        private final TextView productPrice;
        private final Button deleteFav;
        private final Button moveToCart;

        public String productId;

        private FavViewHolder(View itemView) {
            super(itemView);
            productNameEle = itemView.findViewById(R.id.favHolderText);
            productImage = itemView.findViewById(R.id.favHolderImage);
            productPrice = itemView.findViewById(R.id.favHolderPrice);
            deleteFav = itemView.findViewById(R.id.deleteFav);
            moveToCart = itemView.findViewById(R.id.moveToCart);
        }
    }
}
