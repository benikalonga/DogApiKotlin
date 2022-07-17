package beni.thedelta.benidogapp.dog

import beni.thedelta.benidogapp.breed.Breed
import com.squareup.moshi.Json

data class DogList(
    @Json(name = "message") val imageUrls: List<String>?,
    @Json(name = "status") val statusResponse: String?
)

data class Dog(
    @Json(name = "message") val imageUrl: String?,
    val breed: String? = null
)
