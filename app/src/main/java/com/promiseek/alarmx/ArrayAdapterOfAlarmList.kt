package com.promiseek.alarmx

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.promiseek.alarmx.Database.AlarmsPogo
import com.promiseek.alarmx.MainActivity.Companion.addTimer
import com.promiseek.alarmx.MainActivity.Companion.alarmItemClicked
import com.promiseek.alarmx.MainActivity.Companion.alarmListView
import com.promiseek.alarmx.MainActivity.Companion.alarmManager
import com.promiseek.alarmx.MainActivity.Companion.database
import com.promiseek.alarmx.MainActivity.Companion.myIntent
import com.promiseek.alarmx.MainActivity.Companion.parentToolbar
import com.promiseek.alarmx.MainActivity.Companion.pendingIntent
import com.promiseek.alarmx.MainActivity.Companion.selectedAlams
import com.promiseek.alarmx.MainActivity.Companion.selectedItems
import com.promiseek.alarmx.Timer_Setting.Companion.setNewAlarm
import kotlinx.coroutines.*

class ArrayAdapterOfAlarmList(var arrayListOfAlarms: List<AlarmsPogo>, var context: Context ): BaseAdapter() {
    override fun getCount(): Int {
        return arrayListOfAlarms.size

    }



    companion object {

    }

    override fun getItem(p0: Int): Any {
        return arrayListOfAlarms.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }



