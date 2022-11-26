package com.potatomeme.jsoupmovieapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potatomeme.jsoupmovieapp.data.model.MovieTier
import com.potatomeme.jsoupmovieapp.util.Constants.BASE_URL
import com.potatomeme.jsoupmovieapp.util.Constants.TIER_URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
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
            Log.e("getImageInformation", httpStatusException.message.toString())
            httpStatusException.printStackTrace()
        } catch (exception: Exception) {
            Log.e("getImageInformation", exception.message.toString())
            exception.printStackTrace()
        }
        Log.d(TAG, "searchTier: End")
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}