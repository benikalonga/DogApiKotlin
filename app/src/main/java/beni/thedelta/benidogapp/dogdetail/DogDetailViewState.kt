package beni.thedelta.benidogapp.dogdetail

sealed class DogDetailViewState {
    object Loading : DogDetailViewState()
    object Success : DogDetailViewState()
    object Error : DogDetailViewState()
}