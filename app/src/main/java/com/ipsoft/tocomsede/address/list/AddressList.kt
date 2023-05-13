package com.ipsoft.tocomsede.address.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
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
import com.ipsoft.tocomsede.core.ui.theme.darkBlue80
import com.ipsoft.tocomsede.core.ui.theme.gray
import com.ipsoft.tocomsede.core.ui.theme.itemDividerPadding
import com.ipsoft.tocomsede.core.ui.theme.mediumPadding
import com.ipsoft.tocomsede.core.ui.theme.smallPadding
import com.ipsoft.tocomsede.core.ui.theme.xxLargePadding
import com.ipsoft.tocomsede.core.util.network.NetworkHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressList(
    viewModel: AddressListViewModel = hiltViewModel(),
    onNewAddressClick: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    val hasInternet = remember {
        mutableStateOf(NetworkHandler(context).isNetworkAvailable())
    }
    val addressesState = viewModel.addressState.value

    val visibility = remember { mutableStateOf(false) }

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
                        text = stringResource(id = R.string.addresses),
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
            ExtendedFloatingActionButton(
                onClick = onNewAddressClick,
                modifier = Modifier.padding(smallPadding),
                containerColor = darkBlue80
            ) {
                Row(
                    modifier = Modifier.padding(smallPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.login)
                    )
                    Spacer(modifier = Modifier.padding(smallPadding))
                    Text(text = stringResource(id = R.string.new_address))
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
                addressesState.error?.let {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = it)
                        Spacer(modifier = Modifier.padding(mediumPadding))
                        Button(
                            onClick = { viewModel.getAddresses() },
                            modifier = Modifier.wrapContentSize()
                        ) {
                            Text(text = stringResource(id = R.string.try_again))
                        }
                    }
                }

                if (addressesState.loading) {
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
                            Column {
                                LazyColumn {
                                    addressesState.addresses?.forEach { address ->
                                        item {
                                        }
                                        if (addressesState.addresses.last() == address) {
                                            item {
                                                Spacer(
                                                    modifier = Modifier.padding(
                                                        xxLargePadding
                                                    )
                                                )
                                            }
                                        } else {
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
