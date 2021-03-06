package ru.tagilov.avitotrainee.city.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.tagilov.avitotrainee.city.data.CityRepository
import ru.tagilov.avitotrainee.city.ui.entity.CityModel
import ru.tagilov.avitotrainee.city.ui.entity.toSaved
import ru.tagilov.avitotrainee.city.ui.screen.CityState
import ru.tagilov.avitotrainee.core.util.TypedResult
import timber.log.Timber
import javax.inject.Inject

@OptIn(FlowPreview::class)
class CityViewModel @Inject constructor(
        private val cityRepository: CityRepository,
) : ViewModel() {

    private val entryMutableStateFlow = MutableStateFlow("")
    private val newSearchMutableStateFlow = MutableStateFlow("")

    private val screenStateMutableFlow = MutableStateFlow<CityState>(CityState.None)
    val screenStateFlow: StateFlow<CityState>
        get() = screenStateMutableFlow

    private val searchCityListMutableFlow = MutableStateFlow<List<CityModel>?>(null)
    val searchCityListFlow: StateFlow<List<CityModel>?>
        get() = searchCityListMutableFlow

    private val savedCitiesMutableFlow = MutableStateFlow<List<CityModel>?>(null)
    val savedCitiesFlow: StateFlow<List<CityModel>?>
        get() = savedCitiesMutableFlow

    fun newEntry(s: String) {
        viewModelScope.launch {
            entryMutableStateFlow.emit(s)
        }
    }

    private val searchFocusedMutableFlow = MutableStateFlow(false)
    val searchFocusedFlow: StateFlow<Boolean>
        get() = searchFocusedMutableFlow

    fun newSearchFocus(isFocused: Boolean) {
        viewModelScope.launch {
            searchFocusedMutableFlow.emit(isFocused)
            searchCityListMutableFlow.emit(null)
            screenStateMutableFlow.emit(CityState.Saved)
        }
    }

    private var currentSearchJob: Job? = null
    private fun search(query: String) {
        currentSearchJob?.cancel()
        currentSearchJob = viewModelScope.launch {
            screenStateMutableFlow.emit(CityState.Search.Loading)
            cityRepository.searchCities(query).collect { cities ->
                Timber.d("$cities")
                when (cities) {
                     is TypedResult.Err -> {
                        screenStateMutableFlow.emit(CityState.Search.Error)
                    }
                    is TypedResult.Ok -> {
                        if (cities.result.isEmpty())
                            screenStateMutableFlow.emit(CityState.Search.Empty)
                        else {
                            screenStateMutableFlow.emit(CityState.Search.Content)
                            searchCityListMutableFlow.emit(cities.result)
                        }
                    }
                }
            }
            currentSearchJob = null
        }
    }

    fun retry() {
        viewModelScope.launch {
            search(entryMutableStateFlow.value)
        }
    }

    fun delete(city: CityModel) {
        viewModelScope.launch {
            cityRepository.deleteFromSaved(city.toSaved())
        }
    }

    init {
        cityRepository.savedCities.onEach {
            savedCitiesMutableFlow.emit(it)
        }.launchIn(viewModelScope)

        entryMutableStateFlow
            .onEach {
                if (it == "") {
                    currentSearchJob?.cancel()
                    screenStateMutableFlow.emit(CityState.Saved)
                } else
                    screenStateMutableFlow.emit(CityState.Search.Loading)
            }
            .debounce(500)
            .onEach {
                newSearchMutableStateFlow.emit(it)
            }
            .launchIn(viewModelScope)

        newSearchMutableStateFlow
            .filter { it != "" }
            .onEach {
                search(it)
            }
            .launchIn(viewModelScope)

        searchCityListMutableFlow
            .onEach {
                Timber.d("Response cities = $it")
            }.launchIn(viewModelScope)

        screenStateFlow.onEach {
            Timber.d("New screen state = $it")
        }.launchIn(viewModelScope)

        screenStateMutableFlow
            .filter { it is CityState.Saved }
            .onEach { Timber.d("Show saved") }
            .launchIn(viewModelScope)
    }
}