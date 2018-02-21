package com.cart.app.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.cart.app.R;
import com.cart.app.dagger.AppComponentProvider;
import com.cart.app.retrofit.NetworkService;
import com.cart.app.room.Cart;
import com.cart.app.utils.AppUtils;

import java.util.List;

/**
 * Created by padmanabhan on 2/13/18.
 */

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.CartViewHolder> {

    public CartInterface cartInterface;
    private NetworkService networkService =  AppComponentProvider.getInstance().getAppComponent().getNetWorkService();
    private final LayoutInflater mInflater;
    private List<Cart> mCarts;

    public CartListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.better_cart_item, parent, false);
        return new CartViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {

        if (mCarts != null) {

            Cart current = mCarts.get(position);
            holder.productNameEle.setText(current.productName);
            holder.productId = current.productId;
            holder.count = current.count;

            holder.productQuantity.setText(""+current.count);
            holder.priceHolderEle.setText(current.count +" x $ "+ current.price +" :");
            Double price = Double.parseDouble(current.price);
            price = price*current.count;
            holder.productPriceEle.setText(AppUtils.addDollar(AppUtils.round(price, 2)));

            holder.imageUrl = current.image;
            networkService.loadImage(holder.productImage.getContext(), holder.productImage, holder.imageUrl);

            RxView.clicks(holder.buttonUp)
                        .subscribe(doThis -> {
                            incrementQuantity(holder, position, current.price);
                        });
            RxView.clicks(holder.buttonDown)
                        .subscribe(doThis -> {
                            decrementQuantity(holder, position, current.price);
                        });

            RxView.clicks(holder.deleteCart)
                    .subscribe(doThis -> {
                        cartInterface.removeItem(holder.productId);
                    });

            calculateTotalPrice();
        }
    }

    @SuppressLint("SetTextI18n")
    private void decrementQuantity(CartViewHolder holder, int position, String price) {
        if (holder.count > 1) {
            holder.count--;
            holder.productQuantity.setText(Integer.toString(holder.count));
            updateCount(position, holder.count);
            updatePriceValues(holder, Double.parseDouble(price));
            calculateTotalPrice();
        }
    }

    @SuppressLint("SetTextI18n")
    private void incrementQuantity(CartViewHolder holder, int position, String price){
        holder.count++;
        holder.productQuantity.setText(Integer.toString(holder.count));
        updateCount(position, holder.count);
        updatePriceValues(holder, Double.parseDouble(price));
        calculateTotalPrice();
        cartInterface.updateCount(holder.productId, holder.count);
    }

    @SuppressLint("DefaultLocale")
    private void updatePriceValues(CartViewHolder holder, Double price){
        holder.productQuantity.setText(String.format("%d", holder.count));
        holder.priceHolderEle.setText(String.format("%d x $%s :", holder.count, price));
        Double nPrice = price * holder.count;
        nPrice = AppUtils.round(nPrice, 2);
        holder.productPriceEle.setText(AppUtils.addDollar(nPrice));
        cartInterface.updateCount(holder.productId, holder.count);
    }

    private void calculateTotalPrice(){
        double tPrice = 0;
        for(Cart cart: mCarts){
            tPrice+= (cart.count * Double.parseDouble(cart.price));
        }
        cartInterface.updateTotalPrice(tPrice);
    }

    private void updateCount(int pos, int count){
        Cart current = mCarts.get(pos);
        current.count = count;
        mCarts.set(pos, current);
    }

    public void setCarts(List<Cart> carts){
        mCarts = carts;
        notifyDataSetChanged();
        calculateTotalPrice();
        cartInterface.updateByNowButton( mCarts.size() > 0);
    }

    @Override
    public int getItemCount() {
        if (mCarts != null)
            return mCarts.size();
        else return 0;
    }

    public interface CartInterface{
        void removeItem(String productId);
        void updateByNowButton(boolean enable);
        void updateTotalPrice(double price);
        void updateCount(String productId, int count);
    }


    class CartViewHolder extends RecyclerView.ViewHolder {

        private final TextView productNameEle;
        private final TextView productPriceEle;
        private final TextView productQuantity;
        private final TextView priceHolderEle;
        private final ImageView productImage;
        private final Button buttonUp;
        private final Button buttonDown;
        private final Button deleteCart;

        public String productId;
        private String imageUrl;
        private int count;

        private CartViewHolder(View itemView) {
            super(itemView);
            productNameEle = itemView.findViewById(R.id.nameHolder);
            productPriceEle = itemView.findViewById(R.id.priceView);
            productImage = itemView.findViewById(R.id.cartHolderImage);
            productQuantity = itemView.findViewById(R.id.quantityHolder);
            priceHolderEle = itemView.findViewById(R.id.priceHolder);
            buttonDown = itemView.findViewById(R.id.buttonDown);
            buttonUp = itemView.findViewById(R.id.buttonUp);
            deleteCart = itemView.findViewById(R.id.deleteCart);
        }
    }
}