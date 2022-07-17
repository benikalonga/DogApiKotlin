package beni.thedelta.benidogapp.dog

sealed class DogViewState {
    object Loading : DogViewState()
    data class Content(val dogs: List<String>) : DogViewState()
    object Error : DogViewState()
}