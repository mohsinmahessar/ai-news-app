package com.smartreader.ai.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smartreader.ai.data.repository.AiUsageManager
import com.smartreader.ai.data.repository.AuthManager
import com.smartreader.ai.data.repository.BillingManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authManager: AuthManager,
    usageManager: AiUsageManager,
    val billingManager: BillingManager,
) : ViewModel() {

    val email = authManager.email
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "")
    val isPremium = usageManager.isPremium
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)
    val remaining = usageManager.remainingToday
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AiUsageManager.FREE_DAILY_LIMIT)

    init { billingManager.connect() }

    fun signOut(onDone: () -> Unit) {
        viewModelScope.launch { authManager.signOut(); onDone() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val email by viewModel.email.collectAsStateWithLifecycle()
    val isPremium by viewModel.isPremium.collectAsStateWithLifecycle()
    val remaining by viewModel.remaining.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(20.dp)) {
                    Text(if (isPremium) "Premium" else "Free plan", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    if (isPremium) {
                        Text("Unlimited explanations, summaries, and vocabulary export.", modifier = Modifier.padding(top = 6.dp))
                    } else {
                        Text(
                            "$remaining of ${AiUsageManager.FREE_DAILY_LIMIT} free explanations left today.",
                            modifier = Modifier.padding(top = 6.dp),
                        )
                        Button(
                            onClick = { (context as? android.app.Activity)?.let { viewModel.billingManager.launchPurchase(it) } },
                            modifier = Modifier.padding(top = 12.dp),
                        ) { Text("Upgrade to Premium") }
                    }
                }
            }

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(20.dp)) {
                    Text("Account", style = MaterialTheme.typography.titleLarge)
                    Text(email.ifBlank { "Guest" }, modifier = Modifier.padding(top = 6.dp))
                    OutlinedButton(onClick = { viewModel.signOut(onBack) }, modifier = Modifier.padding(top = 12.dp)) {
                        Text("Sign out")
                    }
                }
            }

            Text(
                "Theme follows your system Light/Dark setting. Reading mode can be toggled in the reader.",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            )
        }
    }
}
