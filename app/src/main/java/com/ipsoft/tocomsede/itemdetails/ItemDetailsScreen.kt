package com.ipsoft.tocomsede.itemdetails

import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ipsoft.tocomsede.R
import com.ipsoft.tocomsede.core.extensions.showMsg
import com.ipsoft.tocomsede.core.ui.components.SquaredButton
import com.ipsoft.tocomsede.core.ui.theme.lightBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailsScreen(
    itemId: Int?,
    viewModel: ItemDetailsViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    itemId?.let { viewModel.getItemById(itemId = it) }

    val selectedQuantity = remember { mutableStateOf(1) }
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
                        modifier = Modifier.clickable { onBack.invoke() }
                    )
                },
                title = {
                    Text(
                        text = title.value ?: stringResource(id = R.string.item_details),
                        maxLines = 1
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = lightBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        content = { padding ->

            val item = viewModel.items.value

            val cartAddedSuccess = viewModel.isSuccessFullCartAdded.value

            item.item?.name?.let { title.value = it }

            if (cartAddedSuccess) {
                LocalContext.current.showMsg(stringResource(id = R.string.item_added_to_cart))
                viewModel.resetCartAddedStatus()
                onBack.invoke()
            }

            item.error?.let {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = it)
                    Spacer(modifier = Modifier.padding(8.dp))
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

            if (item.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.wrapContentSize())
                }
            } else {
                Box(modifier = Modifier.padding(padding)) {
                    LazyColumn {
                        item {
                            ItemDetailsCard(item)
                        }
                        item {
                            Spacer(modifier = Modifier.padding(8.dp))
                        }

                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                item.item?.quantity?.let { QuantitySelector(selectedQuantity, it) }

                                SquaredButton(
                                    text = stringResource(id = R.string.add_to_cart),
                                    onClick = {
                                        item.item?.let {
                                            viewModel.addItemToCart(
                                                it,
                                                selectedQuantity.value
                                            )
                                        }
                                    },
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}
