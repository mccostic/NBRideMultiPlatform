package com.dovoh.android_mvi.feature.home.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.dovoh.android_mvi.feature.home.presentation.RideScreen
import com.dovoh.android_mvi.feature.home.presentation.RideState
import com.dovoh.android_mvi.feature.home.presentation.components.map.NativeMapView
import com.dovoh.android_mvi.feature.home.presentation.model.MapMarkerData
import com.dovoh.android_mvi.feature.home.presentation.model.MapMarkerType
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.random.Random
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val SF_LAT = 37.7749
private const val SF_LNG = -122.4194
private const val DEFAULT_ZOOM = 13f
private const val ROUTE_SEGMENTS = 30

/**
 * Compute a quadratic bezier route in geo-coordinates between origin and destination.
 */
private fun computeBezierRoute(
    originLat: Double, originLng: Double,
    destLat: Double, destLng: Double,
): List<Pair<Double, Double>> {
    // Control point offset to create a curved route
    val ctrlLat = (originLat + destLat) / 2.0 + (destLng - originLng) * 0.15
    val ctrlLng = (originLng + destLng) / 2.0 - (destLat - originLat) * 0.15

    return (0..ROUTE_SEGMENTS).map { i ->
        val t = i.toDouble() / ROUTE_SEGMENTS
        val oneMinusT = 1.0 - t
        val lat = oneMinusT * oneMinusT * originLat +
            2.0 * oneMinusT * t * ctrlLat +
            t * t * destLat
        val lng = oneMinusT * oneMinusT * originLng +
            2.0 * oneMinusT * t * ctrlLng +
            t * t * destLng
        Pair(lat, lng)
    }
}

/**
 * Interpolate a position along the bezier route at a given progress [0..1].
 */
private fun interpolateRoute(
    originLat: Double, originLng: Double,
    destLat: Double, destLng: Double,
    progress: Float,
): Pair<Double, Double> {
    val ctrlLat = (originLat + destLat) / 2.0 + (destLng - originLng) * 0.15
    val ctrlLng = (originLng + destLng) / 2.0 - (destLat - originLat) * 0.15

    val t = progress.toDouble().coerceIn(0.0, 1.0)
    val oneMinusT = 1.0 - t
    val lat = oneMinusT * oneMinusT * originLat +
        2.0 * oneMinusT * t * ctrlLat +
        t * t * destLat
    val lng = oneMinusT * oneMinusT * originLng +
        2.0 * oneMinusT * t * ctrlLng +
        t * t * destLng
    return Pair(lat, lng)
}

/**
 * Calculate heading angle (degrees) from tangent of bezier at given progress.
 */
private fun routeHeading(
    originLat: Double, originLng: Double,
    destLat: Double, destLng: Double,
    progress: Float,
): Float {
    val ctrlLat = (originLat + destLat) / 2.0 + (destLng - originLng) * 0.15
    val ctrlLng = (originLng + destLng) / 2.0 - (destLat - originLat) * 0.15

    val t = progress.toDouble().coerceIn(0.0, 1.0)
    val oneMinusT = 1.0 - t
    val tangentLat = 2.0 * oneMinusT * (ctrlLat - originLat) + 2.0 * t * (destLat - ctrlLat)
    val tangentLng = 2.0 * oneMinusT * (ctrlLng - originLng) + 2.0 * t * (destLng - ctrlLng)
    return (atan2(tangentLng, tangentLat) * (180.0 / PI)).toFloat()
}

private class GeoCarState(
    val lat: Animatable<Float, AnimationVector1D>,
    val lng: Animatable<Float, AnimationVector1D>,
    val rotation: Animatable<Float, AnimationVector1D>,
)

/**
 * Creates and animates nearby car marker positions in geo-coordinates.
 */
