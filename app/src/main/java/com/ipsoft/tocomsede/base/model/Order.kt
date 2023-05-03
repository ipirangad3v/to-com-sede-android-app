package com.ipsoft.tocomsede.base.model

import java.util.Date

data class Order(
    val id: Int,
    val items: List<Item>,
    val date: Date,
    val price: Double,
)
