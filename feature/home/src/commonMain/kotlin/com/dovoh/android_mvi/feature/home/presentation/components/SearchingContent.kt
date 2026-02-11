package com.dovoh.android_mvi.feature.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dovoh.android_mvi.feature.home.presentation.RideIntent
import com.dovoh.android_mvi.feature.home.presentation.RideState
import com.dovoh.android_mvi.feature.home.presentation.theme.RideColors

@Composable
fun SearchingContent(
    state: RideState,
    onIntent: (RideIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        // Map background
        MapView(state = state)

        // Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(RideColors.Background.copy(alpha = 0.92f)),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Pulsing indicator
                Box(
                    modifier = Modifier.size(100.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(80.dp),
                        color = RideColors.Cyan.copy(alpha = 0.3f),
                        strokeWidth = 2.dp,
                    )
                    Text(text = "\uD83D\uDE97", fontSize = 36.sp)
                }

                Spacer(Modifier.height(28.dp))

                Text(
                    text = "Finding your driver...",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "This usually takes under 30 seconds",
                    color = RideColors.TextTertiary,
                    fontSize = 14.sp,
                )

                Spacer(Modifier.height(36.dp))

                Text(
                    text = "Cancel",
                    color = RideColors.TextSecondary,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(30.dp))
                        .background(RideColors.SurfaceWhite7)
                        .border(1.dp, RideColors.SurfaceWhite15, RoundedCornerShape(30.dp))
                        .clickable { onIntent(RideIntent.CancelRide) }
                        .padding(horizontal = 28.dp, vertical = 12.dp),
                )
            }
        }
    }
}
