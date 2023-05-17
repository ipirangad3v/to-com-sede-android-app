package com.ipsoft.tocomsede.data.firebaserealtimedb.user

import com.google.firebase.database.DatabaseReference
import com.ipsoft.tocomsede.core.model.ResultState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class RealtimeUsersRepositoryImpl(private val dbReference: DatabaseReference) : RealtimeUsersRepository {
    override fun deleteUserNode(userId: String): Flow<ResultState<Boolean>> = callbackFlow {
        val db = dbReference.child("users").child(userId)
        db.removeValue { error, _ ->
            if (error == null) {
                trySend(ResultState.Success(true))
            } else {
                trySend(ResultState.Failure(error.toException()))
            }
        }
        awaitClose { close() }
    }
}
