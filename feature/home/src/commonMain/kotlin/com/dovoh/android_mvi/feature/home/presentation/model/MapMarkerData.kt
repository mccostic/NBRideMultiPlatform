package com.dovoh.android_mvi.feature.home.presentation.model

data class MapMarkerData(
    val id: String,
    val lat: Double,
    val lng: Double,
    val type: MapMarkerType,
    val rotation: Float = 0f,
)

enum class MapMarkerType {
    Origin,
    Destination,
    YouAreHere,
    NearbyCar,
    Driver,
}
