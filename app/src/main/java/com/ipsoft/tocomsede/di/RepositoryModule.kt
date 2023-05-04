package com.ipsoft.tocomsede.di

import com.ipsoft.tocomsede.data.firebaserealtimedb.RealtimeRepository
import com.ipsoft.tocomsede.data.firebaserealtimedb.RealtimeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun providesRealtimeRepository(
        repo: RealtimeRepositoryImpl,
    ): RealtimeRepository
}