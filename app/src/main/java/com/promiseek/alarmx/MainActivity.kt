package com.promiseek.alarmx

import android.app.*
import android.app.AlarmManager.AlarmClockInfo
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.nfc.NfcAdapter.EXTRA_ID
import android.os.Build
import android.os.Bundle
import android.util.Log
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

        //setAlarm method set alarm
        fun setAlarm(c: Calendar, context: Context, tick:Long, dayChoosen:ArrayList<String>) {
            val manager = context.getSystemService(ALARM_SERVICE) as AlarmManager






            //alarm fire next day if this condition is not statisfied
//        if (c.before(Calendar.getInstance())) {
//            c.add(Calendar.DATE, 1)
//        }
            //set alarm
//        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.timeInMillis, 24*60*60*1000,intentAlarm)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////                manager.setAlarmClock(AlarmClockInfo(System.currentTimeMillis()+10000, intentAlarm), intentAlarm
//                manager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+10000, intentAlarm);
//
//
//            } else{
//                manager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+10000,intentAlarm)
//            }

            //Notification Broadcast intent
            val intentAlarm = Intent(context, MyReceiver::class.java).let {
//                it.putExtra("daysChoosenArrayList", dayChoosen)
//                it.putExtra("id", dayChoosen)
                var flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.FLAG_MUTABLE //this is needed in Android 12
                } else {
                    PendingIntent.FLAG_CANCEL_CURRENT
                }
                PendingIntent.getBroadcast(context, tick.toInt(), it, PendingIntent.FLAG_UPDATE_CURRENT)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                manager.setAlarmClock(AlarmManager.AlarmClockInfo(System.currentTimeMillis()+10000, null), intentAlarm)
            }

//            val pendingAlarm = Intent("com.promiseek.alarmx.AAA")
//                .apply {
//                    setClass(context, MyReceiver::class.java)
//                    putExtra("id", tick.toInt())
//                    putExtra("fasdf","dfasd")
//                }
//                .let { PendingIntent.getBroadcast(context,0, it, PendingIntent.FLAG_UPDATE_CURRENT) }
//
//            val pendingShowList = PendingIntent.getActivity(
//                context,
//                0,
//                Intent(context, MainActivity::class.java),
//                PendingIntent.FLAG_UPDATE_CURRENT
//            )



        }

        //get notification
        fun notification(context: Context, text:String ){
            // pendingIntent is an intent for future use i.e after
            // the notification is clicked, this intent will come into action
            val intent = Intent(context, MainActivity::class.java)

            // FLAG_UPDATE_CURRENT specifies that if a previous
            // PendingIntent already exists, then the current one
            // will update it with the latest intent
            // 0 is the request code, using it later with the
            // same method again will get back the same pending
            // intent for future reference
            // intent passed here is to our afterNotification class
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            // RemoteViews are used to use the content of
            // some different layout apart from the current activity layout
//            val contentView = RemoteViews(context.packageName, R.layout.activity_after_notification)

            // checking if android version is greater than oreo(API 26) or not
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

//        OnTaskRemovedDetectorService()
//        notification(this,"heheh")

        alarmListView = findViewById(R.id.listOfAlarm) as ListView
//
//        alarmListView.getLayoutParams().height = alarmListView.getHeight() - getNavigationBarHeight();
//        Log.i("fehejfe", getNavigationBarHeight().toString())
        alarmListView.emptyView = findViewById(R.id.empty_view)



        addTimer = findViewById(R.id.addTimer) as ImageView
        addTimer.setOnClickListener(View.OnClickListener {
            startActivity(Intent(applicationContext,Timer_Setting::class.java))


        })

        database = AlarmDatabase.getDatabase(this)
        database.alarmDao().getAlarms().observe(this, Observer {
            var alarmManager:AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

            for (i in it){
                var alarmCalendar: Calendar =Calendar.getInstance()
//                alarmCalendar.set(Calendar.DAY_OF_WEEK,1);

                alarmCalendar.set(Calendar.HOUR,Integer.parseInt((i.time).split(" ").get(0).split(":").get(0)));
                alarmCalendar.set(Calendar.MINUTE, Integer.parseInt((i.time).split(" ").get(0).split(":").get(1)));
                alarmCalendar.set(Calendar.SECOND, 0);
                alarmCalendar.set(Calendar.AM_PM,if(i.time.split(" ").get(1)=="am")Calendar.AM else Calendar.PM)
                setAlarm(alarmCalendar,this,i.id, i.dayChosen)
                Log.i("alrmsss",alarmCalendar.time.toString())



            }
            alarmListView.adapter = ArrayAdapterOfAlarmList(it,this)

        })

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


