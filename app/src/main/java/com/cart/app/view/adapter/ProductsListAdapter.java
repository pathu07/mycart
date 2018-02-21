package com.cart.app.view.adapter;

/**
 * Created by padmanabhan on 2/13/18.
 */


import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
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
import com.cart.app.model.Pricing;
import com.cart.app.model.ThumbData;
import com.cart.app.retrofit.NetworkService;
import com.cart.app.room.Cart;
import com.cart.app.utils.AppUtils;
import com.cart.app.viewmodel.cart.CartViewModel;

import java.util.List;

public class ProductsListAdapter extends RecyclerView.Adapter<ProductsListAdapter.ViewHolder> {

    private List<ThumbData> thumbDataList;
    private adapterInterface  myself;

    private CartViewModel cartViewModel;
    private NetworkService networkService =  AppComponentProvider.getInstance().getAppComponent().getNetWorkService();

    public List<ThumbData> getThumbDataList() {
        return thumbDataList;
    }

    public ProductsListAdapter(adapterInterface myself, List<ThumbData> thumbDataList, CartViewModel cViewModel) {
        this.thumbDataList = thumbDataList;
        this.myself = myself;
        cartViewModel = cViewModel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        ThumbData holderThumbData = thumbDataList.get(position);
        String imageUrl = holderThumbData.getImage();

        holder.textView.setText(holderThumbData.getTitle());
        holder.productId = holderThumbData.getId()+"";
        holder.productName = holderThumbData.getTitle();
        holder.price = updatePrice(holderThumbData.getPrice());
        holder.imageUrl = holderThumbData.getImage();
        holder.count = 1;

        priceWatch(holder, holderThumbData.getPrice());

        networkService.loadImage(holder.holderImage.getContext(), holder.holderImage, imageUrl);

        RxView.clicks(holder.myHolder)
                .subscribe(doThis -> new Handler().postDelayed(() -> myself.showDetailedPage(String.valueOf(holderThumbData.getId())), 300));// To Show Ripple Effect.

        RxView.clicks(holder.holderCart)
                .subscribe(doThis -> insertToCart(holder));
    }

    Double updatePrice (Pricing pricing){
        double price;
        if(pricing.getOn_sale() == 1){
            price = pricing.getPromo_price();
        }else{
            price = pricing.getPrice();
        }
        return price;
    }

    @Override
    public int getItemCount() {
        return getThumbDataList().size();
    }


    /**
     * @apiNote This util method will calculate price details and update corresponding Views
     * @param holder
     * @param pricing
     */
    private void priceWatch(ViewHolder holder, Pricing pricing){

        double price;
        //Normal Price
        price = pricing.getPrice();
        if(pricing.getOn_sale() == 1){
            //Promo Price
            double promoPrice =  pricing.getPromo_price();

            holder.holderOldPrice.setText(AppUtils.addDollar(AppUtils.round(price,2)));
            holder.holderOldPrice.setPaintFlags(holder.holderOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            price = promoPrice;
        }
        holder.holderPrice.setText(AppUtils.addDollar(AppUtils.round(price,2)));
    }

    private void insertToCart(ViewHolder viewHolder){
        Cart cart = new Cart();
        cart.productId = viewHolder.productId;
        cart.productName = viewHolder.productName;
        cart.count  =viewHolder.count;
        cart.image = viewHolder.imageUrl;
        cart.price = viewHolder.price.toString().replace("$", "");
        try {
            cartViewModel.insertCart(cart);
           Snackbar
                    .make(viewHolder.myHolder, R.string.added_to_cart, Snackbar.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public interface adapterInterface{
        void showDetailedPage(String productId);
    }

    /**
     * ViewHolder class to hold each Item's content
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private RelativeLayout myHolder;
        private ImageView holderImage;
        private TextView holderPrice;
        private TextView holderOldPrice;
        private Button holderCart;

        String productId;
        String productName;
        int count;
        Double price;
        String imageUrl;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.holderText);
            holderOldPrice = itemView.findViewById(R.id.holderPriceOld);
            holderPrice = itemView.findViewById(R.id.holderPrice);
            myHolder = (RelativeLayout) itemView;
            holderImage = itemView.findViewById(R.id.holderImage);
            holderCart = itemView.findViewById(R.id.holderCart);
        }
    }
}