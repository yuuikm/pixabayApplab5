package com.lab5.pixabayapp.api

import com.google.gson.annotations.SerializedName
import com.lab5.pixabayapp.models.Photo

class PhotoSearchResponse(
    @SerializedName("totalHits")
    val totalHits: Int = 0,
    @SerializedName("hits")
    val photos: List<Photo>,
    @SerializedName("total")
    val total: Int
)