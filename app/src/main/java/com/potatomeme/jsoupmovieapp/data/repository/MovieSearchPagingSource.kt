package com.potatomeme.jsoupmovieapp.data.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bumptech.glide.load.HttpException
import com.potatomeme.jsoupmovieapp.data.model.SearchMovieList
import com.potatomeme.jsoupmovieapp.util.Constants
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException
import kotlin.math.ceil

class MovieSearchPagingSource(
    private val query: String,
) : PagingSource<Int, SearchMovieList>() {//Int -> 페이지 타입, SearchMovieList -> Data 타입

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchMovieList> { // 페이지가 데이터를 호출할때마다 불리는 함수
        return try {
            val pageNumber = params.key ?: STARTING_PAGE_INDEX

            val jsoup = Jsoup.connect(String.format(Constants.BASE_URL + Constants.SEARCH_URL + query + "&page=$pageNumber"))
            val doc: Document = jsoup.get()


            val totalPage = doc.select("div.type_2 cbody_type_2").select("span.num").text().split(" / ")[1].split("건")[0].toInt()
            val endOfPaginationReached =  ceil(totalPage/10.0).toInt() == pageNumber

            Log.d(TAG, "load: $totalPage $endOfPaginationReached")

            val elements: Elements = doc
                            .select("ul.search_list_1")
                            .select("li")

            var data = mutableListOf<SearchMovieList>()
            elements.forEach { element ->
                element.run {
                    data.add(
                        SearchMovieList(
                            name = select("dt").text(),
                            imgUrl = select("p.result_thumb").select("a").select("img").attr("src"),
                            url = select("dt").select("a").attr("href")
                        )
                    )
                }
            }

            val prevKey = if (pageNumber == STARTING_PAGE_INDEX) null else pageNumber - 1
            val nextKey = if (endOfPaginationReached) {
                null
            } else {
                pageNumber + 1
            }
            LoadResult.Page(
                data = data.toList(),
                prevKey = prevKey,
                nextKey = nextKey,
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
//        catch (exception: Exception) {
//            LoadResult.Error(exception)
//        }
    }

    override fun getRefreshKey(state: PagingState<Int, SearchMovieList>): Int? {// 여러이유로 페이지를 갱신을 할때 호출이되는 함수
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object {
        private const val TAG = "MovieSearchPagingSource"
        const val STARTING_PAGE_INDEX = 1 // 키 초기값 용
    }
}