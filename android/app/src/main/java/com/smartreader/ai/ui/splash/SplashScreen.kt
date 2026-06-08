package com.smartreader.ai.ui.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.smartreader.ai.R
import com.smartreader.ai.data.repository.AuthManager
import com.smartreader.ai.ui.theme.brandGradientVertical
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authManager: AuthManager,
) : ViewModel() {
    suspend fun isLoggedIn(): Boolean = authManager.isLoggedInNow()
}

@Composable
fun SplashScreen(
    onTimeout: (loggedIn: Boolean) -> Unit,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    var visible by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (visible) 1f else 0.8f, tween(600), label = "scale")
    val alpha by animateFloatAsState(if (visible) 1f else 0f, tween(600), label = "alpha")

    LaunchedEffect(Unit) {
        visible = true
        delay(1300)
        onTimeout(viewModel.isLoggedIn())
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brandGradientVertical()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(96.dp)
                    .scale(scale)
                    .alpha(alpha),
            )
            Text(
                "SmartReader AI",
                style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(top = 20.dp).alpha(alpha),
            )
            Text(
                "Understand Every Word While You Read.",
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp, start = 32.dp, end = 32.dp).alpha(alpha),
            )
        }
    }
}
