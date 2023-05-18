package com.ipsoft.tocomsedeadmin.data.orders

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.ipsoft.tocomsede.core.model.FirebaseToComSedeUser
import com.ipsoft.tocomsede.core.model.Order
import com.ipsoft.tocomsede.core.model.ResultState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class OrdersRepositoryImpl @Inject constructor(
    private val dbReference: DatabaseReference
) : OrdersRepository {

    val orders: MutableList<Order> =
        mutableListOf()

    private val ordersReference
        get() = dbReference.child("users")

    override suspend fun getOrders(): Flow<ResultState<List<Order>>> = callbackFlow {
        trySend(ResultState.Loading)
        val ordersListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(FirebaseToComSedeUser::class.java)
                    if (user?.orders != null) {
                        user.orders?.forEach { order ->
                            orders.add(order.value.copy(id = order.key))
                        }
                    }
                }
                trySend(ResultState.Success(orders))
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Lida com erros de leitura do banco de dados
                println("Erro ao ler as ordens: ${databaseError.message}")
            }
        }

        ordersReference.addValueEventListener(ordersListener)

        awaitClose {
            ordersReference.removeEventListener(ordersListener)
            close()
        }
    }

    override suspend fun getOrderById(id: String): Flow<ResultState<Order?>> = callbackFlow {
        trySend(ResultState.Loading)
        val orderListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(FirebaseToComSedeUser::class.java)
                    if (user?.orders != null) {
                        user.orders?.forEach { order ->
                            if (order.key == id) {
                                trySend(ResultState.Success(order.value.copy(id = order.key)))
                            }
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Lida com erros de leitura do banco de dados
                println("Erro ao ler as ordens: ${databaseError.message}")
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
        ordersReference.child(order.user?.uid!!).child("orders").child(order.id).setValue(order)
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
