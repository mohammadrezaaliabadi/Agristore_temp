package com.example.agristore.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.agristore.data.entities.Bill
import com.example.agristore.data.entities.Customer

data class BillAndCustomer(
    @Embedded
    val bill: Bill,
    @Relation(parentColumn = "customerId", entityColumn = "id")
    val customer: Customer
)