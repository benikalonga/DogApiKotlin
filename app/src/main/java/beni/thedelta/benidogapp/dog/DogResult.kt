package beni.thedelta.benidogapp.dog

sealed class DogResult {
    data class Content(val breeds: List<String>) : DogResult()
    data class Error(val throwable: Throwable) : DogResult()
}