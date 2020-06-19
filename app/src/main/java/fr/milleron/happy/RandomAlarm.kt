package fr.milleron.happy

import android.content.Context

class RandomAlarm(val appContext:Context) {


     fun doWork() {
        TODO("Not yet implemented")

    }

    companion object {
        fun initiateFirstWorker(appContext:Context) {

            //val test = WorkManager.getInstance(appContext).getWorkInfosForUniqueWork("toto")
            //val constraints = Constraints.Builder().build()
            //val randomRequest = PeriodicWorkRequestBuilder<RandomWorker>(1, TimeUnit.DAYS).setConstraints(constraints).build()
            //WorkManager.getInstance(appContext).enqueueUniquePeriodicWork("toto", ExistingPeriodicWorkPolicy.REPLACE, randomRequest)
        }
    }
}