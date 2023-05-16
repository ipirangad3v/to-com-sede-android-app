package com.ipsoft.tocomsede.data.firebaserealtimedb.store

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
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
                val isOpen = snapshot.child("isOpen").getValue(Boolean::class.java)
                val defaultDeliveryFee =
                    snapshot.child("defaultDeliveryFee").getValue(Double::class.java)

                trySend(ResultState.Success(Store(isOpen ?: false, defaultDeliveryFee ?: 0.0)))
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
}
