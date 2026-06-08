package com.smartreader.ai.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
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
import com.smartreader.ai.data.repository.ThemeManager
import com.smartreader.ai.data.repository.ThemeMode
import com.smartreader.ai.ui.theme.brandGradient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authManager: AuthManager,
    private val themeManager: ThemeManager,
    usageManager: AiUsageManager,
    val billingManager: BillingManager,
) : ViewModel() {

    val email = authManager.email
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "")
    val isPremium = usageManager.isPremium
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)
    val remaining = usageManager.remainingToday
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AiUsageManager.FREE_DAILY_LIMIT)
    val themeMode = themeManager.themeMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ThemeMode.LIGHT)

    init { billingManager.connect() }

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch { themeManager.setThemeMode(mode) }
    }

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
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
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
            // Premium plan card — gradient when not yet premium to entice upgrade.
            Card(shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = (if (!isPremium) Modifier.background(brandGradient()) else Modifier).padding(20.dp)
                ) {
                    val onCard = if (!isPremium) Color.White else MaterialTheme.colorScheme.onSurface
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.WorkspacePremium,
                            contentDescription = null,
                            tint = if (!isPremium) Color.White else MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp),
                        )
                        Text(
                            if (isPremium) "  Premium" else "  Go Premium",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = onCard,
                        )
                    }
                    if (isPremium) {
                        Text(
                            "Unlimited explanations, summaries, and vocabulary export.",
                            color = onCard,
                            modifier = Modifier.padding(top = 8.dp),
                        )
                    } else {
                        Text(
                            "$remaining of ${AiUsageManager.FREE_DAILY_LIMIT} free explanations left today. Upgrade for unlimited everything.",
                            color = onCard.copy(alpha = 0.9f),
                            modifier = Modifier.padding(top = 8.dp),
                        )
                        Button(
                            onClick = { (context as? android.app.Activity)?.let { viewModel.billingManager.launchPurchase(it) } },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = MaterialTheme.colorScheme.primary,
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.padding(top = 14.dp),
                        ) { Text("Upgrade to Premium", fontWeight = FontWeight.SemiBold) }
                    }
                }
            }

            // Appearance / theme selector
            Card(shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(20.dp)) {
                    Text("Appearance", style = MaterialTheme.typography.titleLarge)
                    Text(
                        "Choose how SmartReader AI looks.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 2.dp, bottom = 8.dp),
                    )
                    ThemeOption("Light", ThemeMode.LIGHT, themeMode, viewModel::setThemeMode)
                    ThemeOption("Dark", ThemeMode.DARK, themeMode, viewModel::setThemeMode)
                    ThemeOption("Follow system", ThemeMode.SYSTEM, themeMode, viewModel::setThemeMode)
                }
            }

            // Account
            Card(shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(20.dp)) {
                    Text("Account", style = MaterialTheme.typography.titleLarge)
                    Text(
                        email.ifBlank { "Guest" },
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 6.dp),
                    )
                    OutlinedButton(
                        onClick = { viewModel.signOut(onBack) },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.padding(top = 12.dp),
                    ) { Text("Sign out") }
                }
            }
        }
    }
}

@Composable
private fun ThemeOption(
    label: String,
    mode: ThemeMode,
    selected: ThemeMode,
    onSelect: (ThemeMode) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = mode == selected, onClick = { onSelect(mode) })
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = mode == selected, onClick = { onSelect(mode) })
        Text(label, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(start = 8.dp))
    }
}
