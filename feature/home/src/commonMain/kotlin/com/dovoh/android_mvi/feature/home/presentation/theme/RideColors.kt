package com.dovoh.android_mvi.feature.home.presentation.theme

import androidx.compose.ui.graphics.Color

object RideColors {
    // Background tones
    val MapDark1 = Color(0xFF0F1923)
    val MapDark2 = Color(0xFF131F2E)
    val MapDark3 = Color(0xFF0D1C29)
    val Background = Color(0xFF0A121C)
    val FrameBackground = Color(0xFF050A0F)

    // Accent - cyan/blue
    val Cyan = Color(0xFF00D4FF)
    val CyanDim = Color(0x2600D4FF)   // ~15% alpha
    val CyanSubtle = Color(0x1400D4FF) // ~8% alpha

    // Accent - purple
    val Purple = Color(0xFF7C3AED)
    val PurpleDim = Color(0x26124058)   // ~15% alpha

    // Map elements
    val GridLine = Color(0xFF4A9EFF)
    val Road = Color(0xFF2A6496)
    val RoadMajor = Color(0xFF1A4A6E)
    val CityBlock = Color(0x661E5078) // ~40% alpha
    val CityBlockBorder = Color(0x264A9EFF) // ~15% alpha

    // Surface / card
    val CardBackground = Color(0x0DFFFFFF) // ~5% white
    val CardBorder = Color(0x1AFFFFFF)     // ~10% white
    val SurfaceWhite4 = Color(0x0AFFFFFF) // ~4% white
    val SurfaceWhite7 = Color(0x12FFFFFF) // ~7% white
    val SurfaceWhite12 = Color(0x1FFFFFFF) // ~12% white
    val SurfaceWhite15 = Color(0x26FFFFFF) // ~15% white

    // Text
    val TextPrimary = Color(0xFFFFFFFF)
    val TextSecondary = Color(0x99FFFFFF) // ~60% white
    val TextTertiary = Color(0x66FFFFFF)  // ~40% white
    val TextHint = Color(0x80FFFFFF)      // ~50% white
    val TextLabel = Color(0x59FFFFFF)     // ~35% white

    // Status
    val Success = Color(0xFF4ADE80)
    val SuccessSubtle = Color(0x1A22C55E)
    val Error = Color(0xFFF87171)
    val ErrorSubtle = Color(0x1AEF4444)
    val ErrorBorder = Color(0x4DEF4444)
}
