package com.dovoh.android_mvi.feature.home.presentation.components.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.MapKit.MKCoordinateRegionMakeWithDistance
import platform.MapKit.MKMapView
import platform.MapKit.MKMapViewDelegateProtocol
import platform.MapKit.MKOverlayProtocol
import platform.MapKit.MKOverlayRenderer
import platform.MapKit.MKPointAnnotation
import platform.MapKit.MKPolyline
import platform.MapKit.MKPolylineRenderer
import platform.UIKit.UIColor
import platform.UIKit.UIUserInterfaceStyle
import platform.darwin.NSObject
import kotlin.math.pow

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun NativeMapView(
    modifier: Modifier,
    cameraLat: Double,
    cameraLng: Double,
    cameraZoom: Float,
    markers: List<MapMarker>,
    routePoints: List<MapLatLng>?,
    darkMode: Boolean,
) {
    val delegate = remember { MapViewDelegate() }

    UIKitView(
        modifier = modifier,
        factory = {
            MKMapView().apply {
                this.delegate = delegate
                this.overrideUserInterfaceStyle = if (darkMode) {
                    UIUserInterfaceStyle.UIUserInterfaceStyleDark
                } else {
                    UIUserInterfaceStyle.UIUserInterfaceStyleLight
                }
            }
        },
        update = { mapView ->
            // Center on camera position
            val center = CLLocationCoordinate2DMake(cameraLat, cameraLng)
            val metersPerZoom = 40_000_000.0 / 2.0.pow(cameraZoom.toDouble())
            val region = MKCoordinateRegionMakeWithDistance(center, metersPerZoom, metersPerZoom)
            mapView.setRegion(region, animated = true)

            // Clear existing annotations and overlays
            val existingAnnotations = mapView.annotations.filterNotNull()
            if (existingAnnotations.isNotEmpty()) {
                mapView.removeAnnotations(existingAnnotations)
            }
            val existingOverlays = mapView.overlays.filterNotNull()
            if (existingOverlays.isNotEmpty()) {
                mapView.removeOverlays(existingOverlays)
            }

            // Add markers as annotations
            markers.forEach { marker ->
                val annotation = MKPointAnnotation().apply {
                    setCoordinate(CLLocationCoordinate2DMake(marker.lat, marker.lng))
                    setTitle(marker.title)
                }
                mapView.addAnnotation(annotation)
            }

            // Add route polyline
            routePoints?.let { points ->
                if (points.size >= 2) {
                    memScoped {
                        val coordinates = points.map {
                            CLLocationCoordinate2DMake(it.lat, it.lng)
                        }
                        val coordsPtr = allocArrayOf(*coordinates.toTypedArray())
                        val polyline = MKPolyline.polylineWithCoordinates(
                            coords = coordsPtr,
                            count = coordinates.size.toULong(),
                        )
                        @Suppress("UNCHECKED_CAST")
                        mapView.addOverlay(polyline as MKOverlayProtocol)
                    }
                }
            }
        },
    )
}

@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
private class MapViewDelegate : NSObject(), MKMapViewDelegateProtocol {
    @OptIn(ExperimentalForeignApi::class)
    override fun mapView(
        mapView: MKMapView,
        rendererForOverlay: MKOverlayProtocol,
    ): MKOverlayRenderer {
        val overlay = rendererForOverlay
        if (overlay is MKPolyline) {
            return MKPolylineRenderer(overlay).apply {
                strokeColor = UIColor(
                    red = 0.0,
                    green = 212.0 / 255.0,
                    blue = 1.0,
                    alpha = 1.0,
                )
                lineWidth = 4.0
            }
        }
        return MKOverlayRenderer(overlay)
    }
}
