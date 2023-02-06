package com.example.agristore.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "item_unit")
data class ItemUnit(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
)