package com.ipsoft.tocomsede.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ipsoft.tocomsede.core.model.ResultState
import com.ipsoft.tocomsede.core.model.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PreferencesDataStore(private val context: Context) {

    private val userName = stringPreferencesKey("user_name")
    private val userEmail = stringPreferencesKey("user_email")
    private val userPhone = stringPreferencesKey("user_phone")
    private val userPhotoUrl = stringPreferencesKey("user_photo_url")

    suspend fun storeUser(user: User): ResultState<Boolean> {
        context.preferencesDataStore.edit { preferences ->
            preferences[userName] = user.name
            preferences[userEmail] = user.email
            preferences[userPhone] = user.phone
            preferences[userPhotoUrl] = user.photoUrl ?: ""
        }
        return ResultState.Success(true)
    }

    suspend fun readUser(): ResultState<User?> {
        if (context.preferencesDataStore.data.first()[userName].isNullOrEmpty()) {
            return ResultState.Success(
                null
            )
        }
        val user = context.preferencesDataStore.data.map { preferences ->
            return@map ResultState.Success(
                User(
                    preferences[userName] ?: "",
                    preferences[userEmail] ?: "",
                    preferences[userPhone] ?: "",
                    preferences[userPhotoUrl] ?: ""
                )
            )
        }
        return user.first()
    }

    suspend fun deleteUser(): ResultState<Boolean> {
        context.preferencesDataStore.edit { preferences ->
            preferences[userName] = ""
            preferences[userEmail] = ""
            preferences[userPhone] = ""
            preferences[userPhotoUrl] = ""
        }
        return ResultState.Success(true)
    }

    private val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore("preferences")
}
