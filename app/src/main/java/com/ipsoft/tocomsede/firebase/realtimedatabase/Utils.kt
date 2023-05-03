package com.ipsoft.tocomsede.firebase.realtimedatabase

import com.google.firebase.auth.FirebaseAuth

fun isUserLogged() = FirebaseAuth.getInstance().currentUser != null