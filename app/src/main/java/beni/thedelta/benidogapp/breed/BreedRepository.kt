package beni.thedelta.benidogapp.breed

import RetrofitDog
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object BreedRepository {

    fun load(bResult: (BreedResult) -> Unit) {

        val response = RetrofitDog.service.getAllBreads()

        response.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                val breeds = mutableListOf<Breed>()

                val res = JSONObject(response.body()!!.string()).getJSONObject("message")

                val i: Iterator<String> = res.keys()
                while (i.hasNext()) {

                    val key = i.next()

                    val subArray = res.getJSONArray(key)
                    val subList = List<String>(subArray.length()) {
                        subArray.getString(it)
                    }.joinToString()

                    breeds.add(Breed(designation = key,  list = subList))
                }

                bResult.invoke(BreedResult.Content(breeds))
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
                bResult.invoke(BreedResult.Error(t))
            }

        })
    }
}