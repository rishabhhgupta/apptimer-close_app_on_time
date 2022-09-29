package com.promiseek.alarmx

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle

import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ListView
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import ca.antonious.materialdaypicker.MaterialDayPicker
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.promiseek.alarmx.Database.AlarmsPogo
import com.promiseek.alarmx.MainActivity.Companion.alarmItemClicked
import com.promiseek.alarmx.MainActivity.Companion.database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Time

import java.text.Format
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Timer_Setting : AppCompatActivity(){
    var dayPicker: MaterialDayPicker? = null
    var dayTextView:TextView?=null


   companion object{
       lateinit var selectedAppsListView:ArrayList<String>
       //gettimeInAMandPM
       fun getTimesortedInampm(hr: Int, min: Int): String? {
           val tme = Time(hr, min, 0) //seconds by default set to zero
           val formatter: Format
           formatter = SimpleDateFormat("h:mm a")
           return formatter.format(tme)
       }

       fun twentyFourHrsFormat(t:String):String{
           val displayFormat = SimpleDateFormat("HH:mm")
           val parseFormat = SimpleDateFormat("hh:mm a")
           val date: Date = parseFormat.parse(t) as Date
           return displayFormat.format(date)
       }

   }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer_setting)
        var timeScheduler = findViewById(R.id.timeScheduler) as ConstraintLayout
        var appsListView = findViewById(R.id.appsListView) as ListView
        appsListView.emptyView = findViewById(R.id.listEmptyView)
        selectedAppsListView = ArrayList()
        var selectedDaysArrayList = ArrayList<String>()
        //day picker
        dayPicker = findViewById<MaterialDayPicker>(R.id.day_picker)
        //day text view
        dayTextView = findViewById(R.id.dayTextView)


        var bundle = intent.extras
        var time_picker = findViewById(R.id.timePicker) as TimePicker


        //if it come from intent
        if (alarmItemClicked)  {

            val clickedTimeList:String = twentyFourHrsFormat(bundle!!.getString("time",""))

            time_picker.currentHour= Integer.parseInt(clickedTimeList.split(":").get(0))
            time_picker.currentMinute= Integer.parseInt(clickedTimeList.split(":").get(1))
            var materialDayPickerFromIntent = ArrayList<MaterialDayPicker.Weekday>()
            for (day in bundle!!.getStringArrayList("day")!!){
                if (day=="Once"){
                    dayTextView!!.text= "Once"
                }else if (day=="Daily"){
                    dayTextView!!.text= "Daily"
                }else{
                    dayTextView!!.text= "Custom"
                    if (day=="MONDAY"){
                        materialDayPickerFromIntent.add((MaterialDayPicker.Weekday.MONDAY))
                    }else if (day=="TUESDAY"){
                        materialDayPickerFromIntent.add((MaterialDayPicker.Weekday.TUESDAY))
                    }else if (day=="WEDNESDAY"){
                        materialDayPickerFromIntent.add((MaterialDayPicker.Weekday.WEDNESDAY))
                    }else if (day=="THURSDAY"){
                        materialDayPickerFromIntent.add((MaterialDayPicker.Weekday.THURSDAY))
                    }else if (day=="FRIDAY"){
                        materialDayPickerFromIntent.add((MaterialDayPicker.Weekday.FRIDAY))
                    }else if (day=="SATURDAY"){
                        materialDayPickerFromIntent.add((MaterialDayPicker.Weekday.SATURDAY))
                    }else if (day=="SUNDAY"){
                        materialDayPickerFromIntent.add((MaterialDayPicker.Weekday.SUNDAY))
                    }
                    Log.i("djfsdkf",day)

                }
            }
            if (dayTextView!!.text=="Custom"){
                dayPicker!!.visibility= VISIBLE
                Log.i("djfsdkf",materialDayPickerFromIntent.toString())
                dayPicker!!.setSelectedDays(materialDayPickerFromIntent)
            }

        }
        time_picker.setIs24HourView(false)






        //toolbar
        var toolbar:MaterialToolbar = findViewById(R.id.toolBar)
        toolbar.inflateMenu(R.menu.main_menu)
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)


//       get all the app info
        lifecycleScope.launch(Dispatchers.Default) {
            var appsArrayAdapter:AppsArrayAdapter
            if (alarmItemClicked) {


                appsArrayAdapter = AppsArrayAdapter(installedApps(), context = applicationContext,
                    bundle!!.getStringArrayList("apps") as ArrayList<String>
                )
            }else{
                appsArrayAdapter = AppsArrayAdapter(installedApps(), context = applicationContext, arrayListOf())
            }

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

                val userSetTime= getTimesortedInampm(time_picker.currentHour,time_picker.currentMinute)


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

                    if (alarmItemClicked){
                        alarmItemClicked=false
                        var alarmPogo = AlarmsPogo(id = bundle!!.getLong("id"),time = userSetTime!!, dayChosen = selectedDaysArrayList, packageNames = selectedAppsListView,onOrOf = true)
                        lifecycleScope.launch(Dispatchers.Default) {
                            database.alarmDao().update(alarmPogo)
                        }
                    }else{
                        var alarmPogo = AlarmsPogo(id = 0,time = userSetTime!!, dayChosen = selectedDaysArrayList, packageNames = selectedAppsListView,onOrOf = true)
                        lifecycleScope.launch(Dispatchers.Default) {
                            database.alarmDao().insert(alarmPogo)
                        }
                    }


                    onBackPressed()


                }else{
                    Toast.makeText(this, "Please select day", Toast.LENGTH_SHORT).show()
                }


            }
            false
        })
        toolbar.setNavigationOnClickListener(View.OnClickListener {

            if (alarmItemClicked){
                alarmItemClicked=false
            }
            onBackPressed()
        })

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