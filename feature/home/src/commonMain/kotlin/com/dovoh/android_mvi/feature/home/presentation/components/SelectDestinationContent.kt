package com.dovoh.android_mvi.feature.home.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dovoh.android_mvi.feature.home.presentation.RideIntent
import com.dovoh.android_mvi.feature.home.presentation.RideScreen
import com.dovoh.android_mvi.feature.home.presentation.RideState
import com.dovoh.android_mvi.feature.home.presentation.model.Place
import com.dovoh.android_mvi.feature.home.presentation.theme.RideColors
import kotlin.math.sqrt

@Composable
fun SelectDestinationContent(
    state: RideState,
    onIntent: (RideIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    val isSearching = state.searchQuery.isNotEmpty()

    val allPlaces = remember(state.recentPlaces, state.suggestedPlaces) {
        (state.recentPlaces + state.suggestedPlaces).distinctBy { it.id }
    }

    val searchResults = if (isSearching) {
        allPlaces.filter { p ->
            p.name.contains(state.searchQuery, ignoreCase = true) ||
                p.address.contains(state.searchQuery, ignoreCase = true)
        }
    } else {
        null
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(RideColors.Background),
    ) {
        // --- Top bar ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 52.dp, start = 16.dp, end = 16.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(RideColors.SurfaceWhite7)
                    .clickable { onIntent(RideIntent.NavigateTo(RideScreen.Home)) },
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "\u2190", color = Color.White, fontSize = 18.sp)
            }
            Spacer(Modifier.width(14.dp))
            Text(
                text = "Plan your ride",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        // --- Trip indicator + fields card ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(RideColors.CardBackground)
                .border(1.dp, RideColors.CardBorder, RoundedCornerShape(16.dp))
                .padding(16.dp),
        ) {
            // Left: vertical trip indicator
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 4.dp),
            ) {
                // Cyan origin dot
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(RideColors.Cyan),
                )

                // Dashed connecting line
                Canvas(
                    modifier = Modifier
                        .width(2.dp)
                        .height(36.dp),
                ) {
                    drawLine(
                        color = Color.White.copy(alpha = 0.2f),
                        start = Offset(size.width / 2, 0f),
                        end = Offset(size.width / 2, size.height),
                        strokeWidth = 2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(6.dp.toPx(), 4.dp.toPx()),
                        ),
                    )
                }

                // Purple destination square
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(RideColors.Purple),
                )
            }

            Spacer(Modifier.width(14.dp))

            // Right: fields
            Column(modifier = Modifier.weight(1f)) {
                // Origin field
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(RideColors.CyanSubtle)
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Current Location",
                        color = RideColors.Cyan,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }

                Spacer(Modifier.height(10.dp))

                // Destination search field
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(RideColors.SurfaceWhite7)
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = "\uD83D\uDCCD", fontSize = 14.sp)
                    Spacer(Modifier.width(8.dp))
                    BasicTextField(
                        value = state.searchQuery,
                        onValueChange = { onIntent(RideIntent.SetSearchQuery(it)) },
                        textStyle = TextStyle(
                            color = Color.White,
                            fontSize = 14.sp,
                        ),
                        cursorBrush = SolidColor(RideColors.Cyan),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester),
                        decorationBox = { inner ->
                            if (state.searchQuery.isEmpty()) {
                                Text(
                                    text = "Where to?",
                                    color = RideColors.TextHint,
                                    fontSize = 14.sp,
                                )
                            }
                            inner()
                        },
                    )

                    // Clear button
                    AnimatedVisibility(
                        visible = isSearching,
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f))
                                .clickable { onIntent(RideIntent.SetSearchQuery("")) },
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "\u2715",
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 11.sp,
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // --- Saved places row ---
        AnimatedVisibility(
            visible = !isSearching,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    state.savedPlaces.forEach { place ->
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(28.dp))
                                .background(RideColors.SurfaceWhite7)
                                .border(
                                    1.dp,
                                    Color.White.copy(alpha = 0.08f),
                                    RoundedCornerShape(28.dp),
                                )
                                .clickable { onIntent(RideIntent.SetDestination(place)) }
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(text = place.icon, fontSize = 16.sp)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = place.name,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                            )
                        }
                    }
                }

                Spacer(Modifier.height(14.dp))

                // "Set on map" option
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(RideColors.SurfaceWhite4)
                        .clickable { /* TODO: map pick mode */ }
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(RideColors.Purple.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = "\uD83D\uDDFA\uFE0F", fontSize = 16.sp)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Set location on map",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                        )
                        Text(
                            text = "Tap to drop a pin",
                            color = RideColors.TextTertiary,
                            fontSize = 12.sp,
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))
            }
        }

        HorizontalDivider(color = Color.White.copy(alpha = 0.06f), thickness = 1.dp)

        // --- Results list ---
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
        ) {
            // Section header
            item {
                Text(
                    text = if (isSearching) "RESULTS" else "SUGGESTED",
                    color = RideColors.TextLabel,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.2.sp,
                    modifier = Modifier.padding(top = 14.dp, bottom = 10.dp),
                )
            }

            // No results state
            if (isSearching && searchResults?.isEmpty() == true) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(text = "\uD83D\uDD0D", fontSize = 36.sp)
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = "No places found",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Try a different search term",
                            color = RideColors.TextTertiary,
                            fontSize = 13.sp,
                        )
                    }
                }
            }

            // Results or suggested
            val displayPlaces = searchResults ?: state.suggestedPlaces
            items(displayPlaces, key = { it.id }) { place ->
                PlaceResultRow(
                    place = place,
                    onClick = { onIntent(RideIntent.SetDestination(place)) },
                )
            }

            // Bottom padding
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun PlaceResultRow(
    place: Place,
    onClick: () -> Unit,
) {
    val eta = remember(place.lat, place.lng) { estimateMinutes(place.lat, place.lng) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White.copy(alpha = 0.06f)),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = place.icon, fontSize = 20.sp)
        }

        Spacer(Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
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

        // ETA chip
        if (place.lat != 0.0) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(RideColors.SurfaceWhite4)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Text(
                    text = "${eta} min",
                    color = RideColors.TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

/** Rough driving time estimate from SF center (37.7749, -122.4194). */
private fun estimateMinutes(destLat: Double, destLng: Double): Int {
    val dlat = destLat - 37.7749
    val dlng = destLng - (-122.4194)
    val km = sqrt(dlat * dlat + dlng * dlng) * 111.0
    return (km * 3.2).toInt().coerceAtLeast(3)
}