    override fun getView(i: Int, p1: View?, p2: ViewGroup?): View {
        val inflater = LayoutInflater.from(context)
        var view: View? = p1
        view = inflater.inflate(R.layout.item_of_alarm, p2, false)
        var timeTextView: TextView= view.findViewById(R.id.time)
        var amORpm: TextView= view.findViewById(R.id.amORpm)
        var timesRing: TextView= view.findViewById(R.id.timesRing)
        var onOrOffSwitch: SwitchCompat= view.findViewById(R.id.onOrOffSwitch)
        var item: GridLayout= view.findViewById(R.id.item)
        var selectedRadio:RadioButton = view.findViewById(R.id.radioOnOroff)
        if(selectedItems){
            selectedRadio.visibility= VISIBLE
            onOrOffSwitch.visibility=GONE

            for (j in selectedAlams){
                if(j==arrayListOfAlarms.get(i)){
                    selectedRadio.isChecked= true
                    item.setBackgroundColor(context.resources.getColor(R.color.toolbarColor))
                }

            }
        }else{
            selectedRadio.visibility= GONE
            onOrOffSwitch.visibility= VISIBLE
        }







        timeTextView.text = arrayListOfAlarms.get(i).time.split(" ").get(0)
        amORpm.text = arrayListOfAlarms.get(i).time.split(" ").get(1)
        onOrOffSwitch.isChecked = arrayListOfAlarms.get(i).onOrOf

        MainActivity.selectAll.setOnMenuItemClickListener {
            selectedAlams.addAll(arrayListOfAlarms)
            alarmListView.adapter = ArrayAdapterOfAlarmList(arrayListOfAlarms,context)
            true
        }

        //long click on and off
        item.setOnLongClickListener(View.OnLongClickListener {
            Log.i("hehehd",it.toString())
            selectedItems=true
            parentToolbar.setNavigationIcon(context.resources.getDrawable(R.drawable.cross))
            MainActivity.selectAll.setVisible(true)
            selectedAlams.add(arrayListOfAlarms.get(i))
            addTimer.setImageDrawable(context.resources.getDrawable(R.drawable.plus)) //here have dlt

            alarmListView.adapter = ArrayAdapterOfAlarmList(arrayListOfAlarms,context)



            true
        })


        selectedRadio.setOnClickListener(View.OnClickListener {
            Log.i("sdfasdf",selectedRadio.isChecked.toString())
            //iT is not working
            if(selectedRadio.isChecked){
                selectedRadio.isChecked= false
//                Log.i("sdfasdf",selectedRadio.isChecked.toString())
                selectedAlams.remove(arrayListOfAlarms.get(i))

                item.setBackgroundColor(context.resources.getColor(R.color.white))
            }else{
                selectedRadio.isChecked= true
//                Log.i("sdfasdf",selectedRadio.isChecked.toString())
                selectedAlams.add(arrayListOfAlarms.get(i))

                item.setBackgroundColor(context.resources.getColor(R.color.toolbarColor))
            }
        })
        item.setOnClickListener(View.OnClickListener {
            if(selectedItems){
                Log.i("sdfasdf",selectedRadio.isChecked.toString())
                if(selectedRadio.isChecked){
                    selectedAlams.remove(arrayListOfAlarms.get(i))
                    selectedRadio.isChecked= false
                    item.setBackgroundColor(context.resources.getColor(R.color.white))
                }else{
                    selectedAlams.add(arrayListOfAlarms.get(i))
                    selectedRadio.isChecked= true
                    item.setBackgroundColor(context.resources.getColor(R.color.toolbarColor))
                }

            }else{
                var intent = Intent(context,Timer_Setting::class.java)
                alarmItemClicked=true
                val bundle = Bundle()

                bundle.putString("time",arrayListOfAlarms.get(i).time)
                bundle.putLong("id",arrayListOfAlarms.get(i).id)
                bundle.putStringArrayList("day",arrayListOfAlarms.get(i).dayChosen)
                bundle.putStringArrayList("apps",arrayListOfAlarms.get(i).packageNames)
                intent.putExtras(bundle)
                Log.i("fdsfjsd",arrayListOfAlarms.get(i).dayChosen.toString())
                context.startActivity(intent)
            }
        })

        parentToolbar.setNavigationOnClickListener(View.OnClickListener {
            selectedItems=false
            parentToolbar.setNavigationIcon(null)
            MainActivity.selectAll.setVisible(false)
            selectedAlams.clear()
            addTimer.setImageDrawable(context.resources.getDrawable(R.drawable.plus))

            alarmListView.adapter = ArrayAdapterOfAlarmList(arrayListOfAlarms,context)
        })

        var chooseDayString = ""

        for(j in arrayListOfAlarms.get(i).dayChosen){
            if(j=="Once" || j=="Daily"){
                chooseDayString+=j+","
            }else{
                chooseDayString+=j.substring(0,3)+","
            }

        }
        timesRing.text = chooseDayString.substring(0,chooseDayString.lastIndex)

        onOrOffSwitch.setOnClickListener() {

                GlobalScope.launch(Dispatchers.Default) {
                    val alarmPogo = AlarmsPogo(id = arrayListOfAlarms.get(i).id
                        ,time = arrayListOfAlarms.get(i).time,
                        dayChosen = arrayListOfAlarms.get(i).dayChosen,
                        packageNames = arrayListOfAlarms.get(i).packageNames,
                        onOrOf =onOrOffSwitch.isChecked)
                    database.alarmDao().update(alarmPogo)
                    if(!onOrOffSwitch.isChecked){

//                        pendingIntent  = PendingIntent.getBroadcast(context,
//                            arrayListOfAlarms.get(i).id.toInt(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                        pendingIntent  = PendingIntent.getBroadcast(context,
                            arrayListOfAlarms.get(i).id.toInt(), myIntent, PendingIntent.FLAG_MUTABLE);
                        alarmManager!!.cancel(pendingIntent)
                    }else{
                        launch(Dispatchers.Default){
                            setNewAlarm(userSetTime = arrayListOfAlarms.get(i).time, selectedDaysArrayList = arrayListOfAlarms.get(i).dayChosen,
                                id = arrayListOfAlarms.get(i).id,context,arrayListOfAlarms.get(i).packageNames)
                        }
                    }

                }

        }


        addTimer.setOnClickListener(View.OnClickListener {
                if (selectedItems) {
                    for (j in selectedAlams){
                        CoroutineScope(Dispatchers.Default).launch {
                            arrayListOfAlarms.filter {
                                it!=j
                            }
                            pendingIntent  = PendingIntent.getBroadcast(context,
                                j.id.toInt(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            alarmManager!!.cancel(pendingIntent)
                            database.alarmDao().delete(j)

                        }

                    }
                    selectedItems=false
                    parentToolbar.setNavigationIcon(null)
                    MainActivity.selectAll.setVisible(false)
                    selectedAlams.clear()
                    addTimer.setImageDrawable(context.resources.getDrawable(R.drawable.plus))

                    alarmListView.adapter = ArrayAdapterOfAlarmList(arrayListOfAlarms,context)

                    Log.i("asdffdf",arrayListOfAlarms.toString())
                }else{
                    context.startActivity(Intent(context,Timer_Setting::class.java))
                }

            })


        return view
    }




}



