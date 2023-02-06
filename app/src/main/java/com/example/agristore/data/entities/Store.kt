package com.example.agristore.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Store(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "tel")
    val tel: String,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "logo_image_id")
    val logoImageId: Int,
    @ColumnInfo(name = "stamp_image_id")
    val stampImageId: Int,
    @ColumnInfo(name = "signature_image_id")
    val signatureImageId: Int
)
