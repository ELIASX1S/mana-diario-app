package com.elias.manadiario

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.elias.manadiario.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        // NOVIDADE: Encontrando a preferência do tema escuro pela sua "key"
        val darkModePreference = findPreference<SwitchPreferenceCompat>("tema_escuro")

        // NOVIDADE: Adicionando um "ouvinte" para quando o valor dela mudar
        darkModePreference?.setOnPreferenceChangeListener { preference, newValue ->
            // O 'newValue' é o novo estado do interruptor (true se ligado, false se desligado)
            val isDarkModeOn = newValue as Boolean

            if (isDarkModeOn) {
                // Aplica o tema escuro
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                // Aplica o tema claro
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            // Retornamos 'true' para dizer ao sistema que a mudança foi aceita e pode ser salva.
            true
        }
    }
}