package com.example.agristore.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.agristore.data.entities.BillItem
import com.example.agristore.data.entities.Item

data class BillItemWithItem(
    @Embedded
    val billItem: BillItem,
    @Relation(parentColumn = "itemId", entityColumn = "id")
    val item: Item)