package com.example.gifsearchapp.repository
//
//import com.example.gifsearchapp.model.GiphySearchResponse
//import com.example.gifsearchapp.network.GiphyAPI
//import retrofit2.Response
//import javax.inject.Inject
//
//class GiphyRepository @Inject constructor(private val api: GiphyAPI) {
//
//
//    suspend fun getGiphyData(
//        api_key: String,
//        query: String,
//        limit: Int,
//        offset: Int,
//        //rating: String
//    ): Response<GiphySearchResponse> {
//        return api.searchGifs(api_key,query,limit,offset)
//    }
//}