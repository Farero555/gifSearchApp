package com.example.gifsearchapp.screens

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GiphySearchViewModel @Inject constructor(private val repository: GiphyAPI): ViewModel() {

    var uiSearchQuery = MutableStateFlow("")
    private val apiSearchQuery = MutableStateFlow("")

    private val contentRating = MutableStateFlow("")
    var isGridView by mutableStateOf(false)

    private var searchJob: Job? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    val data: Flow<PagingData<GifItem>> = combine(apiSearchQuery, contentRating) { query, contentRating ->
        Pair(query, contentRating)
    }.flatMapLatest { (query, contentRating) ->
                Pager(
                    PagingConfig(
                        pageSize = 40,
                        prefetchDistance = 20,
                    )
                ) {
                    GiphyRepository(repository, query,
                        if (contentRating == "All") "" else contentRating
                    )
                }.flow
            }.cachedIn(viewModelScope)

    fun onSearchQueryChange(newQuery: String) {

        uiSearchQuery.value = newQuery

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(450)
            apiSearchQuery.value = newQuery
        }
    }
    fun onGridViewChange(isGrid: Boolean) {
        isGridView = isGrid
    }
    fun onContentRatingChange(newRating: String) {
        contentRating.value = newRating
    }

    fun shareGif(context: Context, gifUrl: String) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, gifUrl)
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(shareIntent, null))
    }
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}