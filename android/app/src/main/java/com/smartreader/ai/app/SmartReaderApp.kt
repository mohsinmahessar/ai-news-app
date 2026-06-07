package com.smartreader.ai.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class. [@HiltAndroidApp] triggers Hilt code generation and creates
 * the app-level dependency container that every Activity/ViewModel pulls from.
 */
@HiltAndroidApp
class SmartReaderApp : Application()
