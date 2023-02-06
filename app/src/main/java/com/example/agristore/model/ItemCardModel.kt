package com.example.agristore.model

import com.example.agristore.data.entities.Item

data class ItemCardModel (
    val item:Item,
    var quantity:Long
)
