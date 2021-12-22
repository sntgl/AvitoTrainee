package ru.tagilov.avitotrainee.city.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.FlowPreview
import ru.tagilov.avitotrainee.R
import ru.tagilov.avitotrainee.city.ui.component.Cities
import ru.tagilov.avitotrainee.city.ui.component.CityLoadError
import ru.tagilov.avitotrainee.city.ui.component.EmptySearch
import ru.tagilov.avitotrainee.city.ui.component.SearchBar
import ru.tagilov.avitotrainee.city.ui.viewmodel.CityViewModel

@ExperimentalMaterialApi
@FlowPreview
@ExperimentalAnimationApi
@Composable
fun City(
    navController: NavController,
) {
    val searchBarState = remember { mutableStateOf(TextFieldValue()) }
    val vm: CityViewModel = viewModel()
    val screenState = remember { vm.screenStateFlow }.collectAsState()
    val loadedCities = remember { vm.searchCityListFlow }.collectAsState()
    val savedCities = remember { vm.savedCitiesFlow }.collectAsState()

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
            isFocused = vm.searchFocusedFlow,
            onFocusChanged = { vm.newSearchFocus(it) }
        )
        when (screenState.value) {
            CityState.None -> {

            }
            CityState.Saved -> {
                Cities(
                    cities = savedCities.value,
                    navController = navController,
                    title = stringResource(id = R.string.saved_cities),
                    dismissible = true,
                    onDismiss = {vm.delete(it)}
                )
            }
            CityState.Search.Content -> {
                Cities(
                    cities = loadedCities.value,
                    navController = navController,
                    title = stringResource(id = R.string.searched_cities),
                    dismissible = false,
                    onDismiss = {  }
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
                    dismissible = false,
                    onDismiss = { }
                )
            }
        }
    }
}