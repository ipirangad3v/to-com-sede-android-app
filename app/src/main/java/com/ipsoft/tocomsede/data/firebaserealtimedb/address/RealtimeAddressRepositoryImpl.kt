package com.ipsoft.tocomsede.data.firebaserealtimedb.address

import com.google.firebase.database.DatabaseReference
import com.ipsoft.tocomsede.core.exception.UserNotLoggedException
import com.ipsoft.tocomsede.core.model.Address
import com.ipsoft.tocomsede.core.model.ResultState
import com.ipsoft.tocomsede.utils.UserInfo.userUid
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RealtimeAddressRepositoryImpl @Inject constructor(dbReference: DatabaseReference) :
    RealtimeAddressRepository {
    private val userReference = dbReference.child("users").child(userUid ?: "")

    override suspend fun saveAddress(address: Address): Flow<ResultState<Boolean>> = callbackFlow {
        trySend(ResultState.Loading)
        if (userUid == null) {
            trySend(ResultState.Failure(UserNotLoggedException("User not logged")))
        } else {
            userReference.child("addresses").push().setValue(address)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        trySend(ResultState.Success(true))
                    } else {
                        trySend(
                            ResultState.Failure(
                                it.exception ?: Exception("Error saving address")
                            )
                        )
                    }
                }.addOnFailureListener { ResultState.Failure(it) }
        }
        awaitClose {
            close()
        }
    }

    override suspend fun getAddresses(): Flow<ResultState<List<Address>>> = callbackFlow {
        trySend(ResultState.Loading)
        if (userUid == null) {
            trySend(ResultState.Failure(UserNotLoggedException("User not logged")))
        } else {
            userReference.child("addresses").get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val addresses = mutableListOf<Address>()
                    it.result?.children?.forEach { address ->
                        address.getValue(Address::class.java)?.apply { id = address.key.toString() }
                            ?.let { addressValue -> addresses.add(addressValue) }
                    }
                    trySend(ResultState.Success(addresses))
                } else {
                    trySend(
                        ResultState.Failure(
                            it.exception ?: Exception("Error getting addresses")
                        )
                    )
                }
            }.addOnFailureListener { ResultState.Failure(it) }
        }
        awaitClose {
            close()
        }
    }

    override suspend fun deleteAddress(address: Address): Flow<ResultState<Boolean>> =
        callbackFlow {
            trySend(ResultState.Loading)
            if (userUid == null) {
                trySend(ResultState.Failure(UserNotLoggedException("User not logged")))
            } else {
                userReference.child("addresses").child(address.id).removeValue()
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            trySend(ResultState.Success(true))
                        } else {
                            trySend(
                                ResultState.Failure(
                                    it.exception ?: Exception("Error deleting address")
                                )
                            )
                        }
                    }.addOnFailureListener { ResultState.Failure(it) }
            }
            awaitClose {
                close()
            }
        }

    override suspend fun updateAddress(address: Address): Flow<ResultState<Boolean>> =
        callbackFlow {
            trySend(ResultState.Loading)
            if (userUid == null) {
                trySend(ResultState.Failure(UserNotLoggedException("User not logged")))
            } else {
                if (address.isFavorite) {
                    userReference.child("addresses").get().addOnCompleteListener {
                        if (it.isSuccessful) {
                            it.result?.children?.forEach { address ->
                                address.getValue(Address::class.java)
                                    ?.apply { id = address.key.toString() }
                                    ?.let { addressValue ->
                                        if (addressValue.isFavorite) {
                                            addressValue.isFavorite = false
                                            userReference.child("addresses").child(addressValue.id)
                                                .setValue(addressValue)
                                        }
                                    }
                            }
                        }
                    }
                }
                userReference.child("addresses").child(address.id).setValue(address)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            trySend(ResultState.Success(true))
                        } else {
                            trySend(
                                ResultState.Failure(
                                    it.exception ?: Exception("Error updating address")
                                )
                            )
                        }
                    }.addOnFailureListener { ResultState.Failure(it) }
            }
            awaitClose {
                close()
            }
        }
}
