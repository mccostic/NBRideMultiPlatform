package com.dovoh.android_mvi.feature.home.presentation.components.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.MapKit.MKCoordinateRegionMakeWithDistance
import platform.MapKit.MKMapView
import platform.MapKit.MKMapViewDelegateProtocol
import platform.UIKit.UIUserInterfaceStyle
import platform.darwin.NSObject
import kotlin.math.abs
import kotlin.math.pow

private class MapPickerDelegate(
    private val onRegionChanged: (lat: Double, lng: Double) -> Unit,
) : NSObject(), MKMapViewDelegateProtocol {

    @OptIn(ExperimentalForeignApi::class)
    override fun mapViewDidChangeVisibleRegion(mapView: MKMapView) {
        val center = mapView.centerCoordinate
        val lat = center.useContents { this.latitude }
        val lng = center.useContents { this.longitude }
        onRegionChanged(lat, lng)
    }
}

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun InteractiveMapView(
    modifier: Modifier,
    centerLat: Double,
    centerLng: Double,
    zoom: Float,
    darkMode: Boolean,
    onCameraMoved: (lat: Double, lng: Double) -> Unit,
) {
    val currentOnCameraMoved by rememberUpdatedState(onCameraMoved)

    val delegate = remember {
        MapPickerDelegate { lat, lng ->
            currentOnCameraMoved(lat, lng)
        }
    }

    val lastProgrammaticCenter = remember { mutableStateOf(Pair(centerLat, centerLng)) }

    UIKitView(
        modifier = modifier,
        factory = {
            MKMapView().apply {
                overrideUserInterfaceStyle = if (darkMode) {
                    UIUserInterfaceStyle.UIUserInterfaceStyleDark
                } else {
                    UIUserInterfaceStyle.UIUserInterfaceStyleLight
                }

                val center = CLLocationCoordinate2DMake(centerLat, centerLng)
                val metersPerZoom = 40_000_000.0 / 2.0.pow(zoom.toDouble())
                val region = MKCoordinateRegionMakeWithDistance(center, metersPerZoom, metersPerZoom)
                setRegion(region, animated = false)

                this.delegate = delegate
            }
        },
        update = { mapView: MKMapView ->
            val (lastLat, lastLng) = lastProgrammaticCenter.value
            if (abs(centerLat - lastLat) > 0.0001 || abs(centerLng - lastLng) > 0.0001) {
                lastProgrammaticCenter.value = Pair(centerLat, centerLng)
                val center = CLLocationCoordinate2DMake(centerLat, centerLng)
                val metersPerZoom = 40_000_000.0 / 2.0.pow(zoom.toDouble())
                val region = MKCoordinateRegionMakeWithDistance(center, metersPerZoom, metersPerZoom)
                mapView.setRegion(region, animated = true)
            }
        },
    )
}
