package com.smartreader.ai.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.smartreader.ai.ui.analytics.AnalyticsScreen
import com.smartreader.ai.ui.home.HomeScreen
import com.smartreader.ai.ui.login.LoginScreen
import com.smartreader.ai.ui.reader.ReaderScreen
import com.smartreader.ai.ui.settings.SettingsScreen
import com.smartreader.ai.ui.splash.SplashScreen
import com.smartreader.ai.ui.vocabulary.VocabularyScreen

/** Type-safe-ish route definitions for the whole app. */
object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val HOME = "home"
    const val READER = "reader/{bookId}"
    const val VOCABULARY = "vocabulary"
    const val ANALYTICS = "analytics"
    const val SETTINGS = "settings"

    fun reader(bookId: String) = "reader/$bookId"
}

@Composable
fun SmartReaderNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.SPLASH) {

        composable(Routes.SPLASH) {
            SplashScreen(
                onTimeout = { loggedIn ->
                    val dest = if (loggedIn) Routes.HOME else Routes.LOGIN
                    navController.navigate(dest) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                onSignedIn = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                onOpenBook = { bookId -> navController.navigate(Routes.reader(bookId)) },
                onOpenVocabulary = { navController.navigate(Routes.VOCABULARY) },
                onOpenAnalytics = { navController.navigate(Routes.ANALYTICS) },
                onOpenSettings = { navController.navigate(Routes.SETTINGS) },
            )
        }

        composable(
            route = Routes.READER,
            arguments = listOf(navArgument("bookId") { type = NavType.StringType }),
        ) {
            ReaderScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.VOCABULARY) { VocabularyScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.ANALYTICS) { AnalyticsScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.SETTINGS) { SettingsScreen(onBack = { navController.popBackStack() }) }
    }
}
