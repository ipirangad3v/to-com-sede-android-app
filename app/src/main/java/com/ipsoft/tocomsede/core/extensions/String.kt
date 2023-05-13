package com.ipsoft.tocomsede.core.extensions

fun String.toCurrency(currency: String = "R$"): String =
    if (this.isNotEmpty() && this.toDoubleOrNull() != null) {
        val value = this.toDouble()
        val formattedValue = "%.2f".format(value)
        "$currency $formattedValue"
    } else {
        ""
    }

fun String.isValidCep(): Boolean {
    val regex = Regex("^\\d{8}\$")
    return regex.matches(this)
}
