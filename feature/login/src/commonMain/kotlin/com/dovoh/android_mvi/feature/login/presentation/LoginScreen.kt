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
import com.dovoh.android_mvi.core.common.ErrorDialog
import com.dovoh.android_mvi.core.logging.Log
import com.dovoh.android_mvi.core.mvi.CommonEffect
import com.dovoh.android_mvi.core.navigation.Route.Home
import com.dovoh.android_mvi.core.navigation.Route.Login
import com.dovoh.android_mvi.core.navigation.Route.Register
import kotlinx.coroutines.flow.Flow

@Composable
fun LoginScreen(
    state: LoginState,
    action:(LoginIntent)-> Unit= {},
    effects: Flow<LoginEffect>,
    commonEffects: Flow<CommonEffect>,
    navController: NavController
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val passwordFocus = remember { FocusRequester() }

    // Collect effects
    LaunchedEffect(Unit) {
        commonEffects.collect { effect ->
            when (effect) {
                is CommonEffect.ServerIssue -> action(LoginIntent.ShowDialog("Invalid credentials"))
                else -> Unit
            }
        }
    }
    LaunchedEffect(Unit) {
        effects.collect {
            when (it) {
                LoginEffect.NavigateHome -> {
                    Log.d("Nav", "Logged in!")
                    navController.navigate(Home) {
                        popUpTo(Login) { inclusive = true }
                        launchSingleTop = true
                    }
                }
                LoginEffect.NavigateRegister -> {
                    navController.navigate(Register) { launchSingleTop = true }
                }
            }
        }
    }

    if (state.showErrorDialog) {
        ErrorDialog(
            title = "Sorry!",
            message = state.error ?: "Invalid credentials",
            onDismiss = { action(LoginIntent.HideDialog) }
        )
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
                    enabled = !state.loading && state.email.isNotBlank() && state.password.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    if (state.loading) {
                        CircularProgressIndicator(
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text("Sign in")
                    }
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

            if (state.error != null && !state.showErrorDialog) {
                Spacer(Modifier.height(8.dp))
                AssistChip(
                    onClick = { action(LoginIntent.ShowDialog(state.error)) },
                    label = { Text(state.error) },
                    colors = AssistChipDefaults.assistChipColors(
                        labelColor = MaterialTheme.colorScheme.error
                    )
                )
            }
        }
    }
}



