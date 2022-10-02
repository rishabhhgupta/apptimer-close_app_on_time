package com.promiseek.alarmx

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class MyReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val alarmIntent = Intent(context, WakeupAlarmActivity::class.java)
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        //open Activitiy
        //open Activitiy
        context!!.startActivity(alarmIntent)
    }



}



//        val mp = MediaPlayer.create(intent!!, Settings.System.DEFAULT_RINGTONE_URI)

//        mp.isLooping=true
//        mp.start()