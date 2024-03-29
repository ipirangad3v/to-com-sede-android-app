package com.ipsoft.tocomsedeadmin.data.store

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.ipsoft.tocomsede.core.model.PaymentMethod
import com.ipsoft.tocomsede.core.model.ResultState
import com.ipsoft.tocomsede.core.model.Store
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RealtimeStoreRepositoryImpl @Inject constructor(
    private val dbReference: DatabaseReference
) : RealtimeStoreRepository {

    private val storeRef
        get() = dbReference.child("store")

    override suspend fun getStore(): Flow<ResultState<Store>> = callbackFlow {
        val valueEvent = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val isOpen = snapshot.child("open").getValue(Boolean::class.java)
                val defaultDeliveryFee =
                    snapshot.child("defaultDeliveryFee").getValue(Double::class.java)

                val paymentsMap: Map<String, Boolean>? =
                    snapshot.child("payments").getValue<Map<String, Boolean>>()

                val payments: List<PaymentMethod> = paymentsMap?.entries?.filter { it.value }?.map {
                    when (it.key) {
                        "credit_CARD" -> PaymentMethod.CREDIT_CARD
                        "debit_CARD" -> PaymentMethod.DEBIT_CARD
                        "money" -> PaymentMethod.MONEY
                        "pix" -> PaymentMethod.PIX
                        else -> PaymentMethod.MONEY
                    }
                } ?: emptyList()

                trySend(
                    ResultState.Success(
                        Store(isOpen ?: false, defaultDeliveryFee ?: 0.0, payments)
                    )
                )
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Failure(error.toException()))
            }
        }

        storeRef.addValueEventListener(valueEvent)
        awaitClose {
            storeRef.removeEventListener(valueEvent)
            close()
        }
    }

    override suspend fun updateStore(store: Store): Flow<ResultState<Store>> = callbackFlow {
        storeRef.setValue(store.toFirebaseStore()).addOnCompleteListener {
            if (it.isSuccessful) {
                trySend(ResultState.Success(store))
            } else {
                trySend(
                    ResultState.Failure(
                        it.exception ?: Exception("Error saving Store")
                    )
                )
            }
        }
        awaitClose {
            close()
        }
    }
}
