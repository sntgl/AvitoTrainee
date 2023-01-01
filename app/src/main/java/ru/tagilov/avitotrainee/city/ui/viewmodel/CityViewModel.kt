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

    private val screenStateMutableFlow = MutableStateFlow<CityState>(CityState.Saved.Empty)
    val screenStateFlow: StateFlow<CityState>
        get() = screenStateMutableFlow

    private val searchFocusedMutableFlow = MutableStateFlow(false)
    val searchFocusedFlow: StateFlow<Boolean>
        get() = searchFocusedMutableFlow

    private val entryMutableStateFlow = MutableStateFlow("")
    private val newSearchMutableStateFlow = MutableStateFlow("")
    private val savedCitiesState = MutableStateFlow<List<CityModel>?>(null)

    init {
        cityRepository.savedCities.onEach {
            savedCitiesState.emit(it)
            if (screenStateMutableFlow.value is CityState.Saved) {
                screenStateMutableFlow.emit(CityState.Saved.Content(it))
            }
        }.launchIn(viewModelScope)

        entryMutableStateFlow
            .onEach {
                if (it == "") {
                    currentSearchJob?.cancel()
                    emitSavedOrEmptyState()
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

        screenStateFlow.onEach {
            Timber.d("New screen state = $it")
        }.launchIn(viewModelScope)

        screenStateMutableFlow
            .filter { it is CityState.Saved }
            .onEach { Timber.d("Show saved") }
            .launchIn(viewModelScope)
    }

    fun newEntry(s: String) {
        viewModelScope.launch {
            entryMutableStateFlow.emit(s)
        }
    }

    fun newSearchFocus(isFocused: Boolean) {
        viewModelScope.launch {
            searchFocusedMutableFlow.emit(isFocused)
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
                            screenStateMutableFlow.emit(CityState.Search.Content(cities.result))
                        }
                    }
                }
            }
            currentSearchJob = null
        }
    }


    private suspend fun emitSavedOrEmptyState() {
        screenStateMutableFlow.emit(
            savedCitiesState.value.let {
                if (it.isNullOrEmpty()) {
                    CityState.Saved.Empty
                } else {
                    CityState.Saved.Content(it)
                }
            }
        )
    }
}