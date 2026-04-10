package com.elias.manadiario

import android.content.Context
import androidx.preference.PreferenceManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class JourneyManager(private val context: Context) {

    companion object {
        private const val KEY_PLAN_ID = "journey_plan_id"
        private const val KEY_CURRENT_DAY = "journey_current_day"
        private const val KEY_COMPLETED_DAYS = "journey_completed_days"
        private const val KEY_CURRENT_STREAK = "journey_current_streak"
        private const val KEY_LAST_COMPLETION_DATE = "journey_last_completion_date"
        private const val KEY_PLAN_START_DATE = "journey_plan_start_date"
    }

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    fun startPlan(planId: String) {
        val today = getTodayString()

        prefs.edit()
            .putString(KEY_PLAN_ID, planId)
            .putInt(KEY_CURRENT_DAY, 1)
            .putInt(KEY_COMPLETED_DAYS, 0)
            .putInt(KEY_CURRENT_STREAK, 0)
            .putString(KEY_LAST_COMPLETION_DATE, "")
            .putString(KEY_PLAN_START_DATE, today)
            .apply()
    }

    fun hasActivePlan(): Boolean {
        return !getActivePlanId().isNullOrEmpty()
    }

    fun getActivePlanId(): String? {
        return prefs.getString(KEY_PLAN_ID, null)
    }

    fun getActivePlan(): DevotionalPlan? {
        val planId = getActivePlanId() ?: return null
        return DevotionalRepository.getPlanById(planId)
    }

    fun getCurrentDay(): Int {
        return prefs.getInt(KEY_CURRENT_DAY, 1)
    }

    fun getCompletedDays(): Int {
        return prefs.getInt(KEY_COMPLETED_DAYS, 0)
    }

    fun getCurrentStreak(): Int {
        return prefs.getInt(KEY_CURRENT_STREAK, 0)
    }

    fun getPlanStartDate(): String {
        return prefs.getString(KEY_PLAN_START_DATE, "") ?: ""
    }

    fun getLastCompletionDate(): String {
        return prefs.getString(KEY_LAST_COMPLETION_DATE, "") ?: ""
    }

    fun isTodayCompleted(): Boolean {
        return getLastCompletionDate() == getTodayString()
    }

    fun getProgressPercentage(): Int {
        val plan = getActivePlan() ?: return 0
        if (plan.dias.isEmpty()) return 0

        return ((getCompletedDays().toFloat() / plan.dias.size.toFloat()) * 100).toInt()
    }

    fun getTodayDevotional(): DevotionalDay? {
        val plan = getActivePlan() ?: return null
        val currentDay = getCurrentDay()

        return plan.dias.find { it.dia == currentDay }
    }

    fun completeToday(): Boolean {
        if (!hasActivePlan()) return false
        if (isTodayCompleted()) return false

        val plan = getActivePlan() ?: return false
        val today = getTodayString()
        val yesterday = getYesterdayString()

        var completedDays = getCompletedDays()
        var currentDay = getCurrentDay()
        var currentStreak = getCurrentStreak()
        val lastCompletionDate = getLastCompletionDate()

        completedDays += 1

        currentStreak = when {
            lastCompletionDate.isEmpty() -> 1
            lastCompletionDate == yesterday -> currentStreak + 1
            else -> 1
        }

        if (currentDay < plan.dias.size) {
            currentDay += 1
        }

        prefs.edit()
            .putInt(KEY_COMPLETED_DAYS, completedDays)
            .putInt(KEY_CURRENT_DAY, currentDay)
            .putInt(KEY_CURRENT_STREAK, currentStreak)
            .putString(KEY_LAST_COMPLETION_DATE, today)
            .apply()

        return true
    }

    fun resetJourney() {
        prefs.edit()
            .remove(KEY_PLAN_ID)
            .remove(KEY_CURRENT_DAY)
            .remove(KEY_COMPLETED_DAYS)
            .remove(KEY_CURRENT_STREAK)
            .remove(KEY_LAST_COMPLETION_DATE)
            .remove(KEY_PLAN_START_DATE)
            .apply()
    }

    private fun getTodayString(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }

    private fun getYesterdayString(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)

        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return formatter.format(calendar.time)
    }
}