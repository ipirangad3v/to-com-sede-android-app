package com.ipsoft.tocomsede.core.model

data class FirebaseToComSedeUser(
    val addresses: Map<String, Address>? = null,
    val orders: Map<String, Order>? = null,
    val userUid: String = "",
) {
    @Suppress("unused")
    constructor() : this(addresses = null, orders = null)
}