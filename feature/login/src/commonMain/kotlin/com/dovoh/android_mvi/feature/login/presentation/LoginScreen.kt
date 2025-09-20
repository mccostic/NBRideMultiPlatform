package com.dovoh.android_mvi.feature.login.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dovoh.android_mvi.core.mvi.CommonEffect
import com.dovoh.android_mvi.core.navigation.Route.Home
import com.dovoh.android_mvi.core.navigation.Route.Login
import com.dovoh.android_mvi.core.navigation.Route.Register
import com.dovoh.android_mvi.designsystem.components.CollectCommonEffects
import com.dovoh.android_mvi.designsystem.components.ErrorDialog
import com.dovoh.android_mvi.designsystem.components.InlineErrorChip
import com.dovoh.android_mvi.designsystem.components.LabeledTextField
import com.dovoh.android_mvi.designsystem.components.PrimaryButton
import com.dovoh.android_mvi.designsystem.components.SecondaryTextButton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

@Composable
fun LoginScreen(
    state: LoginState,
    action: (LoginIntent) -> Unit = {},
    effects: Flow<LoginEffect>,
    commonEffects: Flow<CommonEffect>,
    navController: NavController
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val passwordFocus = remember { FocusRequester() }

    val enable by remember(state.email, state.password, state.loading) {
        derivedStateOf {
            !state.loading && state.email.isNotBlank() && state.password.isNotBlank()
        }
    }

    val showErrorDialog by remember(state.showErrorDialog) {
        derivedStateOf {
            state.error != null && !state.showErrorDialog
        }
    }

    CollectCommonEffects(commonEffects) { msg ->
        action(LoginIntent.ShowDialog(msg))
    }

    LaunchedEffect(effects) {
        effects
            .mapNotNull { it.toNavAction(navController) }
            .collect { navigate -> navigate() }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            // Sticky bottom actions
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                PrimaryButton(
                    text = "Sign in",
                    onClick = {
                        keyboardController?.hide()
                        action(LoginIntent.Submit)
                    },
                    enabled = enable,
                    loading = state.loading,
                    shape = MaterialTheme.shapes.small
                )
                SecondaryTextButton(
                    text = "Create an account",
                    onClick = { action(LoginIntent.GoToRegister) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }
        }
    ) { inner ->
        // Centered content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(horizontal = 24.dp)
                .statusBarsPadding(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Welcome back",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Sign in to continue",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(24.dp))

            LabeledTextField(
                value = state.email,
                onValueChange = { action(LoginIntent.EmailChanged(it)) },
                label = "Username",
                singleLine = true,
                keyboardType = KeyboardType.Email,
                onImeAction = {
                    passwordFocus.requestFocus()
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            LabeledTextField(
                value = state.password,
                onValueChange = { action(LoginIntent.PasswordChanged(it)) },
                label = "Password",
                singleLine = true,
                password = true,
                imeAction = ImeAction.Done,
                onImeAction = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    action(LoginIntent.Submit)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(passwordFocus)
            )

            if (showErrorDialog) {
                InlineErrorChip(
                    modifier = Modifier.padding(top = 8.dp),
                    message = state.error.orEmpty(),
                    onClick = { action(LoginIntent.ShowDialog(state.error.orEmpty())) },
                )
            }
        }
    }

    ErrorDialog(
        showDialog = state.showErrorDialog,
        title = "Sorry!",
        message = state.error ?: "Invalid credentials",
        onDismiss = { action(LoginIntent.HideDialog) }
    )
}

private fun LoginEffect.toNavAction(
    navController: NavController
): (() -> Unit)? = when (this) {
    LoginEffect.NavigateHome -> {
        {
            navController.navigate(Home) {
                popUpTo(Login) { inclusive = true }
                launchSingleTop = true
            }
        }
    }
    LoginEffect.NavigateRegister -> {
        { navController.navigate(Register) { launchSingleTop = true } }
    }
}
