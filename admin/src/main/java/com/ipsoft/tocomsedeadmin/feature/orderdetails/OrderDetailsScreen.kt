package com.ipsoft.tocomsedeadmin.feature.orderdetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ipsoft.tocomsede.core.extensions.millisToDateTime
import com.ipsoft.tocomsede.core.extensions.toCurrency
import com.ipsoft.tocomsede.core.model.Address
import com.ipsoft.tocomsede.core.model.Order
import com.ipsoft.tocomsede.core.model.OrderStatus
import com.ipsoft.tocomsede.core.model.PaymentMethod
import com.ipsoft.tocomsede.core.model.User
import com.ipsoft.tocomsedeadmin.R
import com.ipsoft.tocomsedeadmin.base.ui.theme.darkBlue80
import com.ipsoft.tocomsedeadmin.base.ui.theme.largePadding
import com.ipsoft.tocomsedeadmin.base.ui.theme.mediumPadding
import com.ipsoft.tocomsedeadmin.base.ui.theme.smallPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailsScreen(
    orderDetailsViewModel: OrderDetailsViewModel = hiltViewModel(),
    orderId: String?,
    onBackClick: () -> Unit
) {
    val orderState = orderDetailsViewModel.orderState.value

    val visible = remember { mutableStateOf(!orderState.isLoading) }

    LaunchedEffect(true) {
        orderId?.let { orderDetailsViewModel.getOrderById(orderId = it) }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            visible.value = false
                            onBackClick.invoke()
                        }
                    )
                },
                title = {
                    Text(
                        text = stringResource(id = R.string.order_details),
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
        content = { padding ->
            orderState.error?.let {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = it)
                    Spacer(modifier = Modifier.padding(smallPadding))
                    Button(
                        onClick = {
                            if (orderId != null) {
                                orderDetailsViewModel.getOrderById(orderId)
                            }
                        },
                        modifier = Modifier.wrapContentSize()
                    ) {
                        Text(text = stringResource(id = R.string.try_again))
                    }
                }
            }

            if (orderState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.wrapContentSize())
                }
            } else {
                visible.value = true
                AnimatedVisibility(
                    visible = visible.value,
                    modifier = Modifier.fillMaxSize(),
                    exit = fadeOut(),
                    enter = fadeIn()
                ) {
                    Box(modifier = Modifier.padding(padding)) {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            item { Spacer(modifier = Modifier.size(smallPadding)) }
                            orderState.order?.let { order ->

                                item {
                                    OrderDetailsContainer(
                                        order = order
                                    )
                                }
                                item { Spacer(modifier = Modifier.size(smallPadding)) }
                                order.user?.let {
                                    item {
                                        ClientDetailsContainer(
                                            client = it
                                        )
                                    }
                                    item { Spacer(modifier = Modifier.size(smallPadding)) }
                                }
                                order.address?.let {
                                    item {
                                        AddressDetailsContainer(
                                            address = it
                                        )
                                    }
                                    item { Spacer(modifier = Modifier.size(smallPadding)) }
                                }
                                item {
                                    StatusDetailsContainer(
                                        order = order,
                                        orderDetailsViewModel
                                    )
                                }
                                item { Spacer(modifier = Modifier.size(smallPadding)) }
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun StatusDetailsContainer(order: Order, viewModel: OrderDetailsViewModel) {
    Surface(color = Color.White) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.change_order_status),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(smallPadding)
            )
            Spacer(modifier = Modifier.size(smallPadding))
            when (order.status) {
                OrderStatus.PENDING -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(smallPadding),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ElevatedButton(onClick = {
                            viewModel.updateOrder(
                                order.copy(
                                    status = OrderStatus.CANCELED
                                )
                            )
                        }) {
                            Text(text = stringResource(id = R.string.cancel_order))
                        }
                        ElevatedButton(onClick = {
                            viewModel.updateOrder(
                                order.copy(
                                    status = OrderStatus.CONFIRMED
                                )
                            )
                        }) {
                            Text(text = stringResource(id = R.string.accept_order))
                        }
                    }
                }

                OrderStatus.CONFIRMED -> {
                    ElevatedButton(onClick = {
                        viewModel.updateOrder(
                            order.copy(
                                status = OrderStatus.DELIVERING
                            )
                        )
                    }) {
                        Text(text = stringResource(id = R.string.delivering))
                    }
                }

                OrderStatus.DELIVERING -> {
                    ElevatedButton(onClick = {
                        viewModel.updateOrder(
                            order.copy(
                                status = OrderStatus.CONCLUDED
                            )
                        )
                    }) {
                        Text(text = stringResource(id = R.string.concluded))
                    }
                }

                OrderStatus.CONCLUDED -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(largePadding),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.this_order_was),
                            color = Color.Green
                        )

                        Text(
                            text = stringResource(id = R.string.concluded),
                            color = Color.Green
                        )
                    }
                }

                OrderStatus.CANCELED -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(smallPadding),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.padding(smallPadding))
                        Text(
                            text = stringResource(id = R.string.canceled),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddressDetailsContainer(address: Address) {
    Surface(color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = largePadding, horizontal = largePadding)
        ) {
            Text(
                text = stringResource(id = R.string.delivery_at),
                style = MaterialTheme.typography.titleMedium
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
            }
        }
    }
}

@Composable
fun ClientDetailsContainer(client: User) {
    Surface(color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = largePadding, horizontal = largePadding)
        ) {
            Text(
                text = stringResource(id = R.string.client_details),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.size(smallPadding))

            // Client name
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.name),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = client.name,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.size(smallPadding))

            // Client email
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.email),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = client.email,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.size(smallPadding))

            // Client phone
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.phone),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = client.phone.ifEmpty { stringResource(id = R.string.not_informed) },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun OrderDetailsContainer(order: Order) {
    Surface(color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = largePadding, horizontal = largePadding)
        ) {
            Text(
                text = stringResource(id = R.string.order_items),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.size(smallPadding))

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
                            OrderStatus.CONFIRMED -> Color.Gray
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
                    text = stringResource(id = R.string.change_for),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = if (order.change.hasChange) {
                        order.change.toString().toCurrency()
                            .toCurrency()
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
