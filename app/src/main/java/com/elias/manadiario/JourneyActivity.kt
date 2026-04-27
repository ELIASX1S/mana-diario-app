package com.elias.manadiario

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class JourneyActivity : AppCompatActivity() {

    private lateinit var journeyManager: JourneyManager

    private lateinit var planNameTextView: TextView
    private lateinit var dayTextView: TextView
    private lateinit var progressTextView: TextView
    private lateinit var streakTextView: TextView
    private lateinit var startDateTextView: TextView
    private lateinit var statusTextView: TextView
    private lateinit var completionMessageTextView: TextView
    private lateinit var completedDaysTextView: TextView
    private lateinit var remainingDaysTextView: TextView
    private lateinit var streakValueTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var continueButton: Button
    private lateinit var changePlanButton: Button
    private lateinit var restartPlanButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journey)

        journeyManager = JourneyManager(this)

        val toolbar: Toolbar = findViewById(R.id.journeyToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Minha Jornada"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        planNameTextView = findViewById(R.id.planNameTextView)
        dayTextView = findViewById(R.id.dayTextView)
        progressTextView = findViewById(R.id.progressTextView)
        streakTextView = findViewById(R.id.streakTextView)
        startDateTextView = findViewById(R.id.startDateTextView)
        statusTextView = findViewById(R.id.statusTextView)
        completionMessageTextView = findViewById(R.id.completionMessageTextView)
        completedDaysTextView = findViewById(R.id.completedDaysTextView)
        remainingDaysTextView = findViewById(R.id.remainingDaysTextView)
        streakValueTextView = findViewById(R.id.streakValueTextView)
        progressBar = findViewById(R.id.journeyProgressBar)
        continueButton = findViewById(R.id.continueJourneyButton)
        changePlanButton = findViewById(R.id.changePlanButton)
        restartPlanButton = findViewById(R.id.restartPlanButton)

        continueButton.setOnClickListener {
            val intent = Intent(this, DevotionalDayActivity::class.java)
            startActivity(intent)
        }

        changePlanButton.setOnClickListener {
            val intent = Intent(this, ChoosePlanActivity::class.java)
            startActivity(intent)
            finish()
        }

        restartPlanButton.setOnClickListener {
            confirmarReinicio()
        }
    }

    override fun onResume() {
        super.onResume()
        atualizarTela()
    }

    private fun atualizarTela() {
        val activePlan = journeyManager.getActivePlan()

        if (activePlan == null) {
            startActivity(Intent(this, ChoosePlanActivity::class.java))
            finish()
            return
        }

        val currentDay = journeyManager.getCurrentDay()
        val completedDays = journeyManager.getCompletedDays()
        val currentStreak = journeyManager.getCurrentStreak()
        val progressPercentage = journeyManager.getProgressPercentage()
        val startDate = journeyManager.getPlanStartDate()
        val todayCompleted = journeyManager.isTodayCompleted()
        val totalDays = activePlan.dias.size
        val remainingDays = (totalDays - completedDays).coerceAtLeast(0)
        val planFinished = completedDays >= totalDays

        planNameTextView.text = activePlan.nome
        dayTextView.text = if (planFinished) {
            "Plano concluído"
        } else {
            "Dia $currentDay de $totalDays"
        }

        progressTextView.text = "Progresso: $progressPercentage% ($completedDays/$totalDays)"
        streakTextView.text = "Sequência atual: $currentStreak dia(s)"
        startDateTextView.text = "Iniciado em: $startDate"
        completedDaysTextView.text = completedDays.toString()
        remainingDaysTextView.text = remainingDays.toString()
        streakValueTextView.text = currentStreak.toString()
        progressBar.progress = progressPercentage

        if (planFinished) {
            statusTextView.text = "Parabéns! Você concluiu este plano 🎉"
            completionMessageTextView.text =
                "Que alegria ver sua constância! Continue firme na presença de Deus e leve adiante tudo o que Ele falou ao seu coração durante esta jornada."
            completionMessageTextView.visibility = TextView.VISIBLE
            continueButton.text = "Rever último devocional"
            continueButton.isEnabled = true
        } else {
            val nextDay = (currentDay + 1).coerceAtMost(totalDays)
            statusTextView.text = if (todayCompleted) {
                "Você já concluiu o devocional de hoje ✅"
            } else {
                "Seu devocional de hoje está pronto 🙏"
            }

            completionMessageTextView.text = if (todayCompleted) {
                "Descanse no que Deus já falou hoje. Amanhã você continua com o dia $nextDay."
            } else {
                "Faltam $remainingDays dia(s) para concluir seu plano. Continue firme!"
            }
            completionMessageTextView.visibility = TextView.VISIBLE

            continueButton.text = if (todayCompleted) {
                "Ver devocional de hoje"
            } else {
                "Continuar devocional"
            }
            continueButton.isEnabled = true
        }
    }

    private fun confirmarReinicio() {
        val activePlan = journeyManager.getActivePlan() ?: return

        AlertDialog.Builder(this)
            .setTitle("Reiniciar plano")
            .setMessage("Deseja recomeçar o plano \"${activePlan.nome}\" desde o dia 1?")
            .setPositiveButton("Reiniciar") { _, _ ->
                journeyManager.startPlan(activePlan.id)
                Toast.makeText(this, "Plano reiniciado com sucesso.", Toast.LENGTH_SHORT).show()
                atualizarTela()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
