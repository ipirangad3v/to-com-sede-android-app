package com.ipsoft.tocomsede.core.model

import java.util.Date

data class Store(
    val isOpen: Boolean,
    val defaultDeliveryFee: Double,
    val payments: List<PaymentMethod>,
) {
    @Suppress("unused")
    constructor() : this(
        isOpen = false,
        defaultDeliveryFee = 0.0,
        payments = emptyList()
    )
}