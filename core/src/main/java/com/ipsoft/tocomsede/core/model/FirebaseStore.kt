package com.ipsoft.tocomsede.core.model

data class FirebaseStore(
    val defaultDeliveryFee: Double,
    val open: Boolean,
    val payments: Payments,
) {
    data class Payments(
        val CREDIT_CARD: Boolean,
        val DEBIT_CARD: Boolean,
        val MONEY: Boolean,
        val PIX: Boolean,
    )
}