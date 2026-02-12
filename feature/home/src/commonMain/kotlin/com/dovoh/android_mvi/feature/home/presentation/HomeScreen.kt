package com.dovoh.android_mvi.feature.home.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import com.dovoh.android_mvi.feature.home.presentation.components.DriverFoundContent
import com.dovoh.android_mvi.feature.home.presentation.components.HomeContent
import com.dovoh.android_mvi.feature.home.presentation.components.InRideContent
import com.dovoh.android_mvi.feature.home.presentation.components.RideCompleteContent
import com.dovoh.android_mvi.feature.home.presentation.components.RideOptionsContent
import com.dovoh.android_mvi.feature.home.presentation.components.SearchingContent
import com.dovoh.android_mvi.feature.home.presentation.components.MapPickerContent
import com.dovoh.android_mvi.feature.home.presentation.components.SelectDestinationContent

@Composable
fun HomeScreen(
    state: RideState,
    onIntent: (RideIntent) -> Unit,
) {
    AnimatedContent(
        targetState = state.screen,
        transitionSpec = {
            (slideInVertically { it / 20 } + fadeIn()) togetherWith
                (slideOutVertically { -it / 20 } + fadeOut())
        },
        label = "screen_transition",
    ) { screen ->
        when (screen) {
            RideScreen.Home -> HomeContent(state = state, onIntent = onIntent)
            RideScreen.SelectDestination -> SelectDestinationContent(state = state, onIntent = onIntent)
            RideScreen.MapPicker -> MapPickerContent(state = state, onIntent = onIntent)
            RideScreen.RideOptions -> RideOptionsContent(state = state, onIntent = onIntent)
            RideScreen.Searching -> SearchingContent(state = state, onIntent = onIntent)
            RideScreen.DriverFound -> DriverFoundContent(state = state, onIntent = onIntent)
            RideScreen.InRide -> InRideContent(state = state, onIntent = onIntent)
            RideScreen.RideComplete -> RideCompleteContent(state = state, onIntent = onIntent)
        }
    }
}
