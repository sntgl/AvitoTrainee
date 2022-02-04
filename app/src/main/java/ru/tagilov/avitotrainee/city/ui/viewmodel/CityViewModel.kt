package ru.tagilov.avitotrainee.city.ui.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ru.tagilov.avitotrainee.city.data.CityRepository
import ru.tagilov.avitotrainee.city.ui.entity.CityModel
import ru.tagilov.avitotrainee.city.ui.entity.toSaved
import ru.tagilov.avitotrainee.city.ui.screen.CityState
import ru.tagilov.avitotrainee.core.util.TypedResult
import ru.tagilov.avitotrainee.core.util.plusAssign
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CityViewModel @Inject constructor(
    private val cityRepository: CityRepository,
) : ViewModel() {
    private val disposables = CompositeDisposable()

    private val entrySubject: BehaviorSubject<String> = BehaviorSubject.create()

    private val screenStateSubject: BehaviorSubject<CityState> = BehaviorSubject.create()
    val screenStateObservable: Observable<CityState>
        get() = screenStateSubject.hide()

    private val searchCityListSubject: BehaviorSubject<List<CityModel>> = BehaviorSubject.create()
    val searchCityListObservable: Observable<List<CityModel>>
        get() = searchCityListSubject.hide()

    private val savedCitiesSubject: BehaviorSubject<List<CityModel>> = BehaviorSubject.create()
    val savedCitiesObservable: Observable<List<CityModel>>
        get() = savedCitiesSubject.hide()

    private val searchFocusedSubject: BehaviorSubject<Boolean> = BehaviorSubject.create()
    val searchFocusedObservable: Observable<Boolean>
        get() = searchFocusedSubject.hide()


    fun newEntry(s: String) {
        entrySubject.onNext(s)
    }

    fun newSearchFocus(isFocused: Boolean) {
        searchFocusedSubject.onNext(isFocused)
        searchCityListSubject.onNext(emptyList())
        screenStateSubject.onNext(CityState.Saved)
    }

    fun retry() {
        entrySubject.value?.let { search(it) }
    }

    fun delete(city: CityModel) {
        disposables += cityRepository
            .deleteFromSavedRx(city.toSaved())
            .subscribe({}, {})
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    private fun handleError(throwable: Throwable? = null) {
        Timber.d("Rxjava err: $throwable")
        screenStateSubject.onNext(CityState.Search.Error)
    }

    private fun search(query: String) {
        disposables += cityRepository.searchCitiesRx(query).subscribe(
            { cities ->
                Timber.d("Rxjava  ok: $cities")
                when (cities) {
                    is TypedResult.Err -> handleError()
                    is TypedResult.Ok -> {
                        if (cities.result.isEmpty())
                            screenStateSubject.onNext(CityState.Search.Empty)
                        else {
                            searchCityListSubject.onNext(cities.result)
                            screenStateSubject.onNext(CityState.Search.Content)
                        }
                    }
                }
            },
            { handleError(it) }
        )
    }

    init {
        disposables += cityRepository.savedCities
            .subscribe { if (it is TypedResult.Ok) savedCitiesSubject.onNext(it.result) }

        disposables += entrySubject
            .doOnEach {
                screenStateSubject.onNext(
                    if (it.value == "") CityState.Saved else CityState.Search.Loading
                )
            }
            .debounce(500, TimeUnit.MILLISECONDS)
            .filter { it != "" }
            .subscribe { search(it) }
    }
}