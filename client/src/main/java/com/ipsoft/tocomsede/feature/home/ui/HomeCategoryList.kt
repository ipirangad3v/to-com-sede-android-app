package com.ipsoft.tocomsede.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ipsoft.tocomsede.base.ui.theme.mediumPadding
import com.ipsoft.tocomsede.base.ui.theme.smallPadding

@Composable
fun HomeCategoryList(category: com.ipsoft.tocomsede.core.model.Category, navController: NavHostController) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(smallPadding)

        ) {
            val height =
                if (category.items.size >= 2) ((category.items.size * 250) / 2).dp else ((category.items.size * 250)).dp

            category.name?.let { Text(text = it) }
            Spacer(modifier = Modifier.padding(smallPadding))
            LazyVerticalGrid(
                modifier = Modifier.height(height),
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(mediumPadding),
                verticalArrangement = Arrangement.spacedBy(mediumPadding)
            ) {
                category.items.let { items ->
                    items(items.size) {
                        items[it]?.let { cardItem ->
                            CardListItem(
                                item = cardItem,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}
