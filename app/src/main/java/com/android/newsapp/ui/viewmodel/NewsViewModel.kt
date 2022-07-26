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
    var breakingArticles: NewsModel? = null
    val searchingNewsData: MutableLiveData<Resource<NewsModel>> = MutableLiveData()
    var searchingNewPage = 1
    var searchArticles: NewsModel? = null

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
                breakingNewPage++
                if (breakingArticles == null) {
                    breakingArticles = newsResponse
                } else {
                    val oldArticles = breakingArticles?.articles
                    val newArticles = newsResponse.articles
                    oldArticles?.addAll(newArticles)
                }

                return Resource.Success(breakingArticles ?: newsResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchingNewsResponse(response: Response<NewsModel>): Resource<NewsModel> {
        if (response.isSuccessful) {
            response.body()?.let { newsResponse ->
                searchingNewPage++
                if (searchArticles == null) {
                    searchArticles = newsResponse
                } else {
                    val oldArticles = searchArticles?.articles
                    val newArticles = newsResponse.articles
                    oldArticles?.addAll(newArticles)
                }

                return Resource.Success(searchArticles ?: newsResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) =
        viewModelScope.launch(IODispat) { newsRepo.insert(article) }

    fun deleteArticle(article: Article) =
        viewModelScope.launch(IODispat) { newsRepo.delete(article) }

    fun getAllArticle() = newsRepo.getAllArticles()
}
