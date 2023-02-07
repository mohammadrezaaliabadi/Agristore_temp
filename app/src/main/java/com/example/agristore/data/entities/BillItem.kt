package com.example.agristore.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BillItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val billId: Long,
    val itemId: Int,
    val quantity: Long,
    val price:Long,
    val off:Long
)
