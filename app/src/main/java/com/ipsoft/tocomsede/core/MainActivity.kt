package com.ipsoft.tocomsede.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.ipsoft.tocomsede.R
import com.ipsoft.tocomsede.core.model.User
import com.ipsoft.tocomsede.core.ui.components.Screen
import com.ipsoft.tocomsede.core.ui.components.Screen.Companion.items
import com.ipsoft.tocomsede.core.ui.theme.ToComSedeTheme
import com.ipsoft.tocomsede.data.datastore.PreferencesRepository
import com.ipsoft.tocomsede.home.ui.HomeScreen
import com.ipsoft.tocomsede.utils.Info.loggedUser
import com.ipsoft.tocomsede.utils.ResultState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesRepository: PreferencesRepository

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    private val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.PhoneBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build(),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ToComSedeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    val navController = rememberNavController()
                    Scaffold(
                        bottomBar = {
                            BottomNavigation {
                                val navBackStackEntry by navController.currentBackStackEntryAsState()
                                val currentDestination = navBackStackEntry?.destination
                                items.forEach { screen ->
                                    BottomNavigationItem(
                                        icon = {
                                            Icon(
                                                screen.icon,
                                                contentDescription = null
                                            )
                                               },
                                        label = { Text(stringResource(screen.resourceId)) },
                                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                        onClick = {
                                            navController.navigate(screen.route) {
                                                // Pop up to the start destination of the graph to
                                                // avoid building up a large stack of destinations
                                                // on the back stack as users select items
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                // Avoid multiple copies of the same destination when
                                                // reselecting the same item
                                                launchSingleTop = true
                                                // Restore state when reselecting a previously selected item
                                                restoreState = true
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController,
                            startDestination = Screen.Home.route,
                            Modifier.padding(innerPadding)
                        ) {
                            composable(Screen.Home.route) {
                                HomeScreen {
                                    signInLauncher.launch(
                                        AuthUI.getInstance()
                                            .createSignInIntentBuilder()
                                            .setAvailableProviders(providers)
                                            .setLogo(R.drawable.garafa)
                                            .build()
                                    )
                                }
                            }
                        }
                    }


                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadUser()
    }

    private fun loadUser() {
        lifecycleScope.launch {
            preferencesRepository.readUser().let { result ->
                if (result is ResultState.Success && result.data != null) {
                    loggedUser = result.data
                }
            }
        }
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            FirebaseAuth.getInstance().currentUser?.let { user ->

                lifecycleScope.launch {
                    preferencesRepository.storeUser(
                        User(
                            name = user.displayName ?: "",
                            email = user.email ?: "",
                            phone = user.phoneNumber ?: "",
                            photoUrl = user.photoUrl?.toString() ?: ""
                        )
                    ).let {
                        if (it is ResultState.Success) {
                            loadUser()
                        }

                    }
                }
            }

        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }
}
