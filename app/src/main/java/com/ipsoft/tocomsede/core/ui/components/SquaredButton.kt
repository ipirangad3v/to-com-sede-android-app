package com.ipsoft.tocomsede.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ipsoft.tocomsede.core.ui.theme.almostWhite
import com.ipsoft.tocomsede.core.ui.theme.darkBlue80

@Composable
fun SquaredButton(modifier: Modifier = Modifier, text: String = "Button", onClick: () -> Unit = {}) {
    ElevatedCard(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .clickable { onClick() }
            .wrapContentSize(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = darkBlue80,
            contentColor = almostWhite
        )
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp),
            maxLines = 1
        )
    }
}
