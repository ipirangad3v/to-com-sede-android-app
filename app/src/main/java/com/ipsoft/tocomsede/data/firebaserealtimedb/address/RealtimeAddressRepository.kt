package com.ipsoft.tocomsede.data.firebaserealtimedb.address

import com.ipsoft.tocomsede.core.model.Address
import com.ipsoft.tocomsede.core.model.ResultState
import kotlinx.coroutines.flow.Flow

interface RealtimeAddressRepository {
    suspend fun saveAddress(address: Address): Flow<ResultState<Boolean>>
    suspend fun getAddresses(): Flow<ResultState<List<Address>>>
    suspend fun deleteAddress(address: Address): Flow<ResultState<Boolean>>
    suspend fun updateAddress(address: Address): Flow<ResultState<Boolean>>
}
