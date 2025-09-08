package org.example.project.nbride.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import nbridemultiplatform.composeapp.generated.resources.Res
import nbridemultiplatform.composeapp.generated.resources.poppins_black
import nbridemultiplatform.composeapp.generated.resources.poppins_blackitalic
import nbridemultiplatform.composeapp.generated.resources.poppins_bold
import nbridemultiplatform.composeapp.generated.resources.poppins_bolditalic
import nbridemultiplatform.composeapp.generated.resources.poppins_extrabold
import nbridemultiplatform.composeapp.generated.resources.poppins_extrabolditalic
import nbridemultiplatform.composeapp.generated.resources.poppins_extralight
import nbridemultiplatform.composeapp.generated.resources.poppins_extralightitalic
import nbridemultiplatform.composeapp.generated.resources.poppins_italic
import nbridemultiplatform.composeapp.generated.resources.poppins_light
import nbridemultiplatform.composeapp.generated.resources.poppins_lightitalic
import nbridemultiplatform.composeapp.generated.resources.poppins_medium
import nbridemultiplatform.composeapp.generated.resources.poppins_mediumitalic
import nbridemultiplatform.composeapp.generated.resources.poppins_regular
import nbridemultiplatform.composeapp.generated.resources.poppins_semibold
import nbridemultiplatform.composeapp.generated.resources.poppins_semibolditalic
import nbridemultiplatform.composeapp.generated.resources.poppins_thin
import nbridemultiplatform.composeapp.generated.resources.poppins_thinitalic
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font


@OptIn(ExperimentalResourceApi::class)
@Composable
fun PoppinsFontFamily(): FontFamily = FontFamily(
    // Thin
    Font(Res.font.poppins_thin, weight = FontWeight.Thin),
    Font(Res.font.poppins_thinitalic, weight = FontWeight.Thin, style = FontStyle.Italic),

    // ExtraLight
    Font(Res.font.poppins_extralight, weight = FontWeight.ExtraLight),
    Font(Res.font.poppins_extralightitalic, weight = FontWeight.ExtraLight, style = FontStyle.Italic),

    // Light
    Font(Res.font.poppins_light, weight = FontWeight.Light),
    Font(Res.font.poppins_lightitalic, weight = FontWeight.Light, style = FontStyle.Italic),

    // Regular
    Font(Res.font.poppins_regular, weight = FontWeight.Normal),
    Font(Res.font.poppins_italic, weight = FontWeight.Normal, style = FontStyle.Italic),

    // Medium
    Font(Res.font.poppins_medium, weight = FontWeight.Medium),
    Font(Res.font.poppins_mediumitalic, weight = FontWeight.Medium, style = FontStyle.Italic),

    // SemiBold
    Font(Res.font.poppins_semibold, weight = FontWeight.SemiBold),
    Font(Res.font.poppins_semibolditalic, weight = FontWeight.SemiBold, style = FontStyle.Italic),

    // Bold
    Font(Res.font.poppins_bold, weight = FontWeight.Bold),
    Font(Res.font.poppins_bolditalic, weight = FontWeight.Bold, style = FontStyle.Italic),

    // ExtraBold
    Font(Res.font.poppins_extrabold, weight = FontWeight.ExtraBold),
    Font(Res.font.poppins_extrabolditalic, weight = FontWeight.ExtraBold, style = FontStyle.Italic),

    // Black
    Font(Res.font.poppins_black, weight = FontWeight.Black),
    Font(Res.font.poppins_blackitalic, weight = FontWeight.Black, style = FontStyle.Italic),
)