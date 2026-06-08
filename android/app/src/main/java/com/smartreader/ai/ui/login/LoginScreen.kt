package com.smartreader.ai.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Login
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartreader.ai.R
import com.smartreader.ai.data.repository.AuthManager
import com.smartreader.ai.ui.components.PrimaryButton
import com.smartreader.ai.ui.theme.brandGradient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authManager: AuthManager,
) : ViewModel() {
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
        Column(modifier = Modifier.fillMaxSize()) {
            // Gradient hero with logo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .clip(RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp))
                    .background(brandGradient()),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(R.drawable.ic_logo),
                        contentDescription = null,
                        modifier = Modifier.size(88.dp),
                    )
                    Text(
                        "SmartReader AI",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(top = 16.dp),
                    )
                    Text(
                        "Understand Every Word While You Read.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 6.dp, start = 32.dp, end = 32.dp),
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    "Read smarter, not harder.",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                )
                Text(
                    "Tap any word for an instant, context-aware explanation.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp, bottom = 32.dp),
                )

                PrimaryButton(
                    text = "Continue with Google",
                    leadingIcon = Icons.Default.Login,
                    onClick = {
                        // TODO: launch Credential Manager Google Sign-In, then pass real profile.
                        viewModel.completeSignIn("Reader", "reader@example.com", onSignedIn)
                    },
                )
                TextButton(
                    onClick = { viewModel.completeSignIn("Guest", "", onSignedIn) },
                    modifier = Modifier.padding(top = 8.dp),
                ) {
                    Text("Continue as guest")
                }
            }
        }
    }
}
