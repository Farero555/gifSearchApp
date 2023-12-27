package com.example.gifsearchapp.screens

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
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
import com.example.gifsearchapp.components.DropdownSearchBox
import com.example.gifsearchapp.components.GifInputText
import com.example.gifsearchapp.model.GifItem
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    searchQuery: MutableStateFlow<String>,
    onSearchQueryChange: (String) -> Unit,
    isGridView: Boolean,
    onGridViewChange: (Boolean) -> Unit,
    onContentRatingChange: (String) -> Unit,
    imageLoader: ImageLoader,
    data: LazyPagingItems<GifItem>,
    shareGif: (Context, String) -> Unit,
    isNetworkAvailable: (Context) -> Boolean

){

    Column(
        modifier = Modifier.fillMaxSize(),
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
                ToggleListViewButton(
                    isSelected = !isGridView,
                    icon = Icons.Filled.ViewList,
                    contentDescription = "List View",
                ) { onGridViewChange(!it) }

                ToggleListViewButton(
                    isSelected = isGridView,
                    icon = Icons.Filled.ViewModule,
                    contentDescription = "Grid View"
                ) { onGridViewChange(it) }
            }
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
            ) {
                GifInputText(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 14.dp),
                    text = searchQuery.collectAsState().value,
                    maxLine = 1,
                    label = "Search",
                    onTextChange = {
                        onSearchQueryChange(it)
                    })
                DropdownSearchBox(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .width(120.dp),
                    onTextChange = { rating ->
                        val lowerCaseRating = rating.lowercase(Locale.getDefault())
                        onContentRatingChange(lowerCaseRating)
                    }
                )
            }

            if (!isNetworkAvailable(LocalContext.current)) {
                Text("No internet connection", color = Color.Red, modifier = Modifier.padding(16.dp))
            } else if (data.itemCount == 0) {
                StartScreen()
            } else {
                if (isGridView) {
                    key(searchQuery) {
                        LazyVerticalStaggeredGrid(
                            columns = StaggeredGridCells.Fixed(2),
                            modifier = Modifier.padding(16.dp)
                        ) {
                            items(data.itemCount) { index ->
                                data[index]?.let {
                                    GifItem(
                                        gifItem = it,
                                        imageLoader = imageLoader,
                                        shareGif = shareGif
                                    )
                                }
                            }
                        }
                    }

                } else {
                    key(searchQuery) {
                        LazyColumn(modifier = Modifier.padding(16.dp))
                        {
                            items(data.itemCount) { index ->
                                data[index]?.let {
                                    GifItem(
                                        gifItem = it,
                                        imageLoader = imageLoader,
                                        shareGif = shareGif
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ToggleListViewButton(
    isSelected: Boolean,
    icon: ImageVector,
    contentDescription: String,
    onToggle: (Boolean) -> Unit
) {
    val activeTintColor = Color.Black
    val inactiveTintColor = Color.Gray

    IconToggleButton(
        checked = isSelected,
        enabled = !isSelected,
        onCheckedChange = onToggle
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (isSelected) activeTintColor else inactiveTintColor,
            modifier = Modifier.size(32.dp)
        )
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GifItem(
    gifItem: GifItem,
    imageLoader: ImageLoader,
    shareGif: (Context, String) -> Unit
    ){
    val haptics = LocalHapticFeedback.current
    val context = LocalContext.current

    ElevatedCard(
        modifier = Modifier
            .padding(4.dp)
            .height(gifItem.images.fixed_width.height.dp)
            .width(gifItem.images.fixed_width.width.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ){
        SubcomposeAsyncImage(
            model = gifItem.images.fixed_width.webp,
            contentDescription = gifItem.alt_text,
            imageLoader = imageLoader,
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
                .combinedClickable(
                    onLongClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        shareGif(context, gifItem.bitly_url)
                    }
                ) {},
            contentScale = ContentScale.Crop,
            loading = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x2f17E7FC))
                ){
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .clipToBounds(),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(gifItem.images.fixed_width_still.url)
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

