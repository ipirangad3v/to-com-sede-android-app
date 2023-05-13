package com.ipsoft.tocomsede.address

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.ipsoft.tocomsede.R
import com.ipsoft.tocomsede.core.extensions.isValidCep
import com.ipsoft.tocomsede.core.model.Address
import com.ipsoft.tocomsede.core.ui.theme.largePadding
import com.ipsoft.tocomsede.core.ui.theme.mediumPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressFormScreen(addressFormViewModel: AddressFormViewModel = hiltViewModel()) {
    val cepState = addressFormViewModel.cepState

    val cep = remember { mutableStateOf("") }
    val number = remember { mutableStateOf("") }
    val complement = remember { mutableStateOf("") }
    val neighborhood = remember { mutableStateOf("") }
    val city = remember { mutableStateOf("") }
    val state = remember { mutableStateOf("") }
    val street = remember { mutableStateOf("") }

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
                .padding(largePadding)
        ) {
            Text(
                text = "Preencha seu endereço",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = largePadding)
            )
            OutlinedTextField(
                value = cep.value,
                onValueChange = {
                    cep.value = it
                    if (it.isValidCep()) {
                        addressFormViewModel.getCep(it)
                    }
                },
                label = { Text(stringResource(R.string.cep)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = largePadding)
            )
            OutlinedTextField(
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
                    value = number.value,
                    onValueChange = { number.value = it },
                    label = { Text(stringResource(R.string.number)) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = mediumPadding)
                )
                OutlinedTextField(
                    value = complement.value,
                    onValueChange = { complement.value = it },
                    label = { Text(stringResource(R.string.complement)) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = mediumPadding)
                )
            }
            OutlinedTextField(
                value = neighborhood.value,
                onValueChange = { neighborhood.value = it },
                label = { Text(stringResource(R.string.neighborhood)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = largePadding)
            )
            OutlinedTextField(
                value = city.value,
                onValueChange = { city.value = it },
                label = { Text(stringResource(R.string.city)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = largePadding)
            )
            OutlinedTextField(
                value = state.value,
                onValueChange = { state.value = it },
                label = { Text(stringResource(R.string.state)) },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    addressFormViewModel.saveAddress(
                        Address(
                            zipCode = cep.value,
                            street = street.value,
                            number = number.value,
                            complement = complement.value,
                            neighborhood = neighborhood.value,
                            city = city.value,
                            state = state.value
                        )
                    )
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
