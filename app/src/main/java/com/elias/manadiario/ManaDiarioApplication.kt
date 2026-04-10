package com.elias.manadiario

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager

class ManaDiarioApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Acessa as preferências salvas do usuário
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        // Lê a preferência "tema_escuro". Se não encontrar, assume 'false' (desligado).
        val isDarkModeOn = sharedPreferences.getBoolean("tema_escuro", false)

        // Aplica o tema escolhido
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}