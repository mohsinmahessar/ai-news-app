package com.smartreader.ai.ui.vocabulary

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smartreader.ai.data.local.dao.VocabularyDao
import com.smartreader.ai.data.local.entity.VocabularyEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VocabularyViewModel @Inject constructor(
    private val dao: VocabularyDao,
) : ViewModel() {
    val words = dao.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun markMastered(word: VocabularyEntity) {
        viewModelScope.launch {
            dao.upsert(word.copy(mastered = true, timesReviewed = word.timesReviewed + 1))
        }
    }

    fun delete(word: VocabularyEntity) {
        viewModelScope.launch { dao.delete(word) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabularyScreen(
    onBack: () -> Unit,
    viewModel: VocabularyViewModel = hiltViewModel(),
) {
    val words by viewModel.words.collectAsStateWithLifecycle()
    var flashcardMode by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vocabulary") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { flashcardMode = !flashcardMode }) {
                        Icon(Icons.Default.Style, contentDescription = "Flashcards")
                    }
                },
            )
        },
    ) { padding ->
        if (words.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text("No saved words yet", style = MaterialTheme.typography.titleLarge)
                Text(
                    "Tap difficult words while reading — they're saved here automatically.",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    modifier = Modifier.padding(16.dp),
                )
            }
        } else if (flashcardMode) {
            FlashcardDeck(words = words, modifier = Modifier.padding(padding)) { viewModel.markMastered(it) }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(words, key = { it.id }) { word ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp)) {
                            Text(word.word, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text(word.meaning, modifier = Modifier.padding(top = 4.dp))
                            if (word.example.isNotBlank()) {
                                Text(
                                    "“${word.example}”",
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                    modifier = Modifier.padding(top = 6.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FlashcardDeck(
    words: List<VocabularyEntity>,
    modifier: Modifier = Modifier,
    onMastered: (VocabularyEntity) -> Unit,
) {
    var index by remember { mutableStateOf(0) }
    var revealed by remember { mutableStateOf(false) }
    val word = words[index.coerceIn(0, words.lastIndex)]

    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().clickable { revealed = !revealed },
        ) {
            Column(Modifier.padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(word.word, style = MaterialTheme.typography.headlineMedium)
                if (revealed) {
                    Text(word.meaning, modifier = Modifier.padding(top = 16.dp))
                    if (word.example.isNotBlank()) Text("“${word.example}”", modifier = Modifier.padding(top = 8.dp))
                } else {
                    Text("Tap to reveal", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), modifier = Modifier.padding(top = 16.dp))
                }
            }
        }
        Text("${index + 1} / ${words.size}", modifier = Modifier.padding(top = 16.dp))
        Row(Modifier.padding(top = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FilledTonalButton(onClick = {
                onMastered(word)
                revealed = false
                index = (index + 1).coerceAtMost(words.lastIndex)
            }) { Text("Got it ✓") }
            FilledTonalButton(onClick = {
                revealed = false
                index = (index + 1) % words.size
            }) { Text("Next →") }
        }
    }
}
