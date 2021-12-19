package ru.tagilov.avitotrainee.city.ui.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.FlowPreview
import ru.tagilov.avitotrainee.city.ui.component.Cities
import ru.tagilov.avitotrainee.city.ui.component.SearchBar
import ru.tagilov.avitotrainee.city.ui.viewmodel.CityViewModel

@FlowPreview
@ExperimentalAnimationApi
@Composable
fun City(
    navController: NavController,
) {
    val searchBarState = remember{ mutableStateOf(TextFieldValue()) }
    val vm: CityViewModel = viewModel()
    val screenState = remember { vm.screenStateFlow }.collectAsState()
    val cities = remember { vm.searchCityListFlow }.collectAsState()

    Column(
        Modifier.background(color=MaterialTheme.colors.surface)
    ){
        SearchBar(state = searchBarState, textUpdated = {vm.newEntry(it)})
        if (screenState.value == CityState.Search.Content || screenState.value == CityState.Search.Loading)
            Cities(cities = cities.value, navController = navController)
    }
}