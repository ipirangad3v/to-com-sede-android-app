package com.ipsoft.tocomsede.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ipsoft.tocomsede.R

@Composable
fun HomeItemList(itemState: ItemsState, navController: NavHostController) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)

        ) {
            Text(text = stringResource(R.string.top_products))
            Spacer(modifier = Modifier.padding(4.dp))
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                itemState.item.let { items ->
                    items(items.size) {
                        Spacer(modifier = Modifier.padding(8.dp))
                        CardListItem(item = items[it], navController = navController)
                    }
                }
            }
        }
    }
}
