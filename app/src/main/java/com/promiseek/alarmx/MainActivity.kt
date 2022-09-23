package com.promiseek.alarmx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import androidx.lifecycle.Observer
import com.promiseek.alarmx.Database.AlarmDatabase

class MainActivity : AppCompatActivity() {
    companion object{
        open lateinit var database:AlarmDatabase


    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val alarmListView = findViewById(R.id.listOfAlarm) as ListView
        alarmListView.emptyView = findViewById(R.id.empty_view)

        val addTimer = findViewById(R.id.addTimer) as ImageView
        addTimer.setOnClickListener(View.OnClickListener {
            startActivity(Intent(applicationContext,Timer_Setting::class.java))


        })

        database = AlarmDatabase.getDatabase(this)
        database.alarmDao().getAlarms().observe(this, Observer {

            alarmListView.adapter = ArrayAdapterOfAlarmList(it,this)
        })


    }




}


