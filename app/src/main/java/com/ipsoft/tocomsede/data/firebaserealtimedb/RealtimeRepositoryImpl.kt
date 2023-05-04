package com.ipsoft.tocomsede.data.firebaserealtimedb

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.ipsoft.tocomsede.core.model.Item
import com.ipsoft.tocomsede.utils.ResultState
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class RealtimeRepositoryImpl @Inject constructor(
    private val db: DatabaseReference,
) : RealtimeRepository {
    override fun getItems(): Flow<ResultState<List<Item>>> = callbackFlow {

        trySend(ResultState.Loading)

        val valueEvent = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.map {
                    it.getValue(Item::class.java)
                }
                trySend(ResultState.Success(items) as ResultState<List<Item>>)
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Failure(error.toException()))
            }

        }

        db.addValueEventListener(valueEvent)
        awaitClose {
            db.removeEventListener(valueEvent)
            close()
        }

    }
}