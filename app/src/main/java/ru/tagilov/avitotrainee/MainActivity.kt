package ru.tagilov.avitotrainee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import coil.annotation.ExperimentalCoilApi
import ru.tagilov.avitotrainee.ui.component.DailyPreview
import ru.tagilov.avitotrainee.ui.component.HourlyPreview
import ru.tagilov.avitotrainee.ui.component.WeatherIcon
import ru.tagilov.avitotrainee.ui.theme.AvitoTheme


class MainActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    @ExperimentalCoilApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AvitoTheme {
                // A surface container using the 'background' color from the theme
                LazyColumn(
                    modifier = Modifier
                        .background(color = MaterialTheme.colors.background)
                ) {
                    item { HourlyPreview() }
                    item { DailyPreview() }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AvitoTheme {
        Greeting("Android")
    }
}