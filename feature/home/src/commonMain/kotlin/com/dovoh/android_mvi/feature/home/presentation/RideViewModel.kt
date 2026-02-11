package com.dovoh.android_mvi.feature.home.presentation

import com.dovoh.android_mvi.core.common.BusinessException
import com.dovoh.android_mvi.core.mvi.MviViewModel
import com.dovoh.android_mvi.feature.home.presentation.model.Driver
import kotlinx.coroutines.delay

class RideViewModel : MviViewModel<RideIntent, RideState, RideEffect>(RideState()) {

    override suspend fun handleIntent(intent: RideIntent) = when (intent) {
        is RideIntent.NavigateTo -> setState { copy(screen = intent.screen) }

        is RideIntent.SetSearchQuery -> setState { copy(searchQuery = intent.query) }

        is RideIntent.SetDestination -> setState {
            copy(
                destination = intent.place,
                screen = RideScreen.RideOptions,
                searchQuery = "",
            )
        }

        is RideIntent.SelectRide -> setState { copy(selectedRide = intent.ride) }

        RideIntent.ApplyPromo -> setState {
            copy(
                promoApplied = true,
                rideOptions = rideOptions.map { r ->
                    r.copy(price = (r.price * 0.9 * 100).toLong() / 100.0)
                },
            )
        }

        RideIntent.RequestRide -> {
            setState { copy(screen = RideScreen.Searching) }
            simulateDriverSearch()
        }

        is RideIntent.DriverAssigned -> setState {
            val fareAmount = selectedRide?.let { ride ->
                if (promoApplied) (ride.price * 0.9 * 100).toLong() / 100.0
                else ride.price
            } ?: 0.0
            copy(
                screen = RideScreen.DriverFound,
                driver = intent.driver,
                eta = intent.eta,
                fare = fareAmount,
            )
        }

        RideIntent.RideStarted -> setState {
            copy(screen = RideScreen.InRide, rideProgress = 0f)
        }

        is RideIntent.UpdateProgress -> setState { copy(rideProgress = intent.progress) }

        RideIntent.RideCompleted -> setState {
            copy(screen = RideScreen.RideComplete, rideProgress = 100f)
        }

        is RideIntent.RateDriver -> setState { copy(rating = intent.stars) }

        RideIntent.CancelRide -> setState {
            copy(
                screen = RideScreen.Home,
                destination = null,
                selectedRide = null,
                driver = null,
                rideProgress = 0f,
            )
        }

        RideIntent.GoHome -> {
            val recent = state.value.recentPlaces
            setState {
                RideState(recentPlaces = recent)
            }
        }
    }

    private fun simulateDriverSearch() {
        launchGuarded {
            delay(2800)
            val driver = DefaultData.drivers.random()
            val eta = (2..6).random()
            sendIntent(RideIntent.DriverAssigned(driver, eta))
        }
    }

    override fun onBusinessError(e: BusinessException) {
        // No business API calls yet for ride feature
    }
}
