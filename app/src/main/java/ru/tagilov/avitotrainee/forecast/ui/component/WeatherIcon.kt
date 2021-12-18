package ru.tagilov.avitotrainee.forecast.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import ru.tagilov.avitotrainee.R
import ru.tagilov.avitotrainee.forecast.ui.theme.AvitoTheme
import ru.tagilov.avitotrainee.forecast.ui.util.shimmerRound

@ExperimentalCoilApi
@Composable
fun WeatherIcon(
    icon: String,
    size: Dp = 60.dp
) {
    //Подгрузка с API связана с тем, что могут, например,
    // добавить новые изображения или изменить их. (а так можно было скачать)
    val contentPainter = rememberImagePainter(
        data = "https://openweathermap.org/img/wn/$icon@4x.png",
    ) {
        CircleCropTransformation()
    }
    val errorPainter = painterResource(id = R.drawable.error)
    val isLoading = contentPainter.state is ImagePainter.State.Loading
    val isError = contentPainter.state is ImagePainter.State.Error
    val isSkeleton = icon == ""
    Image(
        painter = if (isError) errorPainter else contentPainter,
        contentDescription = "weather image",
        modifier = Modifier
            .size(size)
            .padding(if (isError) 8.dp else 0.dp)
            .clip(CircleShape)
            .shimmerRound(isLoading || isSkeleton),
        contentScale = ContentScale.Crop,
    )
}

@ExperimentalCoilApi
@Preview
@Composable
private fun WeatherIconPreview() {
    AvitoTheme {
        Column {
            WeatherIcon(icon = "13n")
            WeatherIcon(icon = "02d")
            WeatherIcon(icon = "10d")
            WeatherIcon(icon = "")
        }
    }
}
