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
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class GiphySearchViewModel @Inject constructor(private val repository: GiphyAPI): ViewModel() {

    private val searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val data: Flow<PagingData<GifItem>> = searchQuery
        .flatMapLatest { query ->
            Pager(PagingConfig(
                pageSize = 40,
                prefetchDistance = 20,
            )){
                GiphyRepository(repository, query)
            }.flow
        }.cachedIn(viewModelScope)

    fun updateQuery(newQuery: String) {
        searchQuery.value = newQuery
    }
}