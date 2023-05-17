package com.ipsoft.tocomsede.data.firebaserealtimedb.orders

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.ipsoft.tocomsede.core.model.Order
import com.ipsoft.tocomsede.core.model.ResultState
import com.ipsoft.tocomsede.core.utils.UserInfo.userUid
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class RealtimeOrdersRepositoryImpl(
    private val dbReference: DatabaseReference
) : RealtimeOrdersRepository {

    private val ordersReference
        get() = dbReference.child("users").child(userUid ?: "").child("orders")

    override suspend fun getOrders(): Flow<ResultState<List<Order>>> = callbackFlow {
        trySend(ResultState.Loading)
        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orders = snapshot.children.map {
                    it.getValue(Order::class.java).apply { this?.id = it.key ?: "" }
                }
                trySend(ResultState.Success(orders.filterNotNull()))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Failure(error.toException()))
            }
        }
        ordersReference.addValueEventListener(valueEvent)
        awaitClose {
            ordersReference.removeEventListener(valueEvent)
            close()
        }
    }

    override suspend fun getOrderById(id: String): Flow<ResultState<Order?>> = callbackFlow {
        trySend(ResultState.Loading)
        val orderListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val order = snapshot.children.map {
                    it.getValue(Order::class.java).apply { this?.id = it.key ?: "" }
                }.find { it?.id == id }
                trySend(ResultState.Success(order))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Failure(error.toException()))
            }
        }

        ordersReference.addValueEventListener(orderListener)

        awaitClose {
            ordersReference.removeEventListener(orderListener)
            close()
        }
    }

    override suspend fun updateOrder(order: Order): Flow<ResultState<Order>> = callbackFlow {
        trySend(ResultState.Loading)
        ordersReference.child(order.id).setValue(order)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    trySend(ResultState.Success(order))
                } else {
                    trySend(
                        ResultState.Failure(
                            it.exception ?: Exception("Error updating order")
                        )
                    )
                }
            }
        awaitClose {
            close()
        }
    }
}
