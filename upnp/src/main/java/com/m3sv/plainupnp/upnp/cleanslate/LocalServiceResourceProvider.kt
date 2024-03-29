package com.m3sv.plainupnp.upnp.cleanslate

import android.content.Context
import android.content.pm.PackageManager
import com.m3sv.plainupnp.upnp.R
import com.m3sv.plainupnp.upnp.getSettingContentDirectoryName
import timber.log.Timber

class LocalServiceResourceProvider(private val context: Context) {
    val appName = context.getString(R.string.app_name)
    val appUrl = context.getString(R.string.app_url)
    val settingContentDirectoryName = getSettingContentDirectoryName(context)
    val appVersion: String
        get() {
            var result = "1.0"
            try {
                result = context.packageManager.getPackageInfo(context.packageName, 0).versionName
            } catch (e: PackageManager.NameNotFoundException) {
                Timber.e(e, "Application version name not found")
            }
            return result
        }
}
