package com.smartreader.ai.data.repository

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.acknowledgePurchase
import com.android.billingclient.api.queryProductDetails
import com.android.billingclient.api.queryPurchasesAsync
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import com.android.billingclient.api.AcknowledgePurchaseParams
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Google Play Billing wrapper for the Premium subscription.
 *
 * Flow:
 *  1. Connect to Play, query the "premium_monthly" subscription product.
 *  2. [launchPurchase] starts the Play purchase UI.
 *  3. On a successful purchase we acknowledge it and flip the premium flag in
 *     [AiUsageManager], which immediately unlocks unlimited explanations.
 *
 * Set up the product id in the Play Console (Monetize → Subscriptions) before
 * testing on a real device with a license-test account.
 */
@Singleton
class BillingManager @Inject constructor(
    @ApplicationContext context: Context,
    private val usageManager: AiUsageManager,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var premiumProduct: ProductDetails? = null

    private val purchasesListener = PurchasesUpdatedListener { result, purchases ->
        if (result.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            purchases.forEach { handlePurchase(it) }
        }
    }

    private val client: BillingClient = BillingClient.newBuilder(context)
        .setListener(purchasesListener)
        .enablePendingPurchases()
        .build()

    fun connect() {
        if (client.isReady) return
        client.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    scope.launch { queryProducts(); restorePurchases() }
                }
            }
            override fun onBillingServiceDisconnected() { /* Play will reconnect on next call */ }
        })
    }

    private suspend fun queryProducts() {
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(PREMIUM_PRODUCT_ID)
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build()
                )
            ).build()
        premiumProduct = client.queryProductDetails(params).productDetailsList?.firstOrNull()
    }

    fun launchPurchase(activity: Activity) {
        val product = premiumProduct ?: return
        val offerToken = product.subscriptionOfferDetails?.firstOrNull()?.offerToken ?: return
        val params = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(
                listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(product)
                        .setOfferToken(offerToken)
                        .build()
                )
            ).build()
        client.launchBillingFlow(activity, params)
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState != Purchase.PurchaseState.PURCHASED) return
        scope.launch {
            if (!purchase.isAcknowledged) {
                client.acknowledgePurchase(
                    AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()
                )
            }
            usageManager.setPremium(true)
        }
    }

    private suspend fun restorePurchases() {
        val params = com.android.billingclient.api.QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS).build()
        val purchases = client.queryPurchasesAsync(params).purchasesList
        val active = purchases.any { it.purchaseState == Purchase.PurchaseState.PURCHASED }
        usageManager.setPremium(active)
    }

    companion object {
        const val PREMIUM_PRODUCT_ID = "premium_monthly"
    }
}
