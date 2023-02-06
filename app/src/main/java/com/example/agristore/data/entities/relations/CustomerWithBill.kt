package com.example.agristore.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.agristore.data.entities.Bill
import com.example.agristore.data.entities.Customer

data class CustomerWithBill(
    @Embedded
    val customer: Customer,
    @Relation(parentColumn = "id", entityColumn = "customerId")
    val bills: List<Bill>
)