package com.smartreader.ai.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smartreader.ai.data.repository.ThemeMode
import com.smartreader.ai.ui.navigation.SmartReaderNavHost
import com.smartreader.ai.ui.theme.SmartReaderTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Single-activity host. The theme is resolved here (above the NavHost) from the
 * user's persisted preference so there's no light→dark flash on launch.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val mode by themeViewModel.themeMode.collectAsStateWithLifecycle()
            val dark = when (mode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }
            SmartReaderTheme(darkTheme = dark) {
                SmartReaderNavHost()
            }
        }
    }
}
