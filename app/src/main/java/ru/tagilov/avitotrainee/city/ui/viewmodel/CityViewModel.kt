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

    private val mEntry: BehaviorSubject<String> = BehaviorSubject.create()

    private val mScreenState: BehaviorSubject<CityState> = BehaviorSubject.create()
    val screenState: Observable<CityState>
        get() = mScreenState.hide()

    private val mSearchCityList: BehaviorSubject<List<CityModel>> = BehaviorSubject.create()
    val searchCityList: Observable<List<CityModel>>
        get() = mSearchCityList.hide()

    private val mSavedCities: BehaviorSubject<List<CityModel>> = BehaviorSubject.create()
    val savedCities: Observable<List<CityModel>>
        get() = mSavedCities.hide()

    private val mSearchFocused: BehaviorSubject<Boolean> = BehaviorSubject.create()
    val searchFocused: Observable<Boolean>
        get() = mSearchFocused.hide()


    fun newEntry(s: String) {
        mEntry.onNext(s)
    }

    fun newSearchFocus(isFocused: Boolean) {
        mSearchFocused.onNext(isFocused)
        mSearchCityList.onNext(emptyList())
        mScreenState.onNext(CityState.Saved)
    }

    fun retry() {
        val q = mEntry.value
        if (q != null) search(q)
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
        mScreenState.onNext(CityState.Search.Error)
    }

    private fun search(query: String) {
        disposables += cityRepository.searchCitiesRx(query).subscribe(
            { cities ->
                Timber.d("Rxjava  ok: $cities")
                when (cities) {
                    is TypedResult.Err -> handleError()
                    is TypedResult.Ok -> {
                        if (cities.result.isEmpty())
                            mScreenState.onNext(CityState.Search.Empty)
                        else {
                            mSearchCityList.onNext(cities.result)
                            mScreenState.onNext(CityState.Search.Content)
                        }
                    }
                }
            },
            { handleError(it) }
        )
    }

    init {
        disposables += cityRepository.savedCities
            .subscribe { if (it is TypedResult.Ok) mSavedCities.onNext(it.result) }

        disposables += mEntry
            .doOnEach {
                mScreenState.onNext(
                    if (it.value == "") CityState.Saved else CityState.Search.Loading
                )
            }
            .debounce(500, TimeUnit.MILLISECONDS)
            .filter { it != "" }
            .subscribe { search(it) }
    }
}