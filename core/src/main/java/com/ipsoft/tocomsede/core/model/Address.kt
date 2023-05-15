package com.ipsoft.tocomsede.core.model

data class Address(
    val city: String,
    val state: String,
    val zipCode: String,
    val street: String,
    val number: String,
    val complement: String,
    val neighborhood: String,
    var id: String,
    var isFavorite: Boolean
) {
    @Suppress("unused")
    constructor() : this("", "", "", "", "", "", "", id = "", isFavorite = false)

    override fun toString(): String {
        return "$street, $number \n $neighborhood \n $city - $state, \n ${zipCode.format("#####-###")}"
    }
}
