package com.elias.manadiario

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        configurarTemaEscuro()
        configurarTamanhoFonte()
        configurarHorarioNotificacao()
        configurarNotificacoes()
    }

    private fun configurarTemaEscuro() {
        val darkModePreference = findPreference<SwitchPreferenceCompat>("tema_escuro")
        darkModePreference?.setOnPreferenceChangeListener { _, newValue ->
            val isDarkModeOn = newValue as Boolean
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkModeOn) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
            true
        }
    }

    private fun configurarTamanhoFonte() {
        val fontSizePreference = findPreference<ListPreference>("tamanho_fonte")
        fontSizePreference?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()
        fontSizePreference?.setOnPreferenceChangeListener { preference, newValue ->
            val selectedValue = newValue as String
            val listPreference = preference as? ListPreference
            val index = listPreference?.findIndexOfValue(selectedValue) ?: -1
            preference.summary = if (index >= 0) listPreference?.entries?.get(index) else selectedValue
            Toast.makeText(requireContext(), "Tamanho da fonte atualizado.", Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun configurarHorarioNotificacao() {
        val notificationTimePreference = findPreference<ListPreference>("notificacao_horario")
        notificationTimePreference?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()
        notificationTimePreference?.setOnPreferenceChangeListener { preference, newValue ->
            val selectedTime = newValue as String
            preference.summary = selectedTime

            val notificationsEnabled =
                findPreference<SwitchPreferenceCompat>("notificacoes")?.isChecked == true
            if (notificationsEnabled) {
                DailyReminderScheduler.schedule(requireContext())
                Toast.makeText(
                    requireContext(),
                    "Lembrete diário ajustado para $selectedTime.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            true
        }
    }

    private fun configurarNotificacoes() {
        val notificationsPreference = findPreference<SwitchPreferenceCompat>("notificacoes")
        notificationsPreference?.setOnPreferenceChangeListener { preference, newValue ->
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
                    Toast.makeText(context, "Notificação diária ativada.", Toast.LENGTH_SHORT).show()
                    true
                } else {
                    Toast.makeText(
                        context,
                        "Permita notificações nas configurações do Android para receber o lembrete diário.",
                        Toast.LENGTH_LONG
                    ).show()
                    (preference as? SwitchPreferenceCompat)?.isChecked = false
                    false
                }
            } else {
                DailyReminderScheduler.cancel(context)
                Toast.makeText(context, "Notificação diária desativada.", Toast.LENGTH_SHORT).show()
                true
            }
        }
    }
}
