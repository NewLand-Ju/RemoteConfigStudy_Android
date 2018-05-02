package com.algorigo.remoteconfigstudy

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkGooglePlayServices()

        button.setOnClickListener { checkVersion() }
    }

    private fun checkGooglePlayServices() {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val status = googleApiAvailability.isGooglePlayServicesAvailable(this)

        if (status != ConnectionResult.SUCCESS) {
            val dialog = googleApiAvailability.getErrorDialog(this, status, -1)
            dialog.setOnDismissListener { _ -> finish() }
            dialog.show()

            googleApiAvailability.showErrorNotification(this, status)
        }
    }

    private fun checkVersion() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()

        val latestVersion = remoteConfig.getString("latest_version")
        val currentVersion = getAppVersion(this)

        currentAppVersion.text = currentVersion
        latestAppVersion.text = latestVersion

        if (!TextUtils.equals(currentVersion, latestVersion)) {
            showUpdateDialog()
        }
    }

    private fun getAppVersion(context: Context): String {
        var result = ""

        try {
            result = context.packageManager
                    .getPackageInfo(context.packageName, 0)
                    .versionName
            result = result.replace("[a-zA-Z]|-".toRegex(), "")
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("getAppVersion", e.message)
        }

        return result
    }

    private fun showUpdateDialog() {
        val dialog = AlertDialog.Builder(this)
                .setTitle("New version available")
                .setMessage("Please, update app to new version")
                .setPositiveButton("Update", { dialog, _ -> dialog.dismiss() })
                .setNegativeButton("No, thanks", { dialog, _ -> dialog.dismiss() })
                .create()
        dialog.show()
    }
}
