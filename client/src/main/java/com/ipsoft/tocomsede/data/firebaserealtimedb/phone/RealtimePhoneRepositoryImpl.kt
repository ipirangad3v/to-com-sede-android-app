package com.ipsoft.tocomsede.data.firebaserealtimedb.phone

import com.google.firebase.database.DatabaseReference
import com.ipsoft.tocomsede.core.model.ResultState
import com.ipsoft.tocomsede.core.utils.UserInfo
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RealtimePhoneRepositoryImpl @Inject constructor(private val dbReference: DatabaseReference) :
    RealtimePhoneRepository {

    private val userReference
        get() = dbReference.child("users").child(UserInfo.userUid ?: "")

    override suspend fun savePhone(phone: String): Flow<ResultState<Boolean>> = callbackFlow {
        val phoneReference = userReference.child("phone")
        phoneReference.setValue(phone).addOnCompleteListener {
            if (it.isSuccessful) {
                trySend(ResultState.Success(true))
            } else {
                trySend(
                    ResultState.Failure(
                        it.exception ?: Exception("Error saving phone")
                    )
                )
            }
        }
        awaitClose {
            close()
        }
    }

    override suspend fun getPhone(): Flow<ResultState<String?>> = callbackFlow {
        val phoneReference = userReference.child("phone")
        phoneReference.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val phone = it.result?.value as? String
                trySend(ResultState.Success(phone))
            } else {
                trySend(
                    ResultState.Failure(
                        it.exception ?: Exception("Error getting phone")
                    )
                )
            }
        }
        awaitClose {
            close()
        }
    }
}
