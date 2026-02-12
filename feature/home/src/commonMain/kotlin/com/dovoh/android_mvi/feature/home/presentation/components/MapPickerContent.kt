package com.dovoh.android_mvi.feature.home.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dovoh.android_mvi.feature.home.presentation.DefaultData
import com.dovoh.android_mvi.feature.home.presentation.RideIntent
import com.dovoh.android_mvi.feature.home.presentation.RideScreen
import com.dovoh.android_mvi.feature.home.presentation.RideState
import com.dovoh.android_mvi.feature.home.presentation.components.map.NativeMapView
import com.dovoh.android_mvi.feature.home.presentation.theme.RideColors
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.math.sqrt

private const val SF_CENTER_LAT = 37.7749
private const val SF_CENTER_LNG = -122.4194

// Degrees per pixel at zoom 14 (approximate for SF latitude)
private const val DEG_PER_PX_LAT = 0.00003
private const val DEG_PER_PX_LNG = 0.000037

@Composable
fun MapPickerContent(
    state: RideState,
    onIntent: (RideIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var cameraLat by remember { mutableStateOf(SF_CENTER_LAT) }
    var cameraLng by remember { mutableStateOf(SF_CENTER_LNG) }
    var isDragging by remember { mutableStateOf(false) }

    val pinScale = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    // Entry animation
    LaunchedEffect(Unit) {
        pinScale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow))
    }

    // Bounce pin on drag end
    LaunchedEffect(isDragging) {
        if (!isDragging) {
            pinScale.animateTo(0.85f, tween(80))
            pinScale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
        }
    }

    val nearbyPlace by remember(cameraLat, cameraLng) {
        derivedStateOf { findNearbyPlace(cameraLat, cameraLng) }
    }

    val addressText by remember(cameraLat, cameraLng, nearbyPlace) {
        derivedStateOf {
            nearbyPlace?.let { "Near ${it.name}" }
                ?: generateAddress(cameraLat, cameraLng)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        // --- Map background (pannable) ---
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { isDragging = true },
                        onDragEnd = { isDragging = false },
                        onDragCancel = { isDragging = false },
                    ) { change, dragAmount ->
                        change.consume()
                        cameraLat += dragAmount.y * DEG_PER_PX_LAT
                        cameraLng -= dragAmount.x * DEG_PER_PX_LNG
                    }
                },
        ) {
            NativeMapView(
                modifier = Modifier.fillMaxSize(),
                cameraLat = cameraLat,
                cameraLng = cameraLng,
                cameraZoom = 14f,
                darkMode = true,
            )
        }

        // --- Top bar overlay ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            RideColors.Background.copy(alpha = 0.85f),
                            Color.Transparent,
                        ),
                    ),
                )
                .padding(top = 52.dp, start = 16.dp, end = 16.dp, bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(RideColors.SurfaceWhite12)
                    .clickable {
                        onIntent(RideIntent.NavigateTo(RideScreen.SelectDestination))
                    },
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "\u2190", color = Color.White, fontSize = 18.sp)
            }
            Spacer(Modifier.width(14.dp))
            Text(
                text = "Set location on map",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        // --- Center pin ---
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Pin head
            Box(
                modifier = Modifier
                    .scale(pinScale.value)
                    .offset { IntOffset(0, if (isDragging) (-12).dp.roundToPx() else 0) },
                contentAlignment = Alignment.BottomCenter,
            ) {
                // Pin shadow
                Box(
                    modifier = Modifier
                        .offset(y = 32.dp)
                        .size(width = 20.dp, height = 6.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.3f)),
                )
                // Pin body
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .shadow(8.dp, CircleShape)
                            .clip(CircleShape)
                            .background(RideColors.Purple),
                        contentAlignment = Alignment.Center,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                        )
                    }
                    // Pin pointer triangle (small stem)
                    Box(
                        modifier = Modifier
                            .size(width = 4.dp, height = 10.dp)
                            .background(RideColors.Purple),
                    )
                }
            }
        }

        // --- Re-center button ---
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp)
                .offset(y = 80.dp)
                .size(44.dp)
                .shadow(4.dp, CircleShape)
                .clip(CircleShape)
                .background(RideColors.CardBackground)
                .clickable {
                    scope.launch {
                        cameraLat = SF_CENTER_LAT
                        cameraLng = SF_CENTER_LNG
                    }
                },
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "\u2316", color = RideColors.Cyan, fontSize = 20.sp)
        }

        // --- Bottom card ---
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            RideColors.Background.copy(alpha = 0.9f),
                            RideColors.Background,
                        ),
                    ),
                )
                .padding(top = 32.dp, bottom = 16.dp),
        ) {
            // Address card
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(RideColors.CardBackground)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(RideColors.Purple.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "\uD83D\uDCCD", fontSize = 20.sp)
                }
                Spacer(Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = nearbyPlace?.name ?: "Pinned Location",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                    )
                    Text(
                        text = addressText,
                        color = RideColors.TextTertiary,
                        fontSize = 12.sp,
                    )
                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        CoordinateChip(
                            label = "%.4f".format(cameraLat),
                        )
                        CoordinateChip(
                            label = "%.4f".format(cameraLng),
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Confirm button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(RideColors.Purple)
                    .clickable {
                        onIntent(
                            RideIntent.ConfirmMapPin(
                                lat = cameraLat,
                                lng = cameraLng,
                                address = addressText,
                            ),
                        )
                    },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Confirm location",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                )
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun CoordinateChip(label: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(RideColors.SurfaceWhite4)
            .padding(horizontal = 6.dp, vertical = 2.dp),
    ) {
        Text(
            text = label,
            color = RideColors.TextLabel,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

/** Find the nearest known place within ~200m. */
private fun findNearbyPlace(lat: Double, lng: Double): com.dovoh.android_mvi.feature.home.presentation.model.Place? {
    val allPlaces = DefaultData.savedPlaces + DefaultData.suggestedPlaces
    val threshold = 0.002 // ~200m in degrees
    return allPlaces.minByOrNull { p ->
        val d = sqrt((p.lat - lat) * (p.lat - lat) + (p.lng - lng) * (p.lng - lng))
        d
    }?.takeIf { p ->
        sqrt((p.lat - lat) * (p.lat - lat) + (p.lng - lng) * (p.lng - lng)) < threshold
    }
}

/** Generate a plausible SF street address from coordinates. */
private fun generateAddress(lat: Double, lng: Double): String {
    val streets = listOf(
        "Market St", "Mission St", "Valencia St", "Folsom St",
        "Howard St", "Geary Blvd", "Divisadero St", "Hayes St",
        "Fillmore St", "Polk St", "Van Ness Ave", "Columbus Ave",
        "Broadway", "Montgomery St", "Kearny St", "Stockton St",
    )
    // Deterministic-ish street pick from coordinates
    val index = ((lat * 10000 + lng * 10000).toInt().and(0x7FFFFFFF)) % streets.size
    val number = (((lat - 37.7) * 40000).toInt().and(0x7FFF)) + 100
    return "$number ${streets[index]}, San Francisco"
}
