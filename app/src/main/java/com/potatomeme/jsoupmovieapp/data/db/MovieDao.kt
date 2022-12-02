package com.potatomeme.jsoupmovieapp.data.db

import androidx.room.*
import com.potatomeme.jsoupmovieapp.data.model.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie)

    @Delete
    suspend fun deleteMovie(movie: Movie)

    @Query("SELECT * FROM movies")
    fun getFavoriteMovies(): Flow<List<Movie>>

    @Query("SELECT * FROM movies WHERE name LIKE :name OR name_eng LIKE :name")
    fun getFavoriteMoviesWithName(
        name: String
    ): Flow<List<Movie>>

}