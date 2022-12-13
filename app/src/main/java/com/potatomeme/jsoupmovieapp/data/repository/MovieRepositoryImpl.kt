package com.potatomeme.jsoupmovieapp.data.repository

import com.potatomeme.jsoupmovieapp.data.api.RetrofitInstance
import com.potatomeme.jsoupmovieapp.data.db.MovieDataBase
import com.potatomeme.jsoupmovieapp.data.model.ApiResponse
import com.potatomeme.jsoupmovieapp.data.model.Movie
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class MovieRepositoryImpl(
    private val db : MovieDataBase
):MovieRepository {
    override suspend fun insertMovie(movie: Movie) {
        db.movieDao().insertMovie(movie)
    }

    override suspend fun deleteMovie(movie: Movie) {
        db.movieDao().deleteMovie(movie)
    }

    override fun getSavedMovies(): Flow<List<Movie>> {
        return db.movieDao().getFavoriteMovies()
    }

    override fun getSavedMoviesWithName(name: String): Flow<List<Movie>> {
        return db.movieDao().getFavoriteMoviesWithName(name)
    }

    //api
    override suspend fun getRanking(date:String) : Response<ApiResponse> {
        return RetrofitInstance.api.getRanking(date = date)
    }

}