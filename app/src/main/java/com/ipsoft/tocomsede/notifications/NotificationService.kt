package com.ipsoft.tocomsede.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ipsoft.tocomsede.MainActivity
import com.ipsoft.tocomsede.R.drawable
import com.ipsoft.tocomsede.R.mipmap
import com.ipsoft.tocomsede.R.string
import com.ipsoft.tocomsede.core.model.Order
import com.ipsoft.tocomsede.core.model.OrderStatus
import com.ipsoft.tocomsede.core.ui.components.Screen
import com.ipsoft.tocomsede.core.util.Constants.NOTIFICATION_CHANNEL_DESCRIPTION
import com.ipsoft.tocomsede.core.util.Constants.NOTIFICATION_CHANNEL_ID
import com.ipsoft.tocomsede.core.util.Constants.NOTIFICATION_CHANNEL_NAME
import com.ipsoft.tocomsede.utils.UserInfo.userUid

class NotificationService : Service() {

    private lateinit var intent: Intent
    private lateinit var context: Context

    private lateinit var databaseRef: DatabaseReference
    private lateinit var valueEventListener: ValueEventListener
    private lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()

        context = applicationContext

        intent = Intent(context, MainActivity::class.java)

        intent.putExtra("NavitateTo", Screen.Orders.route)

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_MUTABLE
        )

        // Configurar o Firebase Realtime Database
        FirebaseApp.initializeApp(this)
        val database = FirebaseDatabase.getInstance()
        databaseRef = database.getReference("users").child(userUid!!).child("orders")

        // Configurar o gerenciador de notificações
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()

        databaseRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) = Unit

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val order = snapshot.getValue(Order::class.java)
                if (order != null) {
                    // Enviar uma notificação local
                    val notificationBuilder =
                        NotificationCompat.Builder(
                            this@NotificationService,
                            NOTIFICATION_CHANNEL_ID
                        )
                            .setContentTitle(
                                getString(string.status_changed)
                            )
                            .setSmallIcon(mipmap.ic_launcher)
                            .setContentText(
                                getString(string.notification_message).format(
                                    when (order.status) {
                                        OrderStatus.CONFIRMED -> getString(string.confirmed)
                                        OrderStatus.CANCELED -> getString(string.canceled)
                                        OrderStatus.DELIVERING -> getString(string.delivering)
                                        OrderStatus.CONCLUDED -> getString(string.concluded)
                                        OrderStatus.PENDING -> getString(string.pending)
                                    }
                                )
                            )
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT).addAction(
                                drawable.ic_check_order,
                                getString(string.view_order),
                                pendingIntent
                            )
                    notificationManager.notify(
                        0,
                        notificationBuilder.build()
                    )
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) = Unit
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) = Unit

            override fun onCancelled(error: DatabaseError) = Unit
        })
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = NOTIFICATION_CHANNEL_DESCRIPTION
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remover o ouvinte do Firebase Realtime Database
        if (::databaseRef.isInitialized && ::valueEventListener.isInitialized) {
            databaseRef.removeEventListener(valueEventListener)
        }
    }

    override fun onBind(p0: Intent?): IBinder? = null
}
