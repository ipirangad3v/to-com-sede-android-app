package com.ipsoft.tocomsede.itemdetails

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ipsoft.tocomsede.R
import com.ipsoft.tocomsede.base.extensions.showMsg
import com.ipsoft.tocomsede.base.ui.theme.darkBlue80
import com.ipsoft.tocomsede.base.ui.theme.largePadding
import com.ipsoft.tocomsede.base.ui.theme.smallPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailsScreen(
    itemId: Int?,
    viewModel: ItemDetailsViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val itemState = viewModel.items.value

    val visible = remember { mutableStateOf(!itemState.isLoading) }

    val cartAddedSuccess = viewModel.isSuccessFullCartAdded.value

    LaunchedEffect(true) {
        itemId?.let { viewModel.getItemById(itemId = it) }
    }
    val title: MutableState<String?> = remember {
        mutableStateOf(
            null
        )
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
                            onBack.invoke()
                        }
                    )
                },
                title = {
                    Text(
                        text = title.value ?: stringResource(id = R.string.item_details),
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

            itemState.item?.name?.let { title.value = it }

            if (cartAddedSuccess) {
                LocalContext.current.showMsg(stringResource(id = R.string.item_added_to_cart))
                viewModel.resetCartAddedStatus()
                visible.value = false
                onBack.invoke()
            }

            itemState.error?.let {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = it)
                    Spacer(modifier = Modifier.padding(smallPadding))
                    Button(
                        onClick = {
                            if (itemId != null) {
                                viewModel.getItemById(itemId)
                            }
                        },
                        modifier = Modifier.wrapContentSize()
                    ) {
                        Text(text = stringResource(id = R.string.try_again))
                    }
                }
            }

            if (itemState.isLoading) {
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
                        val context = LocalContext.current
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            item {
                                ItemDetailsCard(itemState)
                            }
                            item {
                                ItemAddContainer(
                                    itemState,
                                    viewModel,
                                    context
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun ItemAddContainer(
    itemDetailScreenState: ItemDetailScreenState,
    viewModel: ItemDetailsViewModel,
    context: Context
) {
    val selectedQuantity =
        remember { mutableStateOf(if (itemDetailScreenState.canAddToCart) 1 else 0) }

    val canAddToCart = itemDetailScreenState.canAddToCart

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = smallPadding)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(largePadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            itemDetailScreenState.item?.quantity?.let { quantity ->
                QuantitySelector(
                    selectedQuantity,
                    quantity - itemDetailScreenState.quantityInCart,
                    itemDetailScreenState.item.isAvailable
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            ElevatedButton(
                onClick = {
                    itemDetailScreenState.item?.let { item ->
                        if (canAddToCart) {
                            viewModel.addItemToCart(
                                item,
                                selectedQuantity.value
                            )
                        } else {
                            context.showMsg(context.getString(R.string.item_not_available))
                        }
                    }
                }
            ) {
                Row(
                    Modifier
                        .padding(smallPadding)
                        .wrapContentSize(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.add_to),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Icon(
                        imageVector = Icons.Filled.ShoppingCart,
                        contentDescription = null,
                        tint = darkBlue80,
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(smallPadding)
                    )
                }
            }
        }
    }
}
