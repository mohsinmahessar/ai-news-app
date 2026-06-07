package com.smartreader.ai.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

private val Context.usageDataStore by preferencesDataStore(name = "ai_usage")

/**
 * Enforces the freemium quota: 20 AI explanations per day on the Free plan,
 * unlimited on Premium. The counter resets at local midnight.
 *
 * Premium status is set by [BillingManager] after a successful purchase.
 */
@Singleton
class AiUsageManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val countKey = intPreferencesKey("usage_count")
    private val dayKey = longPreferencesKey("usage_day")
    private val premiumKey = booleanPreferencesKey("is_premium")

    val isPremium: Flow<Boolean> =
        context.usageDataStore.data.map { it[premiumKey] ?: false }

    /** Remaining free explanations today (Int.MAX_VALUE if premium). */
    val remainingToday: Flow<Int> = context.usageDataStore.data.map { prefs ->
        if (prefs[premiumKey] == true) return@map Int.MAX_VALUE
        val storedDay = prefs[dayKey] ?: 0L
        val used = if (storedDay == today()) prefs[countKey] ?: 0 else 0
        (FREE_DAILY_LIMIT - used).coerceAtLeast(0)
    }

    /** @return true if the action is allowed; increments the counter when it is. */
    suspend fun tryConsume(): Boolean {
        val prefs = context.usageDataStore.data.first()
        if (prefs[premiumKey] == true) return true

        val storedDay = prefs[dayKey] ?: 0L
        val used = if (storedDay == today()) prefs[countKey] ?: 0 else 0
        if (used >= FREE_DAILY_LIMIT) return false

        context.usageDataStore.edit {
            it[dayKey] = today()
            it[countKey] = used + 1
        }
        return true
    }

    suspend fun setPremium(premium: Boolean) {
        context.usageDataStore.edit { it[premiumKey] = premium }
    }

    private fun today(): Long = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    companion object {
        const val FREE_DAILY_LIMIT = 20
    }
}

/** Thrown when the free daily quota is exhausted, so the UI can show the paywall. */
class QuotaExceededException : Exception("Daily free limit reached")
