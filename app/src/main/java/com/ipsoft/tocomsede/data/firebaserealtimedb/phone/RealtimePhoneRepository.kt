package com.ipsoft.tocomsede.data.firebaserealtimedb.phone

import com.ipsoft.tocomsede.core.model.ResultState
import kotlinx.coroutines.flow.Flow

interface RealtimePhoneRepository {

    suspend fun savePhone(phone: String): Flow<ResultState<Boolean>>
    suspend fun getPhone(): Flow<ResultState<String>>
}
