package com.example.gifsearchapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.compose.collectAsLazyPagingItems
import coil.ImageLoader
import com.example.gifsearchapp.screens.GiphySearchViewModel
import com.example.gifsearchapp.screens.SearchScreen
import com.example.gifsearchapp.ui.theme.GifSearchAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageLoader: ImageLoader
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GifSearchAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val giphySearchViewModel: GiphySearchViewModel by viewModels()
                    GiphySearchApp(
                        giphySearchViewModel = giphySearchViewModel,
                        imageLoader = imageLoader
                        )

                }
            }
        }
    }
}

@Composable
fun GiphySearchApp(giphySearchViewModel: GiphySearchViewModel, imageLoader: ImageLoader){

    val lazyGifItem = giphySearchViewModel.data.collectAsLazyPagingItems()

    SearchScreen(imageLoader = imageLoader,
        getGifs = {
            giphySearchViewModel.updateQuery(it)
        },
        data = lazyGifItem

    )

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GifSearchAppTheme {

    }
}