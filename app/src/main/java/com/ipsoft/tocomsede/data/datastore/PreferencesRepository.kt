package com.ipsoft.tocomsede.data.datastore

import com.ipsoft.tocomsede.core.model.ResultState
import com.ipsoft.tocomsede.core.model.User
import javax.inject.Inject

class PreferencesRepository @Inject constructor(private val preferencesDataStore: PreferencesDataStore) {
    suspend fun readUser(): ResultState<User?> = preferencesDataStore.readUser()

    suspend fun storeUser(user: User): ResultState<Boolean> =
        preferencesDataStore.storeUser(user)
}
