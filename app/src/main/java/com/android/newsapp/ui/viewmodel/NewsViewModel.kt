package com.android.newsapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.android.newsapp.model.Article
import com.android.newsapp.model.NewsModel
import com.android.newsapp.repo.NewsRepo
import com.android.newsapp.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    val newsRepo: NewsRepo,
    private val IODispat: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    val breakingNewsData: MutableLiveData<Resource<NewsModel>> = MutableLiveData()

    // val breakingNewsData =_breakingNewsData.asFlow()
    var breakingNewPage = 1
    val searchingNewsData: MutableLiveData<Resource<NewsModel>> = MutableLiveData()

    // val breakingNewsData =_breakingNewsData.asFlow()
    var searchingNewPage = 1

    init {

        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String) =
        viewModelScope.launch(IODispat) {
            breakingNewsData.postValue(Resource.Loading())

            val response = newsRepo.getBreakingNews(countryCode, breakingNewPage)
            Log.i("nnn", "getBreakingNews: " + response.body())
            breakingNewsData.postValue(handleBreakingNewsResponse(response))
        }

    fun getSearchingNews(countryCode: String) =
        viewModelScope.launch(IODispat) {
            val response = newsRepo.getSearchingNews(countryCode, searchingNewPage)
            searchingNewsData.postValue(handleSearchingNewsResponse(response))
        }

    private fun handleBreakingNewsResponse(response: Response<NewsModel>): Resource<NewsModel> {
        if (response.isSuccessful) {
            response.body()?.let { newsResponse ->
                return Resource.Success(newsResponse)
            }
        }
        return Resource.Error(response.message())
    }
    private fun handleSearchingNewsResponse(response: Response<NewsModel>): Resource<NewsModel> {
        if (response.isSuccessful) {
            response.body()?.let { newsResponse ->
                return Resource.Success(newsResponse)
            }
        }
        return Resource.Error(response.message())
    }
    fun saveArticle(article: Article)=viewModelScope.launch (IODispat){  newsRepo.insert(article)}
    fun deleteArticle(article: Article)=viewModelScope.launch (IODispat){  newsRepo.delete(article)}
    fun getAllArticle()=newsRepo.getAllArticles()
}