package com.example.gifsearchapp.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gifsearchapp.model.GifItem
import com.example.gifsearchapp.network.GiphyAPI
import com.example.gifsearchapp.util.Constants.API_KEY


class GiphyRepository(
    private val repository: GiphyAPI,
    private val query: String,
    private val rating: String
) : PagingSource<Int, GifItem>(){
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GifItem> {
        val position = params.key ?: 0

        return try{
            val response = repository.searchGifs(API_KEY, query, params.loadSize, position, rating)
            val gifs = response.body()?.data ?: emptyList()
            val nextKey = if (gifs.isEmpty()) null else position + params.loadSize
            val prevKey = if (position == 0) null else position - 1
            LoadResult.Page(gifs, prevKey, nextKey)
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, GifItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(40) ?: anchorPage?.nextKey?.minus(40)
        }
    }
}