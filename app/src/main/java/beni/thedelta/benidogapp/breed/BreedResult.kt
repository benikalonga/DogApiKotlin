package beni.thedelta.benidogapp.breed

sealed class BreedResult {
    data class Content(val breeds: List<Breed>) : BreedResult()
    data class Error(val throwable: Throwable) : BreedResult()
}