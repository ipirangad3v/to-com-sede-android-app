package com.ipsoft.tocomsede.data.firebaserealtimedb.items

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.ipsoft.tocomsede.core.model.Category
import com.ipsoft.tocomsede.core.model.Item
import com.ipsoft.tocomsede.core.model.ResultState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RealtimeItemRepositoryImpl @Inject constructor(
    private val db: DatabaseReference
) : RealtimeItemRepository {

    private val itemReference = db.child("items")
    override suspend fun getItems(): Flow<ResultState<List<Category>>> = callbackFlow {
        trySend(ResultState.Loading)

        val valueEvent = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.map {
                    it.getValue(Item::class.java)
                }

                val categories = items.groupBy { it?.category }.map {
                    Category(it.key, it.value.filterNotNull())
                }

                trySend(ResultState.Success(categories))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Failure(error.toException()))
            }
        }

        itemReference.addValueEventListener(valueEvent)
        awaitClose {
            itemReference.removeEventListener(valueEvent)
            close()
        }
    }

    override suspend fun getItemById(itemId: Int): Flow<ResultState<Item?>> = callbackFlow {
        trySend(ResultState.Loading)

        val valueEvent = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.map {
                    it.getValue(Item::class.java)
                }
                trySend(ResultState.Success(items.find { it?.id == itemId }))
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
