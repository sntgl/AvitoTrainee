package ru.tagilov.avitotrainee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import coil.annotation.ExperimentalCoilApi
import ru.tagilov.avitotrainee.ui.component.WeatherIcon
import ru.tagilov.avitotrainee.ui.theme.AvitoTheme

class MainActivity : ComponentActivity() {
    @ExperimentalCoilApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AvitoTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
//                    Column {
//                        WeatherIcon(icon = "13n")
//                        WeatherIcon(icon = "02d")
//                        WeatherIcon(icon = "02d")
//                        WeatherIcon(icon = "")
//                    }
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