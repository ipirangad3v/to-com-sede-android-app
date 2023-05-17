package com.ipsoft.tocomsedeadmin.feature.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ipsoft.tocomsede.core.utils.UserInfo
import com.ipsoft.tocomsedeadmin.R
import com.ipsoft.tocomsedeadmin.base.ui.theme.darkBlue80
import com.ipsoft.tocomsedeadmin.base.ui.theme.largePadding
import com.ipsoft.tocomsedeadmin.base.ui.theme.mediumPadding
import com.ipsoft.tocomsedeadmin.base.ui.theme.smallPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    viewModel: AccountViewModel = hiltViewModel(),
    onLoginClick: () -> Unit,
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
                    item { Spacer(modifier = Modifier.padding(mediumPadding)) }
                    item {
                        LogoutButton {
                            showDialog = true
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
        }
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
