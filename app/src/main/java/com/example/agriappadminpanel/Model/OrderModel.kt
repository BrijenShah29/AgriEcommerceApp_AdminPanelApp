package com.example.agriappadminpanel.Model


import com.example.agriappadminpanel.Model.OrderedProductsModel


data class OrderModel(
    val orderId : String?= "",
    val customerId : String?="",
    val paymentStatus : String?= "",
    val orderedDate : Long?= 0,
    val orderAmount : String? = "",
    val shipmentStatus : String? ="",
    val shippingDate : String? = "",
    val shipmentAddress : String? ="",
    val shipmentReceiverName : String? = "",
    val shipmentReceiverEmail : String?="",
    val customerPhoneNumber : String? ="",
    val customerName : String?="",
    val orderedProducts : ArrayList<OrderedProductsModel>? = ArrayList()
)
