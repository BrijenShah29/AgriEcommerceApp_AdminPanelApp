package com.example.agriappadminpanel.Model

data class AddProductModel(
    val productName : String? = "",
    val productDescription : String? ="",
    val productCoverImg : String? ="",
    val productCategory: String? ="",
    val productSubCategory : String? ="",
    val productId : String? = "",
    val productPrice : String? = "",
    val discountRate : String? = "",
    val stock : String? = "",
    val onSale : Boolean? = false,
    val productSpecialPrice : String? = "",
    val productScale : String? = "",
    val productImages : ArrayList<String> = ArrayList()
)
