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

        prepararTelaEbookOracao(
            monthlyButton = monthlyButton,
            yearlyButton = yearlyButton,
            deactivateButton = deactivateButton,
            selectedPlanTextView = selectedPlanTextView,
            premiumStatusTextView = premiumStatusTextView,
            legalTextView = legalTextView
        )

        monthlyButton.setOnClickListener {
            startActivity(Intent(this, EbookOracaoActivity::class.java))
        }

        yearlyButton.setOnClickListener {
            startActivity(Intent(this, EbookOracaoActivity::class.java))
        }

        contactButton.setOnClickListener {
            abrirContatoSuporte()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun prepararTelaEbookOracao(
        monthlyButton: Button,
        yearlyButton: Button,
        deactivateButton: Button,
        selectedPlanTextView: TextView,
        premiumStatusTextView: TextView,
        legalTextView: TextView
    ) {
        if (premiumManager.isPremiumActive()) {
            premiumManager.deactivatePremium()
        }

        monthlyButton.text = "Ver E-book de Oração"
        yearlyButton.text = "Liberar acesso pela Google Play"

        deactivateButton.visibility = View.GONE
        selectedPlanTextView.visibility = View.VISIBLE
        selectedPlanTextView.text = "Produto digital premium: E-book de Oração. O acesso é liberado com segurança pela Google Play."

        premiumStatusTextView.text = "E-book premium disponível para liberação."
        legalTextView.text = "A compra é processada pela Google Play. Se você trocar de aparelho, use a opção Restaurar compra dentro da tela do e-book."
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
