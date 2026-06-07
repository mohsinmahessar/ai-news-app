package com.smartreader.ai.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.smartreader.ai.ui.navigation.SmartReaderNavHost
import com.smartreader.ai.ui.theme.SmartReaderTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Single-activity host. All screens are Composables navigated via
 * [SmartReaderNavHost]. [@AndroidEntryPoint] lets Hilt inject ViewModels.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            SmartReaderTheme {
                SmartReaderNavHost()
            }
        }
    }
}
