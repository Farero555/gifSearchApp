package com.example.gifsearchapp.model

data class GifItem(
    val id: String,
    val images:  GifImage,
    val alt_text: String,
    val bitly_url: String

)
