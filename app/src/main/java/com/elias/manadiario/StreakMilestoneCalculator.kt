package com.elias.manadiario

data class StreakMilestone(
    val targetDays: Int,
    val daysRemaining: Int,
    val progressPercent: Int,
    val completed: Boolean
)

object StreakMilestoneCalculator {

    fun calculate(streakDays: Int): StreakMilestone {
        val safeStreak = streakDays.coerceAtLeast(0)
        val milestone = when {
            safeStreak < 3 -> 3
            safeStreak < 7 -> 7
            safeStreak < 14 -> 14
            safeStreak < 30 -> 30
            else -> ((safeStreak / 30) + 1) * 30
        }

        val completed = safeStreak >= milestone
        val daysRemaining = (milestone - safeStreak).coerceAtLeast(0)
        val progress = ((safeStreak.toFloat() / milestone) * 100).toInt().coerceIn(0, 100)

        return StreakMilestone(
            targetDays = milestone,
            daysRemaining = daysRemaining,
            progressPercent = progress,
            completed = completed
        )
    }
}
