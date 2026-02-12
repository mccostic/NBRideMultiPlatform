package com.dovoh.android_mvi.feature.home.presentation.components.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dovoh.android_mvi.feature.home.presentation.model.MapMarkerData

@Composable
expect fun NativeMapView(
    modifier: Modifier,
    cameraLat: Double,
    cameraLng: Double,
    cameraZoom: Float,
    darkMode: Boolean,
    routePoints: List<Pair<Double, Double>> = emptyList(),
    markers: List<MapMarkerData> = emptyList(),
)
