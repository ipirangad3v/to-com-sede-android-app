package com.ipsoft.tocomsede.core.model

import java.util.Date

data class Order(
    val id: Int,
    val items: List<Item>,
    val date: Date,
    val price: Double,
    val client: User
)
