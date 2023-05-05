package com.ipsoft.tocomsede.di

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    fun providesFirebaseRealtimeDatabase() = Firebase.database

    @Provides
    fun providesItemsReference(database: FirebaseDatabase) = database.getReference("items")
}
