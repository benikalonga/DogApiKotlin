package beni.thedelta.benidogapp.config

import RetrofitDog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

object RetrofitImage {

    fun getBitmapFrom(url: String, onComplete: (Bitmap?) -> Unit) {

        RetrofitDog.service.getImageData(url).enqueue(object : retrofit2.Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                onComplete(null)
            }

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                if (response == null || !response.isSuccessful || response.body() == null || response.errorBody() != null) {
                    onComplete(null)
                    return
                }
                val bytes = response.body()!!.bytes()
                onComplete(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
            }
        })
    }
}