package com.example.agriappadminpanel.Model

import androidx.annotation.NonNull

data class OrderedProductsModel(
    var productId : String ="",
    var orderedProductName : String? ="",
    var orderedProductQuantity : String ? = "1",
    var productSellingPrice : String ? = "",
    var productImage : String? =""
) {
}