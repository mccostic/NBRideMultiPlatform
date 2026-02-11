package com.dovoh.android_mvi.feature.home.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.dovoh.android_mvi.feature.home.presentation.RideScreen
import com.dovoh.android_mvi.feature.home.presentation.RideState
import com.dovoh.android_mvi.feature.home.presentation.theme.RideColors

@Composable
fun MapView(
    state: RideState,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        RideColors.MapDark1,
                        RideColors.MapDark2,
                        RideColors.MapDark3,
                    ),
                )
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawGrid()
            drawRoads()
            drawCityBlocks()

            if (state.screen == RideScreen.Home) {
                drawNearbyCars()
            }

            val showRoute = state.screen in listOf(
                RideScreen.RideOptions,
                RideScreen.DriverFound,
                RideScreen.InRide,
            )
            if (showRoute) {
                drawRoute()
            }

            if (state.origin != null) {
                drawOriginPin()
            }

            if (state.destination != null) {
                drawDestinationPin(state.destination.name)
            }

            if (state.screen == RideScreen.InRide) {
                drawDriverOnRoute(state.rideProgress)
            }

            drawVignette()
        }
    }
}

private fun DrawScope.drawGrid() {
    val gridColor = RideColors.GridLine.copy(alpha = 0.12f)
    val step = size.width / 18f
    for (i in 0..20) {
        val y = i * step
        drawLine(gridColor, Offset(0f, y), Offset(size.width, y), strokeWidth = 0.5f)
    }
    for (i in 0..20) {
        val x = i * step
        drawLine(gridColor, Offset(x, 0f), Offset(x, size.height), strokeWidth = 0.5f)
    }
}

private fun DrawScope.drawRoads() {
    val roadColor = RideColors.Road.copy(alpha = 0.3f)
    val majorColor = RideColors.RoadMajor.copy(alpha = 0.3f)
    val w = size.width
    val h = size.height

    // Horizontal roads
    drawLine(roadColor, Offset(0f, h * 0.45f), Offset(w, h * 0.48f), strokeWidth = 3f)
    drawLine(roadColor, Offset(0f, h * 0.65f), Offset(w, h * 0.70f), strokeWidth = 2f)

    // Vertical roads
    drawLine(roadColor, Offset(w * 0.25f, 0f), Offset(w * 0.32f, h), strokeWidth = 3f)
    drawLine(roadColor, Offset(w * 0.65f, 0f), Offset(w * 0.68f, h), strokeWidth = 2f)

    // Major roads
    drawLine(majorColor, Offset(w * 0.45f, 0f), Offset(w * 0.48f, h), strokeWidth = 5f)
    drawLine(majorColor, Offset(0f, h * 0.30f), Offset(w, h * 0.28f), strokeWidth = 5f)
}

private fun DrawScope.drawCityBlocks() {
    val blocks = listOf(
        38f to 28f, 55f to 35f, 42f to 52f, 68f to 45f, 30f to 60f,
        75f to 28f, 62f to 65f, 48f to 78f, 82f to 55f, 20f to 42f,
        90f to 70f, 15f to 75f, 58f to 18f, 35f to 85f, 72f to 82f,
    )
    val blockW = 24f
    val blockH = 16f
    blocks.forEach { (xPct, yPct) ->
        val x = size.width * xPct / 100f - blockW / 2
        val y = size.height * yPct / 100f - blockH / 2
        drawRect(
            color = RideColors.CityBlock,
            topLeft = Offset(x, y),
            size = Size(blockW, blockH),
        )
        drawRect(
            color = RideColors.CityBlockBorder,
            topLeft = Offset(x, y),
            size = Size(blockW, blockH),
            style = Stroke(width = 1f),
        )
    }
}

private fun DrawScope.drawNearbyCars() {
    val cars = listOf(36f to 42f, 55f to 50f, 70f to 38f, 48f to 65f)
    val carColor = RideColors.Cyan.copy(alpha = 0.7f)
    cars.forEach { (xPct, yPct) ->
        val cx = size.width * xPct / 100f
        val cy = size.height * yPct / 100f
        // Glow circle
        drawCircle(
            color = RideColors.Cyan.copy(alpha = 0.15f),
            radius = 12f,
            center = Offset(cx, cy),
        )
        // Car dot
        drawCircle(
            color = carColor,
            radius = 5f,
            center = Offset(cx, cy),
        )
    }
}

private fun DrawScope.drawRoute() {
    val path = Path().apply {
        moveTo(size.width * 0.48f, size.height * 0.70f)
        quadraticTo(
            size.width * 0.40f, size.height * 0.50f,
            size.width * 0.52f, size.height * 0.35f,
        )
    }
    drawPath(
        path = path,
        brush = Brush.horizontalGradient(
            colors = listOf(RideColors.Cyan, RideColors.Purple),
        ),
        style = Stroke(
            width = 3f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 8f), 0f),
            cap = StrokeCap.Round,
        ),
    )
}

private fun DrawScope.drawOriginPin() {
    val cx = size.width * 0.48f
    val cy = size.height * 0.70f
    // Glow
    drawCircle(RideColors.Cyan.copy(alpha = 0.3f), 16f, Offset(cx, cy))
    // Pin dot
    drawCircle(RideColors.Cyan, 6f, Offset(cx, cy))
    // Inner dot
    drawCircle(Color.Black, 2.5f, Offset(cx, cy))
}

private fun DrawScope.drawDestinationPin(name: String) {
    val cx = size.width * 0.52f
    val cy = size.height * 0.35f
    // Glow
    drawCircle(RideColors.Purple.copy(alpha = 0.4f), 16f, Offset(cx, cy))
    // Pin dot
    drawCircle(RideColors.Purple, 6f, Offset(cx, cy))
    // Inner dot
    drawCircle(Color.White, 2.5f, Offset(cx, cy))
}

private fun DrawScope.drawDriverOnRoute(progress: Float) {
    val fraction = progress / 100f
    val startX = size.width * 0.48f
    val startY = size.height * 0.70f
    val endX = size.width * 0.52f
    val endY = size.height * 0.35f
    val cx = startX + (endX - startX) * fraction
    val cy = startY + (endY - startY) * fraction
    // Glow
    drawCircle(RideColors.Cyan.copy(alpha = 0.4f), 14f, Offset(cx, cy))
    // Driver dot
    drawCircle(RideColors.Cyan, 7f, Offset(cx, cy))
    drawCircle(Color.White, 3f, Offset(cx, cy))
}

private fun DrawScope.drawVignette() {
    val center = Offset(size.width / 2f, size.height / 2f)
    val radius = maxOf(size.width, size.height) * 0.7f
    val shader = RadialGradientShader(
        center = center,
        radius = radius,
        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
        colorStops = listOf(0.4f, 1.0f),
    )
    drawRect(
        brush = ShaderBrush(shader),
        size = size,
    )
}
