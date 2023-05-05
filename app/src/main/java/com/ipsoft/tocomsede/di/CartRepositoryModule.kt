package com.ipsoft.tocomsede.di

import com.ipsoft.tocomsede.data.cart.CartRepository
import com.ipsoft.tocomsede.data.cart.CartRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CartRepositoryModule {

    @Provides
    @Singleton
    fun providesCartRepository(): CartRepository = CartRepositoryImpl()
}