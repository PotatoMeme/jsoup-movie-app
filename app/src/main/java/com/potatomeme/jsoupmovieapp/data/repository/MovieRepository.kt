package com.potatomeme.jsoupmovieapp.data.repository

import androidx.paging.PagingData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.potatomeme.jsoupmovieapp.data.model.Movie
import com.potatomeme.jsoupmovieapp.data.model.SearchMovieList
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    // Room
    suspend fun insertMovie(movie: Movie)

    suspend fun deleteMovie(movie: Movie)

    fun getSavedMovies(): Flow<List<Movie>>

    fun getSavedMoviesWithName(
        name: String
    ): Flow<List<Movie>>

    //Paging
    fun searchMoviesPaging(query: String): Flow<PagingData<SearchMovieList>>
}