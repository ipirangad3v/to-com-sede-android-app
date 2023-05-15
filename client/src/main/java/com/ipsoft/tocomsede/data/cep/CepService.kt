package com.ipsoft.tocomsede.data.cep

import com.ipsoft.tocomsede.core.model.CepResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CepService {
    @GET("{cep}")
    suspend fun getCep(@Path("cep") cep: String): CepResponse
}
