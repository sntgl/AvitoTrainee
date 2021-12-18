package ru.tagilov.avitotrainee.forecast.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondaryVariant = Gray200,
    secondary = Gray100,
    background = Gray700,
    onBackground = Gray500,
    //шиммер
    surface = Gray650,
    onSurface = Gray550,
    error = ErrorRed,
)

private val LightColorPalette = lightColors(

    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Gray700,
    secondaryVariant = Gray500,
    background = Gray100,
    onBackground = Gray200,
    //шиммер
    surface = Gray50,
    onSurface = Gray150,
    error = ErrorRed,


    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun AvitoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}