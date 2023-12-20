package com.example.gifsearchapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.gifsearchapp.components.GifInputText
import com.example.gifsearchapp.model.GifItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    imageLoader: ImageLoader,
    getGifs: (String) -> Unit,
    data: List<GifItem>
){
    var query by remember{
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TopAppBar(
            title = {
                Text(text = "Giphy Search App")
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(Color(0xFFDADFE3))
        )
        Column(
            verticalArrangement = Arrangement.Top
        ){
            GifInputText(
                modifier = Modifier
                    .padding(top = 9.dp, bottom = 8.dp),
                text = query,
                label = "Search",
                onTextChange = {
                    query = it
                    getGifs(query)
                })
            LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2), ){
                items(data){
                    GifItem(
                        gifImage = it,
                        imageLoader = imageLoader
                    )
                }
            }
        }
    }
}

@Composable
fun GifItem(gifImage: GifItem, imageLoader: ImageLoader){


    Surface(
        modifier = Modifier.padding(8.dp)
    ){
        AsyncImage(
            model = gifImage.images.fixed_width.webp,
            contentDescription = null,
            imageLoader = imageLoader,
            modifier = Modifier.height(gifImage.images.fixed_width.height.dp)
                .width(gifImage.images.fixed_width.width.dp),
        )
    }
}