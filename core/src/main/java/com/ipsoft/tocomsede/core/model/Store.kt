package com.ipsoft.tocomsede.core.model

import java.util.Date

data class Store(
    val isOpen: Boolean,
    val defaultDeliveryFee: Double,
) {
    @Suppress("unused")
    constructor() : this(
        isOpen = false,
        defaultDeliveryFee = 0.0
    )
}