package com.moviemain.data.model

import com.google.gson.annotations.SerializedName

data class Videos(
    @SerializedName("key") val key: String? = "",
)

data class VideosList(
    @SerializedName("results") val results: List<Videos> = listOf()
)