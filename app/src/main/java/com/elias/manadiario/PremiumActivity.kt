package com.elias.manadiario

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
            mostrarEmBreve()
        }

        yearlyButton.setOnClickListener {
            mostrarEmBreve()
        }

        contactButton.setOnClickListener {
            mostrarEmBreve()
        }

        monthlyButton.isEnabled = false
        yearlyButton.isEnabled = false
        contactButton.isEnabled = false
        monthlyButton.alpha = 0.7f
        yearlyButton.alpha = 0.7f
        contactButton.alpha = 0.7f

        legalTextView.text = getString(R.string.premium_coming_soon_description)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun mostrarEmBreve() {
        Toast.makeText(
            this,
            getString(R.string.premium_coming_soon_toast),
            Toast.LENGTH_LONG
        ).show()
    }
}
