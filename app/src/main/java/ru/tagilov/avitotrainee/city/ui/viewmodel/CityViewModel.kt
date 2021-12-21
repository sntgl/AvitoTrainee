package ru.tagilov.avitotrainee.city.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.tagilov.avitotrainee.city.data.CityRepository
import ru.tagilov.avitotrainee.city.ui.entity.CityModel
import ru.tagilov.avitotrainee.city.ui.screen.CityState
import ru.tagilov.avitotrainee.core.db.Database
import ru.tagilov.avitotrainee.core.db.unwrap
import ru.tagilov.avitotrainee.core.routing.CityParcelable
import timber.log.Timber

@FlowPreview
class CityViewModel : ViewModel() {
    private val cityRepository = CityRepository()

    private val db = Database.instance.cityDao()

    private val entryMutableStateFlow = MutableStateFlow("")
    private val newSearchMutableStateFlow = MutableStateFlow("")

    private val screenStateMutableFlow = MutableStateFlow<CityState>(CityState.None)
    val screenStateFlow: StateFlow<CityState>
        get() = screenStateMutableFlow

    private val searchCityListMutableFlow = MutableStateFlow<List<CityModel>?>(null)
    val searchCityListFlow: StateFlow<List<CityModel>?>
        get() = searchCityListMutableFlow

    private val savedCitiesMutableFlow = MutableStateFlow<List<CityParcelable>?>(null)
    val savedCitiesFlow: StateFlow<List<CityParcelable>?>
        get() = savedCitiesMutableFlow

    fun newEntry(s: String) {
        Timber.d("New search bar text = $s")
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
//            if (isFocused == false) {
            searchCityListMutableFlow.emit(null)
            screenStateMutableFlow.emit(CityState.Saved)
//            }
        }
    }

    private var currentSearchJob: Job? = null
    private fun search(query: String) {
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

    fun retry() {
        viewModelScope.launch {
            search(entryMutableStateFlow.value)
        }
    }

//    private fun loadSavedCities() {
//        viewModelScope.launch {
//            savedCitiesMutableFlow.emit(db.getAll().map { it.unwrap() })
//        }
//    }

    init {
        db.getAll().onEach { newSavedList ->
            savedCitiesMutableFlow.emit(newSavedList.map{ it.unwrap() })
        }.launchIn(viewModelScope)

        entryMutableStateFlow
            .onEach {
                if (it == "")
                    screenStateMutableFlow.emit(CityState.Saved)
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