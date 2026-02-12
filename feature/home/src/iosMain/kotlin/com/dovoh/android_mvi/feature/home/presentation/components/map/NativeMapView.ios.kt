package com.dovoh.android_mvi.feature.home.presentation.components.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import com.dovoh.android_mvi.feature.home.presentation.model.MapMarkerData
import com.dovoh.android_mvi.feature.home.presentation.model.MapMarkerType
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import platform.CoreLocation.CLLocationCoordinate2D
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.MapKit.MKAnnotationProtocol
import platform.MapKit.MKAnnotationView
import platform.MapKit.MKCoordinateRegionMakeWithDistance
import platform.MapKit.MKMapView
import platform.MapKit.MKMapViewDelegateProtocol
import platform.MapKit.MKMarkerAnnotationView
import platform.MapKit.MKOverlayProtocol
import platform.MapKit.MKOverlayRenderer
import platform.MapKit.MKPointAnnotation
import platform.MapKit.MKPolyline
import platform.MapKit.MKPolylineRenderer
import platform.UIKit.UIColor
import platform.UIKit.UIUserInterfaceStyle
import platform.darwin.NSObject
import kotlin.math.abs
import kotlin.math.pow

/**
 * Delegate that renders polyline overlays and annotation marker views.
 */
private class NativeMapOverlayDelegate : NSObject(), MKMapViewDelegateProtocol {

    override fun mapView(mapView: MKMapView, rendererForOverlay: MKOverlayProtocol): MKOverlayRenderer {
        if (rendererForOverlay is MKPolyline) {
            return MKPolylineRenderer(polyline = rendererForOverlay).apply {
                strokeColor = UIColor.cyanColor
                lineWidth = 4.0
            }
        }
        return MKOverlayRenderer(overlay = rendererForOverlay)
    }

    override fun mapView(mapView: MKMapView, viewForAnnotation: MKAnnotationProtocol): MKAnnotationView? {
        val title = viewForAnnotation.title ?: return null
        val tintColor: UIColor
        val glyph: String
        when {
            title.startsWith("origin") -> {
                tintColor = UIColor.cyanColor; glyph = "A"
            }
            title.startsWith("dest") -> {
                tintColor = UIColor.purpleColor; glyph = "B"
            }
            title.startsWith("you") -> {
                tintColor = UIColor.cyanColor; glyph = ""
            }
            title.startsWith("car") -> {
                tintColor = UIColor.darkGrayColor; glyph = ""
            }
            title.startsWith("driver") -> {
                tintColor = UIColor.greenColor; glyph = ""
            }
            else -> return null
        }
        return MKMarkerAnnotationView(annotation = viewForAnnotation, reuseIdentifier = title).apply {
            markerTintColor = tintColor
            glyphText = glyph
            canShowCallout = false
        }
    }
}

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
    val delegate = remember { NativeMapOverlayDelegate() }
    // Track managed annotations by ID for efficient updates
    val managedAnnotations = remember { mutableMapOf<String, MKPointAnnotation>() }
    val currentPolyline = remember { mutableStateOf<MKPolyline?>(null) }
    val lastRoutePoints = remember { mutableStateOf<List<Pair<Double, Double>>>(emptyList()) }
    val lastCamera = remember { mutableStateOf(Pair(cameraLat, cameraLng)) }

    UIKitView(
        modifier = modifier,
        factory = {
            MKMapView().apply {
                overrideUserInterfaceStyle = if (darkMode) {
                    UIUserInterfaceStyle.UIUserInterfaceStyleDark
                } else {
                    UIUserInterfaceStyle.UIUserInterfaceStyleLight
                }
                this.delegate = delegate

                // Set initial region
                val center = CLLocationCoordinate2DMake(cameraLat, cameraLng)
                val metersPerZoom = 40_000_000.0 / 2.0.pow(cameraZoom.toDouble())
                val region = MKCoordinateRegionMakeWithDistance(center, metersPerZoom, metersPerZoom)
                setRegion(region, animated = false)
            }
        },
        update = { mapView ->
            // --- Camera update (only when programmatic position changes) ---
            if (abs(cameraLat - lastCamera.value.first) > 0.0001 ||
                abs(cameraLng - lastCamera.value.second) > 0.0001
            ) {
                lastCamera.value = Pair(cameraLat, cameraLng)
                val center = CLLocationCoordinate2DMake(cameraLat, cameraLng)
                val metersPerZoom = 40_000_000.0 / 2.0.pow(cameraZoom.toDouble())
                val region = MKCoordinateRegionMakeWithDistance(center, metersPerZoom, metersPerZoom)
                mapView.setRegion(region, animated = true)
            }

            // --- Route polyline (only recreate when route changes) ---
            if (routePoints != lastRoutePoints.value) {
                lastRoutePoints.value = routePoints
                currentPolyline.value?.let { mapView.removeOverlay(it) }
                if (routePoints.isNotEmpty()) {
                    val polyline = createPolyline(routePoints)
                    mapView.addOverlay(polyline)
                    currentPolyline.value = polyline
                } else {
                    currentPolyline.value = null
                }
            }

            // --- Markers: add/update/remove efficiently ---
            val currentIds = markers.map { it.id }.toSet()
            val existingIds = managedAnnotations.keys.toSet()

            // Remove stale annotations
            (existingIds - currentIds).forEach { id ->
                managedAnnotations.remove(id)?.let { mapView.removeAnnotation(it) }
            }

            // Add or update annotations
            markers.forEach { marker ->
                val existing = managedAnnotations[marker.id]
                if (existing != null) {
                    // Just update position (efficient, no flicker)
                    existing.setCoordinate(CLLocationCoordinate2DMake(marker.lat, marker.lng))
                } else {
                    // Create new annotation
                    val annotation = MKPointAnnotation().apply {
                        setCoordinate(CLLocationCoordinate2DMake(marker.lat, marker.lng))
                        setTitle(marker.id)
                    }
                    managedAnnotations[marker.id] = annotation
                    mapView.addAnnotation(annotation)
                }
            }
        },
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun createPolyline(points: List<Pair<Double, Double>>): MKPolyline {
    return memScoped {
        val coords = allocArray<CLLocationCoordinate2D>(points.size)
        points.forEachIndexed { i, (lat, lng) ->
            coords[i].latitude = lat
            coords[i].longitude = lng
        }
        MKPolyline.polylineWithCoordinates(coords, points.size.toULong())
    }
}
