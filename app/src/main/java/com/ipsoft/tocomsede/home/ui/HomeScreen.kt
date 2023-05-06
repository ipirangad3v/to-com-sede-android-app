package com.ipsoft.tocomsede.home.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ipsoft.tocomsede.R
import com.ipsoft.tocomsede.core.ui.theme.gray
import com.ipsoft.tocomsede.data.network.NetworkHandler

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val context = LocalContext.current

    val hasInternet = remember {
        mutableStateOf(NetworkHandler(context).isNetworkAvailable())
    }
    val categoryState = homeViewModel.categories.value

    val visibility = remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = gray
    ) {
        if (hasInternet.value) {
            categoryState.error?.let {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Center
                ) {
                    Text(text = it)
                    Spacer(modifier = Modifier.padding(8.dp))
                    Button(
                        onClick = { homeViewModel.loadItems() },
                        modifier = Modifier.wrapContentSize()
                    ) {
                        Text(text = stringResource(id = R.string.try_again))
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
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        LazyColumn {
                            categoryState.item.forEach { category ->
                                item {
                                    HomeCategoryList(
                                        category = category,
                                        navController = navController
                                    )
                                }
                                item { Spacer(modifier = Modifier.padding(8.dp)) }
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
                    Spacer(modifier = Modifier.padding(8.dp))
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
