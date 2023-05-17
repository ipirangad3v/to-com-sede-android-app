package com.ipsoft.tocomsedeadmin.feature.notifications

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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ipsoft.tocomsede.core.model.FirebaseToComSedeUser
import com.ipsoft.tocomsede.core.model.Order
import com.ipsoft.tocomsede.core.utils.UserInfo.userUid
import com.ipsoft.tocomsedeadmin.MainActivity
import com.ipsoft.tocomsedeadmin.R.drawable
import com.ipsoft.tocomsedeadmin.R.mipmap
import com.ipsoft.tocomsedeadmin.R.string
import com.ipsoft.tocomsedeadmin.base.util.Constants.NOTIFICATION_CHANNEL_DESCRIPTION
import com.ipsoft.tocomsedeadmin.base.util.Constants.NOTIFICATION_CHANNEL_ID
import com.ipsoft.tocomsedeadmin.base.util.Constants.NOTIFICATION_CHANNEL_NAME

class NotificationService : Service() {

    private lateinit var intent: Intent
    private lateinit var context: Context

    private lateinit var databaseRef: DatabaseReference
    private lateinit var valueEventListener: ValueEventListener
    private lateinit var notificationManager: NotificationManager

    private val orders = mutableListOf<Order>()

    private val sendNotification
        get() = orders.isNotEmpty()

    private fun listsAreDifferent(
        orders: List<Order>,
        receivedOrders: List<Order>
    ): Boolean {
        if (orders.size != receivedOrders.size) return true
        return false
    }

    override fun onCreate() {
        super.onCreate()

        context = applicationContext

        intent = Intent(context, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_MUTABLE
        )

        // Configurar o Firebase Realtime Database
        FirebaseApp.initializeApp(this)
        val database = FirebaseDatabase.getInstance()
        if (userUid == null) return
        databaseRef = database.getReference("users")

        // Configurar o gerenciador de notificações
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()

        val ordersListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(FirebaseToComSedeUser::class.java)
                    if (user?.orders != null) {
                        val receivedOrders =
                            user.orders?.map { it.value.copy(id = it.key) } ?: emptyList()
                        if (listsAreDifferent(orders, receivedOrders)) {
                            if (sendNotification) {
                                sendNotification(pendingIntent)
                            }
                            orders.clear()
                            orders.addAll(receivedOrders)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) = Unit
        }
        databaseRef.addValueEventListener(ordersListener)
    }

    private fun sendNotification(pendingIntent: PendingIntent) {
        val notificationBuilder =
            NotificationCompat.Builder(
                this@NotificationService,
                NOTIFICATION_CHANNEL_ID
            )
                .setContentTitle(
                    getString(string.new_order)
                )
                .setSmallIcon(mipmap.ic_launcher)
                .setContentText(
                    getString(string.notification_message)
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(
                    drawable.ic_check_order,
                    getString(string.view_order),
                    pendingIntent
                )
        notificationManager.notify(
            0,
            notificationBuilder.build()
        )
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
