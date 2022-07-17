package beni.thedelta.benidogapp.dogdetail

import android.R.attr.bitmap
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import beni.thedelta.benidogapp.plus.Utils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition


class DogDetailViewModel : ViewModel() {

    private fun getBitmap(context : Context, url: String, function: (Bitmap) -> Unit){
        Glide.with(context)
            .asBitmap()
            .load(url)
            .into(object : SimpleTarget<Bitmap?>(100, 100) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?
                ) {
                    function.invoke(resource)
                }
            })
    }

    fun savePhoto(context : Context, url: String, function: (Boolean) -> Unit) {
        getBitmap(context, url){
            Utils.saveImage(it, url.substring(url.lastIndexOf("/")), function)
        }
    }

    fun sharePhoto(context : Context, url: String) {
        getBitmap(context, url){

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
            context.startActivity(Intent.createChooser(intent, "Share"))
        }
    }

}