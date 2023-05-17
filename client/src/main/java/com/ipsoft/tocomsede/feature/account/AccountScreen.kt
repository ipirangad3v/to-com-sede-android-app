package com.ipsoft.tocomsede.feature.account

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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
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
import com.ipsoft.tocomsede.base.extensions.getVerCode
import com.ipsoft.tocomsede.base.ui.theme.darkBlue80
import com.ipsoft.tocomsede.base.ui.theme.largePadding
import com.ipsoft.tocomsede.base.ui.theme.mediumPadding
import com.ipsoft.tocomsede.base.ui.theme.smallPadding
import com.ipsoft.tocomsede.core.utils.UserInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    viewModel: AccountViewModel = hiltViewModel(),
    onAddressesClick: () -> Unit,
    onLoginClick: () -> Unit,
    onPhoneClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
) {
    val isUserLoggedState = viewModel.isUserLogged.value
    var showDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(id = R.string.confirm_logout)) },
            text = { Text(stringResource(id = R.string.confirm_logout_ask)) },
            confirmButton = {
                ElevatedButton(
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

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(id = R.string.confirm_account_deletion)) },
            text = { Text(stringResource(id = R.string.confirm_account_deletion_ask)) },
            confirmButton = {
                ElevatedButton(
                    onClick = {
                        onDeleteAccountClick()
                        showDeleteDialog = false
                    }
                ) {
                    Text(stringResource(id = R.string.delete_account))
                }
            }
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.account),
                        maxLines = 1
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = darkBlue80,
                    navigationIconContentColor = darkBlue80
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.padding(mediumPadding))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(mediumPadding),
                verticalArrangement = Arrangement.Top,
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
                    item { Spacer(modifier = Modifier.padding(mediumPadding)) }
                    item {
                        LogoutButton {
                            showDialog = true
                        }
                    }
                    item {
                        DeleteAccountButton {
                            showDeleteDialog = true
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
}

@Composable
fun DeleteAccountButton(function: () -> Unit) {
    TextButton(
        onClick = function,
        modifier = Modifier
            .fillMaxWidth()
            .padding(mediumPadding),
        colors = ButtonDefaults.textButtonColors(
            contentColor = Color.Red
        )
    ) {
        Text(
            text = stringResource(id = R.string.delete_account),
            style = MaterialTheme.typography.titleMedium
        )
    }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserInfoBanner() {
    val user = UserInfo.loggedUser

    Surface(modifier = Modifier.fillMaxWidth()) {
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
