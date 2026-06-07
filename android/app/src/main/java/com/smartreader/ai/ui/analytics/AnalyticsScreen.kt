package com.smartreader.ai.ui.analytics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smartreader.ai.data.local.dao.ReadingSessionDao
import com.smartreader.ai.data.local.dao.VocabularyDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.util.concurrent.TimeUnit
import javax.inject.Inject

data class AnalyticsState(
    val pagesRead: Int = 0,
    val hoursSpent: String = "0h 0m",
    val vocabularyLearned: Int = 0,
    val streakDays: Int = 0,
)

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    sessionDao: ReadingSessionDao,
    vocabularyDao: VocabularyDao,
) : ViewModel() {

    val state = combine(
        sessionDao.totalPagesRead(),
        sessionDao.totalReadingMillis(),
        vocabularyDao.count(),
        sessionDao.activeDays(),
    ) { pages, millis, vocab, days ->
        AnalyticsState(
            pagesRead = pages,
            hoursSpent = formatDuration(millis),
            vocabularyLearned = vocab,
            streakDays = computeStreak(days),
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AnalyticsState())

    private fun formatDuration(millis: Long): String {
        val h = TimeUnit.MILLISECONDS.toHours(millis)
        val m = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
        return "${h}h ${m}m"
    }

    /** Count consecutive days (including today) that have reading activity. */
    private fun computeStreak(daysDesc: List<Long>): Int {
        if (daysDesc.isEmpty()) return 0
        val oneDay = TimeUnit.DAYS.toMillis(1)
        var streak = 1
        for (i in 1 until daysDesc.size) {
            if (daysDesc[i - 1] - daysDesc[i] == oneDay) streak++ else break
        }
        return streak
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    onBack: () -> Unit,
    viewModel: AnalyticsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val cards = listOf(
        "Pages read" to state.pagesRead.toString(),
        "Time spent" to state.hoursSpent,
        "Words learned" to state.vocabularyLearned.toString(),
        "Day streak" to "🔥 ${state.streakDays}",
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reading analytics") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(cards) { (label, value) ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(20.dp)) {
                        Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        Text(label, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }
        }
    }
}
