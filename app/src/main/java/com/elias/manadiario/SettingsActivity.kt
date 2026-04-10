package com.elias.manadiario

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Este código carrega nosso SettingsFragment dentro do container
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings_container, SettingsFragment())
                .commit()
        }

        // Configura a Toolbar e o botão de voltar
        val toolbar: Toolbar = findViewById(R.id.settings_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // Esta função faz o botão de voltar na Toolbar funcionar
    override fun onSupportNavigateUp(): Boolean {
        finish() // Fecha a tela de configurações e volta para a anterior
        return true
    }
}