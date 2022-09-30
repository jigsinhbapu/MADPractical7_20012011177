package com.example.madpractical7_20012011177

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.icu.util.Calendar.getInstance
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextClock
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import  java.util.*

class MainActivity : AppCompatActivity() {
    var mili:Long=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val CancelAlarmcardview =findViewById<MaterialCardView>(R.id.cardview2)
        val btnCreateAlarm = findViewById<MaterialButton>(R.id.setalarmbutton)
        val SetAlarmTime = findViewById<TextView>(R.id.set_alarm_time)
        val btncancelAlarm = findViewById<MaterialButton>(R.id.cancel_button)
        val clockTc = findViewById<TextClock>(R.id.show_time)

        clockTc.format12Hour ="hh:mm:ss a"

        CancelAlarmcardview.visibility= View.GONE

        btnCreateAlarm.setOnClickListener{
            var cal:Calendar = getInstance()
            var hour = cal.get(Calendar.HOUR_OF_DAY)
            var min = cal.get(Calendar.MINUTE)
            val tpd = TimePickerDialog(this,TimePickerDialog.OnTimeSetListener(function = {view,h,m ->
               mili= getMillis(h,m)
                setAlarm(getMillis(h,m),"Start")
                CancelAlarmcardview.visibility=View.VISIBLE
                SetAlarmTime.text=h.toString()+":"+m.toString()
            }),hour,min,false)
             tpd.show()
        }
        btncancelAlarm.setOnClickListener {
            setAlarm(mili,"Stop")
            CancelAlarmcardview.visibility=View.GONE
        }
    }
    fun setAlarm(millisTime: Long,str:String)
    {
        val intent = Intent(this, AlarmBroadcastReceiver::class.java)
        intent.putExtra("Service1", str)
        val pendingIntent =
            PendingIntent.getBroadcast(applicationContext, 234324243, intent, 0)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        if(str == "Start") {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                millisTime,
                pendingIntent
            )
        }else if(str == "Stop")
        {
            alarmManager.cancel(pendingIntent)
            sendBroadcast(intent)
        }
    }
    fun getMillis(hour:Int, min:Int):Long
    {
        val setcalendar = getInstance()
        setcalendar[Calendar.HOUR_OF_DAY] = hour
        setcalendar[Calendar.MINUTE] = min
        setcalendar[Calendar.SECOND] = 0
        return setcalendar.timeInMillis
    }
}