package com.dovoh.android_mvi.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.dovoh.android_mvi.core.navigation.Route.Login

@Composable
fun AuthGuard(
    isAuthed: Boolean,
    nav: NavController,
    loginRoute: Route = Login,
    content: @Composable () -> Unit
) {
    LaunchedEffect(isAuthed) {
        if (!isAuthed) {
            nav.navigate(loginRoute) {
                popUpTo(0)
                launchSingleTop = true
            }
        }
    }
    if (isAuthed) content()
}
