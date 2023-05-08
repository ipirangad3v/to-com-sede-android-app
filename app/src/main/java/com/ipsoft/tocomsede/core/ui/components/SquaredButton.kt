package com.ipsoft.tocomsede.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ipsoft.tocomsede.core.ui.theme.almostWhite
import com.ipsoft.tocomsede.core.ui.theme.darkBlue80

@Composable
fun SquaredButton(
    modifier: Modifier = Modifier,
    colors: CardColors = CardDefaults.elevatedCardColors(
        containerColor = darkBlue80,
        contentColor = almostWhite
    ),
    onClick: () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    ElevatedCard(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .clickable { onClick() },
        colors = colors,
        elevation = CardDefaults.elevatedCardElevation(6.dp)
    ) {
        content()
    }
}
