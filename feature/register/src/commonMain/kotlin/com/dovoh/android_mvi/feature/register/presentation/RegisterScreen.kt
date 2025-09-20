package com.dovoh.android_mvi.feature.register.presentation

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
fun RegisterScreen(
    state: RegisterState,
    action: (RegisterIntent) -> Unit = {},
    effects: Flow<RegisterEffect>,
    commonEffects: Flow<CommonEffect>,
    navController: NavController
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focus = LocalFocusManager.current

    val emailFocus = remember { FocusRequester() }
    val passwordFocus = remember { FocusRequester() }

    CollectCommonEffects(commonEffects) { msg ->
        action(RegisterIntent.ShowDialog(msg))
    }

    LaunchedEffect(effects) {
        effects
            .mapNotNull { it.toNavAction(navController) }
            .collect { navigate -> navigate() }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                PrimaryButton(
                    text = "Create account",
                    onClick = {
                        keyboard?.hide()
                        action(RegisterIntent.Submit)
                    },
                    enabled = !state.loading &&
                        state.name.isNotBlank() &&
                        state.email.isNotBlank() &&
                        state.password.isNotBlank(),
                    loading = state.loading,
                    shape = MaterialTheme.shapes.small
                )
                SecondaryTextButton(
                    text = "Back to sign in",
                    onClick = { action(RegisterIntent.Back) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(horizontal = 24.dp)
                .statusBarsPadding(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Create your account",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Join us to continue",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(24.dp))

            LabeledTextField(
                value = state.name,
                onValueChange = { action(RegisterIntent.NameChanged(it)) },
                label = "Full name",
                singleLine = true,
                keyboardType = KeyboardType.Text,
                onImeAction = {
                    emailFocus.requestFocus()
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            LabeledTextField(
                value = state.email,
                onValueChange = { action(RegisterIntent.EmailChanged(it)) },
                label = "Email",
                singleLine = true,
                keyboardType = KeyboardType.Email,
                onImeAction = {
                    passwordFocus.requestFocus()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(emailFocus)
            )


            Spacer(Modifier.height(12.dp))

            LabeledTextField(
                value = state.password,
                onValueChange = { action(RegisterIntent.PasswordChanged(it)) },
                label = "Password",
                singleLine = true,
                password = true,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                onImeAction = {
                    keyboard?.hide()
                    focus.clearFocus()
                    action(RegisterIntent.Submit)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(passwordFocus)
            )


            if (state.error != null && !state.showErrorDialog) {
                InlineErrorChip(
                    modifier = Modifier.padding(top = 8.dp),
                    message = state.error,
                    onClick = { action(RegisterIntent.ShowDialog(state.error)) },
                )
            }
        }
    }

    ErrorDialog(
        showDialog = state.showErrorDialog,
        title = "Sorry!",
        message = state.error ?: "Registration failed",
        onDismiss = { action(RegisterIntent.HideDialog) }
    )
}

private fun RegisterEffect.toNavAction(
    navController: NavController
): (() -> Unit)? = when (this) {
    RegisterEffect.NavigateBack -> {
        { navController.popBackStack() }
    }
    RegisterEffect.Registered -> {
        { navController.navigate(Login) {
            popUpTo(Register) { inclusive = true }
            launchSingleTop = true
        } }
    }
}
