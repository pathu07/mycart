package com.cart.app.view

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Paint
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.jakewharton.rxbinding2.view.RxView
import com.cart.app.R
import com.cart.app.dagger.AppComponentProvider
import com.cart.app.model.Pricing
import com.cart.app.model.ProductModel
import com.cart.app.model.Promotion
import com.cart.app.retrofit.NetworkService
import com.cart.app.room.Cart
import com.cart.app.room.Favorite
import com.cart.app.utils.AppUtils
import com.cart.app.view.viewpager.ImageSliderView
import com.cart.app.viewmodel.cart.CartViewModel
import com.cart.app.viewmodel.detailed.DetailedViewModel
import com.wang.avi.AVLoadingIndicatorView

import java.util.ArrayList

import rx.Observable
import kotlinx.android.synthetic.main.fragment_detail.*

/**
 * Created by padmanabhan on 2/13/18.
 */

class DetailFragment : Fragment() {

    private lateinit var networkService: NetworkService
    private var detailedViewModel: DetailedViewModel? = null
    private var cartViewModel: CartViewModel? = null

    private var productId: String? = ""
    private var pName: String? = ""
    private var avi: AVLoadingIndicatorView? = null
    private var imageSliderView = ImageSliderView()


    /**
     * @param savedInstanceState
     * @apiNote detailedViewModel is LiveData and extends Android ViewModel. this will refresh UI based on Data changes and it is safe on orientation change as it follows viewmodel.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkService = AppComponentProvider.getInstance().appComponent.netWorkService
        detailedViewModel = ViewModelProviders.of(activity).get(DetailedViewModel::class.java)
        cartViewModel = ViewModelProviders.of(activity).get(CartViewModel::class.java)

    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater?, parent: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater!!.inflate(R.layout.fragment_detail, parent, false)
        v.setOnTouchListener { _, _ -> true }
        avi = activity.findViewById(R.id.productLoader)
        return v
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        loadViewModelData()
    }

    override fun onResume() {
        super.onResume()
        if(pName !="") {
            AppUtils.prevFragmentTitle = pName
            AppUtils.updateActionBarTitle(this.activity, pName, true)
        }
    }

    override fun onPause() {
        super.onPause()
    }
    override fun onDestroy() {
        super.onDestroy()
        AppUtils.prevFragmentTitle = pName
        AppUtils.updateActionBarTitle(this.activity, getString(R.string.groceries), false)
    }

    private fun loadViewModelData() {
        if (arguments != null) {
            productId = arguments.getString("productId")
            if (productId != null) {
                if (!detailedViewModel!!.isAvailable(productId)) { //not available case
                    detailedViewModel!!.loadProductDetail(productId)
                } else {
                    this.updateDetailedView(detailedViewModel!!.getProductModel(productId))
                }
                detailedViewModel!!.productsLiveData.observe(this, Observer { modelArrayMap -> updateDetailedView(modelArrayMap!![productId]) })
            }
        }
    }

    /**
     * @param productModel
     * @apiNote This will update UI with corresponding data based on detailedViewModel data changes.
     * This will update action bar with corresponding product name.
     */
    private fun updateDetailedView(productModel: ProductModel?) {

        if (productModel != null) {

            try {

                //Title
                productName.text = productModel.title
                pName = productModel.title
                AppUtils.prevFragmentTitle = pName
                AppUtils.updateActionBarTitle(this.activity, pName, true)

                // Description
                productMoreDetails.text = productModel.desc

                // Price
                val pricing = productModel.pricing

                //Promotion
                val promotion = productModel.promotions
                priceWatch(promotion, pricing)


                //Images
                val imgList = productModel.images
                val urls = ArrayList<String>()
                Observable.from(imgList)
                        .map<String>({ it.name }
                        ).subscribe { item -> urls.add(NetworkService.getImageUrl(item)) }.unsubscribe()
                updateImageAdapter(urls)

                addToCart(productModel.id, productModel.title, imgList[0].name, productModel.pricing)
                productAddToCart.visibility = View.VISIBLE


                RxView.clicks(productAddToFav).subscribe { addToFavorite(productModel) }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    private fun addToFavorite(productModel: ProductModel?) {
        val favorite = Favorite()
        if (productModel != null) {
            favorite.productId = productModel.toString()
            favorite.productName = productModel.title
            favorite.price = productModel.pricing.price.toString()
            favorite.image = productModel.img.name
            cartViewModel!!.insertFavorite(favorite)
            Snackbar.make(this.view!!, R.string.item_added_fav, Snackbar.LENGTH_LONG).show()
        }
    }

    /**
     * @param urls
     * @apiNote This will create Image Slider for the given set of Image Urls.
     */
    private fun updateImageAdapter(urls: ArrayList<String>) {
        if (this.context != null) {
            imageSliderView.setSliderLayout(sliderLayout)
            imageSliderView.createPager(this.context, urls)
        }
    }



    /**
     * @param promotion
     * @param pricing
     * @apiNote This util method will calculate price and promotion details and update corresponding Views
     */
    private fun priceWatch(promotion: List<Promotion>?, pricing: Pricing) {

        var price: Double
        //Normal Price
        price = pricing.price
        if (pricing.on_sale == 1) {
            //Promo Price
            val promoPrice = pricing.promo_price

            productOldPrice.text = AppUtils.addDollar(price)
            productOldPrice.paintFlags = productOldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            price = promoPrice
        }

        if (promotion != null && promotion.isNotEmpty()) {
            val text = promotion[0].savings_text
            if (text != "") {
                productPriceSaver.text = text
            }
        }
        productPrice.text = AppUtils.addDollar(AppUtils.round(price,2))
    }


    private fun addToCart(productId: Int, productName:String, imageUrl:String, pricing: Pricing){
        RxView.clicks(productAddToCart).subscribe {
            val cart = Cart()
            cart.productId = productId.toString()
            cart.productName = productName
            cart.count = 1
            cart.image = imageUrl
            cart.price = updatePrice(pricing).toString().replace("$", "")
            try {
                cartViewModel!!.insertCart(cart)
                Snackbar
                        .make(this.view!!, R.string.added_to_cart, Snackbar.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun updatePrice(pricing: Pricing): Double {
        return if (pricing.on_sale == 1) {
            pricing.promo_price
        } else {
            pricing.price
        }
    }
}