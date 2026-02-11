package com.dovoh.android_mvi.feature.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dovoh.android_mvi.feature.home.presentation.RideIntent
import com.dovoh.android_mvi.feature.home.presentation.RideState
import com.dovoh.android_mvi.feature.home.presentation.theme.RideColors
import kotlinx.coroutines.delay
import kotlin.math.ceil

@Composable
fun InRideContent(
    state: RideState,
    onIntent: (RideIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Simulate ride progress
    LaunchedEffect(state.rideProgress) {
        if (state.rideProgress < 100f) {
            delay(300)
            val next = (state.rideProgress + 2.5f).coerceAtMost(100f)
            onIntent(RideIntent.UpdateProgress(next))
            if (next >= 100f) {
                onIntent(RideIntent.RideCompleted)
            }
        }
    }

    val minsLeft = ceil((100f - state.rideProgress) / 100f * 18f).toInt()

    Box(modifier = modifier.fillMaxSize()) {
        MapView(state = state)

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            RideColors.Background.copy(alpha = 0.99f),
                        ),
                        startY = 0f,
                        endY = 60f,
                    )
                )
                .padding(horizontal = 16.dp)
                .padding(bottom = 28.dp, top = 40.dp),
        ) {
            // Progress section
            Column(modifier = Modifier.padding(bottom = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "En route to ${state.destination?.name ?: ""}",
                        color = RideColors.TextHint,
                        fontSize = 12.sp,
                    )
                    Text(
                        text = "$minsLeft min",
                        color = RideColors.Cyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                    )
                }

                Spacer(Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { state.rideProgress / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    color = RideColors.Cyan,
                    trackColor = Color.White.copy(alpha = 0.1f),
                )
            }

            // Driver card compact
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(RideColors.CardBackground)
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = state.driver?.avatar ?: "", fontSize = 28.sp)
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = state.driver?.name ?: "",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                    )
                    Text(
                        text = "${state.driver?.car ?: ""} \u00B7 ${state.driver?.plate ?: ""}",
                        color = RideColors.TextTertiary,
                        fontSize = 12.sp,
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Message button
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(RideColors.CyanSubtle)
                            .border(1.dp, RideColors.Cyan.copy(alpha = 0.3f), CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = "\uD83D\uDCAC", fontSize = 16.sp)
                    }
                    // Call button
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(RideColors.Purple.copy(alpha = 0.1f))
                            .border(1.dp, RideColors.Purple.copy(alpha = 0.3f), CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = "\uD83D\uDCDE", fontSize = 16.sp)
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Share trip status
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(RideColors.SurfaceWhite4)
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(14.dp))
                    .padding(14.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "\uD83D\uDCE4 Share trip status",
                    color = RideColors.TextSecondary,
                    fontSize = 14.sp,
                )
            }
        }
    }
}
