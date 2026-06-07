package com.smartreader.ai.ui.home

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartreader.ai.data.local.entity.BookEntity
import com.smartreader.ai.data.repository.AuthManager
import com.smartreader.ai.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val books: List<BookEntity> = emptyList(),
    val continueReading: List<BookEntity> = emptyList(),
    val greeting: String = "Reader",
    val isImporting: Boolean = false,
    val error: String? = null,
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    authManager: AuthManager,
) : ViewModel() {

    val searchQuery = MutableStateFlow("")

    private val booksFlow = searchQuery.flatMapLatest { query ->
        if (query.isBlank()) bookRepository.observeBooks()
        else bookRepository.searchBooks(query)
    }

    val books: StateFlow<List<BookEntity>> =
        booksFlow.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val continueReading: StateFlow<List<BookEntity>> =
        bookRepository.observeContinueReading()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val greeting: StateFlow<String> =
        authManager.displayName.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "Reader")

    private val _isImporting = MutableStateFlow(false)
    val isImporting: StateFlow<Boolean> = _isImporting

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun onSearchChange(query: String) { searchQuery.value = query }

    fun importPdf(uri: Uri) {
        viewModelScope.launch {
            _isImporting.value = true
            bookRepository.importPdf(uri)
                .onFailure { _error.value = "Could not import PDF: ${it.message}" }
            _isImporting.value = false
        }
    }

    fun deleteBook(book: BookEntity) {
        viewModelScope.launch { bookRepository.deleteBook(book) }
    }

    fun clearError() { _error.value = null }
}
