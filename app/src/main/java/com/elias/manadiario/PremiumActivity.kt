package com.elias.manadiario

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
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

        prepararTelaPremiumEmBreve(
            monthlyButton = monthlyButton,
            yearlyButton = yearlyButton,
            deactivateButton = deactivateButton,
            selectedPlanTextView = selectedPlanTextView,
            premiumStatusTextView = premiumStatusTextView,
            legalTextView = legalTextView
        )

        monthlyButton.setOnClickListener {
            avisarPremiumEmBreve(getString(R.string.premium_plan_monthly_name))
        }

        yearlyButton.setOnClickListener {
            avisarPremiumEmBreve(getString(R.string.premium_plan_yearly_name))
        }

        contactButton.setOnClickListener {
            abrirContatoSuporte()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun prepararTelaPremiumEmBreve(
        monthlyButton: Button,
        yearlyButton: Button,
        deactivateButton: Button,
        selectedPlanTextView: TextView,
        premiumStatusTextView: TextView,
        legalTextView: TextView
    ) {
        // Garante que nenhum usuário fique marcado como Premium por um teste antigo.
        // Quando o Google Play Billing for integrado, essa regra deve ser substituída pela validação real da assinatura.
        if (premiumManager.isPremiumActive()) {
            premiumManager.deactivatePremium()
        }

        monthlyButton.text = getString(R.string.premium_plan_monthly_cta)
        yearlyButton.text = getString(R.string.premium_plan_yearly_cta)

        deactivateButton.visibility = View.GONE
        selectedPlanTextView.visibility = View.VISIBLE
        selectedPlanTextView.text = getString(R.string.premium_coming_soon_description)

        premiumStatusTextView.text = getString(R.string.premium_inactive_status)
        legalTextView.text = getString(R.string.premium_legal_text)
    }

    private fun avisarPremiumEmBreve(planName: String) {
        Toast.makeText(
            this,
            getString(R.string.premium_checkout_placeholder, planName),
            Toast.LENGTH_LONG
        ).show()
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
