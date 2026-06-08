package com.smartreader.ai.ui.home

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smartreader.ai.data.local.entity.BookEntity
import com.smartreader.ai.ui.components.BookCover
import com.smartreader.ai.ui.components.EmptyState
import com.smartreader.ai.ui.components.GradientHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onOpenBook: (String) -> Unit,
    onOpenVocabulary: () -> Unit,
    onOpenAnalytics: () -> Unit,
    onOpenSettings: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val books by viewModel.books.collectAsStateWithLifecycle()
    val continueReading by viewModel.continueReading.collectAsStateWithLifecycle()
    val greeting by viewModel.greeting.collectAsStateWithLifecycle()
    val isImporting by viewModel.isImporting.collectAsStateWithLifecycle()
    val query by viewModel.searchQuery.collectAsStateWithLifecycle()

    val picker = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri -> uri?.let(viewModel::importPdf) }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { picker.launch(arrayOf("application/pdf")) },
                icon = {
                    if (isImporting) CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp, color = Color.White)
                    else Icon(Icons.Default.Add, contentDescription = null)
                },
                text = { Text(if (isImporting) "Importing…" else "Add book") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = padding.calculateBottomPadding() + 90.dp),
        ) {
            item {
                GradientHeader(
                    title = "Hi, $greeting 👋",
                    subtitle = "Continue your reading journey",
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        HeaderChip("Vocabulary", Icons.Default.Style, onOpenVocabulary)
                        HeaderChip("Stats", Icons.Default.BarChart, onOpenAnalytics)
                        HeaderChip("Settings", Icons.Default.Settings, onOpenSettings)
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = query,
                    onValueChange = viewModel::onSearchChange,
                    placeholder = { Text("Search your books") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    ),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                )
            }

            if (continueReading.isNotEmpty() && query.isBlank()) {
                item { SectionTitle("Continue reading") }
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp),
                    ) {
                        items(continueReading, key = { it.id }) { book ->
                            ContinueCard(book) { onOpenBook(book.id) }
                        }
                    }
                }
            }

            item { SectionTitle("My books") }

            if (books.isEmpty()) {
                item {
                    EmptyState(
                        icon = Icons.Default.MenuBook,
                        title = "No books yet",
                        message = "Tap “Add book” to import your first PDF and start reading smarter.",
                    )
                }
            } else {
                items(books, key = { it.id }) { book ->
                    BookRow(
                        book = book,
                        onClick = { onOpenBook(book.id) },
                        onDelete = { viewModel.deleteBook(book) },
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun HeaderChip(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(alpha = 0.18f))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = label, tint = Color.White, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(6.dp))
        Text(label, color = Color.White, style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
    )
}

@Composable
private fun ContinueCard(book: BookEntity, onClick: () -> Unit) {
    Column(modifier = Modifier.width(124.dp).clickable(onClick = onClick)) {
        BookCover(book.title, Modifier.width(124.dp).height(168.dp))
        Text(
            book.title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 8.dp),
        )
        LinearProgressIndicator(
            progress = { book.progress },
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp).clip(RoundedCornerShape(50)),
        )
    }
}

@Composable
private fun BookRow(book: BookEntity, onClick: () -> Unit, onDelete: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BookCover(book.title, Modifier.size(width = 52.dp, height = 70.dp))
            Column(modifier = Modifier.weight(1f).padding(horizontal = 14.dp)) {
                Text(book.title, style = MaterialTheme.typography.titleMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text(
                    "${(book.progress * 100).toInt()}% • ${book.totalPages} pages",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp),
                )
                LinearProgressIndicator(
                    progress = { book.progress },
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp).clip(RoundedCornerShape(50)),
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
