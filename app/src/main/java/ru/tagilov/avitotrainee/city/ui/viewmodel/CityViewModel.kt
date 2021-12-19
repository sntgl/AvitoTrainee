package ru.tagilov.avitotrainee.city.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.tagilov.avitotrainee.city.data.CityRepository
import ru.tagilov.avitotrainee.city.ui.entity.City
import ru.tagilov.avitotrainee.city.ui.screen.CityState
import timber.log.Timber

@FlowPreview
class CityViewModel : ViewModel() {
    private val cityRepository = CityRepository()

    private val entryMutableStateFlow = MutableStateFlow("")
    private val searchMutableStateFlow = MutableStateFlow("")

    private val screenStateMutableFlow = MutableStateFlow<CityState>(CityState.None)
    val screenStateFlow: StateFlow<CityState>
        get() = screenStateMutableFlow

    private val searchCityListMutableFlow = MutableStateFlow<List<City>?>(null)
    val searchCityListFlow: StateFlow<List<City>?>
        get() = searchCityListMutableFlow

    fun newEntry(s: String) {
        Timber.d("New search bar text = $s")
        viewModelScope.launch {
            entryMutableStateFlow.emit(s)
        }
    }


    private var currentSearchJob: Job? = null
    private fun search(query: String){
        currentSearchJob?.cancel()
        currentSearchJob = viewModelScope.launch {
            screenStateMutableFlow.emit(CityState.Search.Loading)
            cityRepository.searchCities(query).collect { cities ->
                Timber.d("$cities")
                when {
                    cities == null -> {
                        screenStateMutableFlow.emit(CityState.Search.Error)
                    }
                    cities.isEmpty() -> {
                        screenStateMutableFlow.emit(CityState.Search.Empty)
                    }
                    else -> {
                        screenStateMutableFlow.emit(CityState.Search.Content)
                        searchCityListMutableFlow.emit(cities)
                    }
                }
            }
            currentSearchJob = null
        }
    }

    init {
        entryMutableStateFlow
            .debounce(500)
            .onEach {
                searchMutableStateFlow.emit(it)
            }
            .launchIn(viewModelScope)

        searchMutableStateFlow
            .onEach {
                if (it != "")
                    search(it)
            }
            .launchIn(viewModelScope)

        searchCityListMutableFlow
            .onEach {
                Timber.d("Response cities = $it")
            }.launchIn(viewModelScope)
    }
}