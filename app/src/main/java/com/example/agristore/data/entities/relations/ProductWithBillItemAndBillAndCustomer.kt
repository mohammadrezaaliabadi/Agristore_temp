package com.example.agristore.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.agristore.data.entities.BillItem
import com.example.agristore.data.entities.Product

data class ProductWithBillItemAndBillAndCustomer(
    @Embedded
    val product: Product,
    @Relation(entity = BillItem::class, parentColumn = "id", entityColumn = "itemId")
    val billItemWithBillAndCustomers: List<BillItemWithBillAndCustomer>
)