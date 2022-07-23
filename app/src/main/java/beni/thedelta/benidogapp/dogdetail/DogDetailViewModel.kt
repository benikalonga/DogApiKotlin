package beni.thedelta.benidogapp.dogdetail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import beni.thedelta.benidogapp.config.RetrofitImage
import beni.thedelta.benidogapp.plus.Utils


class DogDetailViewModel : ViewModel() {

    private val _saveViewState = MutableLiveData<DogDetailViewState>()
    val saveViewState: LiveData<DogDetailViewState> = _saveViewState

    private val _shareViewState = MutableLiveData<DogDetailViewState>()
    val shareViewState: LiveData<DogDetailViewState> = _shareViewState

    fun savePhoto(url: String) {

        _saveViewState.postValue(DogDetailViewState.Loading)

        RetrofitImage.getBitmapFrom(url) {
            // "it" - the bitmap
            if (it != null) {
                Utils.saveImage(it, url.substring(url.lastIndexOf("/") + 1)) { result ->
                    _saveViewState.postValue(if (result) DogDetailViewState.Success else DogDetailViewState.Error)
                }
            } else {
                _saveViewState.postValue(DogDetailViewState.Error)
            }
        }
    }

    //Sharing photo
    fun sharePhoto(context: Context, url: String) {

        _shareViewState.postValue(DogDetailViewState.Loading)

        RetrofitImage.getBitmapFrom(url) {
            // "it" - the bitmap

            _shareViewState.postValue(DogDetailViewState.Success)

            val bitmapPath: String = MediaStore.Images.Media.insertImage(
                context.contentResolver,
                it,
                "palette",
                "share palette"
            )
            val bitmapUri: Uri = Uri.parse(bitmapPath)

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/png"
            intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
            context.startActivity(Intent.createChooser(intent, "Share Image"))
        }
    }
}

