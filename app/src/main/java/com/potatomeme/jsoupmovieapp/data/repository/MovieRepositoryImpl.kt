package com.potatomeme.jsoupmovieapp.data.repository

import com.potatomeme.jsoupmovieapp.data.db.MovieDataBase
import com.potatomeme.jsoupmovieapp.data.model.Movie
import kotlinx.coroutines.flow.Flow

class MovieRepositoryImpl(
    private val db : MovieDataBase
):MovieRepository {
    override suspend fun insertMovie(movie: Movie) {
        db.movieDao().insertMovie(movie)
    }

    override suspend fun deleteMovie(movie: Movie) {
        db.movieDao().deleteMovie(movie)
    }

    override fun getFavoriteMovies(): Flow<List<Movie>> {
        return db.movieDao().getFavoriteMovies()
    }

    override fun getFavoriteMoviesWithName(name: String): Flow<List<Movie>> {
        return db.movieDao().getFavoriteMoviesWithName(name)
    }

}