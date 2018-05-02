package com.algorigo.remoteconfigstudy

import android.app.Application
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings


class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        remoteConfigInit()
    }

    private fun remoteConfigInit() {
        //  developer mode enable when debug
        val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()

        // set in-app defaults
        val remoteConfigDefaults = HashMap<String, Any>()
        remoteConfigDefaults["latest_version"] = "1.0.0"

        // FirebaseRemoteConfig init
        FirebaseRemoteConfig.getInstance().apply {
            setConfigSettings(configSettings)
            setDefaults(remoteConfigDefaults)
            // every 60 minutes refresh cache
            // default value is 12 hours
            fetch(60).addOnCompleteListener({ task: Task<Void> ->
                if (task.isSuccessful) {
                    Log.d("RemoteConfig", "remote config is fetched.")
                    activateFetched()
                }
            })
        }
    }
}