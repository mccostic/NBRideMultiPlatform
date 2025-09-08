package org.example.project.nbride.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Typography


@Composable
fun NBRideTypography(): Typography {
    val poppins = PoppinsFontFamily()

    return Typography(
        // Headlines
        headlineLarge = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            lineHeight = 40.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp,
            lineHeight = 36.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = 32.sp
        ),

        // Titles
        titleLarge = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 22.sp,
            lineHeight = 28.sp
        ),
        titleMedium = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 24.sp
        ),
        titleSmall = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp
        ),

        // Body
        bodyLarge = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp
        ),
        bodySmall = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.Light,
            fontSize = 12.sp,
            lineHeight = 16.sp
        ),

        // Labels (buttons, chips, etc.)
        labelLarge = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 20.sp
        ),
        labelMedium = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = 16.sp
        ),
        labelSmall = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            lineHeight = 16.sp
        )
    )
}
