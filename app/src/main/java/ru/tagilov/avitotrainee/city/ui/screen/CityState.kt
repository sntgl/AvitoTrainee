package ru.tagilov.avitotrainee.city.ui.screen

import ru.tagilov.avitotrainee.city.ui.entity.CityModel

sealed interface CityState {

    sealed interface Saved: CityState {
        object Empty : Saved
        data class Content(override val list: List<CityModel>) : Saved, ListState
    }

    sealed interface Search: CityState {
        data class Content(override val list: List<CityModel>) : Search, ListState
        object Empty : Search
        object Error : Search
        object Loading : Search
    }
}

interface ListState {
    val list: List<CityModel>
}