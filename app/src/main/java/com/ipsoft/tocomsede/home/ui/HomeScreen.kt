package com.ipsoft.tocomsede.home.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ipsoft.tocomsede.R

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navController: NavHostController,
) {

    val itemState = homeViewModel.items.value

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {

        itemState.error?.let {

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

        if (itemState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Center
            ) {
                CircularProgressIndicator(modifier = Modifier.wrapContentSize())
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                HomeItemList(itemState, navController)
            }
        }

    }
}