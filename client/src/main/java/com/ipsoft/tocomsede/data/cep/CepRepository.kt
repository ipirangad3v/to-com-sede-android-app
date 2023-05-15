package com.ipsoft.tocomsede.data.cep

import com.ipsoft.tocomsede.core.model.CepResponse
import com.ipsoft.tocomsede.core.model.ResultState
import kotlinx.coroutines.flow.Flow

interface CepRepository {
    suspend fun getCep(cep: String): Flow<ResultState<CepResponse>>
}
