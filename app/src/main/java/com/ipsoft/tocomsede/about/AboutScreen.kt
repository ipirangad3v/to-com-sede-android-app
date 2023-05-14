package com.ipsoft.tocomsede.about

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ipsoft.tocomsede.R
import com.ipsoft.tocomsede.core.extensions.getVerCode
import com.ipsoft.tocomsede.core.ui.theme.largePadding
import com.ipsoft.tocomsede.core.ui.theme.mediumPadding
import com.ipsoft.tocomsede.core.ui.theme.smallPadding
import com.ipsoft.tocomsede.utils.UserInfo

@Composable
fun AboutScreen(
    viewModel: AboutViewModel = hiltViewModel(),
    onAddressesClick: () -> Unit,
    onLoginClick: () -> Unit,
    onPhoneClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val isUserLoggedState = viewModel.isUserLogged.value
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(id = R.string.confirm_logout)) },
            text = { Text(stringResource(id = R.string.confirm_logout_ask)) },
            confirmButton = {
                Button(
                    onClick = {
                        onLogoutClick()
                        showDialog = false
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        Spacer(modifier = Modifier.padding(mediumPadding))
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
                item {
                    MenuItem(
                        imageVector = Icons.Filled.Call,
                        title = stringResource(id = R.string.phone)
                    ) {
                        onPhoneClick()
                    }
                }
                if (isUserLoggedState) {
                    item { Spacer(modifier = Modifier.padding(mediumPadding)) }
                    item {
                        LogoutButton {
                            showDialog = true
                        }
                    }
                }
            } else {
                item {
                    Text(
                        text = stringResource(id = R.string.login_text),
                        Modifier.padding(mediumPadding),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.padding(mediumPadding))
                    ElevatedButton(onClick = onLoginClick) {
                        Text(
                            text = stringResource(id = R.string.login),
                            Modifier.padding(largePadding)
                        )
                    }
                }
            }
        }
        InfoFooter()
    }
}

@Composable
fun InfoFooter() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(mediumPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = stringResource(id = R.string.version).format(LocalContext.current.getVerCode()),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun LogoutButton(onLogoutClick: () -> Unit = {}) {
    ElevatedButton(
        onClick = onLogoutClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Text(
            text = stringResource(id = R.string.logout),
            modifier = Modifier
                .wrapContentSize()
                .padding(largePadding),
            color = Color.Black
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
