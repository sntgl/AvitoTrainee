package ru.tagilov.avitotrainee.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondaryVariant = Gray200,
    secondary = Gray50,
    background = Gray700,
    onBackground = Gray500,
    surface = Gray650,
    onSurface = Gray550,
    error = ErrorRed,
    onError = ErrorDark,
)

private val LightColorPalette = lightColors(

    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Gray700,
    secondaryVariant = Gray500,
    background = Gray100,
    onBackground = Gray200,
    surface = Gray50,
    onSurface = Gray150,
    error = ErrorRed,
    onError = ErrorBright,
)

@Composable
fun AvitoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
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

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(color=MaterialTheme.colors.primary)
}
