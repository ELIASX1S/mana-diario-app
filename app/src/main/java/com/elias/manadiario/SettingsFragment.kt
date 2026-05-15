package com.elias.manadiario

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val darkModePreference = findPreference<SwitchPreferenceCompat>("tema_escuro")
        darkModePreference?.setOnPreferenceChangeListener { _, newValue ->
            val isDarkModeOn = newValue as Boolean
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkModeOn) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
            true
        }

        val notificationTimePreference = findPreference<ListPreference>("notificacao_horario")
        notificationTimePreference?.summary = notificationTimePreference?.value ?: "08:00"
        notificationTimePreference?.setOnPreferenceChangeListener { preference, newValue ->
            val selectedTime = newValue as String
            preference.summary = selectedTime

            val notificationsEnabled =
                findPreference<SwitchPreferenceCompat>("notificacoes")?.isChecked == true
            if (notificationsEnabled) {
                DailyReminderScheduler.schedule(requireContext())
            }
            true
        }

        val notificationsPreference = findPreference<SwitchPreferenceCompat>("notificacoes")
        notificationsPreference?.setOnPreferenceChangeListener { _, newValue ->
            val enabled = newValue as Boolean
            val context = requireContext()
            if (enabled) {
                val canPost = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                    ContextCompat.checkSelfPermission(
                        context,
                        android.Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED

                if (canPost) {
                    DailyReminderScheduler.schedule(context)
                }
            } else {
                DailyReminderScheduler.cancel(context)
            }
            true
        }
    }
}
