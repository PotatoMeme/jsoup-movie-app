package com.potatomeme.jsoupmovieapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.potatomeme.jsoupmovieapp.data.model.Movie

@Database(
    entities = [Movie::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDataBase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object{
        @Volatile
        private var INSTANCE: MovieDataBase? = null

        private fun buildDatabase(context: Context): MovieDataBase =
            Room.databaseBuilder(
                context.applicationContext,
                MovieDataBase::class.java,
                "favorite-movies"
            ).build()

        fun getInstance(context: Context): MovieDataBase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
    }
}