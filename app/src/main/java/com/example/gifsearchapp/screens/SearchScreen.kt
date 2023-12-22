package com.example.gifsearchapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.paging.compose.LazyPagingItems
import coil.ImageLoader
import coil.compose.SubcomposeAsyncImage
import com.example.gifsearchapp.components.GifInputText
import com.example.gifsearchapp.model.GifItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    imageLoader: ImageLoader,
    getGifs: (String) -> Unit,
    data: LazyPagingItems<GifItem>
){
    var query by remember{
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.padding(8.dp).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TopAppBar(
            title = {
                Text(text = "Giphy Search App")
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(Color(0xFFDADFE3))
        )
        Column(
            modifier =Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
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
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2)
                ){
                items(data.itemCount){index->
                    data[index]?.let {
                        GifItem(
                            gifImage = it,
                            imageLoader = imageLoader

                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GifItem(
    gifImage: GifItem,
    imageLoader: ImageLoader
    ){

    Surface(
        modifier = Modifier.padding(8.dp)
    ){
        SubcomposeAsyncImage(
            model = gifImage.images.fixed_width.webp,
            contentDescription = null,
            imageLoader = imageLoader,
            modifier = Modifier
                .height(gifImage.images.fixed_width.height.dp)
                .width(gifImage.images.fixed_width.width.dp),
            loading = {
                Card(
                    modifier = Modifier
                        .size(width = gifImage.images.fixed_width.width.dp, height = gifImage.images.fixed_width.height.dp),
                    shape = RoundedCornerShape(25.dp)
                ){
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        CircularProgressIndicator()
                    }

                }

            },


        )
    }
}

