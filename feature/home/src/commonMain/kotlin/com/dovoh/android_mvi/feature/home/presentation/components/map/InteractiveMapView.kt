package com.dovoh.android_mvi.feature.home.presentation.components.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A map view that allows native user interaction (pan, zoom) and reports
 * camera position changes back to Compose via [onCameraMoved].
 *
 * Unlike [NativeMapView], this composable does NOT fight with native gesture
 * recognizers — the native map owns all touch handling.
 *
 * [centerLat]/[centerLng] are used for:
 *   - Initial position on first composition
 *   - Programmatic re-centering (e.g. "re-center" button)
 */
@Composable
expect fun InteractiveMapView(
    modifier: Modifier,
    centerLat: Double,
    centerLng: Double,
    zoom: Float,
    darkMode: Boolean,
    onCameraMoved: (lat: Double, lng: Double) -> Unit,
)
