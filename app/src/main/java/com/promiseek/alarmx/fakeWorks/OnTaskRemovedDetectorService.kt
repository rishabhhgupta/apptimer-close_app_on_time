package com.promiseek.alarmx.fakeWorks

import android.app.Service
import android.content.Intent
import android.util.Log

class OnTaskRemovedDetectorService : Service() {
    override fun onBind(intent: Intent?) = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int) = START_STICKY

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.e("AppLog", "onTaskRemoved")
        applicationContext.startActivity(Intent(this, FakeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        stopSelf()
    }

}