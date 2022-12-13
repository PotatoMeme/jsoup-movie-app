package com.potatomeme.jsoupmovieapp.data.api

import com.potatomeme.jsoupmovieapp.data.model.ApiResponse
import com.potatomeme.jsoupmovieapp.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieRankingApi {
    @GET("/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json")
    suspend fun getRanking(
        @Query("key") key: String = Constants.API_KEY,
        @Query("targetDt") date: String,
    ): Response<ApiResponse>
}