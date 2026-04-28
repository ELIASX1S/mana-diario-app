package com.elias.manadiario

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class EbookOracaoActivity : AppCompatActivity() {

    private lateinit var billingManager: EbookPrayerBillingManager
    private lateinit var statusTextView: TextView
    private lateinit var contentTextView: TextView
    private lateinit var buyButton: MaterialButton
    private lateinit var restoreButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ebook_oracao)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "E-book de Oração"

        statusTextView = findViewById(R.id.textEbookStatus)
        contentTextView = findViewById(R.id.textEbookContent)
        buyButton = findViewById(R.id.buttonBuyEbook)
        restoreButton = findViewById(R.id.buttonRestoreEbook)

        billingManager = EbookPrayerBillingManager(
            context = this,
            listener = object : EbookPrayerBillingManager.Listener {
                override fun onBillingReady(productDetails: com.android.billingclient.api.ProductDetails?) {
                    val price = productDetails?.oneTimePurchaseOfferDetails?.formattedPrice
                    buyButton.text = if (price.isNullOrBlank()) {
                        "Liberar e-book"
                    } else {
                        "Liberar e-book por $price"
                    }
                }

                override fun onPurchaseUnlocked() {
                    renderUnlockedState()
                }

                override fun onBillingMessage(message: String) {
                    statusTextView.text = message
                }
            }
        )

        buyButton.setOnClickListener {
            billingManager.launchPurchase(this)
        }

        restoreButton.setOnClickListener {
            billingManager.restorePurchases()
        }

        if (billingManager.isEbookUnlocked()) {
            renderUnlockedState()
        } else {
            renderLockedState()
            billingManager.startConnection()
        }
    }

    private fun renderLockedState() {
        statusTextView.text = "Conteúdo premium bloqueado. Libere o e-book pela Google Play para acessar."
        contentTextView.text = "O E-book de Oração do Maná Diário foi preparado para ajudar você a criar uma rotina de oração mais constante, profunda e guiada."
        buyButton.visibility = View.VISIBLE
        restoreButton.visibility = View.VISIBLE
    }

    private fun renderUnlockedState() {
        statusTextView.text = "E-book liberado ✅"
        contentTextView.text = buildString {
            appendLine("E-book de Oração")
            appendLine()
            appendLine("Capítulo 1 — Comece com sinceridade")
            appendLine("A oração não precisa começar perfeita. Comece falando com Deus com verdade, humildade e constância.")
            appendLine()
            appendLine("Capítulo 2 — Ore antes de decidir")
            appendLine("Antes de tomar decisões importantes, pare, respire e apresente seus caminhos diante de Deus.")
            appendLine()
            appendLine("Capítulo 3 — Crie um horário sagrado")
            appendLine("Escolha um momento do dia para buscar a presença de Deus sem pressa. A constância transforma a fé em hábito.")
            appendLine()
            appendLine("Capítulo 4 — Oração simples para começar")
            appendLine("Senhor, guia meu coração hoje. Fortalece minha fé, limpa meus pensamentos e me aproxima da Tua vontade. Amém.")
        }
        buyButton.visibility = View.GONE
        restoreButton.visibility = View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onDestroy() {
        billingManager.endConnection()
        super.onDestroy()
    }
}
