package com.ipsoft.tocomsede.phone

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ipsoft.tocomsede.R
import com.ipsoft.tocomsede.core.extensions.digitsOnly
import com.ipsoft.tocomsede.core.extensions.isBrazilianPhone
import com.ipsoft.tocomsede.core.extensions.showMsg
import com.ipsoft.tocomsede.core.ui.theme.darkBlue80
import com.ipsoft.tocomsede.core.ui.theme.largePadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneScreen(viewModel: PhoneViewModel = hiltViewModel(), onBack: () -> Unit) {
    var phoneNumber by remember { mutableStateOf(viewModel.phoneState.value.phone) }
    val phoneState = viewModel.phoneState.value
    val context = LocalContext.current

    Scaffold(
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
                        text = stringResource(id = R.string.phone),
                        maxLines = 1
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = darkBlue80,
                    navigationIconContentColor = darkBlue80
                )
            )
        },
        content = { paddingValues ->

            if (phoneState.phoneUpdateSuccess) {
                context.showMsg(stringResource(id = R.string.phone_update_success))
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = largePadding)
            ) {
                OutlinedTextField(
                    isError = !phoneNumber.digitsOnly().isBrazilianPhone(),
                    value = phoneNumber,
                    onValueChange = {
                        val digitsOnly = it.digitsOnly().take(11)

                        // Aplica a máscara "(##) #####-####"
                        phoneNumber = if (digitsOnly.isBrazilianPhone()) {
                            buildString {
                                append("(")
                                append(digitsOnly.take(2))
                                append(") ")
                                append(digitsOnly.substring(2, 7))
                                append("-")
                                append(digitsOnly.substring(7))
                            }
                        } else {
                            digitsOnly
                        }
                    },
                    label = { Text(stringResource(id = R.string.phone_number)) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Phone
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        if (
                            phoneNumber.digitsOnly().isBrazilianPhone()
                        ) {
                            viewModel.savePhone(phoneNumber.digitsOnly())
                        } else {
                            context.showMsg("Telefone inválido")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Text(text = "Salvar")
                }
            }
        }
    )
}
