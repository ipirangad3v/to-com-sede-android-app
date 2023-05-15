package com.ipsoft.tocomsede.core.model

data class CepResponse(
    val cep: String,
    val city: String,
    val location: Location,
    val neighborhood: String,
    val state: String,
    val street: String
) {
    data class Location(
        val coordinates: Coordinates,
        val type: String
    ) {
        data class Coordinates(
            val latitude: String,
            val longitude: String
        )
    }
}
