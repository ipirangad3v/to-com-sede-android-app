package com.ipsoft.tocomsedeadmin.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ipsoft.tocomsede.core.extensions.millisToDateTime
import com.ipsoft.tocomsede.core.extensions.toCurrency
import com.ipsoft.tocomsede.core.model.Order
import com.ipsoft.tocomsede.core.model.OrderStatus
import com.ipsoft.tocomsede.core.model.PaymentMethod
import com.ipsoft.tocomsede.core.utils.UserInfo.isUserLogged
import com.ipsoft.tocomsedeadmin.R
import com.ipsoft.tocomsedeadmin.base.ui.theme.darkBlue80
import com.ipsoft.tocomsedeadmin.base.ui.theme.gray
import com.ipsoft.tocomsedeadmin.base.ui.theme.itemDividerPadding
import com.ipsoft.tocomsedeadmin.base.ui.theme.largePadding
import com.ipsoft.tocomsedeadmin.base.ui.theme.mediumPadding
import com.ipsoft.tocomsedeadmin.base.ui.theme.smallPadding
import com.ipsoft.tocomsedeadmin.base.util.network.NetworkHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel(),
    onLoginClick: () -> Unit
) {
    val homeState = homeViewModel.homeState.value

    val context = LocalContext.current

    val hasInternet = remember {
        mutableStateOf(NetworkHandler(context).isNetworkAvailable())
    }

    val visibility = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
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
        floatingActionButton = {
            if (!isUserLogged) {
                ExtendedFloatingActionButton(
                    shape = MaterialTheme.shapes.extraLarge,
                    onClick = onLoginClick,
                    modifier = Modifier.padding(smallPadding),
                    containerColor = darkBlue80
                ) {
                    Row(
                        modifier = Modifier.padding(smallPadding),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = stringResource(id = R.string.login)
                        )
                        Spacer(modifier = Modifier.padding(smallPadding))
                        Text(text = stringResource(id = R.string.login))
                    }
                }
            }
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            color = gray
        ) {
            if (hasInternet.value) {
                if (homeState.loading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.wrapContentSize())
                    }
                } else {
                    if (isUserLogged) {
                        visibility.value = true
                        AnimatedVisibility(
                            visible = visibility.value,
                            enter = fadeIn() + slideInVertically(),
                            exit = slideOutVertically() + fadeOut(),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(0.dp, smallPadding, 0.dp, 0.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                Column {
                                    StoreToggle(homeViewModel)
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(mediumPadding),
                                            text = stringResource(id = R.string.orders),
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }
                                    LazyColumn {
                                        homeState.orders.sortedBy { it.dateInMillis }.reversed()
                                            .forEach { order ->
                                                item {
                                                    OrderListItem(
                                                        order = order,
                                                        navController = navController
                                                    )
                                                    Spacer(
                                                        modifier = Modifier.padding(
                                                            itemDividerPadding
                                                        )
                                                    )
                                                }
                                            }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = stringResource(id = R.string.no_internet))
                        Spacer(modifier = Modifier.padding(smallPadding))
                        Button(
                            onClick = {
                                hasInternet.value = NetworkHandler(context).isNetworkAvailable()
                            },
                            modifier = Modifier.wrapContentSize()
                        ) {
                            Text(text = stringResource(id = R.string.try_again))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderListItem(
    order: Order,
    navController: NavController
) {
    Surface(
        color = Color.White,
        modifier = Modifier
            .clickable {
                navController.navigate(
                    "order_details" +
                        "/${order.id}"
                )
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = largePadding, horizontal = largePadding)
        ) {
            // Order date and status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = order.dateInMillis.millisToDateTime(),
                    style = MaterialTheme.typography.bodyMedium
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(mediumPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = when (order.status) {
                            OrderStatus.PENDING -> stringResource(id = R.string.pending)
                            OrderStatus.DELIVERING -> stringResource(id = R.string.delivering)
                            OrderStatus.CONCLUDED -> stringResource(id = R.string.concluded)
                            OrderStatus.CANCELED -> stringResource(id = R.string.canceled)
                            OrderStatus.CONFIRMED -> stringResource(id = R.string.confirmed)
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Spacer(modifier = Modifier.padding(smallPadding))

            // List of order items
            order.items.forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${item.name} x${item.selectedQuantity}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = item.price.toString().toCurrency(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.padding(largePadding))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.payment_method),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = when (order.paymentMethod) {
                        PaymentMethod.MONEY -> stringResource(id = R.string.money)
                        PaymentMethod.CREDIT_CARD -> stringResource(id = R.string.credit_card)
                        PaymentMethod.DEBIT_CARD -> stringResource(id = R.string.debit_card)
                        PaymentMethod.PIX -> stringResource(id = R.string.pix)
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.padding(smallPadding))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.change_for),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = if (order.change.hasChange) {
                        order.change.changeFor.toString().toCurrency()
                    } else {
                        stringResource(id = R.string.dont_need_change)
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.padding(smallPadding))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.total),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = order.total.toString().toCurrency(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun StoreToggle(viewModel: HomeViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(mediumPadding)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.store) + " " + stringResource(
                    id = if (viewModel.homeState.value.store.open) R.string.open else R.string.closed
                ),
                style = MaterialTheme.typography.titleSmall
            )
            Switch(
                checked = viewModel.homeState.value.store.open,
                onCheckedChange = { viewModel.toggleStore() }
            )
        }
        Spacer(modifier = Modifier.padding(smallPadding))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(
                id = if (viewModel.homeState.value.store.open) R.string.can_order else R.string.cant_order
            ),
            style = MaterialTheme.typography.titleSmall
        )
    }
}
