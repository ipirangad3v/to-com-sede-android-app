package com.ipsoft.tocomsede.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.ipsoft.tocomsede.R
import com.ipsoft.tocomsede.about.AboutScreen
import com.ipsoft.tocomsede.cart.CartScreen
import com.ipsoft.tocomsede.core.model.ResultState
import com.ipsoft.tocomsede.core.model.User
import com.ipsoft.tocomsede.core.ui.components.Screen
import com.ipsoft.tocomsede.core.ui.components.Screen.Companion.ITEM_ID
import com.ipsoft.tocomsede.core.ui.components.Screen.Companion.items
import com.ipsoft.tocomsede.core.ui.theme.ToComSedeTheme
import com.ipsoft.tocomsede.core.ui.theme.darkBlue80
import com.ipsoft.tocomsede.core.ui.theme.lightBlue
import com.ipsoft.tocomsede.data.datastore.PreferencesRepository
import com.ipsoft.tocomsede.home.ui.HomeScreen
import com.ipsoft.tocomsede.itemdetails.ItemDetailsScreen
import com.ipsoft.tocomsede.orders.OrdersScreen
import com.ipsoft.tocomsede.utils.UserInfo.loggedUser
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
        AuthUI.IdpConfig.GoogleBuilder().build()
    )

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ToComSedeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()

                    when (navBackStackEntry?.destination?.route) {
                        Screen.ItemDetails.route -> {
                            bottomBarState.value = false
                        }

                        else                     -> {
                            bottomBarState.value = true
                        }
                    }

                    Scaffold(
                        bottomBar = {
                            bottomBarState.value.let {
                                if (it) {
                                    BottomNavigation(
                                        backgroundColor = darkBlue80,
                                        modifier = Modifier
                                            .wrapContentHeight()
                                            .fillMaxWidth()
                                    ) {
                                        val currentDestination = navBackStackEntry?.destination
                                        items.forEach { screen ->
                                            val iconColor =
                                                if (currentDestination?.hierarchy?.any { currentDestination -> currentDestination.route == screen.route } == true) {
                                                    lightBlue
                                                } else {
                                                    MaterialTheme.colorScheme.background
                                                }
                                            BottomNavigationItem(
                                                icon = {
                                                    Icon(
                                                        screen.icon,
                                                        contentDescription = null,
                                                        tint = iconColor
                                                    )
                                                },
                                                selected = currentDestination?.hierarchy?.any { currentDestination -> currentDestination.route == screen.route } == true,
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
                                                },
                                            )
                                        }
                                    }
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
                                HomeScreen(navController = navController)
                            }
                            composable(Screen.Cart.route) {
                                CartScreen()
                            }
                            composable(Screen.Orders.route) {
                                OrdersScreen()
                            }
                            composable(Screen.About.route) {
                                AboutScreen {
                                    {
                                        signInLauncher.launch(
                                            AuthUI.getInstance()
                                                .createSignInIntentBuilder()
                                                .setAvailableProviders(providers)
                                                .setLogo(R.drawable.ic_launcher_foreground)
                                                .build()
                                        )
                                    }
                                }
                            }
                            composable(Screen.About.route) {
                                AboutScreen {
                                    {
                                        signInLauncher.launch(
                                            AuthUI.getInstance()
                                                .createSignInIntentBuilder()
                                                .setAvailableProviders(providers)
                                                .setLogo(R.drawable.ic_launcher_foreground)
                                                .build()
                                        )
                                    }
                                }
                            }

                            composable(
                                Screen.ItemDetails.route,
                                arguments = listOf(
                                    navArgument(ITEM_ID) {
                                        type = NavType.IntType
                                    }
                                )
                            ) { navBackEntry ->
                                ItemDetailsScreen(
                                    itemId = navBackEntry.arguments?.getInt(ITEM_ID)
                                ) {
                                    navController.navigateUp()
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
