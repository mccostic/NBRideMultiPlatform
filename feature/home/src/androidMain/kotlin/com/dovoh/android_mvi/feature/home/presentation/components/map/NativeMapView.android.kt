package com.dovoh.android_mvi.feature.home.presentation.components.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

private const val DARK_MAP_STYLE = """[
  { "elementType": "geometry", "stylers": [{ "color": "#0f1923" }] },
  { "elementType": "labels.text.fill", "stylers": [{ "color": "#4a9eff" }] },
  { "elementType": "labels.text.stroke", "stylers": [{ "color": "#0a121c" }] },
  { "featureType": "administrative", "elementType": "geometry", "stylers": [{ "color": "#1a4a6e" }] },
  { "featureType": "poi", "stylers": [{ "visibility": "off" }] },
  { "featureType": "road", "elementType": "geometry", "stylers": [{ "color": "#1a4a6e" }] },
  { "featureType": "road", "elementType": "geometry.stroke", "stylers": [{ "color": "#2a6496" }] },
  { "featureType": "road.highway", "elementType": "geometry", "stylers": [{ "color": "#2a6496" }] },
  { "featureType": "transit", "stylers": [{ "visibility": "off" }] },
  { "featureType": "water", "elementType": "geometry", "stylers": [{ "color": "#0d1c29" }] },
  { "featureType": "water", "elementType": "labels.text.fill", "stylers": [{ "color": "#4a9eff" }] }
]"""

@Composable
actual fun NativeMapView(
    modifier: Modifier,
    cameraLat: Double,
    cameraLng: Double,
    cameraZoom: Float,
    darkMode: Boolean,
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(cameraLat, cameraLng), cameraZoom)
    }

    LaunchedEffect(cameraLat, cameraLng) {
        cameraPositionState.animate(
            CameraUpdateFactory.newLatLng(LatLng(cameraLat, cameraLng))
        )
    }

    val mapProperties = remember(darkMode) {
        MapProperties(
            mapStyleOptions = if (darkMode) MapStyleOptions(DARK_MAP_STYLE) else null,
        )
    }

    val uiSettings = remember {
        MapUiSettings(
            zoomControlsEnabled = false,
            myLocationButtonEnabled = false,
            compassEnabled = false,
            mapToolbarEnabled = false,
            scrollGesturesEnabled = false,
            zoomGesturesEnabled = false,
            rotationGesturesEnabled = false,
            tiltGesturesEnabled = false,
        )
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        uiSettings = uiSettings,
    )
}
