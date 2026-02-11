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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
fun RideCompleteContent(
    state: RideState,
    onIntent: (RideIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(RideColors.Background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // Success icon
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            RideColors.Cyan.copy(alpha = 0.15f),
                            RideColors.Purple.copy(alpha = 0.15f),
                        )
                    )
                )
                .border(2.dp, RideColors.Cyan.copy(alpha = 0.4f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "\u2705", fontSize = 36.sp)
        }

        Spacer(Modifier.height(20.dp))

        Text(
            text = "You've arrived!",
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = state.destination?.name ?: "",
            color = RideColors.TextTertiary,
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(28.dp))

        // Fare card
        Column(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(RideColors.CardBackground)
                .border(1.dp, RideColors.CardBorder, RoundedCornerShape(20.dp))
                .padding(horizontal = 28.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Total charged",
                color = RideColors.TextHint,
                fontSize = 13.sp,
            )
            Text(
                text = "$${state.fare?.formatPrice() ?: "0.00"}",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
            )
            Text(
                text = state.paymentMethod,
                color = RideColors.TextTertiary,
                fontSize = 12.sp,
            )
        }

        Spacer(Modifier.height(28.dp))

        // Rating
        Text(
            text = "Rate ${state.driver?.name ?: ""}",
            color = RideColors.TextSecondary,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 12.dp),
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            for (star in 1..5) {
                val isSelected = (state.rating ?: 0) >= star
                Text(
                    text = "\u2B50",
                    fontSize = 30.sp,
                    modifier = Modifier
                        .clickable { onIntent(RideIntent.RateDriver(star)) }
                        .alpha(if (isSelected) 1f else 0.3f),
                )
            }
        }

        Spacer(Modifier.height(28.dp))

        // Done button
        Box(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(RideColors.Cyan, RideColors.Purple),
                    )
                )
                .clickable { onIntent(RideIntent.GoHome) },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Done",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
        }
    }
}
