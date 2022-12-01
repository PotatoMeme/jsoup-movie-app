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

    fun searchTier() = viewModelScope.launch(Dispatchers.IO) {
        Log.d(TAG, "searchTier: start")
        try {
            val jsoup = Jsoup.connect(String.format(BASE_URL + TIER_URL))
            val doc: Document = jsoup.get()
            val elements: Elements = doc
                .select("table.list_ranking")
                .select("tbody")
                .select("tr")

            var list = mutableListOf<MovieTier>()
            var count = 1
            elements.forEach { element ->
                element.run {
                    if (childrenSize() == 4) {
                        list.add(
                            MovieTier(
                                tier = count++,
                                name = select("td.title").select("div.tit3").select("a")
                                    .attr("title"),
                                url = select("td.title").select("div.tit3").select("a").attr("href")
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
                .text() ?: NOT_FOUND

            val name_eng = elements_info
                .select("strong.h_movie2")
                .text() ?: NOT_FOUND

            val rating_type1 = elements_info
                .select("div.main_score")
                .select("div.score")
                .select("a")
                .select("div.star_score")
                .select("em")
                .text().substring(0 until 7).filter { it != ' ' }

            val rating_type2 = elements_info
                .select("div.main_score")
                .select("div.score")
                .select("div.spc_score_area")
                .select("a.spc")
                .select("div.star_score")
                .select("em")
                .text().filter { it != ' ' }

            val rating_type3 = elements_info
                .select("div.main_score")
                .select("div.score.score_left")
                .select("div.star_score")
                .select("a")
                .select("em")
                .text().filter { it != ' ' }

            var type = ""
            var country = ""
            var runningTime = ""
            var openingPeriod = ""

            var count = 0

            elements_info
                .select("dl.info_spec")
                .select("dd")
                .select("p")
                .select("span").forEach {
                    when (count) {
                        0 -> {
                            type = it.select("a").text()
                        }
                        1 -> {
                            country = it.select("a").text()
                        }
                        2 -> {
                            runningTime = it.text()
                        }
                        3 -> {
                            openingPeriod = it.select("a").text()
                        }
                    }
                }


            val img_url = elements
                .select("div.mv_info_area")
                .select("div.poster")
                .select("a")
                .select("img")
                .attr("src") ?: NOT_FOUND

            //val movie = Movie(name,name_eng,rating_type1,rating_type2,rating_type3,)

            Log.d(TAG, "searchMovie: $type")

            Log.d(TAG, "searchMovie: $country")

            Log.d(TAG, "searchMovie: $runningTime")

            Log.d(TAG, "searchMovie: $openingPeriod")

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