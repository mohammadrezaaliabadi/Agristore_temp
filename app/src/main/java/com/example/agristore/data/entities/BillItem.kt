package com.example.agristore.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.NumberFormat

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

/**
 * Returns the passed in price in currency format.
 */
fun BillItem.getFormattedPrice(): String =
    NumberFormat.getCurrencyInstance().format(price)

fun BillItem.getFormattedOff(): String =
    NumberFormat.getCurrencyInstance().format(off)

fun Long.getFormattedCurrency():String = NumberFormat.getCurrencyInstance().format(this)
