package ru.tagilov.avitotrainee.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

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
