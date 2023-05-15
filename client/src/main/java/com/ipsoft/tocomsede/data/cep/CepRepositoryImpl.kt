package com.ipsoft.tocomsede.data.cep

import com.ipsoft.tocomsede.core.model.CepResponse
import com.ipsoft.tocomsede.core.model.ResultState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class CepRepositoryImpl @Inject constructor(private val api: CepService) : CepRepository {

    override suspend fun getCep(cep: String): Flow<ResultState<CepResponse>> = callbackFlow {
        trySend(ResultState.Loading)
        try {
            val response = api.getCep(cep)

            trySend(ResultState.Success(response))
        } catch (e: Exception) {
            trySend(ResultState.Failure(e))
        }

        awaitClose { close() }
    }
}
