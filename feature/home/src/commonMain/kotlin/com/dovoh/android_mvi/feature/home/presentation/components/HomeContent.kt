package com.dovoh.android_mvi.feature.home.presentation.components

import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dovoh.android_mvi.feature.home.presentation.RideIntent
import com.dovoh.android_mvi.feature.home.presentation.RideScreen
import com.dovoh.android_mvi.feature.home.presentation.RideState
import com.dovoh.android_mvi.feature.home.presentation.theme.RideColors

@Composable
fun HomeContent(
    state: RideState,
    onIntent: (RideIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        // Map background
        MapView(state = state)

        // "rider." branding top-left
        Text(
            text = "rider.",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = (-0.5).sp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 16.dp, top = 52.dp),
        )

        // Avatar button top-right
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 16.dp, top = 52.dp)
                .size(38.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(RideColors.Cyan, RideColors.Purple),
                    )
                )
                .clickable { onIntent(RideIntent.NavigateTo(RideScreen.Home)) },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "A",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
            )
        }

        // Bottom sheet-like panel
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            RideColors.Background.copy(alpha = 0.97f),
                        ),
                        startY = 0f,
                        endY = 120f,
                    )
                )
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp, top = 60.dp),
        ) {
            // "Where to?" button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(RideColors.SurfaceWhite7)
                    .clickable {
                        onIntent(RideIntent.NavigateTo(RideScreen.SelectDestination))
                    }
                    .padding(horizontal = 20.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(RideColors.CyanSubtle),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "\uD83D\uDD0D", fontSize = 16.sp)
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    text = "Where to?",
                    color = RideColors.TextHint,
                    fontSize = 16.sp,
                )
            }

            Spacer(Modifier.height(20.dp))

            // Recent section header
            Text(
                text = "RECENT",
                color = RideColors.TextTertiary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.2.sp,
                modifier = Modifier.padding(bottom = 10.dp),
            )

            // Recent places
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                state.recentPlaces.forEach { place ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(RideColors.SurfaceWhite4)
                            .clickable { onIntent(RideIntent.SetDestination(place)) }
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(RideColors.SurfaceWhite7),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(text = place.icon, fontSize = 18.sp)
                        }
                        Spacer(Modifier.width(14.dp))
                        Column {
                            Text(
                                text = place.name,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                            )
                            Text(
                                text = place.address,
                                color = RideColors.TextTertiary,
                                fontSize = 12.sp,
                            )
                        }
                    }
                }
            }
        }
    }
}
