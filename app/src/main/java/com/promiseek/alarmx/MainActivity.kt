package com.promiseek.alarmx

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.appbar.MaterialToolbar
import com.promiseek.alarmx.Database.AlarmDatabase
import com.promiseek.alarmx.Database.AlarmsPogo
import java.text.SimpleDateFormat
import java.util.*
import android.app.AlarmManager
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {



    companion object{


        open lateinit var database:AlarmDatabase
        lateinit var alarmListView:ListView
        var  selectedItems = false
        lateinit var parentToolbar:MaterialToolbar
        var alarmItemClicked=false
        // declaring variables
        lateinit var notificationManager: NotificationManager
        lateinit var notificationChannel: NotificationChannel
        lateinit var builder: Notification.Builder
        private val channelId = "i.apps.notifications"
        private val description = "Test notification"


        var alarmManager: AlarmManager?=null
        lateinit var pendingIntent: PendingIntent
        lateinit var myIntent:Intent



        var selectedAlams= ArrayList<AlarmsPogo>()

        //hide and show three dots and search menu when profile fragment shown
        lateinit var menu: Menu
        lateinit var selectAll: MenuItem
        lateinit var addTimer:ImageView



        fun currentDateTimeDay():ArrayList<String>{
            var currentDateTimeDay:ArrayList<String> = ArrayList()
            val formatter = SimpleDateFormat("EEE dd/MM/yyyy HH:mm:ss")
            val date = Date()
            currentDateTimeDay.addAll(formatter.format(date).split(" "))
            return  currentDateTimeDay
        }


        //get notification
        fun notification(context: Context, text:String ){
            // pendingIntent is an intent for future use i.e after
            // the notification is clicked, this intent will come into action
            val intent = Intent(context, MainActivity::class.java)

            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.GREEN
                notificationChannel.enableVibration(false)

                notificationManager.createNotificationChannel(notificationChannel)

                builder = Notification.Builder(context, channelId)
                    .setContentText(text)

//                    .setContent(contentView)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_background))
                    .setContentIntent(pendingIntent)
            } else {

                builder = Notification.Builder(context)
                    .setContentText("heheh")
//                    .setContent(contentView)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_background))
                    .setContentIntent(pendingIntent)
            }
            notificationManager.notify(1234, builder.build())
        }

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmListView = findViewById(R.id.listOfAlarm) as ListView

        alarmListView.emptyView = findViewById(R.id.empty_view)
        myIntent = Intent(this, MyReceiver::class.java)

        addTimer = findViewById(R.id.addTimer) as ImageView
        addTimer.setOnClickListener(View.OnClickListener {
            startActivity(Intent(applicationContext,Timer_Setting::class.java))


        })

        database = AlarmDatabase.getDatabase(this)
        database.alarmDao().getAlarms().observe(this, Observer {
            var alarmManager:AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager


            alarmListView.adapter = ArrayAdapterOfAlarmList(it,this)

        })


        GlobalScope.launch(Dispatchers.Default) {
            Log.i("efefhhe",database.alarmDao().getLastId().toString())
        }
        parentToolbar = findViewById<MaterialToolbar>(R.id.parentToolbar)
        parentToolbar.inflateMenu(R.menu.parent_main_menu)
//        parentToolbar.setNavigationIcon(resources.getDrawable(R.drawable.ic_baseline_arrow_back_24))
        menu = parentToolbar.getMenu()
        parentToolbar.setNavigationIcon(null)
        menu =parentToolbar.menu
        selectAll =menu.findItem(R.id.selectAll)
        selectAll.setVisible(false)


        parentToolbar.setBackgroundColor(resources.getColor(R.color.toolbarColor))



    }



}


