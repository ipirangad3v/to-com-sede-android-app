package com.ipsoft.tocomsede.data.address

import com.ipsoft.tocomsede.core.model.Address

interface AddressRepository {

    suspend fun saveAddress(address: Address)
}
