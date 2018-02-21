package com.cart.app.model

/**
 * Created by padmanabhan on 2/12/18.
 */

data class ProductModel(
		val category_tags: List<String>,
		val id: Int, //33973
		val title: String, //Australian Broccoli
		val desc: String, //Product of Australia. Versatile broccoli is delicious raw or cooked, and it is packed with so many complex nutrients that it's often called a superfood.
		val sku: String, //8885003328014
		val categories: List<Int>,
		val types: List<Int>,
		val details: Details,
		val product_life: ProductLife,
		val filters: Filters,
		val img: Img,
		val images: List<Img>,
		val measure: Measure,
		val pricing: Pricing,
		val warehouse: Warehouse,
		val attributes: Attributes,
		val description_fields: DescriptionFields,
		val max_days_on_shelf: Int, //10
		val promotions: List<Promotion>,
		val inventory: Inventory,
		val pr: Int //1
)

data class Details(
		val prod_type: Int, //0
		val uri: String, //australian-broccoli-33973
		val status: Int, //1
		val is_new: Double, //0.0
		val storage_class: String, //Fresh2-5
		val country_of_origin: String //Australia
)

data class Inventory(
		val atp_status: Int, //1
		val max_sale_qty: Int, //48
		val qty_in_carts: Int, //0
		val qty_in_stock: Int, //48
		val stock_status: Int, //1
		val stock_type: Int, //0
		val atp_lots: List<AtpLot>,
		val next_available_date: String, //2018-01-19T04:02:17Z
		val limited_stock_status: Int //0
)

data class AtpLot(
		val from_date: String, //2018-01-19T04:02:17Z
		val to_date: String, //2018-01-20T15:59:59Z
		val stock_status: Int, //0
		val qty_in_stock: Int, //0
		val qty_in_carts: Int //0
)

data class Measure(
		val wt_or_vol: String, //300 g
		val size: Double //0.0
)

data class Pricing(
		val on_sale: Int, //0
		val price: Double, //2.5
		val promo_price: Double, //0.0
		val savings: Double, //10.0
		val savings_type: Int //0
)

data class Filters(
		val mfr_name: String, //Cultured Greens
		val trending_frequency: Int, //6167
		val is_organic: Int, //0
		val country_of_origin: String, //Australia
		val vendor_name: String, //CultGrn
		val brand_name: String, //Australian
		val brand_uri: String, //australian
		val frequency: Int, //36843
		val festive_bbq: String, //Vegetables & Mushrooms
		val potluck: String //Vegetables
)

data class Promotion(
		val id: Int, //140635
		val type: Int, //3
		val savings_text: String, //Any 3 Save 10%
		val promo_label: String, //Buy Any 3 and SAVE 10%
		val current_product_group_id: Int, //1
		val products: List<InnerProducts>
)

data class InnerProducts(
		val images: List<InnerImage>,
		val group_id: Int, //1
		val min_qty: Int //3
)

data class InnerImage(
		val name: String ///i/m/9324152000086_0088_1441190571626.jpg
)

data class Img(
		val h: Int, //0
		val w: Int, //0
		val name: String, ///i/m/8885003328014_0005_1503458367278.jpg
		val position: Int //0
)

data class DescriptionFields(
		val secondary: List<Secondary>,
		val primary: List<Primary>
)

data class Primary(
		val name: String, //Country of Origin
		val content: String //Australia
)

data class Secondary(
		val name: String, //Storage Guidelines
		val content: String //Recommended to store at chilled temperature:1-3 degrees is highly perishable and should be transferred to cool storage immediately. Reduce stocking and practice good stock rotation to maintain freshness. Prolonged storage is not recommended.
)

data class Attributes(
		val dag: List<Any>
)

data class Warehouse(
		val measure: InnerMeasure
)

data class InnerMeasure(
		val vol_metric: String,
		val wt: Double, //300.0
		val wt_metric: String, //g
		val l: Double, //0.0
		val w: Double, //0.0
		val h: Double //0.0
)

data class ProductLife(
		val time: Int, //5
		val metric: String, //D
		val is_minimum: Boolean, //true
		val time_including_delivery: Int //6
)