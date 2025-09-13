package org.example.project.nbride

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.dovoh.android_mvi.core.navigation.AuthedGraph
import com.dovoh.android_mvi.core.navigation.PublicGraph
import com.dovoh.android_mvi.core.navigation.Route.Home
import com.dovoh.android_mvi.core.navigation.Route.Login
import com.dovoh.android_mvi.core.navigation.Route.Register
import com.dovoh.android_mvi.feature.home.presentation.HomeScreen
import com.dovoh.android_mvi.feature.login.presentation.LoginScreen
import com.dovoh.android_mvi.feature.login.presentation.LoginViewModel
import com.dovoh.android_mvi.feature.register.presentation.RegisterScreen
import com.dovoh.android_mvi.feature.register.presentation.RegisterViewModel
import org.koin.compose.viewmodel.koinViewModel

internal fun NavGraphBuilder.publicGraph(nav: NavController) {
    navigation(
        route = PublicGraph::class,
        startDestination = Login,
    ) {
        composable<Login> {
            val vm = koinViewModel<LoginViewModel>()
            val state by vm.state.collectAsStateWithLifecycle()
            LoginScreen(
                state = state,
                action = vm::sendIntent,
                effects = vm.effect,
                commonEffects = vm.commonEffect,
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
    }
}

internal fun NavGraphBuilder.authedGraph(nav: NavController) {
    navigation(
        route = AuthedGraph::class,
        startDestination = Home
    ) {
        composable<Home> {
            HomeScreen()
        }
    }
}




