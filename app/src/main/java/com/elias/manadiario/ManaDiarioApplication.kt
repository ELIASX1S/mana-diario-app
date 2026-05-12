package com.elias.manadiario

import android.app.Application
import android.os.StrictMode
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager

class ManaDiarioApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        enableStrictModeInDebug()
        applyThemeFromPreferences()
    }

    private fun applyThemeFromPreferences() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val isDarkModeOn = sharedPreferences.getBoolean(PREF_DARK_THEME, false)

        val nightMode = if (isDarkModeOn) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }

        AppCompatDelegate.setDefaultNightMode(nightMode)
    }

    private fun enableStrictModeInDebug() {
        if (!BuildConfig.DEBUG) return

        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build()
        )

        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .build()
        )
    }

    private companion object {
        private const val PREF_DARK_THEME = "tema_escuro"
    }
}
