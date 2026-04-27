package com.elias.manadiario

import android.content.Context
import androidx.preference.PreferenceManager

class PremiumManager(context: Context) {

    companion object {
        private const val KEY_PREMIUM_ACTIVE = "premium_active"
        private const val KEY_PREMIUM_PLAN_NAME = "premium_plan_name"
    }

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    fun activatePremium(planName: String) {
        prefs.edit()
            .putBoolean(KEY_PREMIUM_ACTIVE, true)
            .putString(KEY_PREMIUM_PLAN_NAME, planName)
            .apply()
    }

    fun deactivatePremium() {
        prefs.edit()
            .putBoolean(KEY_PREMIUM_ACTIVE, false)
            .remove(KEY_PREMIUM_PLAN_NAME)
            .apply()
    }

    fun isPremiumActive(): Boolean = prefs.getBoolean(KEY_PREMIUM_ACTIVE, false)

    fun getActivePlanName(): String? = prefs.getString(KEY_PREMIUM_PLAN_NAME, null)
}
