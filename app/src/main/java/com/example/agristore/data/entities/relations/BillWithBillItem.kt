package com.example.agristore.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.agristore.data.entities.Bill
import com.example.agristore.data.entities.BillItem

data class BillWithBillItem(
    @Embedded
    val bill: Bill,
    @Relation(parentColumn = "id", entityColumn = "billId")
    val billItems: List<BillItem>
)