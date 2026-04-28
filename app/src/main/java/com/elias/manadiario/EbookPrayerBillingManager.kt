package com.elias.manadiario

import android.app.Activity
import android.content.Context
import androidx.preference.PreferenceManager
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams

class EbookPrayerBillingManager(
    private val context: Context,
    private val listener: Listener
) : PurchasesUpdatedListener {

    interface Listener {
        fun onBillingReady(productDetails: ProductDetails?)
        fun onPurchaseUnlocked()
        fun onBillingMessage(message: String)
    }

    companion object {
        const val PRODUCT_ID_EBOOK_ORACAO = "ebook_oracao_mana_premium"
        private const val KEY_EBOOK_ORACAO_UNLOCKED = "ebook_oracao_unlocked"
    }

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
    private var productDetails: ProductDetails? = null

    private val billingClient: BillingClient = BillingClient.newBuilder(context.applicationContext)
        .setListener(this)
        .enablePendingPurchases()
        .build()

    fun startConnection() {
        if (billingClient.isReady) {
            queryProduct()
            queryExistingPurchases()
            return
        }

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryProduct()
                    queryExistingPurchases()
                } else {
                    listener.onBillingMessage("Não foi possível conectar à Google Play agora.")
                }
            }

            override fun onBillingServiceDisconnected() {
                listener.onBillingMessage("Conexão com a Google Play interrompida. Tente novamente.")
            }
        })
    }

    private fun queryProduct() {
        val product = QueryProductDetailsParams.Product.newBuilder()
            .setProductId(PRODUCT_ID_EBOOK_ORACAO)
            .setProductType(BillingClient.ProductType.INAPP)
            .build()

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(listOf(product))
            .build()

        billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                productDetails = productDetailsList.firstOrNull()
                listener.onBillingReady(productDetails)
            } else {
                listener.onBillingMessage("Produto indisponível no momento.")
            }
        }
    }

    private fun queryExistingPurchases() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()

        billingClient.queryPurchasesAsync(params) { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                purchases.forEach { purchase ->
                    handlePurchase(purchase)
                }
            }
        }
    }

    fun launchPurchase(activity: Activity) {
        val details = productDetails
        if (details == null) {
            listener.onBillingMessage("A compra ainda não está pronta. Verifique se o produto foi criado no Play Console.")
            queryProduct()
            return
        }

        val productDetailsParams = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(details)
            .build()

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(productDetailsParams))
            .build()

        billingClient.launchBillingFlow(activity, billingFlowParams)
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                purchases.orEmpty().forEach { purchase ->
                    handlePurchase(purchase)
                }
            }
            BillingClient.BillingResponseCode.USER_CANCELED -> {
                listener.onBillingMessage("Compra cancelada.")
            }
            else -> {
                listener.onBillingMessage("Não foi possível concluir a compra agora.")
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        val hasEbook = purchase.products.contains(PRODUCT_ID_EBOOK_ORACAO)
        val purchased = purchase.purchaseState == Purchase.PurchaseState.PURCHASED

        if (hasEbook && purchased) {
            prefs.edit().putBoolean(KEY_EBOOK_ORACAO_UNLOCKED, true).apply()
            listener.onPurchaseUnlocked()

            if (!purchase.isAcknowledged) {
                val params = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()

                billingClient.acknowledgePurchase(params) { result ->
                    if (result.responseCode != BillingClient.BillingResponseCode.OK) {
                        listener.onBillingMessage("Compra liberada, mas houve falha ao confirmar na Google Play.")
                    }
                }
            }
        }
    }

    fun isEbookUnlocked(): Boolean {
        return prefs.getBoolean(KEY_EBOOK_ORACAO_UNLOCKED, false)
    }

    fun restorePurchases() {
        if (billingClient.isReady) {
            queryExistingPurchases()
            listener.onBillingMessage("Verificando compras anteriores...")
        } else {
            startConnection()
        }
    }

    fun endConnection() {
        if (billingClient.isReady) {
            billingClient.endConnection()
        }
    }
}
