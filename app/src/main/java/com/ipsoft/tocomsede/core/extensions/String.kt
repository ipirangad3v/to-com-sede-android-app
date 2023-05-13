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
    val cepRegex = Regex("^\\d{5}-\\d{3}$")
    return cepRegex.matches(this)
}

fun String.CepDigitsOnly(): String = this.replace(Regex("[^0-9]"), "")
