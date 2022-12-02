package com.potatomeme.jsoupmovieapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class Movie(
    val name: String,
    val name_eng: String,
    val rating_type1: String,
    val rating_type2: String,
    val rating_type3: String,
    val type: String,
    val country: String,
    val runningTime: String,
    val openingPeriod: String,
    val director: String,
    val actor: String,
    val rating: String,//ex) 15금
    val imgUrl: String,
    val summary: String,
    val url: String,

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
) {
}