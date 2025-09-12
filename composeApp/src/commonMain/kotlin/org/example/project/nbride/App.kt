package org.example.project.nbride

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dovoh.android_mvi.core.navigation.Route.Home
import com.dovoh.android_mvi.core.navigation.Route.Login
import com.dovoh.android_mvi.core.navigation.Route.Register
import com.dovoh.android_mvi.feature.home.presentation.HomeScreen
import com.dovoh.android_mvi.feature.login.presentation.LoginScreen
import com.dovoh.android_mvi.feature.login.presentation.LoginViewModel
import com.dovoh.android_mvi.feature.register.presentation.RegisterScreen
import com.dovoh.android_mvi.feature.register.presentation.RegisterViewModel
import org.example.project.nbride.ui.theme.AppTheme
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun AppRoot() {
    val nav = rememberNavController()
    AppTheme(darkTheme = isSystemInDarkTheme()) {
        NavHost(navController = nav, startDestination = Login) {
            composable<Login> {
                val  viewModel = koinViewModel<LoginViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()
                LoginScreen(
                    state=state,
                    action = viewModel::sendIntent,
                    effects = viewModel.effect,
                    commonEffects = viewModel.commonEffect,
                    navController = nav
                )
            }

            composable<Register> {
                val vm = koinViewModel<RegisterViewModel>()
                val state by vm.state.collectAsStateWithLifecycle()
                RegisterScreen(
                    state = state,
                    action = vm::sendIntent,
                    effects = vm.effect,
                    navController = nav
                )
            }

            composable<Home> {
                HomeScreen()
            }
        }
    }
}
