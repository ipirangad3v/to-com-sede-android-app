package com.ipsoft.tocomsede.core.model

data class Item(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val category: String,
    val quantity: Int
) {
    @Suppress("unused")
    constructor() : this(0, "", "", 0.0, "", "", 0)

    val isAvailable: Boolean
        get() = quantity > 0
}
