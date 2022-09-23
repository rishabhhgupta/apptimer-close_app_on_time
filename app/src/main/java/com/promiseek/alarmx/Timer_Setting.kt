package com.promiseek.alarmx

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import ca.antonious.materialdaypicker.MaterialDayPicker
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.promiseek.alarmx.Database.AlarmsPogo
import com.promiseek.alarmx.MainActivity.Companion.database
import kotlinx.coroutines.*
import java.sql.Time
import java.text.Format
import java.text.SimpleDateFormat


class Timer_Setting : AppCompatActivity(){
    var dayPicker: MaterialDayPicker? = null
    var dayTextView:TextView?=null


   companion object{
       lateinit var selectedAppsListView:ArrayList<String>
   }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer_setting)

        var time_picker = findViewById(R.id.timePicker) as TimePicker
        time_picker.setIs24HourView(false)

        var timeScheduler = findViewById(R.id.timeScheduler) as ConstraintLayout
        var appsListView = findViewById(R.id.appsListView) as ListView
        appsListView.emptyView = findViewById(R.id.listEmptyView)
        selectedAppsListView = ArrayList()
        var selectedDaysArrayList = ArrayList<String>()

        //day picker
        dayPicker = findViewById<MaterialDayPicker>(R.id.day_picker)


        //day text view
        dayTextView = findViewById(R.id.dayTextView)
        Log.i("heheheh", selectedAppsListView.toString())

        //toolbar
        var toolbar:MaterialToolbar = findViewById(R.id.toolBar)
        toolbar.inflateMenu(R.menu.main_menu)
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)


//       get all the app info
        lifecycleScope.launch(Dispatchers.Default) {
            var appsArrayAdapter = AppsArrayAdapter(installedApps(), context = applicationContext )
            withContext(Dispatchers.Main){
                appsListView.adapter = appsArrayAdapter
            }
        }


        //show bottomsheet dialog
        timeScheduler.setOnClickListener(View.OnClickListener {
            showBottomSheetDialog()
        })


        toolbar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener {
            if (it.itemId==R.id.setNewAlarm){

                val userSetTime= getTimesorted(time_picker.currentHour,time_picker.currentMinute)


                selectedDaysArrayList.clear()
                val selectedDay = dayPicker!!.selectedDays
                if(selectedDay.size==0){
                    if(dayTextView!!.text.equals("Custom")){
                        Toast.makeText(this, "Please select day", Toast.LENGTH_SHORT).show()

                    }else{
                        selectedDaysArrayList.add(dayTextView!!.text.toString())
                    }

                }else{
                    for(i in dayPicker!!.selectedDays){
                        selectedDaysArrayList.add(i.toString())
                    }
                }

                if(selectedDaysArrayList.size>0){
                    var alarmPogo = AlarmsPogo(id = 0,time = userSetTime!!, dayChosen = selectedDaysArrayList, packageNames = selectedAppsListView,onOrOf = true)
                    lifecycleScope.launch(Dispatchers.Default) {
                        database.alarmDao().insert(alarmPogo)
                    }

                    onBackPressed()


                }else{
                    Toast.makeText(this, "Please select day", Toast.LENGTH_SHORT).show()
                }


            }
            false
        })
        toolbar.setNavigationOnClickListener(View.OnClickListener {
            onBackPressed()
        })

    }

    //gettimeInAMandPM
    fun getTimesorted(hr: Int, min: Int): String? {
        val tme = Time(hr, min, 0) //seconds by default set to zero
        val formatter: Format
        formatter = SimpleDateFormat("h:mm a")
        return formatter.format(tme)
    }


    //bottomShhetDialog

    fun  showBottomSheetDialog() {

        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout)

        val once = bottomSheetDialog.findViewById<TextView>(R.id.once_textView)
        val daily = bottomSheetDialog.findViewById<TextView>(R.id.daily_textView)
        val custom = bottomSheetDialog.findViewById<TextView>(R.id.custom_textView)

        once!!.setOnClickListener(View.OnClickListener {
            dayPicker!!.visibility = GONE
            dayTextView!!.text = "Once"
            bottomSheetDialog.cancel()



        })

        daily!!.setOnClickListener(View.OnClickListener {
            dayPicker!!.visibility = GONE
            dayTextView!!.text = "Daily"
            bottomSheetDialog.cancel()

        })

        custom!!.setOnClickListener(View.OnClickListener {
            dayPicker!!.visibility = VISIBLE
            dayTextView!!.text = "Custom"
            bottomSheetDialog.cancel()

        })



        bottomSheetDialog.show()

    }


        //get all the intalled apps
    suspend fun installedApps():ArrayList<AppPogo>{
        var appInfoArrayList:ArrayList<AppPogo> = ArrayList()
        val pm = packageManager

        val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)


        for (packageInfo  in packages) {
            if(packageInfo!=null){
                // apps with launcher intent
                // apps with launcher intent
                if (packageInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP !== 0) {
                    // updated system apps
                    var appPogo:AppPogo = AppPogo(packageName =packageInfo.packageName,
                        appName =pm.getApplicationLabel(packageInfo).toString(), appType = "System" )
                    appInfoArrayList.add(appPogo)
                } else if (packageInfo.flags and ApplicationInfo.FLAG_SYSTEM !== 0) {
                    // system apps

                } else {
                    // user installed apps
                    var appPogo:AppPogo = AppPogo(packageName = packageInfo.packageName,
                        appName =pm.getApplicationLabel(packageInfo).toString(), appType = "Application" )
                    appInfoArrayList.add(appPogo)
                }

            }

        }
        return appInfoArrayList
    }




}