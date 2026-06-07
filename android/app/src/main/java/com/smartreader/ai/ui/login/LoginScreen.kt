package com.smartreader.ai.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartreader.ai.data.repository.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authManager: AuthManager,
) : ViewModel() {

    /**
     * Called after a successful Google credential exchange. In production, pass
     * the Firebase user's name/email here. See README → Firebase Setup for the
     * Credential Manager + FirebaseAuth.signInWithCredential wiring.
     */
    fun completeSignIn(name: String, email: String, onDone: () -> Unit) {
        viewModelScope.launch {
            authManager.signIn(name, email)
            onDone()
        }
    }
}

@Composable
fun LoginScreen(
    onSignedIn: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                "Welcome to SmartReader AI",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
            )
            Text(
                "Read smarter. Tap any word to understand it instantly.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 12.dp, bottom = 40.dp),
            )

            Button(
                onClick = {
                    // TODO: launch Credential Manager Google Sign-In, then pass real profile.
                    viewModel.completeSignIn("Reader", "reader@example.com", onSignedIn)
                },
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 14.dp),
            ) {
                Text("Continue with Google")
            }

            TextButton(
                onClick = { viewModel.completeSignIn("Guest", "", onSignedIn) },
                modifier = Modifier.padding(top = 8.dp),
            ) {
                Text("Continue as guest")
            }
        }
    }
}
