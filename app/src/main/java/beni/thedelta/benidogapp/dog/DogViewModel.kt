package beni.thedelta.benidogapp.dog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DogViewModel : ViewModel() {

    private val _dogViewState = MutableLiveData<DogViewState>()

    val dogViewState: LiveData<DogViewState> = _dogViewState

    fun load(breed: String) {

        _dogViewState.value = DogViewState.Loading

        //Load Breed From Internet
        try {
            viewModelScope.launch {
                val dogs = RetrofitDog.service.getAllDogs(breed)
                _dogViewState.postValue(DogViewState.Content(dogs.imageUrls!!))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _dogViewState.value = DogViewState.Error
        }
    }
}