package com.smartreader.ai.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.settingsDataStore by preferencesDataStore(name = "settings")

/** Light by default so the brand always opens bright; user can switch to dark/system. */
enum class ThemeMode { LIGHT, DARK, SYSTEM }

/**
 * App-wide settings store (theme + first-run sample-seeding flag).
 *
 * Kept separate from [AuthManager]'s store because sign-out clears that one —
 * we don't want signing out to reset the user's theme or re-seed samples.
 */
@Singleton
class ThemeManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val themeKey = stringPreferencesKey("theme_mode")
    private val seededKey = booleanPreferencesKey("seeded")

    val themeMode: Flow<ThemeMode> = context.settingsDataStore.data.map { prefs ->
        runCatching { ThemeMode.valueOf(prefs[themeKey] ?: ThemeMode.LIGHT.name) }
            .getOrDefault(ThemeMode.LIGHT)
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.settingsDataStore.edit { it[themeKey] = mode.name }
    }

    suspend fun isSeeded(): Boolean = context.settingsDataStore.data.first()[seededKey] ?: false

    suspend fun setSeeded(seeded: Boolean) {
        context.settingsDataStore.edit { it[seededKey] = seeded }
    }
}
