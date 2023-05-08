package com.ipsoft.tocomsede.checkout.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ipsoft.tocomsede.R
import com.ipsoft.tocomsede.cart.CartItemList
import com.ipsoft.tocomsede.core.ui.components.SquaredButton
import com.ipsoft.tocomsede.core.ui.theme.gray

@Composable
fun CheckoutScreen(
    checkoutViewModel: CheckoutViewModel = hiltViewModel(),
    onCheckoutClick: () -> Unit
) {
    val cartItemState = checkoutViewModel.cartItemState.value
    val cartTotalState = checkoutViewModel.cartTotalState.value

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = gray
    ) {
        cartItemState.error?.let {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = it)
                Spacer(modifier = Modifier.padding(8.dp))
                Button(
                    onClick = { checkoutViewModel.loadCart() },
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
                    LazyColumn {
                        item { CheckoutHeader() }
                        item { CartItemList(cartItemState) }
                        item { CheckoutCartTotal(cartTotalState) }
                        item { Spacer(modifier = Modifier.padding(8.dp)) }
                        item { CheckoutButtonContainer(onCheckoutClick) }
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
fun CheckoutButtonContainer(onCheckoutClick: () -> Unit) {
    Surface {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            SquaredButton(
                onClick = onCheckoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Text(
                    text = stringResource(id = R.string.checkout),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun CheckoutHeader() {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(0.dp, 0.dp, 0.dp, 8.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        shape = MaterialTheme.shapes.extraSmall,
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White
        )
    ) {
        Text(
            text = stringResource(id = R.string.checkout),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CheckoutCartTotal(cartTotalState: String) {
    Surface {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = stringResource(id = R.string.total) + " = $cartTotalState",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
