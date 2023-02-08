package com.example.agristore.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import java.text.NumberFormat
@Entity
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String,
    val price: Long,
    val quantity: Long,
    val description:String,
    val off:Long = 0
)

@Entity(tableName = "item_fts")
@Fts4(contentEntity = Item::class)
data class ItemFTS(
    @PrimaryKey
    @ColumnInfo(name = "rowid")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "description")
    val description:String,
)
//@Entity
//data class Item(
//    @PrimaryKey(autoGenerate = true)
//    val id: Int,
//    val name: String,
//    val price: Long,
//    val itemOff: Long,
//    val quantity: Long,
//    val productionDate: Date,
//    val expirationDate: Date,
//    val productId: Int,
//    val companyId: Int,
//    val itemUnitId: Int,
//    var imageId: String
//)

/**
 * Returns the passed in price in currency format.
 */
fun Item.getFormattedPrice(): String =
    NumberFormat.getCurrencyInstance().format(price)

fun Item.getFormattedOff(): String =
    NumberFormat.getCurrencyInstance().format(off)
fun Long.getLocationFormat(): String =
    NumberFormat.getInstance().format(this)