package org.example.project.nbride

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.dovoh.android_mvi.core.auth.TokenRepository
import com.dovoh.android_mvi.core.navigation.AuthedGraph
import com.dovoh.android_mvi.core.navigation.PublicGraph
import org.example.project.nbride.ui.theme.AppTheme
import org.koin.compose.koinInject

@Composable
fun AppRoot() {
    val nav = rememberNavController()
    val repo = koinInject<TokenRepository>()
    AppTheme(darkTheme = isSystemInDarkTheme()) {
        NavHost(
            navController = nav,
            startDestination = if (repo.isAuthenticated()) AuthedGraph::class else PublicGraph::class
        ) {
            publicGraph(nav)
            authedGraph(nav)
        }
    }
}
