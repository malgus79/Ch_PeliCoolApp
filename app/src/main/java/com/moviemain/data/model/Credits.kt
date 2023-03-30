package com.moviemain.model.data

import com.google.gson.annotations.SerializedName

data class Credits(
    @SerializedName("cast") val cast: List<Cast>? = listOf(),
    @SerializedName("crew") val crew: List<Crew>? = listOf(),
    @SerializedName("id") val id: Int? = -1
)

data class Cast(
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("cast_id") val cast_id: Int? = -1,
    @SerializedName("character") val character: String? = "",
    @SerializedName("credit_id") val credit_id: String? = "",
    @SerializedName("gender") val gender: Int? = -1,
    @SerializedName("id") val id: Int? = -1,
    @SerializedName("known_for_department") val known_for_department: String? = "",
    @SerializedName("name") val name: String? = "",
    @SerializedName("order") val order: Int? = -1,
    @SerializedName("original_name") val original_name: String? = "",
    @SerializedName("popularity") val popularity: Double? = -1.0,
    @SerializedName("profile_path") val profile_path: String? = ""
)

data class Crew(
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("credit_id") val credit_id: String? = "",
    @SerializedName("department") val department: String? = "",
    @SerializedName("gender") val gender: Int? = -1,
    @SerializedName("id") val id: Int? = -1,
    @SerializedName("job") val job: String? = "",
    @SerializedName("known_for_department") val known_for_department: String? = "",
    @SerializedName("name") val name: String? = "",
    @SerializedName("original_name") val original_name: String? = "",
    @SerializedName("popularity") val popularity: Double? = -1.0,
    @SerializedName("profile_path") val profile_path: String? = ""
)