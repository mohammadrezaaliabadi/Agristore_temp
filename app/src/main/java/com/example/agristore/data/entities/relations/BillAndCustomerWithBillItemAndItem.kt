package com.example.agristore.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.agristore.data.entities.BillItem

data class BillAndCustomerWithBillItemAndItem(
    @Embedded
    val billAndCustomer: BillAndCustomer,
    @Relation(entity = BillItem::class, parentColumn = "id", entityColumn = "billId")
    val billItemWithItems: List<BillItemWithItem>
)
