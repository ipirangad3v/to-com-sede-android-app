package com.ipsoft.tocomsede.core

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ipsoft.tocomsede.base.model.Item
import com.ipsoft.tocomsede.core.ui.theme.ToComSedeTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var database: FirebaseDatabase

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
        FirebaseApp.initializeApp(this)

        val items = database.getReference("items")

        items.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(Item::class.java)
                    Log.d("TAG", "Item name: ${item?.name}")
                    Log.d("TAG", "Item name: ${item?.id}")
                    Log.d("TAG", "Item name: ${item?.category}")
                    Log.d("TAG", "Item name: ${item?.description}")
                    Log.d("TAG", "Item name: ${item?.price}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })


        setContent {
            ToComSedeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    val navController = rememberNavController()
//
//                    NavHost(navController = navController, startDestination = "profile") {
////                        composable("profile") { ContactsContract.Profile(/*...*/) }
////                        composable("friendslist") { FriendsList(/*...*/) }
////                        /*...*/
//                    }

                    Button(onClick = {
                        signInLauncher.launch(
                            AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build()
                        )
                    }, modifier = Modifier.wrapContentSize()) {
                        Text(text = "Login")

                    }

                }
            }
        }
    }


    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            FirebaseAuth.getInstance().currentUser?.let { user ->
                Log.d("TAG", "onSignInResult: ${user.email}")
                Log.d("TAG", "onSignInResult: ${user.displayName}")
                Log.d("TAG", "onSignInResult: ${user.phoneNumber}")
                Log.d("TAG", "onSignInResult: ${user.photoUrl}")
                // ...
            }
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }
}
