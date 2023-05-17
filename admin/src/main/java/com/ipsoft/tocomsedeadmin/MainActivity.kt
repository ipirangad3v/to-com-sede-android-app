package com.ipsoft.tocomsedeadmin

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.core.content.ContextCompat
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
import com.ipsoft.tocomsede.core.model.ResultState
import com.ipsoft.tocomsede.core.model.User
import com.ipsoft.tocomsede.core.utils.UserInfo
import com.ipsoft.tocomsede.core.utils.UserInfo.UserInfoListener
import com.ipsoft.tocomsede.core.utils.UserInfo.isUserLogged
import com.ipsoft.tocomsede.core.utils.UserInfo.loggedUser
import com.ipsoft.tocomsedeadmin.base.ui.components.Screen
import com.ipsoft.tocomsedeadmin.base.ui.components.Screen.Account
import com.ipsoft.tocomsedeadmin.base.ui.components.Screen.Companion.ORDER_ID
import com.ipsoft.tocomsedeadmin.base.ui.components.Screen.Companion.items
import com.ipsoft.tocomsedeadmin.base.ui.components.Screen.Home
import com.ipsoft.tocomsedeadmin.base.ui.theme.ToComSedeTheme
import com.ipsoft.tocomsedeadmin.base.ui.theme.darkBlue80
import com.ipsoft.tocomsedeadmin.base.ui.theme.lightBlue
import com.ipsoft.tocomsedeadmin.data.user.PreferencesRepository
import com.ipsoft.tocomsedeadmin.feature.account.AccountScreen
import com.ipsoft.tocomsedeadmin.feature.home.HomeScreen
import com.ipsoft.tocomsedeadmin.feature.notifications.NotificationService
import com.ipsoft.tocomsedeadmin.feature.orderdetails.OrderDetailsScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity(), UserInfoListener {

    @Inject
    lateinit var preferencesRepository: PreferencesRepository

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    private val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build()
    )

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UserInfo.addListener(this)
        setContent {
            ToComSedeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val isDarkTheme = isSystemInDarkTheme()

                    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

                    val navController = rememberNavController()

                    val navBackStackEntry by navController.currentBackStackEntryAsState()

                    when (navBackStackEntry?.destination?.route) {
                        Screen.OrderDetails.route -> {
                            bottomBarState.value = false
                        }

                        else -> {
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
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController,
                            startDestination = Home.route,
                            Modifier.padding(innerPadding)
                        ) {
                            composable(Home.route) {
                                HomeScreen(navController = navController) {
                                    launchLoginActivity(isDarkTheme)
                                }
                            }
                            composable(
                                Screen.OrderDetails.route,
                                arguments = listOf(
                                    navArgument(ORDER_ID) {
                                        type = NavType.StringType
                                    }
                                )
                            ) { navBackEntry ->
                                OrderDetailsScreen(
                                    orderId = navBackEntry.arguments?.getString(ORDER_ID)
                                ) {
                                    navController.navigateUp()
                                }
                            }
                            composable(Account.route) {
                                AccountScreen(
                                    onLogoutClick = {
                                        firebaseLogout()
                                    },

                                    onLoginClick = {
                                        launchLoginActivity(isDarkTheme)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        askNotificationPermission()

        if (isUserLogged) {
            startNotifications()
        }
    }

    private fun startNotifications() {
        startService(Intent(this, NotificationService::class.java))
    }

    private fun firebaseLogout() {
        AuthUI.getInstance()
            .signOut(this@MainActivity)
            .addOnCompleteListener {
                lifecycleScope.launch {
                    preferencesRepository.clearUser()
                    loggedUser = null
                }
            }
    }

    private fun launchLoginActivity(isDarkTheme: Boolean) {
        if (isDarkTheme) {
            signInLauncher.launch(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setTheme(R.style.Theme_FirebaseAuthUIDark)
                    .setLogo(R.drawable.sem_fundo)
                    .build()
            )
        } else {
            signInLauncher.launch(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setLogo(R.drawable.sem_fundo)
                    .build()
            )
        }
    }

    override fun onResume() {
        super.onResume()
        loadUser()
    }

    override fun onDestroy() {
        super.onDestroy()
        UserInfo.removeListener(this)
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

                val userModel = User(
                    name = user.displayName ?: "",
                    email = user.email ?: "",
                    phone = user.phoneNumber ?: "",
                    photoUrl = user.photoUrl?.toString() ?: "",
                    uid = user.uid
                )
                loggedUser = userModel

                lifecycleScope.launch {
                    preferencesRepository.storeUser(
                        userModel
                    )
                }
            }
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // Show a dialog explaining why you need the permission
                AlertDialog.Builder(this)
                    .setTitle(resources.getString(R.string.permission_title))
                    .setMessage(resources.getString(R.string.permission_disclaimer))
                    .setPositiveButton("OK") { _, _ ->
                        // Ask for the permission
                        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                    .setNegativeButton("Cancel") { _, _ ->
                        // You can choose to not ask for the permission,
                        // but then you have to disable the features
                    }
                    .create()
                    .show()
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onUserInfoChanged(isUserLogged: Boolean) {
        askNotificationPermission()
        if (isUserLogged) {
            startNotifications()
        }
    }
}
