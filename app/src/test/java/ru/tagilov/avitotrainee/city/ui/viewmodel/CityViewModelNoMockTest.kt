package ru.tagilov.avitotrainee.city.ui.viewmodel

import io.qameta.allure.kotlin.Epic
import io.qameta.allure.kotlin.junit4.AllureRunner
import io.qameta.allure.kotlin.junit4.DisplayName
import io.qameta.allure.kotlin.junit4.Tag
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.tagilov.avitotrainee.city.data.CityRepository
import ru.tagilov.avitotrainee.city.ui.entity.CityModel
import ru.tagilov.avitotrainee.city.ui.screen.CityState
import ru.tagilov.avitotrainee.core.db.SavedCity
import ru.tagilov.avitotrainee.core.util.TypedResult

@RunWith(AllureRunner::class)
@Epic("City")
@DisplayName("ViewModel logic test (on mocks)")
@Tag("Unit test")
@OptIn(ExperimentalCoroutinesApi::class)
class CityViewModelNoMockTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var vm: CityViewModel

    private lateinit var repo: CityRepository

    private fun initVm() {
        vm = CityViewModel(repo)
    }

    private fun initRepo(
        searchCities: (String) -> Flow<TypedResult<List<CityModel>>> = { flow { } },
        savedCities: Flow<List<CityModel>> = flow { }
    ) {
        repo = object : CityRepository {
            override fun searchCities(query: String): Flow<TypedResult<List<CityModel>>> =
                searchCities(query)

            override val savedCities: Flow<List<CityModel>> = savedCities

            override suspend fun deleteFromSaved(savedCity: SavedCity) = Unit

        }
    }

    @Test
    fun `initial screen state`() = runTest {
        initRepo()
        initVm()

        val screenState = vm.screenStateFlow.first()

        assert(screenState == CityState.Saved.Empty)
    }

    @Test
    fun `city search -- state is changed to loading, then to content`() = runTest {
        val city = randomCity()
        initRepo(searchCities = { flowOf(TypedResult.Ok(listOf(city))) })
        initVm()

        vm.newEntry("blabla")
        val loadingResult = vm.screenStateFlow.drop(1).first()
        val contentResult = vm.screenStateFlow.drop(1).first()

        assert(loadingResult is CityState.Search.Loading)
        assert(contentResult is CityState.Search.Content)
        (contentResult as CityState.Search.Content).let {
            assert(it.list.size == 1)
            assert(it.list[0] == city)
        }
    }

    @Test
    fun `search retry with error -- state is changed to error`() = runTest {
        initRepo(searchCities = { flowOf(TypedResult.Err()) })
        initVm()

        val state = vm.screenStateFlow
        state.drop(1)
        vm.newEntry("blabla")
        state.drop(2)
        vm.retry()
        val contentResult = state.drop(1).first()

        assert(contentResult is CityState.Search.Error)
    }

    @Test
    fun `city search -- error result`() = runTest {
        initRepo(searchCities = { flowOf(TypedResult.Err()) })
        initVm()

        vm.newEntry("blabla")
        val loadingResult = vm.screenStateFlow.drop(1).first()
        val contentResult = vm.screenStateFlow.drop(1).first()

        assert(loadingResult is CityState.Search.Loading)
        assert(contentResult is CityState.Search.Error)
    }

    @Test
    fun `new focus state method invoke -- state is changed`() = runTest {
        initRepo()
        initVm()
        vm.newSearchFocus(true)

        val initialSearchFocus = vm.searchFocusedFlow.first()
        val resultSearchFocus = vm.searchFocusedFlow.drop(1).first()

        assert(!initialSearchFocus)
        assert(resultSearchFocus)
    }

    @Test
    fun `loaded saved cities -- state is changed to saved cities`() = runTest {
        val city1 = randomCity()
        val city2 = randomCity()
        initRepo(savedCities = flowOf(listOf(city1, city2)))
        initVm()

        val resultSavedContent = vm.screenStateFlow.drop(1).first()

        assert(resultSavedContent is CityState.Saved.Content)
        (resultSavedContent as CityState.Saved.Content).list.let {
            assert(it.size == 2)
            assert(it[0] == city1)
            assert(it[1] == city2)
        }
    }

    @Test
    fun `delete from loaded saved cities -- state is changed to saved cities`() = runTest {
        val city1 = randomCity()
        val city2 = randomCity()
        initRepo(savedCities = flow {
            emit(listOf(city1, city2))
            delay(1000)
            emit(listOf(city1))
        })
        initVm()

        vm.delete(city1)
        val resultSavedAfterDeletionContent = vm.screenStateFlow.drop(2).first()

        assert(resultSavedAfterDeletionContent is CityState.Saved.Content)
        (resultSavedAfterDeletionContent as CityState.Saved.Content).list.let {
            assert(it.size == 1)
            assert(it[0] == city1)
        }
    }

    @Test
    fun `search after loaded saved cities -- state is changed to search cities`() = runTest {
        val savedCity1 = randomCity()
        val savedCity2 = randomCity()
        val searchCity1 = randomCity()
        val searchCity2 = randomCity()
        val savedCitiesFlow = flowOf(listOf(savedCity1, savedCity2))
        val searchCitiesFlow = flowOf(
            TypedResult.Ok(
                listOf(
                    searchCity1,
                    searchCity2,
                )
            )
        )
        initRepo(savedCities = savedCitiesFlow, searchCities = { searchCitiesFlow })
        initVm()

        vm.newEntry("search cities")
        val resultLoadingSearch = vm.screenStateFlow.drop(2).first()
        val resultContentSearch = vm.screenStateFlow.drop(1).first()

        assert(resultLoadingSearch is CityState.Search.Loading)
        assert(resultContentSearch is CityState.Search.Content)
        (resultContentSearch as CityState.Search.Content).list.let {
            assert(it.size == 2)
            assert(it[0] == searchCity1)
            assert(it[1] == searchCity2)
        }
    }

    private fun randomCity(): CityModel =
        CityModel(
            id = randomString(),
            name = randomString(),
            lat = randomDouble(),
            lon = randomDouble(),
            countryCode = randomString()
        )

}
