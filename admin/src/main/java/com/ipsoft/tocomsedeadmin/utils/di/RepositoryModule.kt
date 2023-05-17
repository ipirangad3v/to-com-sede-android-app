package com.ipsoft.tocomsedeadmin.utils.di

import com.google.firebase.database.DatabaseReference
import com.ipsoft.tocomsedeadmin.data.orders.OrdersRepository
import com.ipsoft.tocomsedeadmin.data.orders.OrdersRepositoryImpl
import com.ipsoft.tocomsedeadmin.data.store.RealtimeStoreRepository
import com.ipsoft.tocomsedeadmin.data.store.RealtimeStoreRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun providesOrdersRepository(dbReference: DatabaseReference): OrdersRepository =
        OrdersRepositoryImpl(dbReference)

    @Provides
    fun providesStoreRepository(dbReference: DatabaseReference): RealtimeStoreRepository =
        RealtimeStoreRepositoryImpl(dbReference)
}
