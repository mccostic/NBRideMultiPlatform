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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dovoh.android_mvi.feature.home.presentation.RideIntent
import com.dovoh.android_mvi.feature.home.presentation.RideScreen
import com.dovoh.android_mvi.feature.home.presentation.RideState
import com.dovoh.android_mvi.feature.home.presentation.theme.RideColors

@Composable
fun SelectDestinationContent(
    state: RideState,
    onIntent: (RideIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val filtered = if (state.searchQuery.isNotEmpty()) {
        (state.recentPlaces + state.suggestedPlaces).filter { p ->
            p.name.contains(state.searchQuery, ignoreCase = true) ||
                p.address.contains(state.searchQuery, ignoreCase = true)
        }
    } else {
        state.suggestedPlaces
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(RideColors.Background),
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 52.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 14.dp),
            ) {
                // Back button
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(RideColors.SurfaceWhite7)
                        .clickable {
                            onIntent(RideIntent.NavigateTo(RideScreen.Home))
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "\u2190",
                        color = Color.White,
                        fontSize = 16.sp,
                    )
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    text = "Set your destination",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            // Origin pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(RideColors.CyanSubtle)
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(RideColors.Cyan),
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "Current location",
                    color = RideColors.Cyan,
                    fontSize = 14.sp,
                )
            }

            Spacer(Modifier.height(10.dp))

            // Search field
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(RideColors.SurfaceWhite7)
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "\uD83C\uDFAF", fontSize = 16.sp)
                Spacer(Modifier.width(10.dp))
                BasicTextField(
                    value = state.searchQuery,
                    onValueChange = { onIntent(RideIntent.SetSearchQuery(it)) },
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 14.sp,
                    ),
                    cursorBrush = SolidColor(RideColors.Cyan),
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    decorationBox = { inner ->
                        if (state.searchQuery.isEmpty()) {
                            Text(
                                text = "Search destination...",
                                color = RideColors.TextHint,
                                fontSize = 14.sp,
                            )
                        }
                        inner()
                    },
                )
            }
        }

        HorizontalDivider(
            color = Color.White.copy(alpha = 0.06f),
            thickness = 1.dp,
        )

        // Results
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            if (state.searchQuery.isEmpty()) {
                item {
                    Text(
                        text = "SUGGESTED",
                        color = RideColors.TextLabel,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 10.dp),
                    )
                }
            }

            items(filtered, key = { it.id }) { place ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onIntent(RideIntent.SetDestination(place)) }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.06f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = place.icon, fontSize = 20.sp)
                    }
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text(
                            text = place.name,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
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
