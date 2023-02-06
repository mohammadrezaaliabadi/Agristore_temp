package com.example.agristore.model

import com.example.agristore.data.entities.Bill
import com.example.agristore.data.entities.BillItem
import com.example.agristore.data.entities.Customer
import com.example.agristore.data.entities.Item

data class BillModel(
    var customer: Customer,
    var bill: Bill,
    var items: List<Item>,
    var billItems: List<BillItem>
)
