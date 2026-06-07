package com.smartreader.ai.ui.reader

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.NavigateBefore
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smartreader.ai.domain.model.AiResult
import com.smartreader.ai.domain.model.ChatMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(
    onBack: () -> Unit,
    viewModel: ReaderViewModel = hiltViewModel(),
) {
    val ui by viewModel.ui.collectAsStateWithLifecycle()
    val panel by viewModel.panel.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(ui.title, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.openChat() }) {
                        Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "Reading assistant")
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.openChat() }) {
                Icon(Icons.Default.AutoAwesome, contentDescription = "AI tools")
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = viewModel::prevPage, enabled = ui.currentPage > 0) {
                    Icon(Icons.AutoMirrored.Filled.NavigateBefore, contentDescription = "Previous")
                }
                Text("${ui.currentPage + 1} / ${ui.totalPages}")
                IconButton(onClick = viewModel::nextPage, enabled = ui.currentPage < ui.totalPages - 1) {
                    Icon(Icons.AutoMirrored.Filled.NavigateNext, contentDescription = "Next")
                }
            }
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center,
        ) {
            when {
                ui.error != null -> Text(ui.error!!, color = MaterialTheme.colorScheme.error)
                ui.pageBitmap != null -> {
                    // Pinch-to-zoom over the rendered page bitmap.
                    var scale by remember { mutableStateOf(1f) }
                    Image(
                        bitmap = ui.pageBitmap!!.asImageBitmap(),
                        contentDescription = "Page ${ui.currentPage + 1}",
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer(scaleX = scale, scaleY = scale)
                            .pointerInput(Unit) {
                                detectTransformGestures { _, _, zoom, _ ->
                                    scale = (scale * zoom).coerceIn(1f, 4f)
                                }
                            },
                    )
                }
                else -> CircularProgressIndicator()
            }
            if (ui.isRendering) CircularProgressIndicator(Modifier.align(Alignment.TopEnd).padding(16.dp))
        }
    }

    if (panel !is AiPanel.Hidden) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { viewModel.closePanel() },
            sheetState = sheetState,
        ) {
            AiPanelContent(panel = panel, viewModel = viewModel)
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun AiPanelContent(panel: AiPanel, viewModel: ReaderViewModel) {
    val scroll = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 520.dp)
            .verticalScroll(scroll)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        when (panel) {
            is AiPanel.Word -> WordPanel(panel.result)
            is AiPanel.Sentence -> SentencePanel(panel.result)
            is AiPanel.Paragraph -> ParagraphPanel(panel.result)
            is AiPanel.Chat -> ChatPanel(panel) { viewModel.sendChat(it) }
            AiPanel.Hidden -> Unit
        }

        if (panel is AiPanel.Chat) return@Column

        // Quick demo input so the AI features are usable even without a tappable
        // text layer. Real word/sentence taps call the same ViewModel functions.
        var input by remember { mutableStateOf("") }
        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Type a word, sentence or paragraph") },
            modifier = Modifier.fillMaxWidth(),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AssistChip(onClick = { viewModel.explainWord(input.trim()) }, label = { Text("Word") })
            AssistChip(onClick = { viewModel.simplifySentence(input.trim()) }, label = { Text("Sentence") })
            AssistChip(onClick = { viewModel.explainParagraph(input.trim()) }, label = { Text("Paragraph") })
        }
    }
}

@Composable private fun Loading() = Row(verticalAlignment = Alignment.CenterVertically) {
    CircularProgressIndicator(Modifier.height(20.dp)); Spacer(Modifier.height(8.dp)); Text("  Thinking…")
}

@Composable private fun Label(t: String) =
    Text(t, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)

@Composable
private fun WordPanel(result: AiResult<com.smartreader.ai.domain.model.WordExplanation>) {
    when (result) {
        AiResult.Loading -> Loading()
        is AiResult.Error -> Text(result.message, color = MaterialTheme.colorScheme.error)
        is AiResult.Success -> with(result.data) {
            Text(word, style = MaterialTheme.typography.headlineMedium)
            Label("Simple meaning"); Text(simpleMeaning)
            Label("In this context"); Text(contextMeaning)
            Label("Example"); Text(example)
            if (synonyms.isNotEmpty()) { Label("Synonyms"); Text(synonyms.joinToString(", ")) }
            if (translation.isNotBlank()) { Label("Translation"); Text(translation) }
        }
    }
}

@Composable
private fun SentencePanel(result: AiResult<com.smartreader.ai.domain.model.SentenceExplanation>) {
    when (result) {
        AiResult.Loading -> Loading()
        is AiResult.Error -> Text(result.message, color = MaterialTheme.colorScheme.error)
        is AiResult.Success -> with(result.data) {
            Label("Simple English"); Text(simpleEnglish)
            Label("Explained simply"); Text(childFriendly)
            Label("Key takeaway"); Text(keyTakeaway)
        }
    }
}

@Composable
private fun ParagraphPanel(result: AiResult<com.smartreader.ai.domain.model.ParagraphExplanation>) {
    when (result) {
        AiResult.Loading -> Loading()
        is AiResult.Error -> Text(result.message, color = MaterialTheme.colorScheme.error)
        is AiResult.Success -> with(result.data) {
            Label("Summary"); Text(summary)
            Label("Main idea"); Text(mainIdea)
            Label("Easy explanation"); Text(easyExplanation)
            if (importantPoints.isNotEmpty()) {
                Label("Important points")
                importantPoints.forEach { Text("• $it") }
            }
        }
    }
}

@Composable
private fun ChatPanel(panel: AiPanel.Chat, onSend: (String) -> Unit) {
    Label("Reading Assistant")
    panel.messages.forEach { msg ->
        val isUser = msg.role == ChatMessage.Role.USER
        Text(
            text = (if (isUser) "You: " else "AI: ") + msg.content,
            color = if (isUser) MaterialTheme.colorScheme.onSurface
            else MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth(),
        )
    }
    if (panel.sending) Loading()

    var input by remember { mutableStateOf("") }
    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            placeholder = { Text("Ask about this book…") },
            modifier = Modifier.weight(1f),
        )
        IconButton(
            onClick = { if (input.isNotBlank()) { onSend(input.trim()); input = "" } },
            enabled = !panel.sending,
        ) { Icon(Icons.Default.Send, contentDescription = "Send") }
    }
}
