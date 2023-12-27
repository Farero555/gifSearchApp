package com.example.gifsearchapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material.icons.filled.ViewModule
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.gifsearchapp.R
import com.example.gifsearchapp.components.GifInputText
import com.example.gifsearchapp.model.GifItem
import kotlinx.coroutines.delay

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
    var isGridView by remember {
        mutableStateOf(false)
    }
    var isDebouncing by remember {
        mutableStateOf(false)
    }

        Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TopAppBar(
            title = {
                Text(
                    text = "Giphy Search App",
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                )
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(Color.Transparent),
            actions = {
                val activeTintColor = Color.Black
                val inactiveTintColor = Color.Gray

                IconToggleButton(
                    checked = !isGridView,
                    enabled = isGridView,
                    onCheckedChange = {
                        isGridView = !it
                    }) {

                    Icon(
                        imageVector = Icons.Filled.ViewList,
                        contentDescription = "List View",
                        tint = if (isGridView) inactiveTintColor else activeTintColor,
                        modifier = Modifier.size(32.dp)
                    )
                }
                IconToggleButton(
                    checked = isGridView,
                    enabled = !isGridView,
                    onCheckedChange = { isGridView = it }
                ) {
                    Icon(
                        imageVector = Icons.Filled.ViewModule,
                        contentDescription = "Grid View",
                        tint = if (isGridView) activeTintColor else inactiveTintColor,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        )

        Column(
            modifier =Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            GifInputText(
                modifier = Modifier
                    .padding(bottom = 14.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth(),
                text = query,
                maxLine = 1,
                label = "Search",
                onTextChange = {
                    query = it
                    isDebouncing = true
                    //getGifs(query)
                })
            if(data.itemCount != 0){
                if(isGridView){
                    key(query){
                        LazyVerticalStaggeredGrid(
                            columns = StaggeredGridCells.Fixed(2),
                            modifier = Modifier.padding(16.dp)
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

                }else{
                    key(query){
                        LazyColumn(modifier = Modifier.padding(16.dp))
                        {
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
            }else{
                StartScreen()
            }
        }
    }
    LaunchedEffect(query) {
        if (isDebouncing) {
            delay(300)
            getGifs(query)
            isDebouncing = false
        }
    }
}

@Composable
private fun StartScreen() {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.fillMaxSize(),
    ) {

        Image(
            painter = painterResource(id = R.drawable.powered_by_giphy),
            contentDescription = "Powered By Giphy",
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .padding(10.dp)
        )
    }
}

@Composable
fun GifItem(
    gifImage: GifItem,
    imageLoader: ImageLoader
    ){

    ElevatedCard(
        modifier = Modifier.padding(4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ){
        SubcomposeAsyncImage(
            model = gifImage.images.fixed_width.webp,
            contentDescription = null,
            imageLoader = imageLoader,
            modifier = Modifier
                .height(gifImage.images.fixed_width.height.dp)
                .width(gifImage.images.fixed_width.width.dp)
                .clipToBounds(),
            contentScale = ContentScale.Crop,
            loading = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x2f17E7FC))
                ){
                    AsyncImage(
                        modifier = Modifier.fillMaxSize().clipToBounds(),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(gifImage.images.fixed_width_still.url)
                            .crossfade(200)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Box(
                            modifier = Modifier
                                .size(15.dp)
                        ){
                            CircularProgressIndicator()
                        }
                    }
                }
            },
        )
    }
}

