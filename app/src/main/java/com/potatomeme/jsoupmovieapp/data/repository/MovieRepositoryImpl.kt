package com.potatomeme.jsoupmovieapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.potatomeme.jsoupmovieapp.data.db.MovieDataBase
import com.potatomeme.jsoupmovieapp.data.model.Movie
import com.potatomeme.jsoupmovieapp.data.model.SearchMovieList
import com.potatomeme.jsoupmovieapp.util.Constants.PAGING_SIZE
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

    override fun getSavedMovies(): Flow<List<Movie>> {
        return db.movieDao().getFavoriteMovies()
    }

    override fun getSavedMoviesWithName(name: String): Flow<List<Movie>> {
        return db.movieDao().getFavoriteMoviesWithName(name)
    }

    override fun searchMoviesPaging(query: String): Flow<PagingData<SearchMovieList>> {
        val pagingSourceFactory = { MovieSearchPagingSource(query) }
        return Pager(
                config = PagingConfig(
                    pageSize = PAGING_SIZE,
                    enablePlaceholders = false,
                    maxSize = PAGING_SIZE * 3
                ),
        pagingSourceFactory = pagingSourceFactory
        ).flow
    }

}