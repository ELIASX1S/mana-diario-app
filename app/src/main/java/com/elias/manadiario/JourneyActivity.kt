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
        supportActionBar?.title = getString(R.string.journey_toolbar_title)
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
            getString(R.string.journey_plan_completed)
        } else {
            getString(R.string.journey_day_progress, currentDay, totalDays)
        }

        progressTextView.text = getString(R.string.journey_progress_format, progressPercentage, completedDays, totalDays)
        streakTextView.text = getString(R.string.journey_streak_format, currentStreak)
        startDateTextView.text = getString(R.string.journey_started_at, startDate)
        completedDaysTextView.text = completedDays.toString()
        remainingDaysTextView.text = remainingDays.toString()
        streakValueTextView.text = currentStreak.toString()
        progressBar.progress = progressPercentage

        if (planFinished) {
            statusTextView.text = getString(R.string.journey_status_finished)
            completionMessageTextView.text = getString(R.string.journey_finished_message)
            completionMessageTextView.visibility = TextView.VISIBLE
            continueButton.text = getString(R.string.journey_cta_review)
            continueButton.isEnabled = true
        } else {
            val nextDay = (currentDay + 1).coerceAtMost(totalDays)
            statusTextView.text = if (todayCompleted) {
                getString(R.string.journey_status_today_done)
            } else {
                getString(R.string.journey_status_today_pending)
            }

            completionMessageTextView.text = if (todayCompleted) {
                getString(R.string.journey_message_next_day, nextDay)
            } else {
                getString(R.string.journey_message_remaining, remainingDays)
            }
            completionMessageTextView.visibility = TextView.VISIBLE

            continueButton.text = if (todayCompleted) {
                getString(R.string.journey_cta_view_today)
            } else {
                getString(R.string.journey_cta_continue)
            }
            continueButton.isEnabled = true
        }
    }

    private fun confirmarReinicio() {
        val activePlan = journeyManager.getActivePlan() ?: return

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.journey_restart_title))
            .setMessage(getString(R.string.journey_restart_message, activePlan.nome))
            .setPositiveButton(getString(R.string.journey_restart_confirm)) { _, _ ->
                journeyManager.startPlan(activePlan.id)
                Toast.makeText(this, getString(R.string.journey_restart_success), Toast.LENGTH_SHORT).show()
                atualizarTela()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
