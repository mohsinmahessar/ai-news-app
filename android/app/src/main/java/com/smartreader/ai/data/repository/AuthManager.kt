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

private val Context.authDataStore by preferencesDataStore(name = "auth")

/**
 * Lightweight session store. Tracks whether the user is signed in and their
 * display name/email.
 *
 * Google Sign-In integration (Credential Manager + Firebase Auth) calls
 * [signIn] after a successful credential exchange. We keep the session locally so
 * the app builds and runs even before google-services.json is added — see the
 * README "Firebase Setup" section for wiring the real Firebase Auth flow.
 */
@Singleton
class AuthManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val loggedInKey = booleanPreferencesKey("logged_in")
    private val nameKey = stringPreferencesKey("display_name")
    private val emailKey = stringPreferencesKey("email")

    val isLoggedIn: Flow<Boolean> = context.authDataStore.data.map { it[loggedInKey] ?: false }
    val displayName: Flow<String> = context.authDataStore.data.map { it[nameKey] ?: "Reader" }
    val email: Flow<String> = context.authDataStore.data.map { it[emailKey] ?: "" }

    suspend fun isLoggedInNow(): Boolean = context.authDataStore.data.first()[loggedInKey] ?: false

    suspend fun signIn(name: String, email: String) {
        context.authDataStore.edit {
            it[loggedInKey] = true
            it[nameKey] = name
            it[emailKey] = email
        }
    }

    suspend fun signOut() {
        context.authDataStore.edit { it.clear() }
    }
}
