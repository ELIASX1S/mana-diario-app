package com.elias.manadiario

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class DevotionalDayActivity : AppCompatActivity() {

    private lateinit var journeyManager: JourneyManager

    private lateinit var titleTextView: TextView
    private lateinit var dayNumberTextView: TextView
    private lateinit var versiculoPrincipalTextView: TextView
    private lateinit var versiculoComplementarTextView: TextView
    private lateinit var reflexaoTextView: TextView
    private lateinit var oracaoTextView: TextView
    private lateinit var aplicacaoTextView: TextView
    private lateinit var concludeButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_devotional_day)

        journeyManager = JourneyManager(this)

        val toolbar: Toolbar = findViewById(R.id.devotionalDayToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Devocional do Dia"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        titleTextView = findViewById(R.id.devotionalTitleTextView)
        dayNumberTextView = findViewById(R.id.devotionalDayNumberTextView)
        versiculoPrincipalTextView = findViewById(R.id.versiculoPrincipalTextView)
        versiculoComplementarTextView = findViewById(R.id.versiculoComplementarTextView)
        reflexaoTextView = findViewById(R.id.reflexaoTextView)
        oracaoTextView = findViewById(R.id.oracaoTextView)
        aplicacaoTextView = findViewById(R.id.aplicacaoTextView)
        concludeButton = findViewById(R.id.concludeDevotionalButton)

        carregarConteudoDoDia()

        concludeButton.setOnClickListener {
            if (journeyManager.isTodayCompleted()) {
                Toast.makeText(this, "Você já concluiu o devocional de hoje 🙏", Toast.LENGTH_SHORT).show()
            } else {
                val success = journeyManager.completeToday()
                if (success) {
                    Toast.makeText(this, "Devocional concluído com sucesso ✅", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Não foi possível concluir o devocional agora.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun carregarConteudoDoDia() {
        val devotionalDay = journeyManager.getTodayDevotional()
        val activePlan = journeyManager.getActivePlan()

        if (devotionalDay == null || activePlan == null) {
            Toast.makeText(this, "Nenhum devocional ativo encontrado.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        titleTextView.text = devotionalDay.titulo
        dayNumberTextView.text = "Dia ${devotionalDay.dia} de ${activePlan.dias.size}"
        versiculoPrincipalTextView.text = devotionalDay.versiculoPrincipal
        versiculoComplementarTextView.text = devotionalDay.versiculoComplementar
        reflexaoTextView.text = devotionalDay.reflexao
        oracaoTextView.text = devotionalDay.oracao
        aplicacaoTextView.text = devotionalDay.aplicacao

        if (journeyManager.isTodayCompleted()) {
            concludeButton.text = "Devocional concluído hoje ✅"
            concludeButton.isEnabled = false
        } else {
            concludeButton.text = "Concluir devocional"
            concludeButton.isEnabled = true
        }
    }

    override fun onResume() {
        super.onResume()
        carregarConteudoDoDia()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}