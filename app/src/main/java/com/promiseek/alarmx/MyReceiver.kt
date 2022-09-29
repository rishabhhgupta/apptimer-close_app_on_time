package com.promiseek.alarmx

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Build.VERSION.SDK
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.promiseek.alarmx.MainActivity.Companion.notification


class MyReceiver: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        //    notification(p0!!,p1.getIntExtra("id",0).toString())
//
        var mp = MediaPlayer.create(p0, Settings.System.DEFAULT_RINGTONE_URI)
        mp.isLooping=true
        mp.start()
    }



}