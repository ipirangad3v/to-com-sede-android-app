package com.ipsoft.tocomsede.utils.di

import com.google.firebase.database.DatabaseReference
import com.ipsoft.tocomsede.data.cart.CartRepository
import com.ipsoft.tocomsede.data.cart.CartRepositoryImpl
import com.ipsoft.tocomsede.utils.Cart
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
    fun providesCart(): Cart = Cart

    @Provides
    @Singleton
    fun providesCartRepository(cart: Cart, dbReference: DatabaseReference): CartRepository =
        CartRepositoryImpl(cart, dbReference)
}
