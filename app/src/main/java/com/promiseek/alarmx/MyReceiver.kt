package com.promiseek.alarmx

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext


class MyReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var packageList = intent!!.getStringArrayListExtra("selectedApps")
        Log.i("dfjejfje", packageList.toString())
        Log.i("dfjejfje", "fksdjfsa")
        packageList!!.forEach {
            killAppBypackage(it,context)
        }

    }

    private fun killAppBypackage(packageTokill: String,context: Context?) {
        val packages: List<ApplicationInfo>
        val pm: PackageManager
        pm = context!!.getPackageManager()
        //get a list of installed apps.
        packages = pm.getInstalledApplications(0)
        val mActivityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val myPackage: String =
            ApplicationProvider.getApplicationContext<Context>().getPackageName()
        for (packageInfo in packages) {
            if (packageInfo.flags and ApplicationInfo.FLAG_SYSTEM == 1) {
                continue
            }
            if (packageInfo.packageName == myPackage) {
                continue
            }
            if (packageInfo.packageName == packageTokill) {
                Log.i("trucalled ", "Founddddd")
                mActivityManager.killBackgroundProcesses(packageInfo.packageName)
            }
        }
    }


}



//        val mp = MediaPlayer.create(intent!!, Settings.System.DEFAULT_RINGTONE_URI)

//        mp.isLooping=true
//        mp.start()