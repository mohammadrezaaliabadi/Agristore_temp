package com.example.agristore.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.agristore.data.entities.Bill
import com.example.agristore.data.entities.Customer

data class CustomerWithBillAndBillItem(
    @Embedded
    val customer: Customer,
    @Relation(entity = Bill::class, parentColumn = "id", entityColumn ="customerId" )
    val billWithBillItemAndItems: List<BillWithBillItemAndItem>
)

