package com.example.agristore.data.entities

import androidx.room.*
import java.util.Date

@Entity(indices = [Index(value = ["billCode"], unique = true)])
data class Bill(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val billCode: String,
    val date:Date,
    val off: Long = 0,
    val payment:Long = 0,
    val customerId: Int
)


