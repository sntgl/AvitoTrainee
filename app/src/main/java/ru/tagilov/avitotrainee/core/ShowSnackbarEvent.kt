package ru.tagilov.avitotrainee.core

//просто ивент, каждый инстанс которого не равен любым другим инстансам
class ShowSnackbarEvent {
    override fun equals(other: Any?): Boolean {
        return other === this
    }
    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}