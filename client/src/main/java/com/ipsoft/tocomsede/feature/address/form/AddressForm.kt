package com.ipsoft.tocomsede.feature.address.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.ipsoft.tocomsede.R
import com.ipsoft.tocomsede.base.extensions.showMsg
import com.ipsoft.tocomsede.base.ui.theme.darkBlue80
import com.ipsoft.tocomsede.base.ui.theme.largePadding
import com.ipsoft.tocomsede.base.ui.theme.mediumPadding
import com.ipsoft.tocomsede.core.extensions.isValidCep
import com.ipsoft.tocomsede.core.model.Address

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressFormScreen(
    addressFormViewModel: AddressFormViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val context = LocalContext.current

    val cepState = addressFormViewModel.cepState

    val cep = remember { mutableStateOf("") }
    val number = remember { mutableStateOf("") }
    val complement = remember { mutableStateOf("") }
    val neighborhood = remember { mutableStateOf("") }
    val city = remember { mutableStateOf("") }
    val state = remember { mutableStateOf("") }
    val street = remember { mutableStateOf("") }

    val isFilled = cep.value.isNotEmpty() &&
        number.value.isNotEmpty() &&
        neighborhood.value.isNotEmpty() &&
        city.value.isNotEmpty() &&
        state.value.isNotEmpty() &&
        street.value.isNotEmpty()

    val showToast = remember { mutableStateOf(true) }

    if (cepState.value.cepResponse != null) {
        cep.value = cepState.value.cepResponse!!.cep
        street.value = cepState.value.cepResponse!!.street
        neighborhood.value = cepState.value.cepResponse!!.neighborhood
        city.value = cepState.value.cepResponse!!.city
        state.value = cepState.value.cepResponse!!.state
    }

    if (cepState.value.addressAddedSuccess && showToast.value) {
        context.showMsg(stringResource(R.string.address_added_success))
        showToast.value = false
        onBack()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            onBack()
                        }
                    )
                },
                title = {
                    Text(
                        text = stringResource(id = R.string.new_address),
                        maxLines = 1
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = darkBlue80,
                    navigationIconContentColor = darkBlue80
                )
            )
        }
    ) { padding ->
        cepState.value.error?.let {
            context.showMsg(stringResource(id = R.string.cep_not_found))
        }
        if (cepState.value.loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.wrapContentSize())
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(mediumPadding)
            ) {
                Text(
                    text = "Preencha seu endereço",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = largePadding)
                )
                CepTextField(cep) {
                    if (it.isValidCep()) {
                        addressFormViewModel.getCep(it)
                    }
                }
                OutlinedTextField(
                    isError = street.value.isEmpty(),
                    maxLines = 1,
                    value = street.value,
                    onValueChange = { street.value = it },
                    label = { Text(stringResource(R.string.street)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = largePadding)
                )
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        isError = number.value.isEmpty(),
                        value = number.value,
                        maxLines = 1,
                        onValueChange = { number.value = it },
                        label = { Text(stringResource(R.string.number)) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = mediumPadding)
                    )
                    OutlinedTextField(
                        maxLines = 1,
                        value = complement.value,
                        onValueChange = { complement.value = it },
                        label = { Text(stringResource(R.string.complement)) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = mediumPadding)
                    )
                }
                OutlinedTextField(
                    isError = neighborhood.value.isEmpty(),
                    maxLines = 1,
                    value = neighborhood.value,
                    onValueChange = { neighborhood.value = it },
                    label = { Text(stringResource(R.string.neighborhood)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = largePadding)
                )
                OutlinedTextField(
                    isError = city.value.isEmpty(),
                    value = city.value,
                    maxLines = 1,
                    onValueChange = { city.value = it },
                    label = { Text(stringResource(R.string.city)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = largePadding)
                )
                OutlinedTextField(
                    isError = state.value.isEmpty(),
                    maxLines = 1,
                    value = state.value,
                    onValueChange = { state.value = it },
                    label = { Text(stringResource(R.string.state)) },
                    modifier = Modifier.fillMaxWidth()
                )
                ElevatedButton(
                    onClick = {
                        if (isFilled) {
                            addressFormViewModel.saveAddress(
                                Address(
                                    zipCode = cep.value,
                                    street = street.value,
                                    number = number.value,
                                    complement = complement.value,
                                    neighborhood = neighborhood.value,
                                    city = city.value,
                                    state = state.value,
                                    id = "",
                                    isFavorite = true
                                )
                            )
                        } else {
                            context.showMsg(context.getString(R.string.fill_all_fields))
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = largePadding)
                ) {
                    Text(text = stringResource(R.string.save_address))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CepTextField(
    cep: MutableState<String>,
    onCepChange: (String) -> Unit
) {
    OutlinedTextField(
        maxLines = 1,
        isError = !cep.value.isValidCep(),
        value = cep.value,
        onValueChange = {
            if (it.length <= 8) {
                cep.value = it
                onCepChange(it)
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        label = { Text(stringResource(id = R.string.cep)) },
        placeholder = { Text("12345678") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = largePadding)
    )
}
