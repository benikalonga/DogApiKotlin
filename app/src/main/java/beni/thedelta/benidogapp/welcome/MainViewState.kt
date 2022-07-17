package beni.thedelta.benidogapp.welcome

sealed class MainViewState {
    object Loading : MainViewState()
    data class ViewState(val state : String) : MainViewState()
    object Error : MainViewState()
}