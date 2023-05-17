package com.ipsoft.tocomsede.feature.orders

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.ipsoft.tocomsede.R
import com.ipsoft.tocomsede.base.ui.theme.darkBlue80
import com.ipsoft.tocomsede.base.ui.theme.gray
import com.ipsoft.tocomsede.base.ui.theme.itemDividerPadding
import com.ipsoft.tocomsede.base.ui.theme.largePadding
import com.ipsoft.tocomsede.base.ui.theme.mediumPadding
import com.ipsoft.tocomsede.base.ui.theme.smallPadding
import com.ipsoft.tocomsede.base.util.network.NetworkHandler
import com.ipsoft.tocomsede.core.extensions.millisToDateTime
import com.ipsoft.tocomsede.core.extensions.toCurrency
import com.ipsoft.tocomsede.core.model.Order
import com.ipsoft.tocomsede.core.model.OrderStatus
import com.ipsoft.tocomsede.core.model.PaymentMethod

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(viewModel: OrdersViewModel = hiltViewModel(), onLoginClick: () -> Unit) {
    val ordersState = viewModel.state.value

    val isUserLoggedState = viewModel.userLoggedState.value

    val context = LocalContext.current

    val hasInternet = remember {
        mutableStateOf(NetworkHandler(context).isNetworkAvailable())
    }

    val visibility = remember { mutableStateOf(false) }

    val canRetrieve = remember { mutableStateOf(true) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.orders),
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
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            color = gray
        ) {
            if (hasInternet.value) {
                if (ordersState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.wrapContentSize())
                    }
                } else {
                    visibility.value = true
                    AnimatedVisibility(
                        visible = visibility.value,
                        enter = fadeIn() + slideInVertically(),
                        exit = slideOutVertically() + fadeOut(),
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            canRetrieve.value = true
                            if (isUserLoggedState) {
                                Column {
                                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                                        item {
                                            Spacer(
                                                modifier = Modifier.padding(itemDividerPadding)
                                            )
                                        }
                                        if (ordersState.orders.isEmpty()) {
                                            item {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(mediumPadding),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = stringResource(id = R.string.no_orders)
                                                    )
                                                }
                                            }
                                        } else {
                                            ordersState.orders.sortedBy { it.dateInMillis }
                                                .reversed()
                                                .forEach { order ->
                                                    item {
                                                        OrderListItem(order = order)
                                                    }
                                                    item {
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
                            } else {
                                Column {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(mediumPadding),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            modifier = Modifier.fillMaxSize(),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = stringResource(id = R.string.login_to_see_orders),
                                                Modifier.padding(mediumPadding),
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                            Spacer(modifier = Modifier.padding(mediumPadding))
                                            ElevatedButton(onClick = onLoginClick) {
                                                Text(
                                                    text = stringResource(id = R.string.login),
                                                    Modifier.padding(largePadding)
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
                        ElevatedButton(
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
    order: Order
) {
    Surface(color = Color.White) {
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
                    Icon(
                        modifier = Modifier.size(largePadding),
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = when (order.status) {
                            OrderStatus.PENDING -> Color.Yellow
                            OrderStatus.DELIVERING -> Color.Blue
                            OrderStatus.CONCLUDED -> Color.Green
                            OrderStatus.CANCELED -> Color.Red
                            OrderStatus.CONFIRMED -> Color.Green
                        }
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
