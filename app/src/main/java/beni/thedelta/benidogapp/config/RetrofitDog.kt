
import beni.thedelta.benidogapp.dog.Dog
import beni.thedelta.benidogapp.dog.DogList
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

private const val BASE_URL = "https://dog.ceo/api/"

private val networkLoggingInterceptor =
    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .client(OkHttpClient.Builder().addInterceptor(networkLoggingInterceptor).build())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

object RetrofitDog {
    val service: DogApi by lazy {
        retrofit.create((DogApi::class.java))
    }
}

interface DogApi {

    //https://dog.ceo/api/breeds/list
    @GET("breeds/list/all")
    fun getAllBreads(): Call<ResponseBody>

    //https://dog.ceo/api/breeds/image/random
    @GET("breeds/image/random")
    suspend fun getRandomImage(): Dog

    //https://dog.ceo/api/breed/vizsla/images/
    @GET("breed/{breed}/images/")
    suspend fun getAllDogs(@Path("breed") breed: String): DogList

    //https://dog.ceo/api/breed/hound/images/random
    @GET("breed/{breed}/images/random")
    suspend fun getImageByBreed(@Path("breed") breed: String): Dog

    //Full URL for Image
    @GET
    fun getImageData(@Url url: String): Call<ResponseBody>

}