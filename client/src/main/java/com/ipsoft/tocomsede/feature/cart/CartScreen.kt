package com.ipsoft.tocomsede.feature.cart

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ipsoft.tocomsede.R
import com.ipsoft.tocomsede.base.extensions.showMsg
import com.ipsoft.tocomsede.base.ui.theme.darkBlue80
import com.ipsoft.tocomsede.base.ui.theme.gray
import com.ipsoft.tocomsede.base.ui.theme.itemDividerPadding
import com.ipsoft.tocomsede.base.ui.theme.largePadding
import com.ipsoft.tocomsede.base.ui.theme.mediumPadding
import com.ipsoft.tocomsede.base.ui.theme.smallPadding
import com.ipsoft.tocomsede.core.model.Address
import com.ipsoft.tocomsede.core.model.Change
import com.ipsoft.tocomsede.core.model.PaymentMethod
import com.ipsoft.tocomsede.core.model.PaymentMethod.CREDIT_CARD
import com.ipsoft.tocomsede.core.model.PaymentMethod.DEBIT_CARD
import com.ipsoft.tocomsede.core.model.PaymentMethod.MONEY
import com.ipsoft.tocomsede.core.model.PaymentMethod.PIX
import com.ipsoft.tocomsede.core.model.Store

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartViewModel: CartViewModel = hiltViewModel(),
    onEditClick: () -> Unit,
    onPhoneEditClick: () -> Unit,
    onLoginClick: () -> Unit,
    onCheckoutSuccess: () -> Unit
) {
    val cartItemState = cartViewModel.cartItemState.value
    val cartTotalState = cartViewModel.cartTotalState.value
    val addressFavoriteState = cartViewModel.favoriteAddressState.value
    val phoneState = cartViewModel.phoneState.value
    val isUserLoggedState = cartViewModel.userLoggedState.value
    var showDialog by remember { mutableStateOf(false) }
    val store = cartViewModel.store.value
    val context = LocalContext.current

    if (cartItemState.checkoutSuccess) {
        onCheckoutSuccess()
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(id = R.string.confirm_order)) },
            text = { Text(stringResource(id = R.string.confirm_order_ask)) },
            confirmButton = {
                ElevatedButton(
                    onClick = {
                        cartViewModel.checkout()
                        showDialog = false
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.cart),
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
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                color = gray
            ) {
                cartItemState.error?.let {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Text(text = it)
                            Spacer(modifier = Modifier.padding(smallPadding))
                            ElevatedButton(
                                onClick = { cartViewModel.loadCart() },
                                modifier = Modifier.wrapContentSize()
                            ) {
                                Text(text = stringResource(id = R.string.try_again))
                            }
                        }
                    }
                }

                if (cartItemState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.wrapContentSize())
                    }
                } else {
                    if (cartItemState.items.isNotEmpty()) {
                        LaunchedEffect(isUserLoggedState) {
                            if (isUserLoggedState) {
                                cartViewModel.loadFavoriteAddress()
                                cartViewModel.loadPhone()
                            }
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(0.dp, smallPadding, 0.dp, 0.dp)
                        ) {
                            LazyColumn {
                                item { CartItemList(cartItemState) }
                                item { CheckoutCartTotal(cartTotalState) }
                                item { Spacer(modifier = Modifier.padding(itemDividerPadding)) }
                                if (isUserLoggedState) {
                                    item {
                                        CheckoutAddress(addressFavoriteState) {
                                            onEditClick()
                                        }
                                    }
                                    item {
                                        Spacer(modifier = Modifier.padding(itemDividerPadding))
                                    }
                                    item {
                                        CheckoutPhone(
                                            phoneState,
                                            onPhoneEditClick = onPhoneEditClick
                                        )
                                    }
                                    item {
                                        Spacer(modifier = Modifier.padding(itemDividerPadding))
                                    }
                                    item {
                                        PaymentMethodSelection(cartViewModel, store)
                                    }
                                    if (cartViewModel.paymentState.value == MONEY) {
                                        item {
                                            Spacer(modifier = Modifier.padding(itemDividerPadding))
                                        }
                                        item {
                                            ChangeSelectContainer(cartViewModel)
                                        }
                                    }
                                    item {
                                        Spacer(modifier = Modifier.padding(itemDividerPadding))
                                    }
                                    if (!store.open) {
                                        item {
                                            Text(
                                                text = stringResource(id = R.string.store_closed_cart),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(largePadding),
                                                color = Color.Red
                                            )
                                        }
                                    }
                                    item {
                                        CheckoutButtonContainer(store.open) {
                                            if (addressFavoriteState != null) {
                                                showDialog = true
                                            } else {
                                                context.showMsg(
                                                    context.getString(R.string.address_not_found)
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    item { LoginContainer(onLoginClick) }
                                }
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = stringResource(id = R.string.cart_is_empty))
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeSelectContainer(cartViewModel: CartViewModel) {
    Surface {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(largePadding),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text(
                    text = stringResource(id = R.string.change),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(largePadding),
                    textAlign = TextAlign.Center
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(largePadding),
                    verticalArrangement = Arrangement.spacedBy(smallPadding)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = !cartViewModel.changePaymentState.value.hasChange,
                            onClick = {
                                cartViewModel.updateChange(
                                    Change()
                                )
                            }
                        )
                        Text(
                            text = stringResource(id = R.string.no_change),
                            modifier = Modifier.padding(smallPadding)
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = cartViewModel.changePaymentState.value.hasChange,
                            onClick = {
                                cartViewModel.updateChange(
                                    Change(
                                        hasChange = true,
                                        changeFor = cartViewModel.changePaymentState.value.changeFor
                                    )
                                )
                            }
                        )
                        Text(
                            text = stringResource(id = R.string.change_for),
                            modifier = Modifier.padding(smallPadding)
                        )
                        OutlinedTextField(
                            enabled = cartViewModel.changePaymentState.value.hasChange,
                            isError = cartViewModel.changePaymentState.value.changeFor == 0.0,
                            value = cartViewModel.changePaymentState.value.changeFor.toString(),
                            onValueChange = {
                                cartViewModel.updateChange(
                                    Change(
                                        hasChange = true,
                                        changeFor = it.toDouble()
                                    )
                                )
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Decimal
                            ),
                            singleLine = true
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CheckoutPhone(phoneState: String?, onPhoneEditClick: () -> Unit) {
    Surface {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.phone) + ": ",
                modifier = Modifier.padding(largePadding)
            )
            Text(
                text = phoneState ?: stringResource(id = R.string.not_informed),
                style = if (phoneState != null) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(largePadding)
            )
            IconButton(onClick = onPhoneEditClick) {
                Icon(
                    if (phoneState != null) Icons.Default.Edit else Icons.Default.Add,
                    contentDescription = "Edit phone",
                    tint = darkBlue80
                )
            }
        }
    }
}

@Composable
fun CheckoutAddress(addressFavoriteState: Address?, onEditClick: () -> Unit) {
    Surface {
        AddressListContainer(
            address = addressFavoriteState,
            onEditClick = onEditClick
        )
    }
}

@Composable
fun CheckoutButtonContainer(storeOpen: Boolean, onClick: () -> Unit) {
    Surface {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(largePadding),
            contentAlignment = Alignment.BottomCenter
        ) {
            ElevatedButton(
                enabled = storeOpen,
                onClick = { onClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Text(
                    text = stringResource(id = R.string.checkout),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(mediumPadding)
                        .fillMaxWidth(),
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun LoginContainer(onLoginClick: () -> Unit) {
    Surface {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(largePadding),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text(
                    text = stringResource(id = R.string.login_to_continue),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(largePadding),
                    textAlign = TextAlign.Center
                )
                ElevatedButton(
                    onClick = { onLoginClick() },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.login),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(mediumPadding),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center

                    )
                }
            }
        }
    }
}

@Composable
fun PaymentMethodSelection(viewModel: CartViewModel, store: Store) {
    val validPayments = store.payments

    Surface {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(largePadding),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text(
                    text = stringResource(id = R.string.payment_method),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(largePadding),
                    textAlign = TextAlign.Center
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(largePadding),
                    verticalArrangement = Arrangement.spacedBy(smallPadding)
                ) {
                    validPayments.forEach { payment ->
                        PaymentMethodItem(payment, viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentMethodItem(payment: PaymentMethod, viewModel: CartViewModel) {
    Row(
        modifier = Modifier
            .padding(smallPadding)
            .clickable { viewModel.updatePaymentMethod(payment) }
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = viewModel.paymentState.value == payment,
            onClick = { viewModel.updatePaymentMethod(payment) },
            colors = RadioButtonDefaults.colors(
                selectedColor = darkBlue80,
                unselectedColor = darkBlue80
            )
        )
        Text(
            text = when (payment) {
                MONEY -> stringResource(id = R.string.money)
                CREDIT_CARD -> stringResource(id = R.string.credit_card)
                DEBIT_CARD -> stringResource(id = R.string.debit_card)
                PIX -> stringResource(id = R.string.pix)
            },
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(smallPadding)
        )
    }
}

@Composable
fun CheckoutCartTotal(cartTotalState: String) {
    Surface {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(largePadding),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = stringResource(id = R.string.total) + " = $cartTotalState",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun AddressListContainer(
    address: Address?,
    onEditClick: () -> Unit
) {
    Surface(color = Color.White) {
        if (address == null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(smallPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.padding(smallPadding))
                Text(text = stringResource(id = R.string.no_address_found))
                Spacer(modifier = Modifier.padding(smallPadding))
                ElevatedButton(onClick = onEditClick) {
                    Text(text = stringResource(id = R.string.add_address))
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(smallPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.delivery_at),
                    modifier = Modifier.padding(smallPadding)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(smallPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.padding(smallPadding))
                    Text(
                        text = address.toString(),
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(onClick = onEditClick) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit address",
                            tint = darkBlue80
                        )
                    }
                }
            }
        }
    }
}
