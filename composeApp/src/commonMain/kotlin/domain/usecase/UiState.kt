package domain.usecase

sealed class UiState<out T>{
    data class SUCCESS<T>(val response: T): UiState<T>()
    data class ERROR(val throwable: Throwable): UiState<Nothing>()
    data object LOADING: UiState<Nothing>()
}
