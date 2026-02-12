package com.dovoh.android_mvi.feature.home.presentation.components.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.dovoh.android_mvi.feature.home.presentation.model.MapMarkerData
import com.dovoh.android_mvi.feature.home.presentation.model.MapMarkerType
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
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
    routePoints: List<Pair<Double, Double>>,
    markers: List<MapMarkerData>,
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(cameraLat, cameraLng), cameraZoom)
    }

    LaunchedEffect(cameraLat, cameraLng) {
        cameraPositionState.animate(
            CameraUpdateFactory.newLatLng(LatLng(cameraLat, cameraLng)),
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
            scrollGesturesEnabled = true,
            zoomGesturesEnabled = true,
            rotationGesturesEnabled = false,
            tiltGesturesEnabled = false,
        )
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        uiSettings = uiSettings,
    ) {
        // Route polyline
        if (routePoints.isNotEmpty()) {
            Polyline(
                points = routePoints.map { LatLng(it.first, it.second) },
                color = Color(0xFF00D4FF),
                width = 10f,
            )
            // Route glow
            Polyline(
                points = routePoints.map { LatLng(it.first, it.second) },
                color = Color(0x3300D4FF),
                width = 24f,
            )
        }

        // Markers
        markers.forEach { marker ->
            val position = LatLng(marker.lat, marker.lng)
            when (marker.type) {
                MapMarkerType.Origin -> {
                    Circle(
                        center = position,
                        radius = 40.0,
                        fillColor = Color(0x4000D4FF),
                        strokeColor = Color(0xFF00D4FF),
                        strokeWidth = 3f,
                    )
                    Circle(
                        center = position,
                        radius = 15.0,
                        fillColor = Color(0xFF00D4FF),
                        strokeColor = Color.White,
                        strokeWidth = 2f,
                    )
                }

                MapMarkerType.Destination -> {
                    Circle(
                        center = position,
                        radius = 35.0,
                        fillColor = Color(0x337C3AED),
                        strokeColor = Color(0xFF7C3AED),
                        strokeWidth = 3f,
                    )
                    Circle(
                        center = position,
                        radius = 12.0,
                        fillColor = Color(0xFF7C3AED),
                        strokeColor = Color.White,
                        strokeWidth = 2f,
                    )
                }

                MapMarkerType.YouAreHere -> {
                    Circle(
                        center = position,
                        radius = 50.0,
                        fillColor = Color(0x2000D4FF),
                        strokeColor = Color(0x5500D4FF),
                        strokeWidth = 2f,
                    )
                    Circle(
                        center = position,
                        radius = 15.0,
                        fillColor = Color(0xFF00D4FF),
                        strokeColor = Color.White,
                        strokeWidth = 3f,
                    )
                }

                MapMarkerType.NearbyCar -> {
                    Marker(
                        state = MarkerState(position = position),
                        rotation = marker.rotation,
                        icon = BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_CYAN,
                        ),
                        flat = true,
                        anchor = Offset05,
                    )
                }

                MapMarkerType.Driver -> {
                    Circle(
                        center = position,
                        radius = 30.0,
                        fillColor = Color(0x3300D4FF),
                        strokeColor = Color(0xFF00D4FF),
                        strokeWidth = 2f,
                    )
                    Marker(
                        state = MarkerState(position = position),
                        rotation = marker.rotation,
                        icon = BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_GREEN,
                        ),
                        flat = true,
                        anchor = Offset05,
                    )
                }
            }
        }
    }
}

private val Offset05 = androidx.compose.ui.geometry.Offset(0.5f, 0.5f)
