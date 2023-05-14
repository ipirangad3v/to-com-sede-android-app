package com.ipsoft.tocomsede.cart

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ipsoft.tocomsede.R
import com.ipsoft.tocomsede.core.model.Address
import com.ipsoft.tocomsede.core.ui.components.SquaredButton
import com.ipsoft.tocomsede.core.ui.theme.darkBlue80
import com.ipsoft.tocomsede.core.ui.theme.gray
import com.ipsoft.tocomsede.core.ui.theme.itemDividerPadding
import com.ipsoft.tocomsede.core.ui.theme.largePadding
import com.ipsoft.tocomsede.core.ui.theme.mediumPadding
import com.ipsoft.tocomsede.core.ui.theme.smallPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    checkoutViewModel: CartViewModel = hiltViewModel(),
    onEditClick: () -> Unit,
    onPhoneEditClick: () -> Unit,
    onLoginClick: () -> Unit,
    onCheckoutSuccess: () -> Unit
) {
    val cartItemState = checkoutViewModel.cartItemState.value
    val cartTotalState = checkoutViewModel.cartTotalState.value
    val addressFavoriteState = checkoutViewModel.favoriteAddressState.value
    val phoneState = checkoutViewModel.phoneState.value
    val isUserLoggedState = checkoutViewModel.userLoggedState.value

    if (cartItemState.checkoutSuccess) {
        onCheckoutSuccess()
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
                        Text(text = it)
                        Spacer(modifier = Modifier.padding(smallPadding))
                        Button(
                            onClick = { checkoutViewModel.loadCart() },
                            modifier = Modifier.wrapContentSize()
                        ) {
                            Text(text = stringResource(id = R.string.try_again))
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
                                checkoutViewModel.loadFavoriteAddress()
                                checkoutViewModel.loadPhone()
                            }
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(0.dp, smallPadding, 0.dp, 0.dp)
                        ) {
                            LazyColumn {
                                item { CartItemList(cartItemState, showDelete = false) }
                                item { CheckoutCartTotal(cartTotalState) }
                                item { Spacer(modifier = Modifier.padding(itemDividerPadding)) }
                                if (isUserLoggedState) {
                                    item {
                                        CheckoutAddress(addressFavoriteState) {
                                            onEditClick()
                                        }
                                    }
                                    item {
                                        CheckoutPhone(
                                            phoneState,
                                            onPhoneEditClick = onPhoneEditClick
                                        )
                                    }
                                    item { CheckoutButtonContainer(viewModel = checkoutViewModel) }
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

@Composable
fun CheckoutPhone(phoneState: String?, onPhoneEditClick: () -> Unit) {
    Surface {
        phoneState?.let { phone ->
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
                    text = phone,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(largePadding)
                )
                IconButton(onClick = onPhoneEditClick) {
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

@Composable
fun CheckoutAddress(addressFavoriteState: Address?, onEditClick: () -> Unit) {
    Surface {
        addressFavoriteState?.let { address ->
            AddressListContainer(
                address = address,
                onEditClick = onEditClick
            )
        }
    }
}

@Composable
fun CheckoutButtonContainer(viewModel: CartViewModel) {
    Surface {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(largePadding),
            contentAlignment = Alignment.BottomCenter
        ) {
            SquaredButton(
                onClick = { viewModel.checkout() },
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
                SquaredButton(
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
    address: Address,
    onEditClick: () -> Unit
) {
    Surface(color = Color.White, modifier = Modifier.clickable(onClick = onEditClick)) {
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
