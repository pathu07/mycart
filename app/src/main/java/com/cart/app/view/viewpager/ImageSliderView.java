package com.cart.app.view.viewpager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.ArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by padmanabhan on 2/12/18.
 */

public class ImageSliderView {

    private SliderLayout sliderLayout;

    public void createPager(Context context, ArrayList<String> urls){

        Observable.from(urls).map(value -> value).subscribe(result -> createSliderView(context, result));
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.stopAutoCycle();
    }

    private void createSliderView(Context context, String url){
        DefaultSliderView defaultSliderView = new DefaultSliderView(context);
        defaultSliderView
                .image(url)
                .setScaleType(BaseSliderView.ScaleType.FitCenterCrop);

        sliderLayout.addSlider(defaultSliderView);
    }

    public void setSliderLayout(SliderLayout sliderLayout) {
        this.sliderLayout = sliderLayout;
    }

}