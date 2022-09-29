package com.promiseek.alarmx.fakeWorks

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.promiseek.alarmx.R

class FakeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_Translucent)
        Log.d("AppLog", "FakeActivity")
        finish()
    }
}
