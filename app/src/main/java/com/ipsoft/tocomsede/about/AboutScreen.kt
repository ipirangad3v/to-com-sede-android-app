package com.ipsoft.tocomsede.about

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ipsoft.tocomsede.R
import com.ipsoft.tocomsede.core.ui.components.SquaredButton
import com.ipsoft.tocomsede.core.ui.theme.largePadding
import com.ipsoft.tocomsede.core.ui.theme.mediumPadding
import com.ipsoft.tocomsede.core.ui.theme.smallPadding
import com.ipsoft.tocomsede.utils.UserInfo

@Composable
fun AboutScreen(
    viewModel: AboutViewModel = hiltViewModel(),
    onAddressesClick: () -> Unit,
    onLoginClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val isUserLoggedState = viewModel.isUserLogged.value

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = CenterHorizontally
        ) {
            if (isUserLoggedState) {
                item { UserInfoBanner() }
                item { Spacer(modifier = Modifier.padding(mediumPadding)) }

                item {
                    MenuItem(Icons.Filled.LocationOn, stringResource(id = R.string.addresses)) {
                        onAddressesClick()
                    }
                }
                if (isUserLoggedState) {
                    item { Spacer(modifier = Modifier.padding(mediumPadding)) }
                    item {
                        LogoutButton {
                            onLogoutClick()
                        }
                    }
                }
            } else {
                item {
                    Button(onClick = onLoginClick) {
                        Text(text = "Login")
                    }
                }
            }
        }
    }
}

@Composable
fun LogoutButton(onLogoutClick: () -> Unit = {}) {
    SquaredButton(
        onClick = onLogoutClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Text(
            text = stringResource(id = R.string.logout),
            modifier = Modifier
                .wrapContentSize()
                .padding(largePadding)
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserInfoBanner() {
    val user = UserInfo.loggedUser

    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(mediumPadding),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        GlideImage(
            model = user?.photoUrl,
            contentDescription = null,
            modifier = Modifier.clip(shape = CircleShape)
        )
        Spacer(modifier = Modifier.padding(smallPadding))
        user?.name?.let { Text(text = it, style = MaterialTheme.typography.titleLarge) }
    }
}

@Composable
fun MenuItem(imageVector: ImageVector, title: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, smallPadding, 0.dp, smallPadding)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(mediumPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(imageVector = imageVector, contentDescription = null)
            Text(
                text = title,
                modifier = Modifier.padding(mediumPadding),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}
