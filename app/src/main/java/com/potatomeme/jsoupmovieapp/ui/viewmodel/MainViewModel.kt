package com.potatomeme.jsoupmovieapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potatomeme.jsoupmovieapp.data.model.Movie
import com.potatomeme.jsoupmovieapp.data.model.MovieTier
import com.potatomeme.jsoupmovieapp.util.Constants.BASE_URL
import com.potatomeme.jsoupmovieapp.util.Constants.TIER_URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class MainViewModel(
) : ViewModel() {

    private val _tierList = MutableLiveData<List<MovieTier>>()
    val tierList: LiveData<List<MovieTier>> get() = _tierList

    fun searchTier(sort : Int) = viewModelScope.launch(Dispatchers.IO) {
        Log.d(TAG, "searchTier: start")
        Log.d(TAG, "searchTier: $sort")
        try {
            val jsoup = Jsoup.connect(String.format(BASE_URL + TIER_URL
                    + when(sort){
                        0 -> "?sel=cnt"
                        1 -> "?sel=cur"
                        2 -> "?sel=pnt"
                        else -> {""}
                    }

            ))
            val doc: Document = jsoup.get()
            val elements: Elements = doc
                .select("table.list_ranking")
                .select("tbody")
                .select("tr")

            var list = mutableListOf<MovieTier>()
            var count = 1
            val div_tit = if(sort == 0) "div.tit3" else "div.tit5"
            elements.forEach { element ->
                element.run {
                    if (childrenSize() >= 4) {
                        list.add(
                            MovieTier(
                                tier = count++,
                                name = select("td.title").select(div_tit).select("a")
                                    .attr("title"),
                                url = select("td.title").select(div_tit).select("a").attr("href")
                            )
                        )
                    }
                }
            }
            _tierList.postValue(list.toList())
        } catch (httpStatusException: HttpStatusException) {
            Log.e(TAG, httpStatusException.message.toString())
            httpStatusException.printStackTrace()
        } catch (exception: Exception) {
            Log.e(TAG, exception.message.toString())
            exception.printStackTrace()
        }
        Log.d(TAG, "searchTier: End")
    }

    private val _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie> get() = _movie

    fun searchMovie(movieUrl: String) = viewModelScope.launch(Dispatchers.IO) {
        Log.d(TAG, "searchMovie: start")
        try {
            val jsoup = Jsoup.connect(String.format(BASE_URL + movieUrl))
            val doc: Document = jsoup.get()
            val elements: Elements = doc
                .select("div.article")

            val elements_info = elements.select("div.mv_info_area").select("div.mv_info")

            val name = elements_info
                .select("h3.h_movie")
                .select("a")[0]
                .text()

            Log.d(TAG, "searchMovie: name, $name")

            val name_eng = elements_info
                .select("strong.h_movie2")
                .text()

            Log.d(TAG, "searchMovie: name_eng, $name_eng")

            val rating_type1 = elements_info
                .select("div.main_score")
                .select("div.score")
                .select("a")
                .select("div.star_score")
                .select("em")
                .text().filter { it != ' ' }.let {
                    if (it.length > 3) {
                        it.substring(0..3)
                    } else {
                        it
                    }
                }

            Log.d(TAG, "searchMovie: rating_type1, $rating_type1")

            val rating_type2 = elements_info
                .select("div.main_score")
                .select("div.score")
                .select("div.spc_score_area")
                .select("a.spc")
                .select("div.star_score")
                .select("em")
                .text().filter { it != ' ' }

            Log.d(TAG, "searchMovie: rating_type2, $rating_type2")

            val rating_type3 = elements_info
                .select("div.main_score")
                .select("div.score.score_left")
                .select("div.star_score")
                .select("a")
                .select("em")
                .text().filter { it != ' ' }

            Log.d(TAG, "searchMovie: rating_type3, $rating_type3")

            var type = ""
            var country = ""
            var runningTime = ""
            var openingPeriod = ""

            elements_info
                .select("dl.info_spec")
                .select("dd")
                .select("p")
                .select("span").forEach {
                    val str = it.toString()
                    if (str.contains("genre")) {
                        type = it.select("a").text()
                    } else if (str.contains("nation")) {
                        country = it.select("a").text()
                    } else if (str.contains("분")) {
                        runningTime = it.text()
                    } else if (str.contains("open")) {
                        openingPeriod = it.select("a").text().filter { it != ' ' }
                    }
                }

            Log.d(TAG, "searchMovie: type, $type")
            Log.d(TAG, "searchMovie: country, $country")
            Log.d(TAG, "searchMovie: runningTime, $runningTime")
            Log.d(TAG, "searchMovie: openingPeriod, $openingPeriod")

            var director = ""
            var actor = ""
            var rating = ""
            elements_info
                .select("dl.info_spec")
                .select("dd")
                .select("p")
                .forEach {
                    val str = it.toString()
                    if (str.contains("<!-- N=a:ifo.director -->")) {
                        director = it.text()
                    } else if (str.contains("<!-- N=a:ifo.actor -->")) {
                        actor = it.text()
                    } else if (str.contains("<!-- N=a:ifo.filmrate -->")) {
                        rating = it.select("a").text()
                    }
                }

            Log.d(TAG, "searchMovie: director, $director")
            Log.d(TAG, "searchMovie: actor, $actor")
            Log.d(TAG, "searchMovie: rating, $rating")

            val img_url = elements
                .select("div.mv_info_area")
                .select("div.poster")
                .select("a")
                .select("img")
                .attr("src")

            Log.d(TAG, "searchMovie: img_url, $img_url")

            val summary = elements
                .select("div.story_area")
                .select("p.con_tx")
                .text()

            Log.d(TAG, "searchMovie: summary, $summary")
            val data = Movie(
                name,
                name_eng,
                rating_type1,
                rating_type2,
                rating_type3,
                type,
                country,
                runningTime,
                openingPeriod,
                director,
                actor,
                rating,
                img_url,
                summary,
                BASE_URL + movieUrl)
            _movie.postValue(data)


        } catch (httpStatusException: HttpStatusException) {
            Log.e(TAG, httpStatusException.message.toString())
            httpStatusException.printStackTrace()
        } catch (exception: Exception) {
            Log.e(TAG, exception.message.toString())
            exception.printStackTrace()
        }
        Log.d(TAG, "searchMovie: End")
    }

    companion object {
        private const val TAG = "MainViewModel"
        private val NOT_FOUND = "NOT FOUND DATA"
    }
}