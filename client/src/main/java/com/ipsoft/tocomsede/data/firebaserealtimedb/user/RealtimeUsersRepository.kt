package com.ipsoft.tocomsede.data.firebaserealtimedb.user

import com.ipsoft.tocomsede.core.model.ResultState
import kotlinx.coroutines.flow.Flow

interface RealtimeUsersRepository {
    fun deleteUserNode(userId: String): Flow<ResultState<Boolean>>
}
