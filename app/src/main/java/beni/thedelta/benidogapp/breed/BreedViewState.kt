package beni.thedelta.benidogapp.breed

sealed class BreedViewState {
    object Loading : BreedViewState()
    data class Content(val breeds: List<Breed>) : BreedViewState()
    object Error : BreedViewState()
}