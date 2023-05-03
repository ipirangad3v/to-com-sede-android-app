package com.ipsoft.tocomsede.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ipsoft.tocomsede.base.model.Item

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CardItem(item: Item) {
    Card {
        Column {
            GlideImage(model = item.imageUrl, contentDescription = item.description)
            (Text(text = item.name))
        }
    }
}