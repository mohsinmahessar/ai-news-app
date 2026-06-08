package com.smartreader.ai.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartreader.ai.data.repository.ThemeManager
import com.smartreader.ai.data.repository.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/** Activity-level VM that drives the app theme before any screen composes. */
@HiltViewModel
class ThemeViewModel @Inject constructor(
    themeManager: ThemeManager,
) : ViewModel() {
    val themeMode = themeManager.themeMode
        .stateIn(viewModelScope, SharingStarted.Eagerly, ThemeMode.LIGHT)
}
