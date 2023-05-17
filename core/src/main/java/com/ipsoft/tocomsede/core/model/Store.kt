package com.ipsoft.tocomsede.core.model

data class Store(
    val open: Boolean,
    val defaultDeliveryFee: Double,
    val payments: List<PaymentMethod>,
    val timeForAutoCancellationInMillis: Long = 300000,//5 minutes
) {
    @Suppress("unused")
    constructor() : this(
        open = false,
        defaultDeliveryFee = 0.0,
        payments = emptyList()
    )

    fun toFirebaseStore() = FirebaseStore(
        defaultDeliveryFee = this.defaultDeliveryFee,
        open = this.open,
        payments = FirebaseStore.Payments(
            CREDIT_CARD = this.payments.contains(PaymentMethod.CREDIT_CARD),
            DEBIT_CARD = this.payments.contains(PaymentMethod.DEBIT_CARD),
            MONEY = this.payments.contains(PaymentMethod.MONEY),
            PIX = this.payments.contains(PaymentMethod.PIX),
        )
    )
}