@Composable
private fun rememberAnimatedCarMarkers(centerLat: Double, centerLng: Double): List<MapMarkerData> {
    val carCount = 6
    val cars = remember {
        List(carCount) { index ->
            val random = Random(index * 42 + 7)
            GeoCarState(
                lat = Animatable((centerLat + (random.nextFloat() - 0.5f) * 0.018).toFloat()),
                lng = Animatable((centerLng + (random.nextFloat() - 0.5f) * 0.022).toFloat()),
                rotation = Animatable(random.nextFloat() * 360f),
            )
        }
    }

    // Animate each car independently
    cars.forEachIndexed { index, car ->
        LaunchedEffect(index) {
            val carRandom = Random(index * 17 + 3)
            while (true) {
                delay(carRandom.nextLong(2200, 4200))

                val currentLat = car.lat.value
                val currentLng = car.lng.value
                val newLat = (currentLat + (carRandom.nextFloat() - 0.5f) * 0.004f)
                    .coerceIn((centerLat - 0.012).toFloat(), (centerLat + 0.012).toFloat())
                val newLng = (currentLng + (carRandom.nextFloat() - 0.5f) * 0.005f)
                    .coerceIn((centerLng - 0.015).toFloat(), (centerLng + 0.015).toFloat())

                val dx = (newLng - currentLng).toDouble()
                val dy = (newLat - currentLat).toDouble()
                val angle = atan2(dx, dy) * (180.0 / PI)

                launch {
                    car.rotation.animateTo(
                        angle.toFloat(),
                        tween(350, easing = FastOutSlowInEasing),
                    )
                }
                launch {
                    car.lat.animateTo(
                        newLat,
                        tween(3000, easing = LinearOutSlowInEasing),
                    )
                }
                car.lng.animateTo(
                    newLng,
                    tween(3000, easing = LinearOutSlowInEasing),
                )
            }
        }
    }

    return cars.mapIndexed { index, car ->
        MapMarkerData(
            id = "car_$index",
            lat = car.lat.value.toDouble(),
            lng = car.lng.value.toDouble(),
            type = MapMarkerType.NearbyCar,
            rotation = car.rotation.value,
        )
    }
}

@Composable
fun MapView(
    state: RideState,
    modifier: Modifier = Modifier,
) {
    // Determine camera position based on state
    val originLat = state.origin?.lat ?: SF_LAT
    val originLng = state.origin?.lng ?: SF_LNG
    val destLat = state.destination?.lat ?: SF_LAT
    val destLng = state.destination?.lng ?: SF_LNG

    val hasRoute = state.destination != null && state.screen in setOf(
        RideScreen.RideOptions,
        RideScreen.Searching,
        RideScreen.DriverFound,
        RideScreen.InRide,
    )

    // Camera: center between origin and destination when route is shown
    val cameraLat = if (hasRoute) (originLat + destLat) / 2.0 else SF_LAT
    val cameraLng = if (hasRoute) (originLng + destLng) / 2.0 else SF_LNG
    val cameraZoom = if (hasRoute) 12.5f else DEFAULT_ZOOM

    // Route polyline points
    val routePoints = if (hasRoute) {
        computeBezierRoute(originLat, originLng, destLat, destLng)
    } else {
        emptyList()
    }

    // Animated driver position along route
    val driverProgress = remember { Animatable(0f) }
    LaunchedEffect(state.rideProgress) {
        if (state.screen == RideScreen.InRide) {
            driverProgress.animateTo(
                targetValue = state.rideProgress / 100f,
                animationSpec = tween(800, easing = FastOutSlowInEasing),
            )
        }
    }

    // Build markers list
    val markers = mutableListOf<MapMarkerData>()

    when (state.screen) {
        RideScreen.Home -> {
            // "You Are Here" marker + animated nearby cars
            markers.add(
                MapMarkerData(
                    id = "you_are_here",
                    lat = SF_LAT,
                    lng = SF_LNG,
                    type = MapMarkerType.YouAreHere,
                ),
            )
            val carMarkers = rememberAnimatedCarMarkers(SF_LAT, SF_LNG)
            markers.addAll(carMarkers)
        }

        RideScreen.RideOptions,
        RideScreen.Searching,
        RideScreen.DriverFound,
        -> {
            // Origin + destination markers
            markers.add(
                MapMarkerData(
                    id = "origin",
                    lat = originLat,
                    lng = originLng,
                    type = MapMarkerType.Origin,
                ),
            )
            markers.add(
                MapMarkerData(
                    id = "dest",
                    lat = destLat,
                    lng = destLng,
                    type = MapMarkerType.Destination,
                ),
            )
        }

        RideScreen.InRide -> {
            // Origin + destination + driver on route
            markers.add(
                MapMarkerData(
                    id = "origin",
                    lat = originLat,
                    lng = originLng,
                    type = MapMarkerType.Origin,
                ),
            )
            markers.add(
                MapMarkerData(
                    id = "dest",
                    lat = destLat,
                    lng = destLng,
                    type = MapMarkerType.Destination,
                ),
            )

            val (driverLat, driverLng) = interpolateRoute(
                originLat, originLng, destLat, destLng, driverProgress.value,
            )
            val heading = routeHeading(
                originLat, originLng, destLat, destLng, driverProgress.value,
            )
            markers.add(
                MapMarkerData(
                    id = "driver",
                    lat = driverLat,
                    lng = driverLng,
                    type = MapMarkerType.Driver,
                    rotation = heading,
                ),
            )
        }

        else -> {
            // SelectDestination, MapPicker, RideComplete - no markers on main map
        }
    }

    NativeMapView(
        modifier = modifier.fillMaxSize(),
        cameraLat = cameraLat,
        cameraLng = cameraLng,
        cameraZoom = cameraZoom,
        darkMode = true,
        routePoints = routePoints,
        markers = markers,
    )
}
