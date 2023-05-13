package com.ipsoft.tocomsede.data.firebaserealtimedb.address

import com.google.firebase.database.DatabaseReference
import com.ipsoft.tocomsede.core.exception.UserNotLoggedException
import com.ipsoft.tocomsede.core.model.Address
import com.ipsoft.tocomsede.core.model.ResultState
import com.ipsoft.tocomsede.utils.UserInfo.userUid
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RealtimeAddressRepositoryImpl @Inject constructor(dbReference: DatabaseReference) :
    RealtimeAddressRepository {

    private val userReference = dbReference.child("users").child(userUid ?: "")

    override suspend fun saveAddress(address: Address): Flow<ResultState<Boolean>> = callbackFlow {
        trySend(ResultState.Loading)
        if (userUid == null) {
            trySend(ResultState.Failure(UserNotLoggedException("User not logged")))
        } else {
            userReference.child("addresses").push().setValue(address)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        trySend(ResultState.Success(true))
                    } else {
                        trySend(
                            ResultState.Failure(
                                it.exception ?: Exception("Error saving address")
                            )
                        )
                    }
                }.addOnFailureListener { ResultState.Failure(it) }
        }
        awaitClose {
            close()
        }
    }
}
