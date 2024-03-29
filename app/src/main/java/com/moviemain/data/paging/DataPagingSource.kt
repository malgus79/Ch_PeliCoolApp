package com.moviemain.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.moviemain.application.Constants.PAGE_INDEX
import com.moviemain.data.RepositoryImpl
import com.moviemain.data.model.Movie
import com.moviemain.domain.common.getSuccess
import retrofit2.HttpException
import java.io.IOException

class DataPagingSource(private val repository: RepositoryImpl) :
    PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {

        return try {
            val currentPage = params.key ?: PAGE_INDEX
            val response = repository.getUpcomingMovies(currentPage)
            val responseData = mutableListOf<Movie>()
            val data = response.getSuccess()
            if (data != null) {
                responseData.addAll(data)
            }

            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == PAGE_INDEX) null else -1,
                nextKey = currentPage.plus(1)
                //nextKey = if (responseData.isEmpty()) null else currentPage + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}