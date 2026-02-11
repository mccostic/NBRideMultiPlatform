package com.dovoh.android_mvi.feature.home.presentation

import com.dovoh.android_mvi.feature.home.presentation.model.Driver
import com.dovoh.android_mvi.feature.home.presentation.model.Place
import com.dovoh.android_mvi.feature.home.presentation.model.RideOption

data class RideState(
    val screen: RideScreen = RideScreen.Home,
    val origin: Place? = Place(
        id = 0,
        name = "Current Location",
        address = "San Francisco, CA",
        icon = "",
    ),
    val destination: Place? = null,
    val selectedRide: RideOption? = null,
    val driver: Driver? = null,
    val eta: Int? = null,
    val rideProgress: Float = 0f,
    val fare: Double? = null,
    val searchQuery: String = "",
    val recentPlaces: List<Place> = DefaultData.recentPlaces,
    val suggestedPlaces: List<Place> = DefaultData.suggestedPlaces,
    val rideOptions: List<RideOption> = DefaultData.rideOptions,
    val promoApplied: Boolean = false,
    val rating: Int? = null,
    val paymentMethod: String = "Visa \u2022\u2022\u2022\u2022 4242",
)

enum class RideScreen {
    Home,
    SelectDestination,
    RideOptions,
    Searching,
    DriverFound,
    InRide,
    RideComplete,
}

sealed interface RideIntent {
    data class NavigateTo(val screen: RideScreen) : RideIntent
    data class SetSearchQuery(val query: String) : RideIntent
    data class SetDestination(val place: Place) : RideIntent
    data class SelectRide(val ride: RideOption) : RideIntent
    data object ApplyPromo : RideIntent
    data object RequestRide : RideIntent
    data class DriverAssigned(val driver: Driver, val eta: Int) : RideIntent
    data object RideStarted : RideIntent
    data class UpdateProgress(val progress: Float) : RideIntent
    data object RideCompleted : RideIntent
    data class RateDriver(val stars: Int) : RideIntent
    data object CancelRide : RideIntent
    data object GoHome : RideIntent
}

sealed interface RideEffect {
    data object NavigateToProfile : RideEffect
}

object DefaultData {
    val recentPlaces = listOf(
        Place(1, "Home", "742 Evergreen Terrace", "\uD83C\uDFE0", 37.7849, -122.4094),
        Place(2, "Work", "1 Market St, SF", "\uD83D\uDCBC", 37.7941, -122.3949),
        Place(3, "Gym", "24 Hour Fitness, Mission", "\uD83D\uDCAA", 37.7649, -122.4194),
    )

    val suggestedPlaces = listOf(
        Place(4, "SFO Airport", "San Francisco International Airport", "\u2708\uFE0F", 37.6213, -122.379),
        Place(5, "Ferry Building", "1 Ferry Building, SF", "\uD83D\uDEA2", 37.7955, -122.3937),
        Place(6, "Golden Gate Park", "Golden Gate Park, SF", "\uD83C\uDF33", 37.7694, -122.4862),
        Place(7, "Fisherman's Wharf", "Jefferson St, SF", "\uD83E\uDD9E", 37.8080, -122.4177),
    )

    val rideOptions = listOf(
        RideOption("uberx", "UberX", "Affordable everyday rides", "\uD83D\uDE97", "3 min", 12.50, 4),
        RideOption("comfort", "Comfort", "Newer cars with extra legroom", "\uD83D\uDE99", "4 min", 18.00, 4),
        RideOption("xl", "UberXL", "Affordable rides for groups", "\uD83D\uDE90", "6 min", 22.75, 6),
        RideOption("black", "Black", "Premium rides in luxury cars", "\uD83D\uDDA4", "8 min", 38.00, 4),
    )

    val drivers = listOf(
        Driver("Marcus T.", 4.92, 1847, "Tesla Model 3", "7UBR942", "\uD83D\uDC68\uD83C\uDFFD"),
        Driver("Priya K.", 4.98, 3201, "Toyota Camry", "4XKL201", "\uD83D\uDC69\uD83C\uDFFD"),
        Driver("James L.", 4.87, 922, "Honda Accord", "9MST774", "\uD83D\uDC68\uD83C\uDFFB"),
    )
}
