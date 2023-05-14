package com.ipsoft.tocomsede.utils.di

import com.ipsoft.tocomsede.core.util.Constants.CEP_BASE_URL
import com.ipsoft.tocomsede.core.util.Constants.DEBUG
import com.ipsoft.tocomsede.core.util.Constants.SOCKET_TIMEOUT
import com.ipsoft.tocomsede.data.cep.CepRepository
import com.ipsoft.tocomsede.data.cep.CepRepositoryImpl
import com.ipsoft.tocomsede.data.cep.CepService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CepModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(CEP_BASE_URL)
        .client(createClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun createCepService(retrofit: Retrofit): CepService = retrofit.create(
        CepService::class.java
    )

    @Provides
    @Singleton
    fun createCepRepository(cepService: CepService): CepRepository = CepRepositoryImpl(cepService)

    private fun createClient(): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        if (DEBUG) {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
        }
        okHttpClientBuilder.connectTimeout(SOCKET_TIMEOUT, TimeUnit.MILLISECONDS)
        okHttpClientBuilder.readTimeout(SOCKET_TIMEOUT, TimeUnit.MILLISECONDS)

        return okHttpClientBuilder.build()
    }
}
