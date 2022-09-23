package com.promiseek.alarmx

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import com.promiseek.alarmx.Database.AlarmsPogo
import com.promiseek.alarmx.MainActivity.Companion.database
import com.sigma.niceswitch.NiceSwitch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ArrayAdapterOfAlarmList(var arrayListOfAlarms: List<AlarmsPogo>, var context: Context ): BaseAdapter() {
    override fun getCount(): Int {
        return arrayListOfAlarms.size

    }



    companion object {
        var viewHolder: ViewHolder = ViewHolder()
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


        viewHolder.timeTextView = view.findViewById(R.id.time)
        viewHolder.amORpm = view.findViewById(R.id.amORpm)
        viewHolder.timesRing = view.findViewById(R.id.timesRing)
        viewHolder.onOrOffSwitch= view.findViewById(R.id.onOrOffSwitch)



        viewHolder.timeTextView!!.text = arrayListOfAlarms.get(i).time.split(" ").get(0)
        viewHolder.amORpm!!.text = arrayListOfAlarms.get(i).time.split(" ").get(1)
        viewHolder.onOrOffSwitch!!.isChecked = arrayListOfAlarms.get(i).onOrOf




        var chooseDayString = ""

        for(j in arrayListOfAlarms.get(i).dayChosen){
            if(j=="Once" || j=="Daily"){
                chooseDayString+=j+","
            }else{
                chooseDayString+=j.substring(0,3)+","
            }

        }
        viewHolder.timesRing!!.text = chooseDayString.substring(0,chooseDayString.lastIndex)

        viewHolder.onOrOffSwitch!!.setOnClickListener() {
            Log.i("heiheh", viewHolder.onOrOffSwitch!!.isChecked.toString())
            val alarmPogo = AlarmsPogo(id = arrayListOfAlarms.get(i).id
                ,time = arrayListOfAlarms.get(i).time,
                dayChosen = arrayListOfAlarms.get(i).dayChosen,
                packageNames = arrayListOfAlarms.get(i).packageNames,
                onOrOf = viewHolder.onOrOffSwitch!!.isChecked)

                CoroutineScope(Dispatchers.Default).launch() {
                    database.alarmDao().update(alarmPogo)
                }
        }

        return view
    }

}



class ViewHolder {
    var timeTextView: TextView?=null
    var amORpm: TextView?=null
    var timesRing: TextView?=null
    var onOrOffSwitch: SwitchCompat?=null
}



