package com.elias.manadiario

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PremiumActivity : AppCompatActivity() {

    private lateinit var premiumManager: PremiumManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_premium)
        premiumManager = PremiumManager(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.premium_title)

        val monthlyButton = findViewById<Button>(R.id.buttonMonthlyPlan)
        val yearlyButton = findViewById<Button>(R.id.buttonYearlyPlan)
        val contactButton = findViewById<Button>(R.id.buttonContactPremium)
        val deactivateButton = findViewById<Button>(R.id.buttonDeactivatePremium)
        val legalTextView = findViewById<TextView>(R.id.textPremiumLegal)
        val selectedPlanTextView = findViewById<TextView>(R.id.textPremiumSelectedPlan)
        val premiumStatusTextView = findViewById<TextView>(R.id.textPremiumStatus)

        monthlyButton.setOnClickListener {
            selecionarPlano(
                planName = getString(R.string.premium_plan_monthly_name),
                selectedPlanTextView = selectedPlanTextView
            )
            renderPremiumState(selectedPlanTextView, premiumStatusTextView)
        }

        yearlyButton.setOnClickListener {
            selecionarPlano(
                planName = getString(R.string.premium_plan_yearly_name),
                selectedPlanTextView = selectedPlanTextView
            )
            renderPremiumState(selectedPlanTextView, premiumStatusTextView)
        }

        contactButton.setOnClickListener {
            abrirContatoSuporte()
        }

        deactivateButton.setOnClickListener {
            premiumManager.deactivatePremium()
            renderPremiumState(selectedPlanTextView, premiumStatusTextView)
            Toast.makeText(this, getString(R.string.premium_deactivated_toast), Toast.LENGTH_SHORT).show()
        }

        legalTextView.text = getString(R.string.premium_legal_text)
        renderPremiumState(selectedPlanTextView, premiumStatusTextView)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun selecionarPlano(planName: String, selectedPlanTextView: TextView) {
        premiumManager.activatePremium(planName)
        selectedPlanTextView.visibility = TextView.VISIBLE
        selectedPlanTextView.text = getString(R.string.premium_plan_selected_toast, planName)

        Toast.makeText(
            this,
            getString(R.string.premium_checkout_ready),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun renderPremiumState(selectedPlanTextView: TextView, premiumStatusTextView: TextView) {
        val isPremium = premiumManager.isPremiumActive()
        val activePlanName = premiumManager.getActivePlanName()

        premiumStatusTextView.text = if (isPremium && !activePlanName.isNullOrBlank()) {
            getString(R.string.premium_active_status, activePlanName)
        } else {
            getString(R.string.premium_inactive_status)
        }

        if (isPremium && !activePlanName.isNullOrBlank()) {
            selectedPlanTextView.visibility = TextView.VISIBLE
            selectedPlanTextView.text = getString(R.string.premium_plan_selected_toast, activePlanName)
        } else {
            selectedPlanTextView.visibility = TextView.GONE
        }
    }

    private fun abrirContatoSuporte() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:${getString(R.string.premium_contact_email)}")
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.premium_contact_subject))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.premium_contact_body))
        }

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, getString(R.string.premium_support_error), Toast.LENGTH_SHORT).show()
        }
    }
}
