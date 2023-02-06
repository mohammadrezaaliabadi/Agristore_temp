package com.example.agristore.model

data class BillSendModel(
    var customerTel: String = "",
    val billCode: String,
    val billDate: String,
    val billOff: String,
    val billPayment: String,
    val billTotalItemPrice: String,
    val billTotal: String
)
