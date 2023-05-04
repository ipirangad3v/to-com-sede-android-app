package com.ipsoft.tocomsede.itemdetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.ipsoft.tocomsede.R
import com.ipsoft.tocomsede.core.ui.theme.lightBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailsScreen(itemId: Int?, onBack: () -> Unit) {

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null, modifier = Modifier.clickable { onBack.invoke() }
                    )
                },
                title = {
                    Text(
                        text = stringResource(id = R.string.item_details),
                    )
                },
                backgroundColor = lightBlue
            )
        }, content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                Text(text = "Item Details $itemId")
            }
        })
}