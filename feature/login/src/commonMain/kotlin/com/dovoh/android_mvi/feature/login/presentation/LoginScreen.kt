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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.dovoh.android_mvi.core.common.ErrorDialog
import com.dovoh.android_mvi.core.logging.Log
import com.dovoh.android_mvi.core.mvi.CommonEffect
import com.dovoh.android_mvi.core.navigation.Route.Home
import com.dovoh.android_mvi.core.navigation.Route.Login
import com.dovoh.android_mvi.core.navigation.Route.Register
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

@Composable
fun LoginScreen(
    state: LoginState,
    action:(LoginIntent)-> Unit= {},
    effects: Flow<LoginEffect>,
    commonEffects: Flow<CommonEffect>,
    navController: NavHostController
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val passwordFocus = remember { FocusRequester() }

    val enable by remember(state.email,state.password, state.loading) {
        derivedStateOf {
            !state.loading && state.email.isNotBlank() && state.password.isNotBlank()
        }
    }

    val showErrorDialog by remember(state.showErrorDialog) {
        derivedStateOf {
            state.error != null && !state.showErrorDialog
        }
    }

    // Collect effects
    LaunchedEffect(commonEffects) {
        commonEffects.mapNotNull {
            it.toUserMessage()
        }.collect {
            msg -> action(LoginIntent.ShowDialog(msg))
        }
    }

    LaunchedEffect(effects) {
        effects
            .mapNotNull { it.toNavAction(navController) }
            .collect { navigate -> navigate() }
    }

    val bg = MaterialTheme.colorScheme.surface // page background

    Scaffold(
        containerColor = bg,
        bottomBar = {
            // Sticky bottom actions
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Button(
                    onClick = {
                        keyboardController?.hide()
                        action(LoginIntent.Submit)
                    },
                    shape = MaterialTheme.shapes.small,
                    enabled =enable,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    if (state.loading) {
                        CircularProgressIndicator(
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(20.dp)
                        )
                        return@Button
                    }
                    Text("Sign in")
                }
                TextButton(
                    onClick = { action(LoginIntent.GoToRegister) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text("Create an account")
                }
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

            OutlinedTextField(
                value = state.email,
                onValueChange = { action(LoginIntent.EmailChanged(it)) },
                label = { Text("Username") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { passwordFocus.requestFocus() }
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = state.password,
                onValueChange = { action(LoginIntent.PasswordChanged(it)) },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        action(LoginIntent.Submit)

                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(passwordFocus)
            )

            if (showErrorDialog) {
                Spacer(Modifier.height(8.dp))
                AssistChip(
                    onClick = { action(LoginIntent.ShowDialog(state.error.orEmpty())) },
                    label = { Text(state.error.orEmpty()) },
                    colors = AssistChipDefaults.assistChipColors(
                        labelColor = MaterialTheme.colorScheme.error
                    )
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
private fun CommonEffect.toUserMessage(): String? = when (this) {
    is CommonEffect.ServerIssue  -> "Invalid credentials"
    is CommonEffect.NetworkIssue -> "Network Error!"
    is CommonEffect.UnknownIssue -> "Something went wrong! \nPlease try again!"
    else -> null
}

private fun LoginEffect.toNavAction(
    navController: NavHostController
): (() -> Unit)? = when (this) {
    LoginEffect.NavigateHome -> {
        { navController.navigate(Home) {
            popUpTo(Login) { inclusive = true }
            launchSingleTop = true
        } }
    }
    LoginEffect.NavigateRegister -> {
        { navController.navigate(Register) { launchSingleTop = true } }
    }
}



