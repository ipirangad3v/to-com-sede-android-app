package com.ipsoft.tocomsedeadmin.data.items

import com.ipsoft.tocomsede.core.model.Category
import com.ipsoft.tocomsede.core.model.Item
import com.ipsoft.tocomsede.core.model.ResultState
import kotlinx.coroutines.flow.Flow

interface RealtimeItemRepository {
    suspend fun getItems(): Flow<ResultState<List<Category>>>
    suspend fun getItemById(itemId: Int): Flow<ResultState<Item?>>
}
