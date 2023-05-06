package com.ipsoft.tocomsede.cart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ipsoft.tocomsede.R

@Composable
fun CartScreen(cartViewModel: CartViewModel = hiltViewModel()) {
    val cartItemState = cartViewModel.cartItemState.value
    val cartTotalState = cartViewModel.cartTotalState.value

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        cartItemState.error?.let {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = it)
                Spacer(modifier = Modifier.padding(8.dp))
                Button(
                    onClick = { cartViewModel.loadCart() },
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
                Box(modifier = Modifier.fillMaxSize()) {
                    Row {
                        CartItemList(cartItemState)
                        CartTotal(cartTotalState)
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


@Composable
fun CartTotal(cartTotalState: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Text(text = stringResource(id = R.string.total, cartTotalState))
    }
}
