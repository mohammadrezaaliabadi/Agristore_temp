package com.example.agristore.data.entities

import androidx.room.*

@Entity
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "description")
    val description: String,
    val price: Long,
    val itemOff: Long,
    val quantity: Long,
)

@Entity(tableName = "product_fts")
@Fts4(contentEntity = Product::class)
data class ProductFTS(
    @PrimaryKey
    @ColumnInfo(name = "rowid")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "description")
    val description: String
)

/**
 * Returns the passed in price in currency format.
 */