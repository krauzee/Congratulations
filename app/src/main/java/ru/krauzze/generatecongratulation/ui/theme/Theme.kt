package ru.krauzze.generatecongratulation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ru.krauzze.generatecongratulation.util.DataStore

private val DarkColorPalette = darkColors(
    primary = Primary_dark_theme,
    secondary = Primary_dark_theme,
    onSurface = TextLight_Dark,
    background = Background_dark_theme,
    secondaryVariant = Primary_dark_theme,
//    surface = Primary_dark_theme, //Green theme indicator
//    error = Primary_dark_pink_theme //Purple theme indicator
)

private val LightColorPalette = lightColors(
    primary = Primary_light_theme,
    secondary = Primary_light_theme,
    onSurface = TextLight_Light,
    background = Background_light_theme,
    secondaryVariant = Primary_light_theme,
//    surface = Primary_light_theme, //Green theme indicator
//    error = Primary_light_pink_theme //Purple theme indicator

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

private val DarkPinkColorPalette = darkColors(
    primary = Primary_dark_pink_theme,
    secondary = Primary_dark_pink_theme,
    onSurface = TextLight_Dark,
    background = Background_dark_theme,
    secondaryVariant = Primary_dark_pink_theme,
//    surface = Primary_dark_theme, //Green theme indicator
//    error = Primary_dark_pink_theme //Purple theme indicator
)

private val LightPinkColorPalette = lightColors(
    primary = Primary_light_pink_theme,
    secondary = Primary_light_pink_theme,
    onSurface = TextLight_Light,
    background = Background_light_theme,
    secondaryVariant = Primary_light_pink_theme,
//    surface = Primary_light_theme, //Green theme indicator
//    error = Primary_light_pink_theme //Purple theme indicator
)

@Composable
fun purpleIndicatorColor(): Color {
    return if (isSystemInDarkTheme()) Primary_dark_pink_theme else Primary_light_pink_theme
}

@Composable
fun greenIndicatorColor(): Color {
    return if (isSystemInDarkTheme()) Primary_dark_theme else Primary_light_theme
}

@Composable
fun GenerateCongratulationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themeColorSiGreen: Boolean = DataStore.AppConfig.themeColorIsGreen,
    //themeColorSiGreen: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        if (themeColorSiGreen)
            DarkColorPalette
        else
            DarkPinkColorPalette
    } else {
        if (themeColorSiGreen)
            LightColorPalette
        else
            LightPinkColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}