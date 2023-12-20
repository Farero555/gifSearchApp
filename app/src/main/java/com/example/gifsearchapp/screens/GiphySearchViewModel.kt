package com.example.gifsearchapp.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gifsearchapp.model.GifItem
import com.example.gifsearchapp.model.GiphySearchResponse
import com.example.gifsearchapp.network.GiphyAPI
import com.example.gifsearchapp.util.Constants.API_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GiphySearchViewModel @Inject constructor(private val repository: GiphyAPI): ViewModel() {

    private var _data = MutableStateFlow<List<GifItem>>(emptyList())
    var data = _data.asStateFlow()

    private var _query: String? = null
    private var offset: Int = 0

    fun appendGiphyResponse(query: String){
        viewModelScope.launch {
            repository.searchGifs(API_KEY,query,40, offset).body()?.let { getData(query, it) }

        }
    }
    private fun getData(query: String, giphySearchResponse: GiphySearchResponse){
        offset = giphySearchResponse.pagination.offset
        if(_query != query){
            _data.value = emptyList()
            appendData(giphySearchResponse.data)
            _query = query
        }else{
            appendData(giphySearchResponse.data)
        }
    }
    private fun appendData(gifItems: List<GifItem>){
        val currentList = _data.value
        val updatedList = currentList + gifItems
        _data.value = updatedList
    }
}