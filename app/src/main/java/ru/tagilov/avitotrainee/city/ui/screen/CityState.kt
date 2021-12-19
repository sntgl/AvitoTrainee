package ru.tagilov.avitotrainee.city.ui.screen

sealed class CityState {
    object None : CityState()
    object Saved : CityState()
    sealed class Search: CityState() {
        object Error : Search()
        object Loading : Search()
        object Empty : Search()
        object Content : Search()
    }
}