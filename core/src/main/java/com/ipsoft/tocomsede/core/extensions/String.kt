package com.ipsoft.tocomsede.core.extensions

fun String.toCurrency(currency: String = "R$"): String =
    if (this.isNotEmpty() && this.toDoubleOrNull() != null) {
        val value = this.toDouble()
        val formattedValue = "%.2f".format(value)
        "$currency $formattedValue"
    } else {
        ""
    }

fun String.currencyToDouble(): Double {
    val value = this.replace("R$", "").replace(",", ".").trim()
    return value.toDoubleOrNull() ?: 0.0
}

fun String.isValidCep(): Boolean {
    val regex = Regex("^\\d{8}\$")
    return regex.matches(this)
}

fun String.isBrazilianPhone(): Boolean {
    val regex = Regex("^\\d{11}\$")
    return regex.matches(this)
}

fun String.digitsOnly(): String = this.replace("[^\\d]".toRegex(), "")

fun String.millisToDateTime(): String {
    val millis = this.toLong()
    val date = java.util.Date(millis)
    val sdf = java.text.SimpleDateFormat("HH:mm dd/MM/yyyy")
    return sdf.format(date)
}
