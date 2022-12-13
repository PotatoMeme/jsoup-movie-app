package com.potatomeme.jsoupmovieapp.data.repository

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.potatomeme.jsoupmovieapp.data.model.ApiResponse
import com.potatomeme.jsoupmovieapp.data.model.Movie
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface MovieRepository {
    // Room
    suspend fun insertMovie(movie: Movie)

    suspend fun deleteMovie(movie: Movie)

    fun getSavedMovies(): Flow<List<Movie>>

    fun getSavedMoviesWithName(
        name: String
    ): Flow<List<Movie>>

    //API
    suspend fun getRanking(date:String): Response<ApiResponse>
}