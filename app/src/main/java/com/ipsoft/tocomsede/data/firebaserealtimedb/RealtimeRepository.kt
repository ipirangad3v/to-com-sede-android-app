package com.ipsoft.tocomsede.data.firebaserealtimedb

import com.ipsoft.tocomsede.core.model.Item
import com.ipsoft.tocomsede.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface RealtimeRepository {
    fun getItems(): Flow<ResultState<List<Item>>>
}