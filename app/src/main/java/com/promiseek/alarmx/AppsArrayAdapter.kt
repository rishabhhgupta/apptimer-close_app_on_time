package com.promiseek.alarmx

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.promiseek.alarmx.Timer_Setting.Companion.selectedAppsListView

class AppsArrayAdapter(var arrayListOfApps:ArrayList<AppPogo>, var context: Context,var selectedApp:ArrayList<String>): BaseAdapter() {
    override fun getCount(): Int {
        return arrayListOfApps.size
    }

    override fun getItem(p0: Int): Any {
        return 0
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(i: Int, p1: View?, p2: ViewGroup?): View {
        val inflater = LayoutInflater.from(context)
        var view: View? = p1
        view = inflater.inflate(R.layout.apps_arrayadapter, p2, false)
        var appIcon: ImageView = view.findViewById(R.id.appIcon)
        var appName: TextView = view.findViewById(R.id.appName)
        var appType:TextView = view.findViewById(R.id.appType)
        var switch:Switch = view.findViewById(R.id.materialSwitch)

        appIcon.setImageDrawable(context.getPackageManager().getApplicationIcon(arrayListOfApps.get(i).packageName))
        appName.text = arrayListOfApps.get(i).appName
        appType.text = arrayListOfApps.get(i).appType
//        switch.isChecked =
        // To listen for a switch's checked/unchecked state changes
        if (selectedApp!=null){
//            Log.i("asdfasdf",selectedApp.toString())
            for (pkg in selectedApp){

                if (arrayListOfApps.get(i).packageName==pkg){
                    Log.i("asdfasdf",arrayListOfApps.get(i).packageName)
                    switch.isChecked=true
                }
            }
        }

        switch.setOnClickListener(View.OnClickListener {
            if(switch.isChecked){
                selectedAppsListView.add(arrayListOfApps.get(i).packageName)


            }else{
                selectedAppsListView.remove(arrayListOfApps.get(i).packageName)

            }
        })




        return view
    }
}

class AppPogo(var packageName:String, var appName:String, var appType:String)

