package beni.thedelta.benidogapp.dog

sealed class DogViewState {
    object Loading : DogViewState()
    data class Content(val dogs: List<Dog>) : DogViewState()
    object Error : DogViewState()
}