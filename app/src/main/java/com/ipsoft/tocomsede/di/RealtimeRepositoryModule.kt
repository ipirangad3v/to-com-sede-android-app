package com.ipsoft.tocomsede.di

import com.ipsoft.tocomsede.data.firebaserealtimedb.RealtimeRepository
import com.ipsoft.tocomsede.data.firebaserealtimedb.RealtimeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RealtimeRepositoryModule {
    @Binds
    abstract fun providesRealtimeRepository(
        repo: RealtimeRepositoryImpl,
    ): RealtimeRepository

}