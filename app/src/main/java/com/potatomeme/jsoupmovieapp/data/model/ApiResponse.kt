package com.potatomeme.jsoupmovieapp.data.model


import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("boxOfficeResult")
    val boxOfficeResult: BoxOfficeResult
)