package com.ipsoft.tocomsede.core.model

import java.util.Date

data class Order(
    val items: List<Item>,
    val dateInMillis: String = Date().time.toString(),
    val status: OrderStatus = OrderStatus.PENDING
) {
    @Suppress("unused")
    constructor() : this(
        items = emptyList(),
        dateInMillis = Date().time.toString(),
        status = OrderStatus.PENDING
    )

    val total
        get() = items.sumOf { it.selectedQuantity * it.price }
}
