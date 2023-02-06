package com.example.agristore.data.entities

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class Image(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "title")
    val title:String,
    @ColumnInfo(name = "bitmap", typeAffinity = ColumnInfo.BLOB)
    val bitmap: Bitmap

)
