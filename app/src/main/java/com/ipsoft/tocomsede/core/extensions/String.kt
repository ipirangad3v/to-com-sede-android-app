package com.ipsoft.tocomsede.core.extensions

fun String.toCurrency(currency: String = "R$"): String = "$currency $this"
