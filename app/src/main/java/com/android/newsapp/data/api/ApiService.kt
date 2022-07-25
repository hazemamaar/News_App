package com.android.newsapp.data.api

import com.android.newsapp.model.NewsModel
import com.android.newsapp.util.C.Companion.APIKEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country") countryCOde :String ="us",
        @Query("page") pageNumber:Int=1,
        @Query("apikey") apiKey:String=APIKEY
    ):Response<NewsModel>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q") searchQuery :String ="us",
        @Query("page") pageNumber:Int=1,
        @Query("apikey") apiKey:String=APIKEY
    ):Response<NewsModel>
}