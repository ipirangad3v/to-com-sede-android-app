package com.ipsoft.tocomsede.home.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ipsoft.tocomsede.base.model.Item
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class HomeViewModel @Inject constructor(firebaseDatabase: FirebaseDatabase) :
    ViewModel() {

    private val itemList = mutableListOf<Item>()
    private var _homeState = MutableStateFlow(HomeState(isLoading = true, itemList))
    val homeState
        get() = _homeState


    private val items = firebaseDatabase.getReference("items")


    private fun getItems() {
        items.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(Item::class.java)
                    item?.let {
                        itemList.add(it)
                        _homeState.update { HomeState(isLoading = false, itemList) }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("onDataChange", "Failed to read value.", error.toException())
            }
        })
    }

    init {
        getItems()
    }

}