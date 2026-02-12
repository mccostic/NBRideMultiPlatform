package com.dovoh.android_mvi.feature.home.presentation.components.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Interactive map view that handles pan gestures natively without interfering
 * with the Compose pointer input system. The map will report its camera position
 * changes via callback.
 */
@Composable
expect fun InteractiveMapView(
    modifier: Modifier,
    cameraLat: Double,
    cameraLng: Double,
    cameraZoom: Float,
    darkMode: Boolean,
    onCameraPositionChanged: (lat: Double, lng: Double) -> Unit,
)