package com.cart.app.view.adapter;

/**
 * Created by padmanabhan on 2/13/18.
 */


import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;


public class ProductsListView extends RecyclerView {

    private GridLayoutManager manager;
    private int columnWidth = -1;
    public ListViewInterface myInterface;

    public ProductsListView(Context context) {
        super(context);
        init(context, null);
    }

    public ProductsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ProductsListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            int[] attrsArray = {android.R.attr.columnWidth};
            TypedArray array = context.obtainStyledAttributes(attrs, attrsArray);
            columnWidth = array.getDimensionPixelSize(0, -1);
            array.recycle();
        }

        manager = new GridLayoutManager(getContext(), 1);
        setLayoutManager(manager);


        this.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    scrollEnded();
                }
            }
        });
    }

    /**
     * @apiNote onMeasure to sepcify column count based on device with.
     * Max column count set to 4.
     * @param widthSpec
     * @param heightSpec
     */
    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        if (columnWidth > 0) {
            int spanCount = Math.max(1, getMeasuredWidth() / columnWidth);
            spanCount = spanCount>4?4:spanCount;
            manager.setSpanCount(spanCount);
        }
    }

    private void scrollEnded(){
        myInterface.scrollEndAction();
    }

    public interface ListViewInterface{
        void scrollEndAction();
    }
}