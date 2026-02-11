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
import androidx.compose.foundation.layout.width
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
import com.dovoh.android_mvi.feature.home.presentation.RideScreen
import com.dovoh.android_mvi.feature.home.presentation.RideState
import com.dovoh.android_mvi.feature.home.presentation.theme.RideColors
import com.dovoh.android_mvi.feature.home.presentation.util.formatPrice

@Composable
fun RideOptionsContent(
    state: RideState,
    onIntent: (RideIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        // Map background
        MapView(state = state)

        // Bottom panel
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            RideColors.Background.copy(alpha = 0.98f),
                        ),
                        startY = 0f,
                        endY = 80f,
                    )
                )
                .padding(bottom = 28.dp, top = 40.dp),
        ) {
            // Trip summary
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(RideColors.CardBackground)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "\uD83D\uDCCD", fontSize = 12.sp)
                Spacer(Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "From \u2192 To",
                        color = RideColors.TextHint,
                        fontSize = 11.sp,
                    )
                    Text(
                        text = "Current Location \u2192 ${state.destination?.name ?: ""}",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                Text(
                    text = "Edit",
                    color = RideColors.TextSecondary,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(RideColors.SurfaceWhite7)
                        .clickable {
                            onIntent(RideIntent.NavigateTo(RideScreen.SelectDestination))
                        }
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                )
            }

            Spacer(Modifier.height(12.dp))

            // Promo
            if (!state.promoApplied) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    Text(
                        text = "\uD83C\uDFF7 Apply Promo",
                        color = RideColors.Cyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(RideColors.CyanSubtle)
                            .border(1.dp, RideColors.Cyan.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                            .clickable { onIntent(RideIntent.ApplyPromo) }
                            .padding(horizontal = 14.dp, vertical = 5.dp),
                    )
                }
            } else {
                Text(
                    text = "\u2705 Promo RIDE10 applied \u2014 10% off!",
                    color = RideColors.Success,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(RideColors.SuccessSubtle)
                        .border(1.dp, RideColors.Success.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(Modifier.height(12.dp))

            // Ride options list
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                state.rideOptions.forEach { ride ->
                    val isSelected = state.selectedRide?.id == ride.id
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .then(
                                if (isSelected) {
                                    Modifier
                                        .background(RideColors.CyanSubtle)
                                        .border(2.dp, RideColors.Cyan, RoundedCornerShape(16.dp))
                                } else {
                                    Modifier
                                        .background(RideColors.SurfaceWhite4)
                                        .border(2.dp, Color.White.copy(alpha = 0.06f), RoundedCornerShape(16.dp))
                                }
                            )
                            .clickable { onIntent(RideIntent.SelectRide(ride)) }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(text = ride.icon, fontSize = 28.sp)
                        Spacer(Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = ride.name,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                            )
                            Text(
                                text = "${ride.description} \u00B7 ${ride.time} away",
                                color = RideColors.TextTertiary,
                                fontSize = 12.sp,
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "$${ride.price.formatPrice()}",
                                color = if (isSelected) RideColors.Cyan else Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                            )
                            if (state.promoApplied) {
                                Text(
                                    text = "-10%",
                                    color = RideColors.Success,
                                    fontSize = 10.sp,
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            // Payment row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(RideColors.SurfaceWhite4)
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "\uD83D\uDCB3", fontSize = 14.sp)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = state.paymentMethod,
                        color = Color.White,
                        fontSize = 13.sp,
                    )
                }
                Text(
                    text = "\u25BC",
                    color = Color.White.copy(alpha = 0.3f),
                    fontSize = 12.sp,
                )
            }

            Spacer(Modifier.height(12.dp))

            // Book button
            val canBook = state.selectedRide != null
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .then(
                        if (canBook) {
                            Modifier.background(
                                Brush.linearGradient(
                                    colors = listOf(RideColors.Cyan, RideColors.Purple),
                                )
                            )
                        } else {
                            Modifier.background(Color.White.copy(alpha = 0.1f))
                        }
                    )
                    .clickable(enabled = canBook) {
                        onIntent(RideIntent.RequestRide)
                    },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = if (canBook) {
                        "Book ${state.selectedRide!!.name} \u2014 $${state.selectedRide.price.formatPrice()}"
                    } else {
                        "Select a ride"
                    },
                    color = if (canBook) Color.White else Color.White.copy(alpha = 0.3f),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                )
            }
        }
    }
}
