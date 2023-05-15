package com.ipsoft.tocomsede.utils.di

import com.ipsoft.tocomsede.data.firebaserealtimedb.address.RealtimeAddressRepository
import com.ipsoft.tocomsede.data.firebaserealtimedb.address.RealtimeAddressRepositoryImpl
import com.ipsoft.tocomsede.data.firebaserealtimedb.items.RealtimeItemRepository
import com.ipsoft.tocomsede.data.firebaserealtimedb.items.RealtimeItemRepositoryImpl
import com.ipsoft.tocomsede.data.firebaserealtimedb.orders.RealtimeOrdersRepository
import com.ipsoft.tocomsede.data.firebaserealtimedb.orders.RealtimeOrdersRepositoryImpl
import com.ipsoft.tocomsede.data.firebaserealtimedb.phone.RealtimePhoneRepository
import com.ipsoft.tocomsede.data.firebaserealtimedb.phone.RealtimePhoneRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RealtimeRepositoryModule {
    @Binds
    abstract fun providesRealtimeItemRepository(
        repo: RealtimeItemRepositoryImpl
    ): RealtimeItemRepository

    @Binds
    abstract fun providesRealtimeUserRepository(
        repo: RealtimeAddressRepositoryImpl
    ): RealtimeAddressRepository

    @Binds
    abstract fun providesRealtimePhoneRepository(
        repo: RealtimePhoneRepositoryImpl
    ): RealtimePhoneRepository

    @Binds
    abstract fun providesRealtimeOrdersRepository(
        repo: RealtimeOrdersRepositoryImpl
    ): RealtimeOrdersRepository
}
