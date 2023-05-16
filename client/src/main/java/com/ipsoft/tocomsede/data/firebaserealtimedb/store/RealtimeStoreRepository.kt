package com.ipsoft.tocomsede.data.firebaserealtimedb.store

import com.ipsoft.tocomsede.core.model.ResultState
import com.ipsoft.tocomsede.core.model.Store
import kotlinx.coroutines.flow.Flow

interface RealtimeStoreRepository {
    suspend fun getStore(): Flow<ResultState<Store>>
}
