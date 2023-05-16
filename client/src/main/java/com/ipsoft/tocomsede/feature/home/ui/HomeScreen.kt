package com.ipsoft.tocomsede.feature.home.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
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
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ipsoft.tocomsede.R
import com.ipsoft.tocomsede.base.ui.theme.darkBlue80
import com.ipsoft.tocomsede.base.ui.theme.gray
import com.ipsoft.tocomsede.base.ui.theme.itemDividerPadding
import com.ipsoft.tocomsede.base.ui.theme.largePadding
import com.ipsoft.tocomsede.base.ui.theme.mediumPadding
import com.ipsoft.tocomsede.base.ui.theme.smallPadding
import com.ipsoft.tocomsede.base.ui.theme.xxLargePadding
import com.ipsoft.tocomsede.base.util.network.NetworkHandler
import com.ipsoft.tocomsede.utils.UserInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navController: NavHostController,
    onLoginClick: () -> Unit
) {
    val context = LocalContext.current

    val hasInternet = remember {
        mutableStateOf(NetworkHandler(context).isNetworkAvailable())
    }
    val categoryState = homeViewModel.categories.value

    val visibility = remember { mutableStateOf(false) }

    val isUserLogged = homeViewModel.isUserLogged.value

    val storeState = homeViewModel.store.value

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
                categoryState.error?.let {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Center
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Text(text = it)
                            Spacer(modifier = Modifier.padding(mediumPadding))
                            ElevatedButton(
                                onClick = { homeViewModel.loadItems() },
                                modifier = Modifier.wrapContentSize()
                            ) {
                                Text(text = stringResource(id = R.string.try_again))
                            }
                        }
                    }
                }

                if (categoryState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Center
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
                            .padding(0.dp, smallPadding, 0.dp, 0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Column {
                                if (isUserLogged) {
                                    UserBanner()
                                }

                                if (!storeState.isOpen) {
                                    Text(
                                        text = stringResource(id = R.string.store_closed),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(largePadding),
                                        color = Color.Red
                                    )
                                }

                                LazyColumn {
                                    categoryState.item.forEach { category ->
                                        item {
                                            HomeCategoryList(
                                                category = category,
                                                navController = navController
                                            )
                                        }
                                        if (categoryState.item.last() == category && !isUserLogged) {
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
                    contentAlignment = Center
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
fun UserBanner() {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = darkBlue80
    ) {
        Text(
            text = stringResource(id = R.string.user_banner_welcome).format(
                UserInfo.loggedUser?.name?.substringBefore(
                    " "
                )
            ),
            modifier = Modifier.padding(smallPadding),
            style = MaterialTheme.typography.titleSmall
        )
    }
}
