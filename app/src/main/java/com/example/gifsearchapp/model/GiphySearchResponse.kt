package com.example.gifsearchapp.model

data class GiphySearchResponse(
    val data: List<GifItem>,
    val pagination: Pagination
)
