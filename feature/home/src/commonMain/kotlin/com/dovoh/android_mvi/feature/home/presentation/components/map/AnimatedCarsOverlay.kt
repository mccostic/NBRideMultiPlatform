package com.dovoh.android_mvi.feature.home.presentation.components.map

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
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
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.dp
import com.dovoh.android_mvi.feature.home.presentation.theme.RideColors
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.random.Random
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private class CarAnimState(
    val x: Animatable<Float, AnimationVector1D>,
    val y: Animatable<Float, AnimationVector1D>,
    val rotation: Animatable<Float, AnimationVector1D>,
)

@Composable
fun AnimatedCarsOverlay(modifier: Modifier = Modifier) {
    val carCount = 6
    val cars = remember {
        List(carCount) { index ->
            val random = Random(index * 42 + 7)
            CarAnimState(
                x = Animatable(random.nextFloat() * 0.55f + 0.15f),
                y = Animatable(random.nextFloat() * 0.4f + 0.1f),
                rotation = Animatable(random.nextFloat() * 360f),
            )
        }
    }

    // Launch animation coroutine for each car
    cars.forEachIndexed { index, car ->
        LaunchedEffect(index) {
            val carRandom = Random(index * 17 + 3)
            while (true) {
                delay(carRandom.nextLong(2200, 4200))

                val currentX = car.x.value
                val currentY = car.y.value
                val newX = (currentX + (carRandom.nextFloat() - 0.5f) * 0.13f)
                    .coerceIn(0.06f, 0.94f)
                val newY = (currentY + (carRandom.nextFloat() - 0.5f) * 0.1f)
                    .coerceIn(0.06f, 0.65f)

                // Calculate heading angle
                val dx = newX - currentX
                val dy = newY - currentY
                val angle = atan2(dy.toDouble(), dx.toDouble()) * (180.0 / PI)

                // Animate rotation, then position in parallel
                launch {
                    car.rotation.animateTo(
                        angle.toFloat(),
                        tween(350, easing = FastOutSlowInEasing),
                    )
                }
                launch {
                    car.x.animateTo(
                        newX,
                        tween(3000, easing = LinearOutSlowInEasing),
                    )
                }
                car.y.animateTo(
                    newY,
                    tween(3000, easing = LinearOutSlowInEasing),
                )
            }
        }
    }

    // Pulsing "You Are Here" marker
    val infiniteTransition = rememberInfiniteTransition()
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 2.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
    )

    val cyan = RideColors.Cyan

    Canvas(modifier = modifier.fillMaxSize()) {
        val carBodyColor = Color(0xFF1C2333)
        val windshieldColor = cyan.copy(alpha = 0.55f)
        val rearWindowColor = cyan.copy(alpha = 0.25f)
        val carWidth = 30.dp.toPx()
        val carHeight = 14.dp.toPx()

        // Draw animated cars
        cars.forEach { car ->
            val cx = car.x.value * size.width
            val cy = car.y.value * size.height

            withTransform({
                translate(left = cx, top = cy)
                rotate(degrees = car.rotation.value, pivot = Offset.Zero)
            }) {
                // Car body - pill shape
                drawRoundRect(
                    color = carBodyColor,
                    topLeft = Offset(-carWidth / 2, -carHeight / 2),
                    size = Size(carWidth, carHeight),
                    cornerRadius = CornerRadius(carHeight / 2, carHeight / 2),
                )

                // Front windshield
                drawRoundRect(
                    color = windshieldColor,
                    topLeft = Offset(carWidth * 0.12f, -carHeight * 0.22f),
                    size = Size(carWidth * 0.17f, carHeight * 0.44f),
                    cornerRadius = CornerRadius(2.dp.toPx()),
                )

                // Rear window
                drawRoundRect(
                    color = rearWindowColor,
                    topLeft = Offset(-carWidth * 0.35f, -carHeight * 0.18f),
                    size = Size(carWidth * 0.15f, carHeight * 0.36f),
                    cornerRadius = CornerRadius(1.5.dp.toPx()),
                )
            }
        }

        // "You Are Here" pulsing marker at map center
        val centerX = size.width * 0.48f
        val centerY = size.height * 0.42f

        // Pulse ring
        drawCircle(
            color = cyan.copy(alpha = pulseAlpha * 0.4f),
            radius = 14.dp.toPx() * pulseScale,
            center = Offset(centerX, centerY),
        )

        // Outer ring
        drawCircle(
            color = cyan,
            radius = 10.dp.toPx(),
            center = Offset(centerX, centerY),
        )

        // Inner dot
        drawCircle(
            color = Color.White,
            radius = 5.dp.toPx(),
            center = Offset(centerX, centerY),
        )
    }
}
