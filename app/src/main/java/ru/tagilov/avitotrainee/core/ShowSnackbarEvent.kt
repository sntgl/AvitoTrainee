package ru.tagilov.avitotrainee.core

//просто ивент, каждый инстанс которого не равен любым другим инстансам
sealed class SnackbarEvent {
    class Show(
        val state: SnackBarMessage
    ) : SnackbarEvent() {
        override fun equals(other: Any?): Boolean {
            return other === this
        }

        override fun hashCode(): Int {
            return javaClass.hashCode()
        }
    }

    class Empty : SnackbarEvent()
}


enum class SnackBarMessage {
    UNABLE_LOAD, UNABLE_SAVE
}

