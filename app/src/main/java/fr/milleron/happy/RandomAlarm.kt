package fr.milleron.happy

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import java.util.*

class RandomAlarm() : BroadcastReceiver() {

// received when alarm is triggered
    override fun onReceive(context: Context, intent: Intent?) {

        val channelId = context.getString(R.string.channel_id)

        if (intent?.action == "android.intent.action.BOOT_COMPLETED"){
            setRandomAlarm(context)
            return
        }

        // build a notification and send it
        val newIntent = Intent(context, MainActivity::class.java)
        val pendingIntent: PendingIntent
        pendingIntent = PendingIntent.getActivity(context, 1, newIntent, 0)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.happy_icon)
            .setContentTitle("Happy")
            .setContentText("You have a Happy message")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, builder.build())

        // save in the prefs that a message has been sent today and set the new alarm
        val sharedPref = context.getSharedPreferences(context.getString(R.string.pref_happy), Context.MODE_PRIVATE)
        val edit = sharedPref.edit()
        val cal = Calendar.getInstance()
        cal.timeInMillis = System.currentTimeMillis()
        val today = cal.get(Calendar.DAY_OF_MONTH).toString()+cal.get(Calendar.MONTH).toString()+cal.get(Calendar.YEAR).toString()
        edit.putString(context.getString(R.string.pref_last_received_message), today)
        edit.apply()
        setRandomAlarm(context)
    }

    companion object {
        fun setRandomAlarm(appContext:Context, sharedPreferences:SharedPreferences? = null) {
            val intent = Intent(appContext, RandomAlarm::class.java)

            val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
            val alarmIntent: PendingIntent
            alarmIntent = PendingIntent.getBroadcast(appContext, 42, intent, 0)

            val cal = Calendar.getInstance()
            cal.timeInMillis = System.currentTimeMillis()
            val today = cal.get(Calendar.DAY_OF_MONTH).toString()+cal.get(Calendar.MONTH).toString()+cal.get(Calendar.YEAR).toString()

            //get preferences to calculate a random value within the given period
            val sharedPref = sharedPreferences ?: appContext.getSharedPreferences(appContext.getString(R.string.pref_happy), Context.MODE_PRIVATE)

            if  (sharedPref?.getString(appContext.getString(R.string.pref_receive_random), "YES") == "NO")
            {
                alarmManager?.cancel(alarmIntent)
                return
            }


            var minimumTime = sharedPref.getInt(appContext.getString(R.string.pref_min_time), 10)
            val maximumTime = sharedPref.getInt(appContext.getString(R.string.pref_max_time), 18)

            // Check if alarm already triggered today
            if (sharedPref.getString(appContext.getString(R.string.pref_last_received_message), "YES") == today) {
                    cal.add(Calendar.DATE, 1)
                }
            else
            {
                //if alarm has not been triggered today set the minimum time for trigger for next hour
                minimumTime = cal.get(Calendar.HOUR_OF_DAY) + 1
                if (minimumTime >= maximumTime)
                {
                    cal.add(Calendar.DATE, 1)
                    minimumTime = sharedPref.getInt(appContext.getString(R.string.pref_min_time), 10)
                }
            }
            cal.set(Calendar.HOUR_OF_DAY, (minimumTime until maximumTime).random())
            cal.set(Calendar.MINUTE, (0 until 60).random())

                // creation of an alarm for the day after. If the application is somehow open during one day then the alarm is postponed to the next day
            alarmManager?.set(AlarmManager.RTC_WAKEUP,
                cal.timeInMillis,  alarmIntent)

        }

        fun createNotificationChannel(appContext:Context) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = appContext.getString(R.string.channel_name)
                val descriptionText = appContext.getString(R.string.channel_description)
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channelId = appContext.getString(R.string.channel_id)
                val channel = NotificationChannel(channelId, name, importance).apply {
                    description = descriptionText
                }
                // Register the channel with the system
                val notificationManager: NotificationManager =
                    appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }

        fun cancelNotification(appContext:Context){
            val notificationManager: NotificationManager =
                appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(1)
        }

    }


}