package com.example.agristore.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.agristore.data.entities.Bill
import com.example.agristore.data.entities.BillItem

data class BillWithBillItemAndItem(
    @Embedded
    val bill: Bill,

    @Relation(entity = BillItem::class, parentColumn = "id", entityColumn = "billId")
    val billItemWithItems: List<BillItemWithItem>
)