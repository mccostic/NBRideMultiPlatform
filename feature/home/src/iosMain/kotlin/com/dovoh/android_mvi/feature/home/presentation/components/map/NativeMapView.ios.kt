package com.dovoh.android_mvi.feature.home.presentation.components.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.MapKit.MKCoordinateRegionMakeWithDistance
import platform.MapKit.MKMapView
import platform.UIKit.UIUserInterfaceStyle
import kotlin.math.pow

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun NativeMapView(
    modifier: Modifier,
    cameraLat: Double,
    cameraLng: Double,
    cameraZoom: Float,
    darkMode: Boolean,
) {
    UIKitView(
        modifier = modifier,
        factory = {
            MKMapView().apply {
                overrideUserInterfaceStyle = if (darkMode) {
                    UIUserInterfaceStyle.UIUserInterfaceStyleDark
                } else {
                    UIUserInterfaceStyle.UIUserInterfaceStyleLight
                }
            }
        },
        update = { mapView ->
            val center = CLLocationCoordinate2DMake(cameraLat, cameraLng)
            val metersPerZoom = 40_000_000.0 / 2.0.pow(cameraZoom.toDouble())
            val region = MKCoordinateRegionMakeWithDistance(center, metersPerZoom, metersPerZoom)
            mapView.setRegion(region, animated = true)
        },
    )
}
