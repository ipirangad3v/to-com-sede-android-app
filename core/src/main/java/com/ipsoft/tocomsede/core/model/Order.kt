package com.ipsoft.tocomsede.core.model

import com.ipsoft.tocomsede.core.model.PaymentMethod.MONEY
import java.util.Date

data class Order(
    val items: List<Item>,
    val dateInMillis: String = Date().time.toString(),
    var status: OrderStatus = OrderStatus.PENDING,
    val address: Address? = null,
    val paymentMethod: PaymentMethod = MONEY,
    val change: Change = Change(),
    var id: String = "",
    val user: User? = null,
) {
    @Suppress("unused")
    constructor() : this(
        items = emptyList(),
        dateInMillis = Date().time.toString(),
        status = OrderStatus.PENDING,
        user = null,
    )

    val total
        get() = items.sumOf { it.selectedQuantity * it.price }
}
