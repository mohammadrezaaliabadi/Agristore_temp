package com.example.agristore.data.entities

import androidx.room.*

@Entity(indices = [Index(value = ["national_number"], unique = true)])
data class Customer(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "first_name")
    val firstName: String,
    @ColumnInfo(name = "last_name")
    val lastName: String,
    @ColumnInfo(name = "national_number")
    val nationalNumber: Long,
    @ColumnInfo(name = "tel")
    val tel: String,
    @ColumnInfo(name = "email")
    val email: String = "",
    @ColumnInfo(name = "image_id")
    val imageId: Int = 0
)

@Entity(tableName = "customer_fts")
@Fts4(contentEntity = Customer::class)
data class CustomerFTS(
    @PrimaryKey
    @ColumnInfo(name = "rowid")
    val id: Int,
    @ColumnInfo(name = "first_name")
    val firstName: String,
    @ColumnInfo(name = "last_name")
    val lastName: String,
    @ColumnInfo(name = "national_number")
    val nationalNumber: Long,
    @ColumnInfo(name = "tel")
    val tel: String,
    @ColumnInfo(name = "email")
    val email: String

)

