package com.ipsoft.tocomsede.utils.di

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ipsoft.tocomsede.data.firebaserealtimedb.orders.RealtimeOrdersRepositoryImpl
import com.ipsoft.tocomsede.data.firebaserealtimedb.user.RealtimeUsersRepository
import com.ipsoft.tocomsede.data.firebaserealtimedb.user.RealtimeUsersRepositoryImpl
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
    fun providesDatabaseReference(database: FirebaseDatabase) = database.reference

    @Provides
    fun providesRealtimeOrdersRepository(
        dbReference: DatabaseReference
    ) = RealtimeOrdersRepositoryImpl(dbReference)

    @Provides
    fun providesRealtimeUsersRepository(
        dbReference: DatabaseReference
    ): RealtimeUsersRepository = RealtimeUsersRepositoryImpl(dbReference)
}
