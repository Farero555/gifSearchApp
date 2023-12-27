package com.example.gifsearchapp.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.gifsearchapp.model.GifItem
import com.example.gifsearchapp.network.GiphyAPI
import com.example.gifsearchapp.repository.GiphyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class GiphySearchViewModel @Inject constructor(private val repository: GiphyAPI): ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val _contentRating = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val data: Flow<PagingData<GifItem>> = combine(searchQuery, _contentRating) { query, contentRating ->
        Pair(query, contentRating)
    }.flatMapLatest { (query, contentRating) ->
                Pager(
                    PagingConfig(
                        pageSize = 40,
                        prefetchDistance = 20,
                    )
                ) {
                    GiphyRepository(
                        repository,
                        query,
                        if (contentRating == "All") "" else contentRating
                    )
                }.flow
            }.cachedIn(viewModelScope)

    fun updateQuery(newQuery: String, contentRating: String) {
        _contentRating.value = contentRating
        searchQuery.value = newQuery
    }
}