package com.ipsoft.tocomsede.data.firebaserealtimedb.orders

import com.ipsoft.tocomsede.core.model.Order
import com.ipsoft.tocomsede.core.model.ResultState
import kotlinx.coroutines.flow.Flow

interface RealtimeOrdersRepository {
    suspend fun getOrders(): Flow<ResultState<List<Order>>>
    suspend fun getOrderById(id: String): Flow<ResultState<Order?>>
    suspend fun updateOrder(order: Order): Flow<ResultState<Order>>
}
