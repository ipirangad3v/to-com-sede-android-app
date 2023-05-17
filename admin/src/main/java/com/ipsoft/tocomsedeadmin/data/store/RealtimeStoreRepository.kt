package com.ipsoft.tocomsedeadmin.data.store

import com.ipsoft.tocomsede.core.model.ResultState
import com.ipsoft.tocomsede.core.model.Store
import kotlinx.coroutines.flow.Flow

interface RealtimeStoreRepository {
    suspend fun getStore(): Flow<ResultState<Store>>
    suspend fun updateStore(store: Store): Flow<ResultState<Store>>
}
