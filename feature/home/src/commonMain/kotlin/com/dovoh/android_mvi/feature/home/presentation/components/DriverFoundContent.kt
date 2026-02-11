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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dovoh.android_mvi.feature.home.presentation.RideIntent
import com.dovoh.android_mvi.feature.home.presentation.util.formatPrice
import com.dovoh.android_mvi.feature.home.presentation.RideState
import com.dovoh.android_mvi.feature.home.presentation.theme.RideColors

@Composable
fun DriverFoundContent(
    state: RideState,
    onIntent: (RideIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
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
                .padding(bottom = 28.dp, top = 50.dp),
        ) {
            // ETA pill
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(30.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    RideColors.Cyan.copy(alpha = 0.15f),
                                    RideColors.Purple.copy(alpha = 0.15f),
                                )
                            )
                        )
                        .border(1.dp, RideColors.Cyan.copy(alpha = 0.3f), RoundedCornerShape(30.dp))
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                ) {
                    Text(
                        text = "\u23F1 ${state.eta} min away",
                        color = RideColors.Cyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Driver card
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(RideColors.CardBackground)
                    .border(1.dp, RideColors.CardBorder, RoundedCornerShape(20.dp))
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.08f))
                        .border(2.dp, RideColors.Cyan.copy(alpha = 0.3f), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = state.driver?.avatar ?: "", fontSize = 32.sp)
                }

                Spacer(Modifier.width(14.dp))

                // Driver info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = state.driver?.name ?: "",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                    )
                    Text(
                        text = "\u2B50 ${state.driver?.rating} \u00B7 ${state.driver?.trips} trips",
                        color = RideColors.TextHint,
                        fontSize = 13.sp,
                    )
                }

                // Car info
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = state.driver?.car ?: "",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = state.driver?.plate ?: "",
                        color = RideColors.Cyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        letterSpacing = 0.8.sp,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(RideColors.CyanSubtle)
                            .padding(horizontal = 10.dp, vertical = 2.dp),
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            // Fare
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(RideColors.SurfaceWhite4)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Estimated fare",
                    color = RideColors.TextSecondary,
                    fontSize = 14.sp,
                )
                Text(
                    text = "$${state.fare?.formatPrice() ?: "0.00"}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
            }

            Spacer(Modifier.height(14.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                // Cancel
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(RideColors.ErrorSubtle)
                        .border(1.dp, RideColors.ErrorBorder, RoundedCornerShape(14.dp))
                        .clickable { onIntent(RideIntent.CancelRide) },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Cancel Ride",
                        color = RideColors.Error,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                    )
                }

                // Track
                Box(
                    modifier = Modifier
                        .weight(2f)
                        .height(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(RideColors.Cyan, RideColors.Purple),
                            )
                        )
                        .clickable { onIntent(RideIntent.RideStarted) },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "\uD83D\uDCCD Track Driver",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                    )
                }
            }
        }
    }
}
