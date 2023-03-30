package com.moviemain.data.model

import com.google.gson.annotations.SerializedName

data class Details(
    @SerializedName("homepage") val homepage: String? = "",
    @SerializedName("id") val id: Int? = -1,
)