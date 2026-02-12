package com.dovoh.android_mvi.feature.home.presentation.components.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun NativeMapView(
    modifier: Modifier,
    cameraLat: Double,
    cameraLng: Double,
    cameraZoom: Float,
    darkMode: Boolean,
)
