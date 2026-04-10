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

        monthlyButton.setOnClickListener {
            mostrarFluxoDeAssinatura(getString(R.string.premium_plan_monthly_name))
        }

        yearlyButton.setOnClickListener {
            mostrarFluxoDeAssinatura(getString(R.string.premium_plan_yearly_name))
        }

        contactButton.setOnClickListener {
            abrirContatoComercial()
        }

        legalTextView.text = getString(R.string.premium_legal_text)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun mostrarFluxoDeAssinatura(planName: String) {
        Toast.makeText(
            this,
            getString(R.string.premium_checkout_placeholder, planName),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun abrirContatoComercial() {
        val intent = Intent(
            Intent.ACTION_SENDTO,
            Uri.parse("mailto:" + getString(R.string.premium_contact_email))
        ).apply {
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.premium_contact_subject))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.premium_contact_body))
        }

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, getString(R.string.error_no_app_to_share), Toast.LENGTH_SHORT).show()
        }
    }
}
