package ru.tagilov.avitotrainee.city.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavController
import ru.tagilov.avitotrainee.R
import ru.tagilov.avitotrainee.city.ui.component.Cities
import ru.tagilov.avitotrainee.city.ui.component.CityLoadError
import ru.tagilov.avitotrainee.city.ui.component.CurrentCity
import ru.tagilov.avitotrainee.city.ui.component.EmptySearch
import ru.tagilov.avitotrainee.city.ui.component.SearchBar
import ru.tagilov.avitotrainee.city.ui.viewmodel.CityViewModel

@Composable
fun City(
    navController: NavController,
    vm: CityViewModel
) {
    val searchBarState = remember { mutableStateOf(TextFieldValue()) }
    val screenState = remember { vm.screenStateObservable }.subscribeAsState(initial = CityState.None)
    val loadedCities = remember { vm.searchCityListObservable }.subscribeAsState(initial = emptyList())
    val savedCities = remember { vm.savedCitiesObservable }.subscribeAsState(initial = emptyList())
    val searchFocused = remember { vm.searchFocusedObservable }.subscribeAsState(initial = false)

    BackHandler(enabled = screenState.value is CityState.Search) {
        vm.newSearchFocus(false)
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background),
    ) {
        SearchBar(
            state = searchBarState,
            textUpdated = { vm.newEntry(it) },
            focused = searchFocused,
            onFocusChanged = { vm.newSearchFocus(it) }
        )
        when (screenState.value) {
            CityState.None -> {
                CurrentCity(navController = navController)
            }
            CityState.Saved -> {
                Cities(
                    cities = savedCities.value,
                    navController = navController,
                    title = stringResource(id = R.string.saved_cities),
                    isLocal = true,
                    onDismiss = { vm.delete(it) }
                )
            }
            CityState.Search.Content -> {
                Cities(
                    cities = loadedCities.value,
                    navController = navController,
                    title = stringResource(id = R.string.searched_cities),
                    isLocal = false,
                    onDismiss = { }
                )
            }
            CityState.Search.Empty -> {
                EmptySearch()
            }
            CityState.Search.Error -> {
                CityLoadError {
                    vm.retry()
                }
            }
            CityState.Search.Loading -> {
                Cities(
                    cities = null,
                    navController = navController,
                    title = stringResource(id = R.string.searched_cities),
                    isLocal = false,
                    onDismiss = { }
                )
            }
        }
    }
}