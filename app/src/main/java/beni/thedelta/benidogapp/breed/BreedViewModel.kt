package beni.thedelta.benidogapp.breed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.concurrent.Executors

class BreedViewModel : ViewModel() {

    private val _breedViewState = MutableLiveData<BreedViewState>()

    val breedViewState: LiveData<BreedViewState> = _breedViewState

    fun load() {
        _breedViewState.value = BreedViewState.Loading

        //Load Breed From Internet
        BreedRepository.load{ result ->
            // Log.i("Result", "Found ${result::class.java.simpleName} ")

            when (result) {
                is BreedResult.Content -> {
                    // Log.i("Result", "Found ${result.breeds.size} ")
                    _breedViewState.value = BreedViewState.Content(result.breeds)
                }
                is BreedResult.Error -> {
                    // Log.e("Breed Result", "ERROR")
                    result.throwable.printStackTrace()
                    _breedViewState.value = BreedViewState.Error
                }
            }
        }
    }
}