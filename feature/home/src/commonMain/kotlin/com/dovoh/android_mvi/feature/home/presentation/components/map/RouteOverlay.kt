package com.dovoh.android_mvi.feature.home.presentation.components.map

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.dp
import com.dovoh.android_mvi.feature.home.presentation.theme.RideColors
import kotlin.math.PI
import kotlin.math.atan2

@Composable
fun RouteOverlay(
    rideProgress: Float,
    showDriver: Boolean,
    modifier: Modifier = Modifier,
) {
    // Smooth animated progress for driver car
    val animatedProgress = remember { Animatable(0f) }
    LaunchedEffect(rideProgress) {
        animatedProgress.animateTo(
            targetValue = rideProgress / 100f,
            animationSpec = tween(800, easing = FastOutSlowInEasing),
        )
    }

    // Pulsing origin marker
    val infiniteTransition = rememberInfiniteTransition()
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
    )

    // Driver car glow pulsing
    val driverGlow by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse,
        ),
    )

    val cyan = RideColors.Cyan
    val purple = RideColors.Purple

    Canvas(modifier = modifier.fillMaxSize()) {
        // Route control points (relative to canvas)
        val originX = size.width * 0.22f
        val originY = size.height * 0.52f
        val destX = size.width * 0.78f
        val destY = size.height * 0.18f
        val ctrlX = size.width * 0.58f
        val ctrlY = size.height * 0.48f

        // Route path (quadratic bezier)
        val routePath = Path().apply {
            moveTo(originX, originY)
            quadraticBezierTo(ctrlX, ctrlY, destX, destY)
        }

        // Route glow (wide, transparent)
        drawPath(
            path = routePath,
            color = cyan.copy(alpha = 0.12f),
            style = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round),
        )

        // Route line
        drawPath(
            path = routePath,
            color = cyan.copy(alpha = 0.55f),
            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round),
        )

        // --- Origin marker (pulsing cyan dot) ---
        drawCircle(
            color = cyan.copy(alpha = pulseAlpha * 0.3f),
            radius = 14.dp.toPx() * pulseScale,
            center = Offset(originX, originY),
        )
        drawCircle(
            color = cyan,
            radius = 9.dp.toPx(),
            center = Offset(originX, originY),
        )
        drawCircle(
            color = Color.White,
            radius = 4.5.dp.toPx(),
            center = Offset(originX, originY),
        )

        // --- Destination marker (purple pin) ---
        // Pin shadow
        drawCircle(
            color = purple.copy(alpha = 0.2f),
            radius = 14.dp.toPx(),
            center = Offset(destX, destY),
        )
        // Pin outer
        drawCircle(
            color = purple,
            radius = 10.dp.toPx(),
            center = Offset(destX, destY),
        )
        // Pin inner
        drawCircle(
            color = Color.White,
            radius = 5.dp.toPx(),
            center = Offset(destX, destY),
        )

        // --- Driver car on route ---
        if (showDriver) {
            val t = animatedProgress.value
            // Quadratic bezier interpolation: P = (1-t)²P0 + 2(1-t)tP1 + t²P2
            val oneMinusT = 1f - t
            val carX = oneMinusT * oneMinusT * originX +
                2f * oneMinusT * t * ctrlX +
                t * t * destX
            val carY = oneMinusT * oneMinusT * originY +
                2f * oneMinusT * t * ctrlY +
                t * t * destY

            // Tangent for rotation: P' = 2(1-t)(P1-P0) + 2t(P2-P1)
            val tangentX = 2f * oneMinusT * (ctrlX - originX) + 2f * t * (destX - ctrlX)
            val tangentY = 2f * oneMinusT * (ctrlY - originY) + 2f * t * (destY - ctrlY)
            val angleDeg = atan2(tangentY.toDouble(), tangentX.toDouble()) * (180.0 / PI)

            // Outer glow
            drawCircle(
                color = cyan.copy(alpha = driverGlow),
                radius = 20.dp.toPx(),
                center = Offset(carX, carY),
            )

            // Car shape, rotated along direction of travel
            val carW = 32.dp.toPx()
            val carH = 15.dp.toPx()

            withTransform({
                translate(left = carX, top = carY)
                rotate(degrees = angleDeg.toFloat(), pivot = Offset.Zero)
            }) {
                // Car body
                drawRoundRect(
                    color = Color(0xFF0E1A28),
                    topLeft = Offset(-carW / 2, -carH / 2),
                    size = Size(carW, carH),
                    cornerRadius = CornerRadius(carH / 2, carH / 2),
                )

                // Cyan trim around car
                drawRoundRect(
                    color = cyan,
                    topLeft = Offset(-carW / 2 - 1.5.dp.toPx(), -carH / 2 - 1.5.dp.toPx()),
                    size = Size(carW + 3.dp.toPx(), carH + 3.dp.toPx()),
                    cornerRadius = CornerRadius(
                        (carH + 3.dp.toPx()) / 2,
                        (carH + 3.dp.toPx()) / 2,
                    ),
                    style = Stroke(width = 2.dp.toPx()),
                )

                // Windshield
                drawRoundRect(
                    color = cyan.copy(alpha = 0.6f),
                    topLeft = Offset(carW * 0.1f, -carH * 0.22f),
                    size = Size(carW * 0.18f, carH * 0.44f),
                    cornerRadius = CornerRadius(2.dp.toPx()),
                )

                // Rear window
                drawRoundRect(
                    color = cyan.copy(alpha = 0.3f),
                    topLeft = Offset(-carW * 0.35f, -carH * 0.18f),
                    size = Size(carW * 0.14f, carH * 0.36f),
                    cornerRadius = CornerRadius(1.5.dp.toPx()),
                )

                // Headlights
                val headlightY = carH * 0.15f
                drawCircle(
                    color = Color(0xFFFFE082),
                    radius = 2.dp.toPx(),
                    center = Offset(carW / 2 - 2.dp.toPx(), -headlightY),
                )
                drawCircle(
                    color = Color(0xFFFFE082),
                    radius = 2.dp.toPx(),
                    center = Offset(carW / 2 - 2.dp.toPx(), headlightY),
                )
            }
        }
    }
}
