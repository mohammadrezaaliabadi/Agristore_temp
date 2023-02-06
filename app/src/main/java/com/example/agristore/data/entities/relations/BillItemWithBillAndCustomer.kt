package com.example.agristore.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.agristore.data.entities.Bill
import com.example.agristore.data.entities.BillItem

data class BillItemWithBillAndCustomer(
    @Embedded
    val billItem: BillItem,
    @Relation(entity = Bill::class, parentColumn = "billId", entityColumn = "id")
    val billAndCustomer: BillAndCustomer
)


