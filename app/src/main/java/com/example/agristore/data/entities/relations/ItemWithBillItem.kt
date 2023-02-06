package com.example.agristore.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.agristore.data.entities.BillItem
import com.example.agristore.data.entities.Item

data class ItemWithBillItem(
    @Embedded
    val item: Item,
    @Relation(parentColumn = "id", entityColumn = "itemId")
    val billItem: List<BillItem>
)
