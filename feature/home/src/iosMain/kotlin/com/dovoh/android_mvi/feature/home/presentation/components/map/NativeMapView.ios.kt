package com.dovoh.android_mvi.feature.home.presentation.components.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import com.dovoh.android_mvi.feature.home.presentation.model.MapMarkerData
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.MapKit.MKCoordinateRegionMakeWithDistance
import platform.MapKit.MKMapView
import platform.UIKit.UIUserInterfaceStyle
import kotlin.math.abs
import kotlin.math.pow

@OptIn(ExperimentalForeignApi::class)
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
    val lastCamera = remember { mutableListOf(cameraLat, cameraLng) }

    UIKitView(
        modifier = modifier,
        factory = {
            MKMapView().apply {
                overrideUserInterfaceStyle = if (darkMode) {
                    UIUserInterfaceStyle.UIUserInterfaceStyleDark
                } else {
                    UIUserInterfaceStyle.UIUserInterfaceStyleLight
                }

                isScrollEnabled = false
                isZoomEnabled = false
                isRotateEnabled = false
                isPitchEnabled = false

                // Set initial region
                val center = CLLocationCoordinate2DMake(cameraLat, cameraLng)
                val metersPerZoom = 40_000_000.0 / 2.0.pow(cameraZoom.toDouble())
                val region = MKCoordinateRegionMakeWithDistance(center, metersPerZoom, metersPerZoom)
                setRegion(region, animated = false)
            }
        },
        update = { mapView: MKMapView ->
            // Camera update (only when programmatic position changes)
            if (abs(cameraLat - lastCamera[0]) > 0.0001 ||
                abs(cameraLng - lastCamera[1]) > 0.0001
            ) {
                lastCamera[0] = cameraLat
                lastCamera[1] = cameraLng
                val center = CLLocationCoordinate2DMake(cameraLat, cameraLng)
                val metersPerZoom = 40_000_000.0 / 2.0.pow(cameraZoom.toDouble())
                val region = MKCoordinateRegionMakeWithDistance(center, metersPerZoom, metersPerZoom)
                mapView.setRegion(region, animated = true)
            }
        },
    )
}
