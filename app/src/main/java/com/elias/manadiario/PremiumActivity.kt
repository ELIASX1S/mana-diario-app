package com.elias.manadiario

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PremiumActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_premium)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.premium_title)

        val monthlyButton = findViewById<Button>(R.id.buttonMonthlyPlan)
        val yearlyButton = findViewById<Button>(R.id.buttonYearlyPlan)
        val contactButton = findViewById<Button>(R.id.buttonContactPremium)
        val legalTextView = findViewById<TextView>(R.id.textPremiumLegal)
        val selectedPlanTextView = findViewById<TextView>(R.id.textPremiumSelectedPlan)

        monthlyButton.setOnClickListener {
            selecionarPlano(
                planName = getString(R.string.premium_plan_monthly_name),
                selectedPlanTextView = selectedPlanTextView
            )
        }

        yearlyButton.setOnClickListener {
            selecionarPlano(
                planName = getString(R.string.premium_plan_yearly_name),
                selectedPlanTextView = selectedPlanTextView
            )
        }

        contactButton.setOnClickListener {
            abrirContatoSuporte()
        }

        legalTextView.text = getString(R.string.premium_legal_text)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun selecionarPlano(planName: String, selectedPlanTextView: TextView) {
        selectedPlanTextView.visibility = TextView.VISIBLE
        selectedPlanTextView.text = getString(R.string.premium_plan_selected_toast, planName)

        Toast.makeText(
            this,
            getString(R.string.premium_checkout_ready),
            Toast.LENGTH_SHORT
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
