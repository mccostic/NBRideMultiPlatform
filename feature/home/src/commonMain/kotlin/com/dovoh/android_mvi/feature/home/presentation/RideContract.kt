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
    val savedPlaces: List<Place> = DefaultData.savedPlaces,
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
    val savedPlaces = listOf(
        Place(1, "Home", "742 Evergreen Terrace", "\uD83C\uDFE0", 37.7849, -122.4094),
        Place(2, "Work", "1 Market St, SF", "\uD83D\uDCBC", 37.7941, -122.3949),
    )

    val recentPlaces = listOf(
        Place(1, "Home", "742 Evergreen Terrace", "\uD83C\uDFE0", 37.7849, -122.4094),
        Place(2, "Work", "1 Market St, SF", "\uD83D\uDCBC", 37.7941, -122.3949),
        Place(3, "Gym", "24 Hour Fitness, Mission", "\uD83D\uDCAA", 37.7649, -122.4194),
    )

    val suggestedPlaces = listOf(
        Place(4, "SFO Airport", "San Francisco International Airport", "\u2708\uFE0F", 37.6213, -122.3790),
        Place(5, "Ferry Building", "1 Ferry Building, SF", "\uD83D\uDEA2", 37.7955, -122.3937),
        Place(6, "Golden Gate Park", "Golden Gate Park, SF", "\uD83C\uDF33", 37.7694, -122.4862),
        Place(7, "Fisherman's Wharf", "Jefferson St, SF", "\uD83E\uDD9E", 37.8080, -122.4177),
        Place(8, "Union Square", "333 Post St, SF", "\uD83D\uDECD\uFE0F", 37.7879, -122.4074),
        Place(9, "Chinatown", "Grant Ave & Bush St, SF", "\uD83C\uDFEE", 37.7941, -122.4078),
        Place(10, "Mission Dolores Park", "Dolores St & 19th St, SF", "\u2600\uFE0F", 37.7596, -122.4269),
        Place(11, "Oracle Park", "24 Willie Mays Plaza, SF", "\u26BE", 37.7786, -122.3893),
        Place(12, "Coit Tower", "1 Telegraph Hill Blvd, SF", "\uD83D\uDDFC", 37.8024, -122.4058),
        Place(13, "The Castro Theatre", "429 Castro St, SF", "\uD83C\uDFAC", 37.7609, -122.4350),
        Place(14, "Pier 39", "Beach St & The Embarcadero, SF", "\uD83E\uDDAD", 37.8087, -122.4098),
        Place(15, "Twin Peaks", "501 Twin Peaks Blvd, SF", "\u26F0\uFE0F", 37.7544, -122.4477),
        Place(16, "SFMOMA", "151 Third St, SF", "\uD83C\uDFA8", 37.7857, -122.4011),
        Place(17, "Ghirardelli Square", "900 North Point St, SF", "\uD83C\uDF6B", 37.8060, -122.4230),
        Place(18, "Haight-Ashbury", "Haight St & Ashbury St, SF", "\uD83C\uDF3A", 37.7694, -122.4468),
        Place(19, "Lombard Street", "Lombard St, SF", "\uD83D\uDEE3\uFE0F", 37.8021, -122.4187),
        Place(20, "Palace of Fine Arts", "3601 Lyon St, SF", "\uD83C\uDFDB\uFE0F", 37.8029, -122.4483),
        Place(21, "Alcatraz Island", "Alcatraz Island, SF Bay", "\uD83C\uDFDD\uFE0F", 37.8267, -122.4230),
        Place(22, "Japantown", "1610 Geary Blvd, SF", "\u26E9\uFE0F", 37.7853, -122.4294),
        Place(23, "Nob Hill", "California St & Mason St, SF", "\uD83C\uDFE8", 37.7930, -122.4110),
        Place(24, "Presidio", "The Presidio of San Francisco", "\uD83C\uDF32", 37.7989, -122.4662),
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
